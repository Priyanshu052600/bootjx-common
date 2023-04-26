package com.boot.jx.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.http.CommonHttpRequest.ApiRequestDetail;
import com.boot.jx.rest.AppRequestInterfaces.AppAuthFilter;
import com.boot.jx.rest.AppRequestInterfaces.AppAuthUser;
import com.boot.utils.ArgUtil;

@Component
public class DefaultAppAuthFilter implements AppAuthFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAppAuthFilter.class);

    @Autowired(required = false)
    private AppAuthUser appAuthUser;

    @Override
    public boolean filterAppRequest(ApiRequestDetail apiRequest, CommonHttpRequest req, String traceId) {
	if (ArgUtil.is(appAuthUser)) {
	    return appAuthUser.hasAccess(apiRequest, req);
	}
	return true;
    }

}