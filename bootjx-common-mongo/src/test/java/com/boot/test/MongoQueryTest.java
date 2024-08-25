package com.boot.test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boot.jx.mongo.CommonMongoQB.MongoQueryBuilder;
import com.boot.jx.mongo.CommonMongoQueryBuilder;
import com.boot.jx.mongo.QA;
import com.boot.utils.JsonUtil;

public class MongoQueryTest { // Noncompliant

	public static void main(String[] args) throws ParseException, IOException {
		Query query = new Query();
		String agent = "Vinod";
		String dateRange1 = "GT_DATE";
		String dateRange2 = "LT_DATE";
		
		query.addCriteria(Criteria.where("assignedToAgent").is(agent));
		query.addCriteria(Criteria.where("assignedAgentStamp").gt(dateRange1).lt(dateRange2));
		query.fields().include("assignedToAgent").include("assignedAgentStamp").include("contactId").include("contact");

		System.out.println(query.toString());

		// Construct the query
		Query query2 = new Query(
				Criteria.where("assignedToAgent").is(agent).and("assignedAgentStamp").gt(dateRange1).lt(dateRange2));
		query2.fields().include("assignedToAgent").include("assignedAgentStamp").include("contactId")
				.include("contact");

		System.out.println(query2.toString());

	}

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void main3(String[] args) throws ParseException, IOException {
		List<Document> list = new ArrayList<Document>();
		list.add(Aggregation.match(Criteria.where("sessionId").is("622753392ce8572032037399")) // Match
				.toDocument(Aggregation.DEFAULT_CONTEXT));
		list.add(QA.project("statuss", QA.objectToArray("stamps")));
		list.add(Aggregation.unwind("statuss").toDocument(Aggregation.DEFAULT_CONTEXT));
		list.add(Aggregation.group("statuss.k").count().as("count").toDocument(Aggregation.DEFAULT_CONTEXT));

		System.out.println(JsonUtil.toJson(list));

	}

	public static void main2(String[] args) throws ParseException, IOException {
		MongoQueryBuilder<MongoQueryTest> qa = CommonMongoQueryBuilder.collection(MongoQueryTest.class)
				.where(Criteria.where("category").regex("^test$", "i"));
		System.out.println(qa.query().toString());

	}

}
