package com.boot.jx.db.multitenant.conf;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.boot.jx.db.multitenant.conf.MultiTenantProperties.DataSourceProperties;

@Configuration
public class MultitenantConfiguration {

	@Autowired
	private MultiTenantProperties multiTenantProperties;

	@Value("${hibernate.show_sql:false}")
	private Boolean showSql;

	@Bean
	public DataSource dataSource() {
		Map<Object, Object> resolvedDataSources = new HashMap<>();

		for (DataSourceProperties dataSourceProp : multiTenantProperties.getDataSourcesProps()) {
			Properties tenantProperties = new Properties();
			DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
			String tenantId = dataSourceProp.getTenantId();
			dataSourceBuilder.driverClassName(dataSourceProp.getDriverClassName());
			dataSourceBuilder.username(dataSourceProp.getUsername());
			dataSourceBuilder.password(dataSourceProp.getPassword());
			dataSourceBuilder.url(dataSourceProp.getUrl());
			resolvedDataSources.put(tenantId, dataSourceBuilder.build());
		}

		AbstractRoutingDataSource dataSource = new MultitenantDataSource();
		dataSource.setDefaultTargetDataSource(resolvedDataSources.get("KWT"));
		dataSource.setTargetDataSources(resolvedDataSources);

		dataSource.afterPropertiesSet();
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.show_sql", showSql);
		properties.put("hibernate.generate_statistics", "false");
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan("com.amx.jax"); // TODO: find way to replace this
		em.setJpaVendorAdapter(jpaVendorAdapter());
		em.setJpaPropertyMap(properties);
		return em;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}
}