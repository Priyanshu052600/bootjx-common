package com.boot.test;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.boot.jx.AppContextUtil;
import com.boot.jx.mongo.CommonMongoSourceProvider;
import com.boot.jx.mongo.CommonMongoTemplate;
import com.boot.jx.mongo.logger.ChangeLogDoc;
import com.boot.utils.ArgUtil;
import com.boot.utils.JsonUtil;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;

public abstract class BaseMongoTest { // Noncompliant

	private static final int PORT = 27017; // Specify your desired port here

	private static MongodExecutable mongodExecutable;

	private static CommonMongoTemplate mongoTemplate;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		String userDir = System.getProperty("user.dir");
		File userDirFile = new File(userDir);;
		File userDirFolder = userDirFile.getParentFile();
		System.out.println("userDirFolder:" + userDirFolder);
		System.setProperty("javax.net.ssl.trustStore", userDirFolder + "/../certs/cacerts");

		MongodStarter starter = MongodStarter.getDefaultInstance();
		IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
				.net(new Net(PORT, false)) // Set the port here
				.build();
		mongodExecutable = starter.prepare(mongodConfig);
		mongodExecutable.start();

		System.out.println("Embedded MongoDB started on port: " + PORT);

	}

	private void initMongo() {

		AppContextUtil.setTenant("demo");
		String connectionString = System.getProperty("spring.data.mongodb.uri");
		if (!ArgUtil.is(connectionString)) {
			connectionString = "mongodb://localhost:27017/testdatabase";
		}

		if (mongoTemplate == null) {
			System.out.println("connecting " + connectionString);
			CommonMongoSourceProvider commonMongoSourceProvider = new CommonMongoSourceProvider();
			commonMongoSourceProvider.setDataSourceUrl(connectionString);
			commonMongoSourceProvider.setGlobalDataSourceUrl(connectionString);
			commonMongoSourceProvider.setGlobalDBProfix("tnt");
			mongoTemplate = new CommonMongoTemplate().using(commonMongoSourceProvider);
		}

	}

	@Before
	public void setUp() throws Exception {
		String mongoUri = "mongodb://localhost:27017/testdatabase";

		CommonMongoSourceProvider commonMongoSourceProvider = new CommonMongoSourceProvider();
		commonMongoSourceProvider.setDataSourceUrl(mongoUri);
		commonMongoSourceProvider.setGlobalDataSourceUrl(mongoUri);
		commonMongoSourceProvider.setGlobalDBProfix("tnt");
		mongoTemplate = new CommonMongoTemplate().using(commonMongoSourceProvider);

		// mongoTemplate = new MongoTemplate(new
		// SimpleMongoClientDatabaseFactory(mongoUri));
	}

	@Test
	public void contactableTest() {
		ChangeLogDoc log = mongoTemplate.findById("66d988af7ecdb50001cc36ba", ChangeLogDoc.class);
		System.out.println("=========================================");
		if (ArgUtil.is(log)) {
			System.out.println("====" + JsonUtil.toJson(log));
		} else {
			System.out.println("==== Not found for 66d988af7ecdb50001cc36ba");
		}
		System.out.println("=========================================");
	}

	@After
	public void tearDown() throws Exception {
		if (mongoTemplate != null) {
			mongoTemplate = null;
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (mongodExecutable != null) {
			mongodExecutable.stop();
			mongodExecutable = null;
		}
	}
}
