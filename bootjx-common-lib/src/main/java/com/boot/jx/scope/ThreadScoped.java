package com.boot.jx.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.boot.jx.AppConstants;

@Scope(value = AppConstants.Scopes.THREAD, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadScoped {
}