package com.boot.jx.mongo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty("spring.data.mongodb.uri")
public class CommonMongoTemplate extends CommonMongoTemplateAbstract<CommonMongoTemplate>{


}
