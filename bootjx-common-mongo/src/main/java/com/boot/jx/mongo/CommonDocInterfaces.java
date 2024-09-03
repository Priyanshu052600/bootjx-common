package com.boot.jx.mongo;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.boot.jx.logger.AuditDetailProvider;
import com.boot.jx.model.AuditCreateEntity;
import com.boot.jx.model.AuditCreateEntity.AuditUpdateEntity;
import com.boot.model.TimeModels.ITimeStampIndex;
import com.boot.model.TimeModels.TimeStampIndexKeyDeserializer;
import com.boot.model.TimeModels.TimeStampSupport;
import com.boot.model.UtilityModels.ProtectedJsonProperty;
import com.boot.model.UtilityModels.PublicJsonProperty;
import com.boot.utils.ArgUtil;
import com.boot.utils.EntityDtoUtil;
import com.boot.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class CommonDocInterfaces {

	public static interface IMongoQueryBuilder<T> {
		public boolean isUpdatedTimeStampSupport();

		public boolean isCreatedTimeStampSupport();

		public void updatedStamp();

		public Update getUpdate();

		public void setUpdate(Update object);

		public Query getQuery();

		public Class<T> getDocClass();

		public String getCollectionName();

		public IMongoQueryBuilder<T> build();

		public IMongoQueryBuilder<T> audit(AuditDetailProvider provider);
	}

	public static interface Patchable<T extends Patchable<T>> {
		public T patch();
	}

	public static interface PatchableIndexed<T extends PatchableIndexed<T, I>, I> extends Patchable<T> {
		public T newInstance();

		public void savePatch(T patch);

		public T fetchPatch();

		default public T patch() {
			if (fetchPatch() == null) {
				T patch = newInstance();
				this.savePatch(patch);
				patch.id(this.id());
			}
			return (T) fetchPatch();
		}

		public void id(I id);

		public I id();
	}

	public static abstract class APatchableIndexed<T extends APatchableIndexed<T, I>, I>
			implements PatchableIndexed<T, I> {
		@JsonIgnore
		private T patch;

		@Override
		public void savePatch(T patch) {
			this.patch = patch;
		}

		@Override
		public T fetchPatch() {
			return this.patch;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static interface OldDocVersion<T extends OldDocVersion<T>> {

		public void setOldVersions(List<T> arrayList);

		public List<T> getOldVersions();

		public default void oldVersion(T oldVersion) {
			if (ArgUtil.is(oldVersion.getOldVersions())) {
				this.setOldVersions(oldVersion.getOldVersions());
			} else {
				this.setOldVersions(new ArrayList<T>());
			}
			oldVersion.setOldVersions(null);
			this.getOldVersions().add(oldVersion);
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static interface DocVersion extends OldDocVersion<DocVersion> {

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static interface IDocument {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static interface SimpleDocument extends IDocument {
		public String getId();

		public void setId(String id);
	}

	@JsonDeserialize(as = ResourceDocumentImpl.class, keyUsing = ResourceDocumentKeyDeserializer.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static interface ResourceDocument extends SimpleDocument {

		public void setCode(String code);

		public String getCode();

		public void setTitle(String title);

		public String getTitle();

	}

	public interface ADocumentDTO<T extends ADocumentDTO<T>> extends IDocument, Serializable {

		@SuppressWarnings("unchecked")
		default public T importFrom(IDocument entity) {

			if (ArgUtil.is(entity)) {
				EntityDtoUtil.entityToDto(entity, this);
			}

			return (T) this;
		}

		default public List<T> importFrom(List<? extends IDocument> entityList) {
			List<T> list = new ArrayList<T>();
			for (IDocument entity : entityList) {
				T dto = this.newInstance().importFrom(entity);
				list.add(dto);
			}
			return list;
		}

		ADocumentDTO<T> newInstance();
	}

	@Document(collection = "ACTIVITY_LOGS")
	public static class AuditActivityDoc implements AuditCreateEntity, Serializable {
		private static final long serialVersionUID = -8573412950623297045L;
		@Id
		@JsonView(PublicJsonProperty.class)
		private String id;

		@JsonView(PublicJsonProperty.class)
		private String docIdentifier;

		@JsonView(ProtectedJsonProperty.class)
		private Object doc;
		@JsonView(PublicJsonProperty.class)
		private String createdBy;
		@JsonView(PublicJsonProperty.class)
		private Long createdStamp;
		@JsonView(PublicJsonProperty.class)
		private String collection;
		@JsonView(PublicJsonProperty.class)
		private String activity;
		@JsonView(PublicJsonProperty.class)
		private String comment;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Object getDoc() {
			return doc;
		}

		public void setDoc(Object doc) {
			this.doc = doc;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		public Long getCreatedStamp() {
			return createdStamp;
		}

		public void setCreatedStamp(Long createdStamp) {
			this.createdStamp = createdStamp;
		}

		public AuditActivityDoc doc(Object doc) {
			this.doc = JsonUtil.toMap(doc);
			return this;
		}

		public AuditActivityDoc collection(String collection) {
			this.collection = collection;
			return this;
		}

		public String getCollection() {
			return collection;
		}

		public void setCollection(String collection) {
			this.collection = collection;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public AuditActivityDoc comment(String comment) {
			this.comment = comment;
			return this;
		}

		public String getActivity() {
			return activity;
		}

		public void setActivity(String activity) {
			this.activity = activity;
		}

		public AuditActivityDoc activity(String activity) {
			this.activity = activity;
			return this;
		}

		public String getDocIdentifier() {
			return docIdentifier;
		}

		public void setDocIdentifier(String docIdentifier) {
			this.docIdentifier = docIdentifier;
		}
	}

	public static class BasicDocument<T extends BasicDocument<T>>
			implements OldDocVersion<T>, IDocument, AuditCreateEntity, Serializable {

		private static final long serialVersionUID = 3330736275464700381L;
		private String createdBy;
		private Long createdStamp;

		@Field("oldVersions")
		private List<T> oldVersions;

		@Override
		public void setOldVersions(List<T> oldVersions) {
			this.oldVersions = oldVersions;
		}

		@Override
		public List<T> getOldVersions() {
			return this.oldVersions;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		public Long getCreatedStamp() {
			return createdStamp;
		}

		public void setCreatedStamp(Long createdStamp) {
			this.createdStamp = createdStamp;
		}

	}

	public interface AuditIdEntity {
		public String getId();
	}

	public interface AuditableByIdEntity extends AuditIdEntity, AuditCreateEntity, AuditUpdateEntity {
	}

	@JsonDeserialize(as = TimeStampIndex.class, keyUsing = TimeStampIndexKeyDeserializer.class)
	public static class TimeStampIndex implements Serializable, ITimeStampIndex<TimeStampIndex> {

		private static final long serialVersionUID = 9114924334759684396L;
		@Indexed
		private long stamp;
		@Indexed
		private long hour;
		@Indexed
		private long day;
		@Indexed
		private long week;
		@Indexed
		private String byUser;

		@Override
		public String getByUser() {
			return byUser;
		}

		@Override
		public void setByUser(String byUser) {
			this.byUser = byUser;
		}

		@Override
		public long getStamp() {
			return stamp;
		}

		@Override
		public void setStamp(long stamp) {
			this.stamp = stamp;
		}

		@Override
		public long getHour() {
			return hour;
		}

		@Override
		public void setHour(long hour) {
			this.hour = hour;
		}

		@Override
		public long getDay() {
			return day;
		}

		@Override
		public void setDay(long day) {
			this.day = day;
		}

		@Override
		public long getWeek() {
			return week;
		}

		@Override
		public void setWeek(long week) {
			this.week = week;
		}

		public static TimeStampIndex from(long stamp) {
			return new TimeStampIndex().fromStamp(stamp);
		}

		public static TimeStampIndex now() {
			return new TimeStampIndex().fromNow();
		}

		public interface UpdatedTimeStampIndexSupport {
			public TimeStampIndex getUpdated();

			public void setUpdated(TimeStampIndex updated);
		}

		public interface CreatedTimeStampIndexSupport {
			public TimeStampIndex getCreated();

			public void setCreated(TimeStampIndex created);
		}

		public interface TimeStampIndexSupport extends CreatedTimeStampIndexSupport, UpdatedTimeStampIndexSupport {
		}

		@CompoundIndexes({ @CompoundIndex(name = "created_stamp", def = "{ 'created.stamp': 1 }"),
				@CompoundIndex(name = "created_hour", def = "{ 'created.hour': 1 }"),
				@CompoundIndex(name = "created_day", def = "{ 'created.day': 1 }"),
				@CompoundIndex(name = "created_week", def = "{ 'created.week': 1 }"),
				@CompoundIndex(name = "created_byUser", def = "{ 'created.byUser': 1 }"),
				@CompoundIndex(name = "updated_hour", def = "{ 'updated.hour': 1 }"),
				@CompoundIndex(name = "updated_day", def = "{ 'updated.day': 1 }"),
				@CompoundIndex(name = "updated_week", def = "{ 'updated.week': 1 }"),
				@CompoundIndex(name = "updated_byUser", def = "{ 'updated.byUser': 1 }"),

		})
		public interface TimeStampDocSupport extends TimeStampSupport {
		}

		public static class UpdatedTimeStampDoc implements UpdatedTimeStampIndexSupport {
			private TimeStampIndex updated;

			public TimeStampIndex getUpdated() {
				return updated;
			}

			public void setUpdated(TimeStampIndex updated) {
				this.updated = updated;
			}
		}

		public static class TimeStampDoc extends UpdatedTimeStampDoc implements TimeStampIndexSupport {
			private TimeStampIndex created;

			public TimeStampIndex getCreated() {
				return created;
			}

			public void setCreated(TimeStampIndex created) {
				this.created = created;
			}
		}
	}

	public static class ResourceDocumentImpl implements ResourceDocument, ADocumentDTO<ResourceDocumentImpl> {
		private static final long serialVersionUID = -2330556618187197003L;
		private String id;
		private String code;
		private String title;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		@Override
		public ADocumentDTO<ResourceDocumentImpl> newInstance() {
			return new ResourceDocumentImpl();
		}

	}

	public static class ResourceDocumentKeyDeserializer extends KeyDeserializer {
		@Override
		public Object deserializeKey(String key, DeserializationContext deserializationContext)
				throws IOException, JsonProcessingException {
			return JsonUtil.getMapper().readValue(key, ResourceDocumentImpl.class);
		}
	}

	static {
		ObjectMapper objectMapper = JsonUtil.getMapper();
		SimpleModule module = new SimpleModule();
		module.addKeyDeserializer(ResourceDocumentImpl.class, new ResourceDocumentKeyDeserializer());
		objectMapper.registerModule(module);

	}
}
