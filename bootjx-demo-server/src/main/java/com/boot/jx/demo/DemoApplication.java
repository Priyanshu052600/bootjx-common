package com.boot.jx.demo;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.context.request.RequestContextListener;

import com.boot.jx.app.CommonAppLauncher;

/**
 * The Class WebApplication.
 */
@ServletComponentScan
@SpringBootApplication
@ComponentScan("com.boot.jx")
@EnableAsync(proxyTargetClass = true)
@EnableCaching
public class DemoApplication extends CommonAppLauncher {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		launch(DemoApplication.class, args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.boot.web.support.SpringBootServletInitializer#configure(
	 * org.springframework.boot.builder.SpringApplicationBuilder)
	 */
	protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
		return applicationBuilder.sources(DemoApplication.class);
	}

	/**
	 * Security filter chain registration.
	 *
	 * @param securityProperties the security properties
	 * @return the delegating filter proxy registration bean
	 */
	@Bean
	@ConditionalOnBean(name = "checkSession")
	public DelegatingFilterProxyRegistrationBean securityFilterChainRegistration(
			SecurityProperties securityProperties) {
		DelegatingFilterProxyRegistrationBean registration = new DelegatingFilterProxyRegistrationBean("checkSession");
		registration.setOrder(securityProperties.getFilter().getOrder());
		return registration;
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

}
