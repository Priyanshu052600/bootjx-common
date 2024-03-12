package com.amx.jax.limitter.throttle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Throttled {

    /**
     * Delay in sec
     * 
     * @return throttle delay defauls to 5 sec
     */
    int delay() default 5;
}
