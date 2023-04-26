package com.boot.jx.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.boot.jx.dict.UserClient.Channel;
import com.boot.utils.Constants;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiRequest {

	public enum ResponeError {
		OK, PROPAGATE, SUPPRESS, DEFAULT
	}

	RequestType type() default RequestType.DEFAULT;

	String deprecated() default Constants.BLANK;

	boolean useAuthKey() default false;

	boolean useAuthToken() default false;

	String tenant() default Constants.BLANK;

	String flow() default Constants.BLANK;

	String feature() default Constants.BLANK;

	String[] rules() default Constants.BLANK;

	String tracefilter() default Constants.BLANK;

	boolean authenticateTenant() default false;

	ResponeError responeError() default ResponeError.DEFAULT;

	Channel channel() default Channel.UNKNOWN;

	/**
	 * To Reset the context Key
	 * 
	 * @return
	 */
	boolean initFlow() default false;

	boolean session() default false;

}