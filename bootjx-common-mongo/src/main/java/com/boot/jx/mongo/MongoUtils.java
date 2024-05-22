
package com.boot.jx.mongo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.boot.jx.mongo.CommonDocInterfaces.IMongoQueryBuilder;
import com.boot.jx.mongo.CommonMongoQB.MQB;
import com.boot.jx.mongo.CommonMongoQB.MongoQueryBuilder;
import com.boot.utils.ArgUtil;
import com.boot.utils.CollectionUtil;
import com.boot.utils.EntityDtoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;

public class MongoUtils {

	public static Criteria[] toArray(List<Criteria> criterias) {
		return criterias.toArray(criterias.toArray(new Criteria[criterias.size()]));
	}

	/**
	 * Will create new Criteria with any of matching conditions in list
	 * 
	 * @param criterias
	 * @return
	 */
	public static Criteria anyCriteria(List<Criteria> criterias) {
		return new Criteria().orOperator(toArray(criterias));
	}

	/**
	 * Will create new Criteria with all matching conditions in list
	 * 
	 * @param criterias
	 * @return
	 */
	public static Criteria allCriteria(List<Criteria> criterias) {
		return new Criteria().andOperator(toArray(criterias));
	}

	public static <T> List<T> toList(DistinctIterable<T> distinctIterable) {
		return CollectionUtil.asList(distinctIterable);
	}

	public static List<Document> newAggregation(AggregationOperation... aggOperations) {
		List<Document> agg = new ArrayList<Document>();
		for (AggregationOperation aggOperation : aggOperations) {
			agg.add(aggOperation.toDocument(Aggregation.DEFAULT_CONTEXT));
		}
		return agg;
	}

	public static List<DBObject> newAggregationDBObject(AggregationOperation... aggOperations) {
		List<DBObject> agg = new ArrayList<DBObject>();
		for (AggregationOperation aggOperation : aggOperations) {
			agg.add(new BasicDBObject(aggOperation.toDocument(Aggregation.DEFAULT_CONTEXT)));;
		}
		return agg;
	}

	public static class MongoResultProcessor<T> {
		protected CommonMongoTemplateDefault mongoTemplate;
		protected String collection;
		protected Class<T> collectionClass;
		protected MongoCollection<Document> col;
		protected MongoIterable<T> iterableResults;
		private List<T> results;
		private MongoQueryBuilder<T> qb;

		public MongoCollection<Document> collection() {
			if (this.col == null) {
				this.col = mongoTemplate.getCollection(collection);
			}
			return this.col;
		}

		public MongoQueryBuilder<T> qb() {
			if (this.qb == null) {
				this.qb = MQB.collection(collectionClass);
			}
			return this.qb;
		}

		public MongoResultProcessor<T> using(CommonMongoTemplateDefault mongoTemplate) {
			this.mongoTemplate = mongoTemplate;
			return this;
		}

		public MongoResultProcessor<T> collection(String collection) {
			this.collection = collection;
			// this.col = mongoTemplate.getCollection(collection);
			return this;
		}

		public MongoResultProcessor<T> collection(Class<T> clazz) {
			this.collectionClass = clazz;
			this.collection = mongoTemplate.getCollectionName(clazz);
			return this;
		}

		public MongoResultProcessor<T> where(String field, Object value) {
			this.qb().where(field, value);
			return this;
		}

		public MongoResultProcessor<T> where(Criteria criteria) {
			this.qb().where(criteria);
			return this;
		}

		public MongoResultProcessor<T> with(Criteria criteria) {
			this.qb().where(criteria);
			return this;
		}

		public MongoResultProcessor<T> set(String key, Object o) {
			this.qb().set(key, o);
			return this;
		}

		public MongoResultProcessor<T> find(IMongoQueryBuilder<T> builder) {
			this.results = mongoTemplate.find(builder);
			return this;
		}

		public MongoResultProcessor<T> find(Criteria criteria) {
			this.qb().where(criteria);
			return this.find(qb);
		}

		public MongoResultProcessor<T> find() {
			return this.find(qb);
		}

		public MongoResultProcessor<T> update() {
			mongoTemplate.update(qb);
			return this;
		}

		public MongoResultProcessor<T> results(MongoIterable<T> aggregate) {
			this.iterableResults = aggregate;
			return this;
		}

		public <TResult> MongoResultProcessor<TResult> aggregate(List<Document> aggreQuery,
				Class<TResult> resultClass) {
			MongoResultProcessor<TResult> newP = new MongoResultProcessor<TResult>();
			return newP.results(collection().aggregate(aggreQuery, resultClass));
		}

		public MongoResultProcessor<Document> aggregate(List<Document> aggreQuery) {
			SimpleMongoResultProcessor newP = new SimpleMongoResultProcessor();
			return newP.results(collection().aggregate(aggreQuery));
		}

		public <TResult> MongoResultProcessor<TResult> aggregate(QA aggreQuery, Class<TResult> resultClass) {
			return this.aggregate(aggreQuery.piplines(), resultClass);
		}

		public MongoResultProcessor<Document> aggregate(QA aggreQuery) {
			return this.aggregate(aggreQuery.piplines());
		}

		public MongoResultProcessor<T> distinct(String fieldkey, Class<T> fieldkeyType) {
			iterableResults = collection().distinct(fieldkey, fieldkeyType);
			return this;
		}

		public MongoResultProcessor<String> distinct(String fieldkey) {
			MongoResultProcessor<String> newP = new MongoResultProcessor<String>();
			return newP.results(collection().distinct(fieldkey, String.class));
		}

		public MongoResultProcessor<T> forEach(Consumer<? super T> action) {
			this.iterableResults.forEach(action);
			return this;
		}

		public MongoCursor<T> iterator() {
			return this.iterableResults.iterator();
		}

		public List<T> asList(List<T> list) {
			MongoCursor<T> cursor = this.iterableResults.iterator();
			while (cursor.hasNext()) {
				T object = cursor.next();
				if (ArgUtil.is(object)) {
					list.add(object);
				}

			}
			return list;
		}

		public List<T> asList() {
			if (this.iterableResults != null) {
				return asList(new LinkedList<T>());
			}
			return this.results;
		}

		public T asFirst() {
			return CollectionUtil.first(asList());
		}

		public <DTO> DTO asFirst(DTO dto) {
			T x = asFirst();
			if (!ArgUtil.is(x)) {
				return null;
			}
			return EntityDtoUtil.entityToDto(x, dto);
		}

	}

	public static class SimpleMongoResultProcessor extends MongoResultProcessor<Document> {

	}

}
