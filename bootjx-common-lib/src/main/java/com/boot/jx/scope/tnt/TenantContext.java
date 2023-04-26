package com.boot.jx.scope.tnt;

import java.util.List;

import com.boot.common.ScopedBeanFactory;

public class TenantContext<T> extends ScopedBeanFactory<String, T> {

    private static final long serialVersionUID = 4007091611441725719L;

    public static final String DEFAULT_VENDOR_KEY = "*";

    public TenantContext(List<T> libs) {
	super(libs);
    }

    @Override
    public String[] getKeys(T lib) {
	TenantSpecific annotation = lib.getClass().getAnnotation(TenantSpecific.class);
	if (annotation != null) {
	    return annotation.value();
	}
	return null;
    }

    @Override
    public String getKey() {
	return TenantContextHolder.currentSite();
    }

    @Override
    public String getDefaultKey() {
	return DEFAULT_VENDOR_KEY;
    }
}
