package com.boot.jx.app;

import java.io.File;
import java.io.IOException;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.boot.utils.ArgUtil;

public class CommonAppLauncher extends SpringBootServletInitializer {

	public static final String APP_TRUST_STORE_LOCAL = "local.ssl.trustStore";

	public static ConfigurableApplicationContext launch(Class<? extends CommonAppLauncher> applicationClass,
			String[] args) throws IOException {

		String isLocalTrustStore = ArgUtil.parseAsString(System.getenv(APP_TRUST_STORE_LOCAL), "false");

		if ("false".equalsIgnoreCase(isLocalTrustStore)) {
			System.out.println("No Local Certificate");
		} else if ("true".equalsIgnoreCase(isLocalTrustStore)) {
			String userDir = System.getProperty("user.dir");
			File userDirFile = new File(userDir);;
			File userDirFolder = userDirFile.getParentFile();
			System.setProperty("javax.net.ssl.trustStore", userDirFolder + "/certs/cacerts");
		} else {
			System.setProperty("javax.net.ssl.trustStore", isLocalTrustStore);
		}

		// System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		return SpringApplication.run(applicationClass, args);
	}

	public static ConfigurableApplicationContext build(Class<? extends CommonAppLauncher> applicationClass,
			String[] args) throws IOException {

		String isLocalTrustStore = ArgUtil.parseAsString(System.getenv(APP_TRUST_STORE_LOCAL), "false");

		if ("false".equalsIgnoreCase(isLocalTrustStore)) {
			System.out.println("No Local Certificate");
		} else if ("true".equalsIgnoreCase(isLocalTrustStore)) {
			String userDir = System.getProperty("user.dir");
			File userDirFile = new File(userDir);;
			File userDirFolder = userDirFile.getParentFile();
			System.setProperty("javax.net.ssl.trustStore", userDirFolder + "/certs/cacerts");
		} else {
			System.setProperty("javax.net.ssl.trustStore", isLocalTrustStore);
		}

		// System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		return new SpringApplicationBuilder(applicationClass).run(args);
	}

	@Value("${server.tomcat.relaxed-query-chars}")
	String relaxedQueryChars;

	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
			@Override
			public void customize(Connector connector) {
				if (ArgUtil.is(relaxedQueryChars)) {
					connector.setProperty("relaxedQueryChars", relaxedQueryChars);
				}
			}
		});
		return factory;
	}
}
