package com.boot.jx.app;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

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
}
