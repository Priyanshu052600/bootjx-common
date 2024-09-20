package com.boot.jx.mongo;

import com.boot.jx.mongo.CommonDocInterfaces.ResourceDocumentImpl;
import com.boot.jx.mongo.CommonDocInterfaces.SimpleDocument;
import com.boot.jx.mongo.CommonDocInterfaces.TimeStampIndex.CreatedTimeStampIndexSupport;
import com.boot.jx.mongo.CommonDocInterfaces.TimeStampIndex.UpdatedTimeStampIndexSupport;
import com.boot.jx.mongo.CommonMongoQB.MongoQueryBuilder;
import com.boot.utils.ArgUtil;

public class CommonMongoQueryBuilder extends MongoQueryBuilder<Object> {

	public static abstract class DocQueryBuilder<T> extends CommonMongoQB<DocQueryBuilder<T>, T> {

		protected T doc;
		protected boolean synced;
		private long updatedStamp;

		public DocQueryBuilder(T doc) {
			this.doc = doc;
			whereId(getId(this.doc));
			this.synced = true;
		}

		public DocQueryBuilder(String id) {
			this.doc = this.newDoc(id);
			whereId(id);
		}

		public abstract T newDoc(String id);

		public abstract String getId(T doc);

		public boolean isSynced() {
			return synced;
		}

		public void setSynced(boolean synced) {
			this.synced = synced;
		}

		public long getUpdatedStamp() {
			return updatedStamp;
		}

		public void setUpdatedStamp(long updatedStamp) {
			this.updatedStamp = updatedStamp;
		}

		@Override
		public boolean isUpdatedTimeStampSupport() {
			if (ArgUtil.is(this.doc)) {
				return this.doc instanceof UpdatedTimeStampIndexSupport;
			}
			return super.isUpdatedTimeStampSupport();
		}

		@Override
		public boolean isCreatedTimeStampSupport() {
			if (ArgUtil.is(this.doc)) {
				return this.doc instanceof CreatedTimeStampIndexSupport;
			}
			return super.isCreatedTimeStampSupport();
		}

		@SuppressWarnings("unchecked")
		public Class<T> getDocClass() {
			if (docClass == null) {
				this.docClass = this.doc == null ? null : (Class<T>) this.doc.getClass();
			}
			return docClass;
		}

		public T getDoc() {
			return doc;
		}

		public String getId() {
			return null;
		}

	}

	public static class SimpleDocQueryBuilder extends DocQueryBuilder<SimpleDocument> {

		public SimpleDocQueryBuilder(SimpleDocument doc) {
			super(doc);
		}

		@Override
		public SimpleDocument newDoc(String id) {
			SimpleDocument doc = new ResourceDocumentImpl();
			doc.setId(id);
			return doc;
		}

		@Override
		public String getId(SimpleDocument doc) {
			return doc.getId();
		}

		public static SimpleDocQueryBuilder doc(SimpleDocument doc) {
			return new SimpleDocQueryBuilder(doc);
		}

		public static <T extends SimpleDocument> SimpleDocQueryBuilder byId(String id, Class<T> clazz)
				throws InstantiationException, IllegalAccessException {
			T doc = clazz.newInstance();
			doc.setId(id);
			return new SimpleDocQueryBuilder(doc);
		}

	}

	public static SimpleDocQueryBuilder doc(SimpleDocument doc) {
		return SimpleDocQueryBuilder.doc(doc);
	}
}
