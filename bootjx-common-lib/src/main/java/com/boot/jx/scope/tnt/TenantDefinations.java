package com.boot.jx.scope.tnt;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;

import com.boot.utils.Constants;

public class TenantDefinations {

	@Retention(RetentionPolicy.RUNTIME)
	@Lazy
	public @interface TenantSpecific {
		String[] value() default Constants.BLANK;
	}

	@Qualifier
	@Retention(RetentionPolicy.RUNTIME)
	public @interface TenantDefaultQualifier {
	}
}
