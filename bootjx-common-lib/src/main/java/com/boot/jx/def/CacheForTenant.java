package com.boot.jx.def;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.cache.annotation.Cacheable;

/**
 * If this annotation is assigned to any method, output of that method will be
 * cached, for tenant scope
 * 
 * @author lalittanwar
 *
 */
@Cacheable(value = CacheForTenantKey.CACHE, keyGenerator = CacheForTenantKey.KEY)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheForTenant {

}