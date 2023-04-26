package com.boot.jx.def;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

@Component
public class CacheForThisKey implements KeyGenerator {

	public static final String CACHE = "othis";
	public static final String KEY = "cacheForThisKey";

	@Override
	public Object generate(Object target, Method method, Object... params) {
		StringBuilder b = new StringBuilder(
				target.getClass().getName() + "#" + String.valueOf(target.hashCode()) + "#" + method.getName());
		if (params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				b.append("#" + params[i]);
			}
		}
		return b.toString();
	}

}