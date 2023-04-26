package com.boot.jx.def;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.cache.annotation.Cacheable;

/**
 * If this annotation is assigned to any method, output of that method will be
 * cached, for target object
 * 
 * @author lalittanwar
 *
 */
@Cacheable(value = CacheForThisKey.CACHE, keyGenerator = CacheForThisKey.KEY)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheForThis {

}