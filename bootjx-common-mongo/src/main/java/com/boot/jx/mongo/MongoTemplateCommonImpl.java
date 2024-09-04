package com.boot.jx.mongo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.DbCallback;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.CloseableIterator;

import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class MongoTemplateCommonImpl extends MongoTemplate {

	@Autowired
	public CommonMongoSourceProvider commonMongoSourceProvider;

	private boolean onlyDefault = false;

	public MongoTemplateCommonImpl(MongoDbFactory mongoDbFactory) {
		super(mongoDbFactory);
	}

	public MongoTemplateCommonImpl(MongoDbFactory factory, MappingMongoConverter mongoConverter) {
		super(factory, mongoConverter);
	}

	public MongoTemplateCommonImpl onlyDefault(boolean onlyDefault) {
		this.onlyDefault = onlyDefault;
		return this;
	}

	protected MongoTemplate getCommonMongoTemplate() {
		if (commonMongoSourceProvider == null) {
			return null;
		}
		CommonMongoSource source = commonMongoSourceProvider.getSource();
		if (source == null) {
			return null;
		}
		if (onlyDefault) {
			return source.getDefaultMongoTemplate();
		}
		return source.getMongoTemplate();
	}

	public void setMongoSourceProvider(CommonMongoSourceProvider commonMongoSourceProvider) {
		this.commonMongoSourceProvider = commonMongoSourceProvider;
	}

	@Override
	public String getCollectionName(Class<?> entityClass) {
		return getCommonMongoTemplate().getCollectionName(entityClass);
	}

	@Override
	public Document executeCommand(String jsonCommand) {
		return getCommonMongoTemplate().executeCommand(jsonCommand);
	}

	@Override
	public Document executeCommand(Document command) {
		return getCommonMongoTemplate().executeCommand(command);
	}

	@Override
	public Document executeCommand(Document command, ReadPreference readPreference) {
		return getCommonMongoTemplate().executeCommand(command, readPreference);
	}

	@Override
	public void executeQuery(Query query, String collectionName, DocumentCallbackHandler dch) {
		getCommonMongoTemplate().executeQuery(query, collectionName, dch);

	}

	@Override
	public <T> T execute(DbCallback<T> action) {
		return getCommonMongoTemplate().execute(action);
	}

	@Override
	public <T> T execute(Class<?> entityClass, CollectionCallback<T> action) {
		return getCommonMongoTemplate().execute(entityClass, action);
	}

	@Override
	public <T> T execute(String collectionName, CollectionCallback<T> action) {
		return getCommonMongoTemplate().execute(collectionName, action);
	}

	@Override
	public <T> CloseableIterator<T> stream(Query query, Class<T> entityType) {
		return getCommonMongoTemplate().stream(query, entityType);
	}

	@Override
	public <T> MongoCollection<Document> createCollection(Class<T> entityClass) {
		return getCommonMongoTemplate().createCollection(entityClass);
	}

	@Override
	public <T> MongoCollection<Document> createCollection(Class<T> entityClass, CollectionOptions collectionOptions) {
		return getCommonMongoTemplate().createCollection(entityClass, collectionOptions);
	}

	@Override
	public MongoCollection<Document> createCollection(String collectionName) {
		return getCommonMongoTemplate().createCollection(collectionName);
	}

	@Override
	public MongoCollection<Document> createCollection(String collectionName, CollectionOptions collectionOptions) {
		return getCommonMongoTemplate().createCollection(collectionName, collectionOptions);
	}

	@Override
	public Set<String> getCollectionNames() {
		return getCommonMongoTemplate().getCollectionNames();
	}

	@Override
	public MongoCollection<Document> getCollection(String collectionName) {
		return getCommonMongoTemplate().getCollection(collectionName);
	}

	@Override
	public <T> boolean collectionExists(Class<T> entityClass) {
		return getCommonMongoTemplate().collectionExists(entityClass);
	}

	@Override
	public boolean collectionExists(String collectionName) {
		return getCommonMongoTemplate().collectionExists(collectionName);
	}

	@Override
	public <T> void dropCollection(Class<T> entityClass) {
		getCommonMongoTemplate().dropCollection(entityClass);
	}

	@Override
	public void dropCollection(String collectionName) {
		getCommonMongoTemplate().dropCollection(collectionName);

	}

	@Override
	public IndexOperations indexOps(String collectionName) {
		return getCommonMongoTemplate().indexOps(collectionName);
	}

	@Override
	public IndexOperations indexOps(Class<?> entityClass) {
		return getCommonMongoTemplate().indexOps(entityClass);
	}

	@Override
	public ScriptOperations scriptOps() {
		return getCommonMongoTemplate().scriptOps();
	}

	@Override
	public BulkOperations bulkOps(BulkMode mode, String collectionName) {
		return getCommonMongoTemplate().bulkOps(mode, collectionName);
	}

	@Override
	public BulkOperations bulkOps(BulkMode mode, Class<?> entityType) {
		return getCommonMongoTemplate().bulkOps(mode, entityType);
	}

	@Override
	public BulkOperations bulkOps(BulkMode mode, Class<?> entityType, String collectionName) {
		return getCommonMongoTemplate().bulkOps(mode, entityType, collectionName);
	}

	@Override
	public <T> List<T> findAll(Class<T> entityClass) {
		return getCommonMongoTemplate().findAll(entityClass);
	}

	@Override
	public <T> List<T> findAll(Class<T> entityClass, String collectionName) {
		return getCommonMongoTemplate().findAll(entityClass, collectionName);
	}

	@Override
	public <T> GroupByResults<T> group(String inputCollectionName, GroupBy groupBy, Class<T> entityClass) {
		return getCommonMongoTemplate().group(inputCollectionName, groupBy, entityClass);
	}

	@Override
	public <T> GroupByResults<T> group(Criteria criteria, String inputCollectionName, GroupBy groupBy,
			Class<T> entityClass) {
		return getCommonMongoTemplate().group(criteria, inputCollectionName, groupBy, entityClass);
	}

	@Override
	public <O> AggregationResults<O> aggregate(TypedAggregation<?> aggregation, String collectionName,
			Class<O> outputType) {
		return getCommonMongoTemplate().aggregate(aggregation, outputType);
	}

	@Override
	public <O> AggregationResults<O> aggregate(TypedAggregation<?> aggregation, Class<O> outputType) {
		return getCommonMongoTemplate().aggregate(aggregation, outputType);
	}

	@Override
	public <O> AggregationResults<O> aggregate(Aggregation aggregation, Class<?> inputType, Class<O> outputType) {
		return getCommonMongoTemplate().aggregate(aggregation, inputType, outputType);
	}

	@Override
	public <O> AggregationResults<O> aggregate(Aggregation aggregation, String collectionName, Class<O> outputType) {
		return getCommonMongoTemplate().aggregate(aggregation, collectionName, outputType);
	}

	@Override
	public <T> MapReduceResults<T> mapReduce(String inputCollectionName, String mapFunction, String reduceFunction,
			Class<T> entityClass) {
		return getCommonMongoTemplate().mapReduce(inputCollectionName, mapFunction, reduceFunction, entityClass);
	}

	@Override
	public <T> MapReduceResults<T> mapReduce(String inputCollectionName, String mapFunction, String reduceFunction,
			MapReduceOptions mapReduceOptions, Class<T> entityClass) {
		return getCommonMongoTemplate().mapReduce(inputCollectionName, mapFunction, reduceFunction, mapReduceOptions,
				entityClass);
	}

	@Override
	public <T> MapReduceResults<T> mapReduce(Query query, String inputCollectionName, String mapFunction,
			String reduceFunction, Class<T> entityClass) {
		return getCommonMongoTemplate().mapReduce(query, inputCollectionName, mapFunction, reduceFunction, entityClass);
	}

	@Override
	public <T> MapReduceResults<T> mapReduce(Query query, String inputCollectionName, String mapFunction,
			String reduceFunction, MapReduceOptions mapReduceOptions, Class<T> entityClass) {
		return mapReduce(query, inputCollectionName, mapFunction, reduceFunction, mapReduceOptions, entityClass);
	}

	@Override
	public <T> GeoResults<T> geoNear(NearQuery near, Class<T> entityClass) {
		return getCommonMongoTemplate().geoNear(near, entityClass);
	}

	@Override
	public <T> GeoResults<T> geoNear(NearQuery near, Class<T> entityClass, String collectionName) {
		return getCommonMongoTemplate().geoNear(near, entityClass, collectionName);
	}

	@Override
	public <T> T findOne(Query query, Class<T> entityClass) {
		return getCommonMongoTemplate().findOne(query, entityClass);
	}

	@Override
	public <T> T findOne(Query query, Class<T> entityClass, String collectionName) {
		return getCommonMongoTemplate().findOne(query, entityClass, collectionName);
	}

	@Override
	public boolean exists(Query query, String collectionName) {
		return getCommonMongoTemplate().exists(query, collectionName);
	}

	@Override
	public boolean exists(Query query, Class<?> entityClass) {
		return getCommonMongoTemplate().exists(query, entityClass);
	}

	@Override
	public boolean exists(Query query, Class<?> entityClass, String collectionName) {
		return getCommonMongoTemplate().exists(query, entityClass, collectionName);
	}

	@Override
	public <T> List<T> find(Query query, Class<T> entityClass) {
		return getCommonMongoTemplate().find(query, entityClass);
	}

	@Override
	public <T> List<T> find(Query query, Class<T> entityClass, String collectionName) {
		return getCommonMongoTemplate().find(query, entityClass, collectionName);
	}

	@Override
	public <T> T findById(Object id, Class<T> entityClass) {
		return getCommonMongoTemplate().findById(id, entityClass);
	}

	@Override
	public <T> T findById(Object id, Class<T> entityClass, String collectionName) {
		return getCommonMongoTemplate().findById(id, entityClass, collectionName);
	}

	@Override
	public <T> T findAndModify(Query query, Update update, Class<T> entityClass) {
		return getCommonMongoTemplate().findAndModify(query, update, entityClass);
	}

	@Override
	public <T> T findAndModify(Query query, Update update, Class<T> entityClass, String collectionName) {
		return getCommonMongoTemplate().findAndModify(query, update, entityClass, collectionName);
	}

	@Override
	public <T> T findAndModify(Query query, Update update, FindAndModifyOptions options, Class<T> entityClass) {
		return getCommonMongoTemplate().findAndModify(query, update, options, entityClass);
	}

	@Override
	public <T> T findAndModify(Query query, Update update, FindAndModifyOptions options, Class<T> entityClass,
			String collectionName) {
		return getCommonMongoTemplate().findAndModify(query, update, options, entityClass, collectionName);
	}

	@Override
	public <T> T findAndRemove(Query query, Class<T> entityClass) {
		return getCommonMongoTemplate().findAndRemove(query, entityClass);
	}

	@Override
	public <T> T findAndRemove(Query query, Class<T> entityClass, String collectionName) {
		return getCommonMongoTemplate().findAndRemove(query, entityClass, collectionName);
	}

	@Override
	public long count(Query query, Class<?> entityClass) {
		return getCommonMongoTemplate().count(query, entityClass);
	}

	@Override
	public long count(Query query, String collectionName) {
		return getCommonMongoTemplate().count(query, collectionName);
	}

	@Override
	public long count(Query query, Class<?> entityClass, String collectionName) {
		return getCommonMongoTemplate().count(query, entityClass, collectionName);
	}

	@Override
	public void insert(Object objectToSave) {
		getCommonMongoTemplate().insert(objectToSave);
	}

	@Override
	public void insert(Object objectToSave, String collectionName) {
		getCommonMongoTemplate().insert(objectToSave, collectionName);
	}

	@Override
	public void insert(Collection<? extends Object> batchToSave, Class<?> entityClass) {
		getCommonMongoTemplate().insert(batchToSave, entityClass);
	}

	@Override
	public void insert(Collection<? extends Object> batchToSave, String collectionName) {
		getCommonMongoTemplate().insert(batchToSave, collectionName);
	}

	@Override
	public void insertAll(Collection<? extends Object> objectsToSave) {
		getCommonMongoTemplate().insertAll(objectsToSave);
	}

	@Override
	public void save(Object objectToSave) {
		getCommonMongoTemplate().save(objectToSave);
	}

	@Override
	public void save(Object objectToSave, String collectionName) {
		getCommonMongoTemplate().save(objectToSave, collectionName);
	}

	@Override
	public UpdateResult upsert(Query query, Update update, Class<?> entityClass) {
		return getCommonMongoTemplate().upsert(query, update, entityClass);
	}

	@Override
	public UpdateResult upsert(Query query, Update update, String collectionName) {
		return getCommonMongoTemplate().upsert(query, update, collectionName);
	}

	@Override
	public UpdateResult upsert(Query query, Update update, Class<?> entityClass, String collectionName) {
		return getCommonMongoTemplate().upsert(query, update, entityClass, collectionName);
	}

	@Override
	public UpdateResult updateFirst(Query query, Update update, Class<?> entityClass) {
		return getCommonMongoTemplate().updateFirst(query, update, entityClass);
	}

	@Override
	public UpdateResult updateFirst(Query query, Update update, String collectionName) {
		return getCommonMongoTemplate().updateFirst(query, update, collectionName);
	}

	@Override
	public UpdateResult updateFirst(Query query, Update update, Class<?> entityClass, String collectionName) {
		return getCommonMongoTemplate().updateFirst(query, update, entityClass, collectionName);
	}

	@Override
	public UpdateResult updateMulti(Query query, Update update, Class<?> entityClass) {
		return getCommonMongoTemplate().updateMulti(query, update, entityClass);
	}

	@Override
	public UpdateResult updateMulti(Query query, Update update, String collectionName) {
		return getCommonMongoTemplate().updateMulti(query, update, collectionName);
	}

	@Override
	public UpdateResult updateMulti(Query query, Update update, Class<?> entityClass, String collectionName) {
		return getCommonMongoTemplate().updateMulti(query, update, entityClass, collectionName);
	}

	@Override
	public DeleteResult remove(Object object) {
		return getCommonMongoTemplate().remove(object);
	}

	@Override
	public DeleteResult remove(Object object, String collection) {
		return getCommonMongoTemplate().remove(object, collection);
	}

	@Override
	public DeleteResult remove(Query query, Class<?> entityClass) {
		return getCommonMongoTemplate().remove(query, entityClass);
	}

	@Override
	public DeleteResult remove(Query query, Class<?> entityClass, String collectionName) {
		return getCommonMongoTemplate().remove(query, entityClass, collectionName);
	}

	@Override
	public DeleteResult remove(Query query, String collectionName) {
		return getCommonMongoTemplate().remove(query, collectionName);
	}

	@Override
	public <T> List<T> findAllAndRemove(Query query, String collectionName) {
		return getCommonMongoTemplate().findAllAndRemove(query, collectionName);
	}

	@Override
	public <T> List<T> findAllAndRemove(Query query, Class<T> entityClass) {
		return getCommonMongoTemplate().findAllAndRemove(query, entityClass);
	}

	@Override
	public <T> List<T> findAllAndRemove(Query query, Class<T> entityClass, String collectionName) {
		return getCommonMongoTemplate().findAllAndRemove(query, entityClass, collectionName);
	}

	@Override
	public MongoConverter getConverter() {
		MongoTemplate tmp = getCommonMongoTemplate();
		if (tmp == null) {
			return null;
		}
		return tmp.getConverter();
	}

	@Override
	public MongoDatabase getDb() {
		return getCommonMongoTemplate().getDb();
	}

	public MongoTemplateCommonImpl using(CommonMongoSourceProvider commonMongoSourceProvider) {
		this.setMongoSourceProvider(commonMongoSourceProvider);
		return this;
	}
}
