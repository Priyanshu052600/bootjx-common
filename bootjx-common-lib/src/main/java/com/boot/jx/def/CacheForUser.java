package com.boot.jx.def;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.cache.annotation.Cacheable;

/**
 * If this annotation is assigned to any method, output of that method will be
 * cached, for user scope using userId as unique ID
 * 
 * @author lalittanwar
 *
 */
@Cacheable(value = CacheForUserKey.CACHE, keyGenerator = CacheForUserKey.KEY)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheForUser {

}