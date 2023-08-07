package com.boot.jx.mongo;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import com.boot.jx.AppContextUtil;
import com.boot.jx.http.CommonHttpRequest.ApiRequestDetail;
import com.boot.jx.scope.tnt.Tenants;
import com.boot.utils.ArgUtil;
import com.boot.utils.StringUtils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class CommonMongoSource {

	public static final String USE_DEFAULT_DB = "USE_DEFAULT_DB";
	public static final String USE_NO_DB = "USE_NO_DB";
	public static final String READ_ONLY_DB = "READ_ONLY_DB";

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private String dataSourceUrl;

	private String globalDataSourceUrl;

	private String globalDBProfix;

	private String tenant;
	private String tenantDB;

	public String getDataSourceUrl() {
		return dataSourceUrl;
	}

	private static Object lockClient = new Object();
	private static MongoClient sharedMongoClient;

	private static Object lock = new Object();
	MongoDatabaseFactory mongoDbFactory;
	MongoTemplate mongoTemplate;

	private static Object lockNoDb = new Object();
	static MongoDatabaseFactory mongoDbFactoryNoDb;
	static MongoTemplate mongoTemplateNoDb;

	private static Object lockDefault = new Object();
	static MongoDatabaseFactory mongoDbFactoryDefault;
	static MongoTemplate mongoTemplateDefault;

	boolean ready = false;

	public static boolean hasRule(String useNoDb) {
		ApiRequestDetail apiDetails = AppContextUtil.getApiRequestDetail();
		return ArgUtil.is(apiDetails) && apiDetails.hasRule(useNoDb);
	}

	public static boolean isReadOnly() {
		return hasRule(READ_ONLY_DB);
	}

	public MongoDatabaseFactory getMongoDbFactory(String dataSourceUrl) {
		String tnt = tenant;
		String dbtnt = tenantDB;

		synchronized (lockClient) {
			if (sharedMongoClient == null) {
				sharedMongoClient = MongoClients.create(dataSourceUrl);
				LOGGER.info("MONGODB: MongoClient:{}:{}  ", tnt, dbtnt);
			}
		}
		String dataBaseName = (globalDBProfix + "_" + dbtnt);
		if (hasRule(USE_NO_DB)) {
			dataBaseName = sharedMongoClient.listDatabaseNames().first();
			// dataBaseName = "nodb";
		} else if ((!ArgUtil.areEqual(StringUtils.trim(dataSourceUrl), StringUtils.trim(globalDataSourceUrl))
				|| Tenants.isDefault(tnt) || (hasRule(USE_DEFAULT_DB)))) {
			dataBaseName = sharedMongoClient.listDatabaseNames().first();
		}
		if (ArgUtil.isEmpty(dataBaseName)) {
			dataBaseName = URI.create(dataSourceUrl).getPath().substring(1);
		}
		LOGGER.info("MONGODB: {}:{}:{}", dataBaseName, Tenants.isDefault(tnt), dbtnt);

		return new SimpleMongoClientDatabaseFactory(sharedMongoClient, dataBaseName);
	}

	public MongoDatabaseFactory getMongoDbFactory() {
		if (hasRule(USE_NO_DB)) {
			if (mongoDbFactoryNoDb == null && ArgUtil.is(dataSourceUrl)) {
				mongoDbFactoryNoDb = getMongoDbFactory(dataSourceUrl);
				LOGGER.warn("mongoDbFactoryNoDb was NULL So created One");
			}
			return mongoDbFactoryNoDb;
		} else if (hasRule(USE_DEFAULT_DB)) {
			if (mongoDbFactoryDefault == null && ArgUtil.is(dataSourceUrl)) {
				mongoDbFactoryDefault = getMongoDbFactory(dataSourceUrl);
				LOGGER.warn("mongoDbFactoryNoDb was NULL So created One");
			}
			return mongoDbFactoryDefault;
		} else {
			if (mongoDbFactory == null && ArgUtil.is(dataSourceUrl)) {
				mongoDbFactory = getMongoDbFactory(dataSourceUrl);
				LOGGER.warn("mongoTemplate was NULL So created One");
				ready = true;
			}
			return mongoDbFactory;
		}
	}

	public MongoTemplate getMongoTemplate() {

		if (hasRule(USE_NO_DB)) {
			if (mongoTemplateNoDb == null) {
				synchronized (lockNoDb) {
					LOGGER.info("mongoTemplateNoDb is NULL So creating One {} {}", getDataSourceUrl());
					mongoDbFactoryNoDb = getMongoDbFactory();
					if (ArgUtil.is(mongoDbFactoryNoDb)) {
						mongoTemplateNoDb = new MongoTemplate(mongoDbFactoryNoDb);
						LOGGER.debug("mongoTemplateNoDb was NULL So created One");
					} else {
						LOGGER.error("mongoDbFactoryNoDb was NULL So cannot create One");
					}
				}
			} else {
				LOGGER.error("mongoDbFactoryNoDb = {}", mongoDbFactoryNoDb.getMongoDatabase().getName());
			}
			return mongoTemplateNoDb;
		} else if (hasRule(USE_DEFAULT_DB)) {
			if (mongoTemplateDefault == null) {
				synchronized (lockDefault) {
					LOGGER.info("mongoTemplate is NULL So creating One {} {}", getDataSourceUrl());
					mongoDbFactoryDefault = getMongoDbFactory();
					if (ArgUtil.is(mongoDbFactoryDefault)) {
						mongoTemplateDefault = new MongoTemplate(mongoDbFactoryDefault);
						LOGGER.debug("mongoTemplateDefault was NULL So created One");
						ready = true;
					} else {
						LOGGER.error("mongoDbFactoryDefault was NULL So cannot create One");
					}
				}
			} else {
				LOGGER.error("mongoDbFactoryDefault = {}", mongoTemplateNoDb.getDb().getName());
			}
			return mongoTemplateDefault;
		} else {
			if (mongoTemplate == null) {
				synchronized (lock) {
					LOGGER.debug("mongoTemplate is NULL So creating One {} {}", getDataSourceUrl());
					mongoDbFactory = getMongoDbFactory();
					if (ArgUtil.is(mongoDbFactory)) {
						mongoTemplate = new MongoTemplate(mongoDbFactory);
						LOGGER.debug("mongoTemplate was NULL So created One");
						ready = true;
					} else {
						LOGGER.error("mongoDbFactory was NULL So cannot create One");
					}
				}
			} else {
				LOGGER.debug("mongoDbFactory = {}", mongoDbFactory.getMongoDatabase().getName());
			}
			return mongoTemplate;
		}
	}

	public boolean isReady() {
		return ready;
	}

	public String getGlobalDataSourceUrl() {
		return globalDataSourceUrl;
	}

	public void setGlobalDataSourceUrl(String globalDataSourceUrl) {
		this.globalDataSourceUrl = globalDataSourceUrl;
	}

	public String getGlobalDBProfix() {
		return globalDBProfix;
	}

	public void setGlobalDBProfix(String globalDBProfix) {
		this.globalDBProfix = globalDBProfix;
	}

	public void setDataSourceUrl(String dataSourceUrl) {
		this.dataSourceUrl = dataSourceUrl;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public String getTenantDB() {
		return tenantDB;
	}

	public void setTenantDB(String tenantDB) {
		this.tenantDB = tenantDB;
	}

}
