package com.boot.jx.mongo;

import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoDatabase;

public class ReadPreferenceMongoDbFactory extends SimpleMongoDbFactory {

	private final ReadPreference readPreference;

	public ReadPreferenceMongoDbFactory(MongoClient mongoClient, String databaseName, ReadPreference readPreference) {
		super(mongoClient, databaseName);
		this.readPreference = readPreference;
	}

	@Override
	public MongoDatabase getDb() {
		return super.getDb().withReadPreference(readPreference);
	}

	@Override
	public MongoDatabase getDb(String dbName) {
		return super.getDb(dbName).withReadPreference(readPreference);
	}
}
