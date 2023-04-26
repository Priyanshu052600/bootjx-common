package com.boot.jx.rbac.session;

import java.time.Duration;
import java.util.Collections;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.session.data.mongo.AbstractMongoSessionConverter;
import org.springframework.session.data.mongo.JacksonMongoSessionConverter;
import org.springframework.session.data.mongo.JdkMongoSessionConverter;
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;

import com.boot.jx.logger.LoggerService;
import com.boot.utils.JsonUtil;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
@ConditionalOnProperty(name = "spring.session.store-type", havingValue = "mongodb")
@EnableMongoHttpSession
public class MongoSessionConfig {

	private static final Logger LOGGER = LoggerService.getLogger(MongoSessionConfig.class);

	@Value("${spring.session.store-type}")
	private String sessionStoreType;

	@Bean
	public JdkMongoSessionConverter jdkMongoSessionConverter() {
		return new JdkMongoSessionConverter(Duration.ofMinutes(30));
	}

	// @Bean
	public AbstractMongoSessionConverter mongoSessionConverter() {
		// List<Module> securityModules =
		// SecurityJackson2Modules.getModules(getClass().getClassLoader());
		// return new JacksonMongoSessionConverter(securityModules);
		return new JacksonMongoSessionConverter();
	}

	public Iterable<Module> getJacksonModules() {
		return Collections
				.<Module>singletonList(new SimpleModule("forMongoSession", new Version(1, 0, 0, null, null, null)));
	}

	// @Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = JsonUtil.createMapper("forMongoSession");
		mapper.registerModule(new CoreJackson2Module());
		return mapper;
	}

	@PostConstruct
	public void init() {
		LOGGER.info("spring.session.store-type=none turns spring session off.");
		LOGGER.info("Monog Session Replication is turned {}.", sessionStoreType.equals("mongodb") ? "ON" : "OFF");
	}

}
