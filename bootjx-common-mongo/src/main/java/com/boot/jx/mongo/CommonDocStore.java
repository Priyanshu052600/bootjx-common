package com.boot.jx.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;

public class CommonDocStore {

    @Autowired
    protected MongoConverter mongoConverter;

    @Autowired
    protected CommonMongoTemplate commonMongoTemplate;

    public <T> List<T> find(Query query, Class<T> entityClass) {
	return commonMongoTemplate.find(query, entityClass);
    }
}
