package com.boot.jx.rest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.core.ParameterizedTypeReference;

public class FakeParameterizedTypeReference<T> extends ParameterizedTypeReference<T> {

	Type wrapperType;
	Type resultType;
	Type metaType;

	public FakeParameterizedTypeReference(Object obj, Type resultType, Type metaType) {
		this.wrapperType = obj.getClass();
		this.resultType = resultType;
		this.metaType = metaType;
	}

	@Override
	public Type getType() {
		return new MyParameterizedTypeImpl((ParameterizedType) wrapperType, new Type[] { resultType, metaType });
	}
}