package com.boot.jx.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.boot.jx.mongo.CommonMongoTemplate.TenantDefaultMongoTemplateImpl;
import com.boot.jx.scope.tnt.TenantDefinations.TenantDefaultQualifier;

@Configuration
@PropertySource("classpath:application-mongo.properties")
public class CommonMongoConfig {

	@Value("${spring.data.mongodb.uri}")
	String dataSourceUrl;

	@Autowired
	private CommonMongoSourceProvider commonMongoSourceProvider;

	@Bean
	@Primary
	public MongoTemplate mongoTemplate() {
		CommonMongoSource source = commonMongoSourceProvider.getSource();
		MongoDbFactory factory = source.getMongoDbFactory(dataSourceUrl);
		return new MongoTemplateCommonImpl(factory, source.mappingMongoConverter(factory));
	}

	@Bean
	@TenantDefaultQualifier
	public MongoTemplate mongoDefaultTemplate() {
		CommonMongoSource source = commonMongoSourceProvider.getSource();
		MongoDbFactory factory = source.getMongoDbFactory(dataSourceUrl);
		return new MongoTemplateCommonImpl(factory, source.mappingMongoConverter(factory)).onlyDefault(true);
	}

	@Bean
	@TenantDefaultQualifier
	public CommonMongoTemplate tenantDefaultMongoTemplate() {
		return new TenantDefaultMongoTemplateImpl();
	}

}
