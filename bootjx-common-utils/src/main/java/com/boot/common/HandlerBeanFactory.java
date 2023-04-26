package com.boot.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import org.springframework.context.annotation.Lazy;

public class HandlerBeanFactory<T> extends ScopedBeanFactory<String, T> {

	private static final long serialVersionUID = 4007091611441725719L;

	@Retention(RetentionPolicy.RUNTIME)
	@Lazy
	public @interface HandlerMapping {
		String[] value();
	}

	public HandlerBeanFactory(List<T> libs) {
		super(libs);
	}

	@Override
	public String[] getKeys(T lib) {
		HandlerMapping annotation = lib.getClass().getAnnotation(HandlerMapping.class);
		if (annotation != null) {
			return annotation.value();
		}
		return null;
	}

}
