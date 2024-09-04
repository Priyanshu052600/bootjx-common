package com.boot.jx.scope.tnt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.http.CommonHttpRequest.ApiRequestDetail;
import com.boot.jx.scope.tnt.TenantAuthContext.TenantAuthFilter;
import com.boot.jx.scope.tnt.TenantDefinations.TenantSpecific;

@Component
@TenantSpecific("DEFAULT")
public class DefaultTenantConfigurer implements TenantAuthFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTenantConfigurer.class);

    @Override
    public boolean filterTenantRequest(ApiRequestDetail apiRequest, CommonHttpRequest req, String traceId) {
	LOGGER.debug("DefaultTenantConfigurer:isAuthorizedTenantRequest {}", traceId);
	return true;
    }

}