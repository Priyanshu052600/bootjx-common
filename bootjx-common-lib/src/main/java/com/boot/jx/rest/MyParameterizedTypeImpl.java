package com.boot.jx.rest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class MyParameterizedTypeImpl implements ParameterizedType {
	private ParameterizedType delegate;
	private Type[] actualTypeArguments;

	MyParameterizedTypeImpl(ParameterizedType delegate, Type[] actualTypeArguments) {
		this.delegate = delegate;
		this.actualTypeArguments = actualTypeArguments;
	}

	@Override
	public Type[] getActualTypeArguments() {
		return actualTypeArguments;
	}

	@Override
	public Type getRawType() {
		return delegate.getRawType();
	}

	@Override
	public Type getOwnerType() {
		return delegate.getOwnerType();
	}

}