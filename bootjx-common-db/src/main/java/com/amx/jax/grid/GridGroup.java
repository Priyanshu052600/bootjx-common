package com.amx.jax.grid;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * If this annotation is assigned to any method, output of that method will be
 * cached, for tenant scope
 * 
 * @author lalittanwar
 *
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface GridGroup {
	String value() default "";
}