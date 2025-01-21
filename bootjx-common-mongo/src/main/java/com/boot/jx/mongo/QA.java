package com.boot.jx.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import com.boot.utils.CollectionUtil;

public class QA {

	private List<Document> piplines;
	private Class<?> as;

	public QA() {
		this.piplines = new ArrayList<Document>();
	}

	public QA as(Class<?> as) {
		this.as = as;
		return this;
	}

	public QA add(Object... aggOperations) {
		for (Object aggOperation : aggOperations) {
			if (aggOperation instanceof AggregationOperation) {
				piplines.add(((AggregationOperation) aggOperation).toDocument(Aggregation.DEFAULT_CONTEXT));
			} else if (aggOperation instanceof Document) {
				piplines.add((Document) aggOperation);
			}
		}
		return this;
	}

	public List<Document> piplines() {
		return this.piplines;
	}

	public Class<?> as() {
		return this.as;
	}

	public static class ProjectBuilder {
		private Document project;

		public ProjectBuilder() {
			this.project = new Document();
		}

		public ProjectBuilder(String field, Document doc) {
			this.project = new Document(field, doc);
		}

		public ProjectBuilder append(String field, Document doc) {
			this.project.append(field, doc);
			return this;
		}

		public Document build() {
			return new Document("$project", project);
		}
	}

	public static ProjectBuilder project() {
		return new ProjectBuilder();
	}

	public static ProjectBuilder project(String field, Document doc) {
		return new ProjectBuilder(field, doc);
	}

	public static Document objectToArray(String field) {
		return new Document("$objectToArray", "$" + field);
	}

	public static Document arrayElemAt(Object field, int index) {
		return new Document("$arrayElemAt", CollectionUtil.asArray("$" + field, index));
	}
}