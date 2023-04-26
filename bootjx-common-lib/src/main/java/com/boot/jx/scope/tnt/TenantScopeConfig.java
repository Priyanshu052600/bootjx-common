package com.boot.jx.scope.tnt;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.boot.jx.scope.ScopeBeanFactoryPostProcessor;

@Configuration
public class TenantScopeConfig {

	@Bean
	public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
		return new ScopeBeanFactoryPostProcessor();
	}


}