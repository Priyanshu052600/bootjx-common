package com.boot.jx.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.boot.jx.scope.tnt.TenantDefinations.TenantDefaultQualifier;

@Primary
@Component
public class CommonMongoTemplate extends CommonMongoTemplateAbstract<CommonMongoTemplate> {

	public static class TenantDefaultMongoTemplateImpl extends CommonMongoTemplate {

		@Autowired
		@TenantDefaultQualifier
		protected MongoTemplate mongoTemplateTenantDefault;

		@Override
		protected MongoTemplate getCommonMongoTemplate() {
			return mongoTemplateTenantDefault;
		}

	}

}
