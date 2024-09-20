package com.boot.jx.mongo;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boot.jx.logger.AuditDetailProvider;
import com.boot.jx.logger.LoggerService;
import com.boot.jx.model.AuditCreateEntity;
import com.boot.jx.model.AuditCreateEntity.AuditIdentifier;
import com.boot.jx.model.AuditCreateEntity.AuditUpdateEntity;
import com.boot.jx.model.ModelPatch;
import com.boot.jx.model.ModelPatch.ModelPatches;
import com.boot.jx.mongo.CommonDocInterfaces.AuditActivityDoc;
import com.boot.jx.mongo.CommonDocInterfaces.AuditableByIdEntity;
import com.boot.jx.mongo.CommonDocInterfaces.DocVersion;
import com.boot.jx.mongo.CommonDocInterfaces.IMongoQueryBuilder;
import com.boot.jx.mongo.CommonDocInterfaces.SimpleDocument;
import com.boot.jx.mongo.CommonDocInterfaces.TimeStampIndex;
import com.boot.jx.mongo.CommonDocInterfaces.TimeStampIndex.CreatedTimeStampIndexSupport;
import com.boot.jx.mongo.CommonDocInterfaces.TimeStampIndex.UpdatedTimeStampIndexSupport;
import com.boot.jx.mongo.CommonMongoQueryBuilder.DocQueryBuilder;
import com.boot.jx.mongo.CommonMongoQueryBuilder.SimpleDocQueryBuilder;
import com.boot.jx.mongo.MongoUtils.MongoResultProcessor;
import com.boot.jx.scope.tnt.TenantDefinations.TenantDefaultQualifier;
import com.boot.model.TimeModels.TimeStampCreatedSupport;
import com.boot.model.TimeModels.TimeStampUpdatedSupport;
import com.boot.utils.ArgUtil;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class CommonMongoTemplateAbstract<TStore extends CommonMongoTemplateAbstract<TStore>>
		extends CommonMongoTemplateDefault {

	public static final Logger LOGGER = LoggerService.getLogger(CommonMongoTemplateAbstract.class);

	public static class CommonMongoStore<DStore extends CommonMongoStore<DStore>>
			extends CommonMongoTemplateAbstract<DStore> {
	}

	public static class TenantDefaultMongoStore<DStore extends TenantDefaultMongoStore<DStore>>
			extends CommonMongoTemplateAbstract<DStore> {

		@Autowired
		@TenantDefaultQualifier
		protected MongoTemplate mongoTemplateTenantDefault;

		@Override
		protected MongoTemplate getCommonMongoTemplate() {
			return mongoTemplateTenantDefault;
		}
	}

	@Autowired
	protected MongoTemplate mongoTemplate;

	// protected MongoConverter mongoConverter;

	@Autowired(required = false)
	protected AuditDetailProvider auditDetailProvider;

	protected MongoTemplate getCommonMongoTemplate() {
		return mongoTemplate;
	}

	@SuppressWarnings("unchecked")
	public TStore using(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
		return (TStore) this;
	}

	public TStore using(CommonMongoSourceProvider commonMongoSourceProvider) {
		return this.using(new MongoTemplateCommonImpl(commonMongoSourceProvider.getSource().getMongoDbFactory())
				.using(commonMongoSourceProvider));
	}

	public MongoResultProcessor<Document> collection(String collection) {
		return new MongoResultProcessor<Document>().using(this).collection(collection);
	}

	public <TResult> MongoResultProcessor<TResult> collection(String collection, Class<TResult> clazz) {
		return new MongoResultProcessor<TResult>().using(this).collection(collection);
	}

	public <TResult> MongoResultProcessor<TResult> collection(Class<TResult> clazz) {
		return new MongoResultProcessor<TResult>().using(this).collection(clazz);
	}

	public void beforeSaveInternal(Object objectToSave, String collectionName) {

		TimeStampIndex timeindex = TimeStampIndex.now();
		if (ArgUtil.is(auditDetailProvider)) {
			timeindex.by(auditDetailProvider.getAuditUser());
		}

		if (objectToSave instanceof UpdatedTimeStampIndexSupport) {
			((UpdatedTimeStampIndexSupport) objectToSave).setUpdated(timeindex);
		}

		if (objectToSave instanceof TimeStampUpdatedSupport) {
			((TimeStampUpdatedSupport) objectToSave).setUpdated(timeindex);
		}

		if (objectToSave instanceof CreatedTimeStampIndexSupport) {
			CreatedTimeStampIndexSupport objectToSaveCreted = (CreatedTimeStampIndexSupport) objectToSave;
			if (ArgUtil.isEmpty(objectToSaveCreted.getCreated())) {
				objectToSaveCreted.setCreated(timeindex);
			}
		}

		if (objectToSave instanceof TimeStampCreatedSupport) {
			TimeStampCreatedSupport objectToSaveCreted = (TimeStampCreatedSupport) objectToSave;
			if (ArgUtil.isEmpty(objectToSaveCreted.getCreated())) {
				objectToSaveCreted.setCreated(timeindex);
			}
		}

		if (ArgUtil.is(auditDetailProvider)) {
			if (objectToSave instanceof AuditableByIdEntity) {
				AuditableByIdEntity auditableByIdEntity = (AuditableByIdEntity) objectToSave;
				auditDetailProvider.auditUpdate(auditableByIdEntity);
				if (!ArgUtil.is(auditableByIdEntity.getId())) {
					auditableByIdEntity.setCreatedBy(auditableByIdEntity.getUpdatedBy());
					auditableByIdEntity.setCreatedStamp(auditableByIdEntity.getUpdatedStamp());
				} else {
					/**
					 * 'Creation' audit can be compromized here as it is being save directly. Should
					 * actually fetch old document and make sure created date is not being
					 * overridden, but can be ignored as log(Object oldDocument, String comment) is
					 * being used everywhere, which anyway is tracjing creation details, and here we
					 * can focus on last update only, in case creation gets oeverriden we can
					 * implement later, as it will have some performance impact.
					 */
//			if (!ArgUtil.is(collectionName)) {
//			    collectionName = mongoTemplate.getCollectionName(objectToSave.getClass());
//			}
//			Object objectToReplace = findById(auditableByIdEntity.getId(), null);
				}
			} else if (objectToSave instanceof AuditUpdateEntity) {
				auditDetailProvider.auditUpdate((AuditUpdateEntity) objectToSave);
			}
		}

	}

	public <T> T save(DocQueryBuilder<T> builder) {
		T doc = builder.getDoc();
		save(doc);
		return doc;
	}

	public <T> T findById(Object id, Class<T> clazz) {
		if (id != null) {
			return getCommonMongoTemplate().findById(id, clazz);
		}
		return null;
	}

	public <T> T findByIdString(String id, Class<T> clazz) {
		if (ArgUtil.is(id)) {
			Criteria c = Criteria.where("_id").is(id);
			return getCommonMongoTemplate().findOne(new Query(c), clazz);
		}
		return null;
	}

	public <T> T findByIdSafeCheck(Object id, Class<T> clazz) {
		Criteria c = Criteria.where("_id").is(id);
		String idStr = ArgUtil.parseAsString(id);
		if (idStr != null && ObjectId.isValid(idStr)) {
			Criteria altC = Criteria.where("_id").is(new ObjectId(idStr));
			Criteria altC2 = Criteria.where("id").is(new ObjectId(idStr));
			c = new Criteria().orOperator(c, altC, altC2);
		}
		if (ArgUtil.is(id)) {
			return getCommonMongoTemplate().findOne(new Query(c), clazz);
		}
		return null;
	}

	public <T> List<T> find(IMongoQueryBuilder<T> builder, Class<T> clazz, String collectionName) {
		return find(builder.build().getQuery(), clazz, collectionName);
	}

	@Override
	public <T> List<T> find(IMongoQueryBuilder<T> builder, Class<T> clazz) {
		return find(builder.build().getQuery(), clazz);
	}

	@Override
	public <T> List<T> find(IMongoQueryBuilder<T> builder) {
		// System.out.println("+++"+builder.getQuery());
		if (ArgUtil.is(builder.getCollectionName())) {
			return find(builder.build().getQuery(), builder.getDocClass(), builder.getCollectionName());
		}
		return find(builder.build().getQuery(), builder.getDocClass());
	}

	@Override
	public <T> T findOne(IMongoQueryBuilder<T> builder) {
		// System.out.println("+++"+builder.getQuery());
		if (ArgUtil.is(builder.getCollectionName())) {
			return findOne(builder.build().getQuery(), builder.getDocClass(), builder.getCollectionName());
		}
		return findOne(builder.build().getQuery(), builder.getDocClass());
	}

	public <T extends DocVersion> T creatNewDocuemnt(String id, Class<T> clazz, T newVersion) {
		if (ArgUtil.is(id)) {
			T oldVersion = getCommonMongoTemplate().findOne(new Query(Criteria.where("_id").is(id)), clazz);
			if (ArgUtil.is(oldVersion)) {
				newVersion.oldVersion(oldVersion);
			}
		}
		return newVersion;
	}

	public <T extends SimpleDocument> UpdateResult patch(ModelPatches patches, Class<T> clazz)
			throws InstantiationException, IllegalAccessException {
		SimpleDocQueryBuilder qb = SimpleDocQueryBuilder.byId(patches.getId(), clazz);
		for (ModelPatch patch : patches.getPatches()) {
			switch (patch.getCommand()) {
			case SET:
				qb.setunset(patch.getField(), patch.getValue());
				break;
			case UNSET:
				qb.unset(patch.getField());
				break;
			default:
				break;
			}
		}
		return update(qb);
	}

	@Override
	public <T> UpdateResult updateFirst(IMongoQueryBuilder<T> builder) {
		UpdateResult ret = null;
		if (ArgUtil.is(builder.getUpdate())) {
			try {
				builder.build();
				builder.audit(auditDetailProvider).updatedStamp();
				// LOGGER.info("Query:{}", builder.getQuery().toString());
				// LOGGER.info("Update:{}", builder.getUpdate().toString());
				ret = mongoTemplate.updateFirst(builder.getQuery(), builder.getUpdate(), builder.getDocClass());
				builder.setUpdate(null);
			} catch (Exception e) {
				LOGGER.debug("Query:{}", builder.getQuery().toString());
				LOGGER.debug("Update:{}", builder.getUpdate().toString());
				throw e;
			}
		}
		return ret;
	}

	@Override
	public <T> UpdateResult update(IMongoQueryBuilder<T> builder) {
		UpdateResult ret = null;
		if (ArgUtil.is(builder.getUpdate())) {
			try {
				builder.build();
				builder.audit(auditDetailProvider).updatedStamp();
				// LOGGER.info("Query:{}", builder.getQuery().toString());
				// LOGGER.info("Update:{}", builder.getUpdate().toString());
				ret = mongoTemplate.updateMulti(builder.getQuery(), builder.getUpdate(), builder.getDocClass());
				builder.setUpdate(null);
			} catch (Exception e) {
				LOGGER.debug("Query:{}", builder.getQuery().toString());
				LOGGER.debug("Update:{}", builder.getUpdate().toString());
				throw e;
			}
		}
		return ret;
	}

	/**
	 * @param builder
	 * @return
	 * 
	 * @see MongoTemplate#upsert(Query,
	 *      org.springframework.data.mongodb.core.query.Update, Class, String)
	 */
	@Override
	public <T> UpdateResult upsert(IMongoQueryBuilder<T> builder) {
		UpdateResult ret = null;
		if (ArgUtil.is(builder.getUpdate())) {
			try {
				builder.build();
				builder.audit(auditDetailProvider).updatedStamp();
				ret = mongoTemplate.upsert(builder.getQuery(), builder.getUpdate(), builder.getDocClass());
			} catch (Exception e) {
				LOGGER.warn("Query:{}", builder.getQuery().toString());
				LOGGER.warn("Update:{}", builder.getUpdate().toString());
				throw e;
			}
		}
		return ret;
	}

	public <T> UpdateResult updateMulti(IMongoQueryBuilder<T> builder) {
		UpdateResult ret = null;
		if (ArgUtil.is(builder.getUpdate())) {
			try {
				builder.build();
				builder.audit(auditDetailProvider).updatedStamp();
				ret = mongoTemplate.updateMulti(builder.getQuery(), builder.getUpdate(), builder.getDocClass());
			} catch (Exception e) {
				LOGGER.warn("Query:{}", builder.getQuery().toString());
				LOGGER.warn("Update:{}", builder.getUpdate().toString());
				throw e;
			}
		}
		return ret;
	}

	public DeleteResult trash(Object object) {
		if (object instanceof AuditCreateEntity && ArgUtil.is(auditDetailProvider)) {
			String collectionName = "ZTRASH_" + mongoTemplate.getCollectionName(object.getClass());
			auditDetailProvider.auditCreate((AuditCreateEntity) object);
			mongoTemplate.save(new AuditActivityDoc().doc(object), collectionName);
		}
		return getCommonMongoTemplate().remove(object);
	}

	public <T> T archive(T oldDocument) {
		String collectionName = "ZCHANGED_" + mongoTemplate.getCollectionName(oldDocument.getClass());
		AuditActivityDoc oldDocumentArchived = new AuditActivityDoc().doc(oldDocument);
		auditDetailProvider.auditCreate(oldDocumentArchived);
		if (oldDocument instanceof AuditIdentifier) {
			oldDocumentArchived.setDocIdentifier(((AuditIdentifier) oldDocument).auditIdentifier());
		}
		mongoTemplate.save(oldDocumentArchived, collectionName);
		return oldDocument;
	}

	public void log(Object copyOfDocument, String activity, String comment) {
		String collectionName = mongoTemplate.getCollectionName(copyOfDocument.getClass());
		AuditActivityDoc oldDocumentArchived = new AuditActivityDoc().collection(collectionName).doc(copyOfDocument)
				.activity(activity).comment(comment);
		auditDetailProvider.auditCreate(oldDocumentArchived);
		if (copyOfDocument instanceof AuditIdentifier) {
			oldDocumentArchived.setDocIdentifier(((AuditIdentifier) copyOfDocument).auditIdentifier());
		}
		mongoTemplate.save(oldDocumentArchived, "ZACTIVITY_LOGS");
	}

	public void log(Object copyOfDocument, String activity) {
		this.log(copyOfDocument, activity, null);
	}

	@SuppressWarnings("unchecked")
	public <T> T findByIdOrDefault(String id, T defaultValue) {
		if (!ArgUtil.is(id)) {
			return defaultValue;
		}
		Object x = getCommonMongoTemplate().findById(id, defaultValue.getClass());
		if (!ArgUtil.is(x)) {
			return defaultValue;
		}
		return (T) x;
	}

	public <T> T saveAndAudit(T objectToSave, boolean isUpdate) {
		String activity = isUpdate ? "updated" : "created";
		if (!isUpdate && (objectToSave instanceof AuditCreateEntity)) {
			auditDetailProvider.auditCreate((AuditCreateEntity) objectToSave);
		}
		save(objectToSave);
		log(objectToSave, activity);
		return objectToSave;
	}

	public <T> T saveAndAudit(T objectToSave) {
		return saveAndAudit(objectToSave, true);
	}

	public <T> T createAndAudit(T objectToSave) {
		return saveAndAudit(objectToSave, false);
	}

	public <T> T removeAndAudit(T objectToSave) {
		if (ArgUtil.is(objectToSave)) {
			getCommonMongoTemplate().remove(objectToSave);
			log(objectToSave, "deleted");
		}
		return objectToSave;
	}

	public <T> T removeAndAudit(String id, Class<T> clazz) {
		T x = getCommonMongoTemplate().findById(id, clazz);
		return removeAndAudit(x);
	}

	public <T> List<T> distinctValues(String collectionName, String key, Class<T> clazz) {
		return collection(collectionName, clazz).distinct(key, clazz).asList();
	}

}
