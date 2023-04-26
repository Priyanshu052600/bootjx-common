package com.boot.jx.tunnel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.boot.jx.async.ExecutorConfig;
import com.boot.utils.Constants;

@Component
@Async(ExecutorConfig.EXECUTER_WORKER)
@Retention(RetentionPolicy.RUNTIME)
public @interface TunnelEventMapping {

	TunnelEventXchange scheme() default TunnelEventXchange.SHOUT_LISTNER;

	String topic() default Constants.BLANK;

	Class<?> byEvent() default TunnelEvent.class;

	/**
	 * If set to true, only one instance of same microservices (behind LB) will be
	 * able to process the message
	 * 
	 * @return
	 */
	boolean integrity() default true;

	/**
	 * @deprecated
	 * 
	 * @return
	 */
	@Deprecated
	boolean queued() default false;
}
