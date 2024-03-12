package com.amx.jax.limitter.throttle;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RMap;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(10)
public class ThrottleAspect {

    @Autowired(required = false)
    RedissonClient redis;

    private static final Logger log = LoggerFactory.getLogger(ThrottleAspect.class);

    @Around(value = "@annotation(com.amx.jax.limitter.throttle.Throttled)")
    public void aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] arguments = proceedingJoinPoint.getArgs();
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        String className = method.getDeclaringClass().getName();
        Throttled throttled = method.getAnnotation(Throttled.class);

        StringBuilder sBuild = new StringBuilder();
        sBuild.append(className);
        sBuild.append("#");
        sBuild.append(methodName);
        sBuild.append("#");

        if (arguments.length > 0) {
            for (Object arg : arguments) {
                if (arg != null) {
                    sBuild.append(arg.toString());
                    sBuild.append("#");
                }
            }
        }
        String key = sBuild.toString();
        RRateLimiter limiter = redis.getRateLimiter(key);
        limiter.trySetRate(RateType.OVERALL, 1, throttled.delay(), RateIntervalUnit.SECONDS);
        boolean success = limiter.tryAcquire(1);
        RMap<Object, Object> keyMap = redis.getMap(key);
        if (keyMap != null) {
            keyMap.expire(throttled.delay(), TimeUnit.SECONDS);
        }
        if (success) {
            proceedingJoinPoint.proceed();
        } else {
            log.info("Call throttled, key= {}", key);
        }
    }

}
