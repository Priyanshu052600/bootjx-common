package com.boot.loaderjs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boot.jx.mongo.CommonMongoQueryBuilder;
import com.boot.utils.TimeUtils;

public class App { // Noncompliant

	private static Logger LOGGER = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		String id = "608c981dda899d32d9636c67";

		Criteria c = Criteria.where("contactId").is("contactId").and("stamps.READ").exists(false).andOperator(
				// Range
				Criteria.where("timestamp").lt(123450L),
				Criteria.where("timestamp").gt(TimeUtils.beforeTimeMillis("24hr")));

		Query q = Query.query(c);

		System.out.println(q.toString());

		System.out.println(new CommonMongoQueryBuilder().whereId(id).getQuery().toString());

	}

}
