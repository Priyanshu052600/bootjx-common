package com.boot.jx.mongo;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.boot.jx.logger.AuditDetailProvider;
import com.boot.jx.mongo.CommonDocInterfaces.IMongoQueryBuilder;
import com.boot.jx.mongo.CommonDocInterfaces.TimeStampIndex;
import com.boot.jx.mongo.CommonDocInterfaces.TimeStampIndex.CreatedTimeStampIndexSupport;
import com.boot.jx.mongo.CommonDocInterfaces.TimeStampIndex.UpdatedTimeStampIndexSupport;
import com.boot.model.TimeModels.TimeStampCreatedSupport;
import com.boot.model.TimeModels.TimeStampUpdatedSupport;
import com.boot.utils.ArgUtil;
import com.boot.utils.PatternUtil;

public class CommonMongoQB<M extends CommonMongoQB<M, T>, T> implements IMongoQueryBuilder<T> {

	public static class QueryCriteria extends Criteria {
		public static Criteria whereId(Object id) {
			return where("_id").is(id);
		}

		public static Criteria whereCode(Object code) {
			return where("code").is(code);
		}

		public static Criteria where(Object key) {
			return where(key);
		}

		public static Criteria whereIdNot(Object id) {
			String idStr = ArgUtil.parseAsString(id);
			if (idStr != null && ObjectId.isValid(idStr)) {
				return where("_id").ne(new ObjectId(idStr));

			}
			return Criteria.where("_id").ne(id);
		}
	}

	Query query;
	Criteria currentCriteria;
	Update update;
	Class<T> docClass;
	String collectionName;
	private boolean skipUpdateStamp;

	private AuditDetailProvider auditDetailProvider;

	@SuppressWarnings("unchecked")
	public M audit(AuditDetailProvider auditDetailProvider) {
		this.auditDetailProvider = auditDetailProvider;
		return (M) this;
	}

	public Query query() {
		if (this.query == null) {
			query = new Query();
		}
		return query;
	}

	public Update update() {
		if (this.update == null) {
			update = new Update();
		}
		return update;
	}

	@SuppressWarnings("unchecked")
	public M query(Query query) {
		this.query = query;
		return (M) this;
	}

	public Criteria criteria(String key) {
		if (currentCriteria == null) {
			currentCriteria = Criteria.where(key);
		} else {
			currentCriteria = currentCriteria.and(key);
		}
		return currentCriteria;
	}

	@Override
	@SuppressWarnings("unchecked")
	public M build() {
		if (currentCriteria != null) {
			query().addCriteria(currentCriteria);
			this.currentCriteria = null;
		}
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M where(Criteria criteria) {
		query().addCriteria(criteria);
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M where(String key, Object o) {
		query().addCriteria(Criteria.where(key).is(o));
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M search(String key, String o) {
		if (ArgUtil.is(o)) {
			query().addCriteria(Criteria.where(key).regex(PatternUtil.contains(o)));
		}
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M having(String key) {
		query().addCriteria(Criteria.where(key).exists(true));
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M without(String key) {
		query().addCriteria(Criteria.where(key).exists(false));
		return (M) this;
	}

	// <--- Where Queries----
	@SuppressWarnings("unchecked")
	public M where(String key) {
		criteria(key);
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M and(String key) {
		criteria(key);
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M is(Object value) {
		this.currentCriteria.is(value);
		return (M) this;
	}

	// --- Where Queries---->

	@SuppressWarnings("unchecked")
	public M sortBy(String byField) {
		this.query().with(new Sort(Direction.ASC, byField));
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M sortBy(String byField, Direction inDir) {
		this.query().with(new Sort(inDir, byField));
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M sortBy(Sort sort) {
		this.query().with(sort);
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M limit(int limit) {
		this.query().limit(limit);
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M limit(long modifiedCount) {
		this.query().limit(ArgUtil.parseAsInteger(modifiedCount));
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M skip(int skip) {
		this.query().skip(skip);
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M page(int pageNo, int pageSize) {
		int pageStart = pageNo * pageSize;
		int pageEnd = pageStart + pageSize;
		this.query().limit(pageSize).skip(pageStart);
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M skipStampUpdate() {
		this.skipUpdateStamp = true;
		return (M) this;
	}

	/**
	 * This is fail Safe '_id' based Search, if Document has 'id' as field
	 * 
	 * @param id
	 * @return
	 */
	public M whereIdSafe(Object id) {
		Criteria c = Criteria.where("_id").is(id);
		String idStr = ArgUtil.parseAsString(id);
		if (idStr != null && ObjectId.isValid(idStr)) {
			Criteria altC = Criteria.where("_id").is(new ObjectId(idStr));
			c = new Criteria().orOperator(c, altC);
		}
		return this.where(c);
	}

	public M whereId(Object id) {
		return this.where(QueryCriteria.whereId(id));
	}

	public M whereCode(Object code) {
		return this.where(QueryCriteria.whereCode(code));
	}

	@SuppressWarnings("unchecked")
	public M whereAll() {
		query().addCriteria(new Criteria());
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M setunset(String key, Object o) {
		if (o == null) {
			update().unset(key);
		} else
			update().set(key, o);
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M set(String key, Object o) {
		update().set(key, o);
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M push(String key, Object o) {
		update().push(key, o);
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M setOnInsert(String key, Object o) {
		update().setOnInsert(key, o);
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M unset(String key) {
		update().unset(key);
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M ref(String key, String id, String collectionName) {
		Map<String, Object> ref = new HashMap<String, Object>();
		ref.put("$ref", collectionName);
		ref.put("$id", new ObjectId(id));
		update().set(key, ref);
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M skipDBRef() {
		if (ArgUtil.is(this.getDocClass())) {
			org.springframework.data.mongodb.core.query.Field fields = this.query().fields();
			for (Field field : this.getDocClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(DBRef.class)) {
					fields.exclude(field.getName());
				}
			}
		}
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M skipDBRefByNames(String... fieldNames) {
		org.springframework.data.mongodb.core.query.Field fields = this.query().fields();
		for (String field : fieldNames) {
			fields.exclude(field);
		}
		return (M) this;
	}

	@SuppressWarnings("unchecked")
	public M includeDBRef(String field) {
		this.query().fields().include(field);
		return (M) this;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public Update getUpdate() {
		return update;
	}

	public void setUpdate(Update update) {
		this.update = update;
	}

	public Class<T> getDocClass() {
		return docClass;
	}

	public void setDocClass(Class<T> docClass) {
		this.docClass = docClass;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	@Override
	public boolean isUpdatedTimeStampSupport() {
		if (ArgUtil.is(this.docClass)) {
			return UpdatedTimeStampIndexSupport.class.isAssignableFrom(this.docClass)
					|| TimeStampUpdatedSupport.class.isAssignableFrom(this.docClass);
		}
		return false;
	}

	@Override
	public boolean isCreatedTimeStampSupport() {
		if (ArgUtil.is(this.docClass)) {
			return CreatedTimeStampIndexSupport.class.isAssignableFrom(this.docClass)
					|| TimeStampCreatedSupport.class.isAssignableFrom(this.docClass);
		}
		return false;
	}

	public void updatedStamp() {
		long updatedStamp = System.currentTimeMillis();
		boolean isUpdatedTimeStampSupport = this.isUpdatedTimeStampSupport();
		boolean isCreatedTimeStampSupport = this.isCreatedTimeStampSupport();

		if (isUpdatedTimeStampSupport || isCreatedTimeStampSupport) {
			TimeStampIndex timeStampIndex = TimeStampIndex.from(updatedStamp);

			if (ArgUtil.is(this.auditDetailProvider)) {
				timeStampIndex.by(auditDetailProvider.getAuditUser());
			}

			if (isUpdatedTimeStampSupport && !skipUpdateStamp) {
				this.set("updated.stamp", timeStampIndex.getStamp());
				this.set("updated.hour", timeStampIndex.getHour());
				this.set("updated.day", timeStampIndex.getDay());
				this.set("updated.week", timeStampIndex.getWeek());
				this.set("updated.byUser", timeStampIndex.getByUser());
			}
			if (isCreatedTimeStampSupport) {
				this.setOnInsert("created", timeStampIndex);
			}
		} else {
			this.set("updatedStamp", updatedStamp);
		}
	}

	public static class MongoQueryBuilder<R> extends CommonMongoQB<MongoQueryBuilder<R>, R> {

	}

	public static class MQB<R> extends CommonMongoQB<MQB<R>, R> {

	}

	public static class MongoQBimpl<R> extends MongoQueryBuilder<R> {

	}

	public static class CommonMongoQBimpl<R> extends CommonMongoQB<CommonMongoQBimpl<R>, R> {

	}

	public static <T> MongoQueryBuilder<T> collection(Class<T> docClass) {
		MongoQueryBuilder<T> x = new MongoQueryBuilder<T>();
		x.setDocClass(docClass);
		return x;
	}

	public static <T> MQB<T> select(Class<T> docClass, String collectionName) {
		MQB<T> x = new MQB<T>();
		x.setDocClass(docClass);
		x.setCollectionName(collectionName);
		return x;
	}

	public static <T> MQB<T> select(Class<T> docClass) {
		MQB<T> x = new MQB<T>();
		x.setDocClass(docClass);
		return x;
	}

	public String getCollectionName() {
		return collectionName;
	}

}
