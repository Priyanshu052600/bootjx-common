package com.boot.jx.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@PropertySource("classpath:application-mongo.properties")
public class CommonMongoConfig {

	@Value("${spring.data.mongodb.uri}")
	String dataSourceUrl;

	@Autowired
	private CommonMongoSourceProvider commonMongoSourceProvider;

	@Bean
	public MongoTemplate mongoTemplate() {
		CommonMongoSource source = commonMongoSourceProvider.getSource();
		MongoDbFactory factory = source.getMongoDbFactory(dataSourceUrl);
		return new MongoTemplateCommonImpl(factory, source.mappingMongoConverter(factory));
	}

}
