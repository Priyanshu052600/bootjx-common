package com.boot.jx.openapi;

import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.boot.jx.AppConfig;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Autowired(required = false)
	private BuildProperties buildProperties;

	@Bean
	public OpenAPI customOpenAPI(AppConfig appConfig) {

		Contact contact = new Contact();
		contact.setEmail("support@example.com");
		contact.setName("Boot Jx");
		String description = getDescription(appConfig);
		String vzn = null;
		String title = appConfig.getAppName();
		if (buildProperties != null) {
			title = buildProperties.getArtifact();
			vzn = buildProperties.getVersion();
		}
		final Info info = new Info().title(title).description(description).version(vzn).contact(contact);
		return new OpenAPI().components(new Components()).info(info);
	}

	private String getDescription(AppConfig appConfig) {
		StringJoiner description = new StringJoiner("#");
		if (StringUtils.isNotBlank(appConfig.getAppName())) {
			description.add(appConfig.getAppName());
		}
		if (StringUtils.isNotBlank(appConfig.getAppDescription())) {
			description.add(appConfig.getAppDescription());
		}
		if (StringUtils.isNotBlank(appConfig.getAppEnv())) {
			description.add(appConfig.getAppEnv());
		}
		if (StringUtils.isNotBlank(appConfig.getAppGroup())) {
			description.add(appConfig.getAppGroup());
		}
		if (StringUtils.isNotBlank(appConfig.getAppId())) {
			description.add(appConfig.getAppId());
		}

		return description.toString();
	}

}
