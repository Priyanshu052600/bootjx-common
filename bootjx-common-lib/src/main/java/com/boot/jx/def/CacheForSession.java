package com.boot.jx.def;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.cache.annotation.Cacheable;

/**
 * If this annotation is assigned to any method, output of that method will be
 * cached, for Current HTTP Session
 * 
 * @author lalittanwar
 *
 */
@Cacheable(value = CacheForSessionKey.CACHE, keyGenerator = CacheForSessionKey.KEY)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheForSession {

}