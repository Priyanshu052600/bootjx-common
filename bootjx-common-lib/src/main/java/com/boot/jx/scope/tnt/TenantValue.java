package com.boot.jx.scope.tnt;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Supports Type, java.lang.String, int, java.lang.Integer, boolean,
 * java.lang.Boolean
 * 
 * @author lalittanwar
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TenantValue {
	String value();
}
