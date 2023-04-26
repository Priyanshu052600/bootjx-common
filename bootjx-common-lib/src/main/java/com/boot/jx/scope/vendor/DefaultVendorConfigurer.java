package com.boot.jx.scope.vendor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.http.CommonHttpRequest.ApiRequestDetail;
import com.boot.jx.scope.vendor.VendorAuthContext.VendorAuthFilter;
import com.boot.jx.scope.vendor.VendorContext.VendorScoped;
import com.boot.jx.scope.vendor.VendorContext.VendorValue;

@Component
@VendorScoped("DEFAULT")
public class DefaultVendorConfigurer implements VendorAuthFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultVendorConfigurer.class);

	@VendorValue("${vendor.auth.id}")
	String basicAuthUser;

	@Autowired
	VendorAuthService vendorAuthService;

	@Override
	public boolean filterVendorRequest(ApiRequestDetail apiRequest, CommonHttpRequest req, String traceId,
			String authToken) {
		LOGGER.debug("isAuthVendorRequest {} {}", authToken, basicAuthUser);
		return vendorAuthService.hasValidBasicAuth(traceId, authToken) && vendorAuthService.hasFeature(apiRequest)
				&& vendorAuthService.hasValidIp(req);
	}

}