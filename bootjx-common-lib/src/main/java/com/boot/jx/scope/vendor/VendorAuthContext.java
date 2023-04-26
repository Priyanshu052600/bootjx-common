package com.boot.jx.scope.vendor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.http.CommonHttpRequest.ApiRequestDetail;
import com.boot.jx.scope.vendor.VendorAuthContext.VendorAuthFilter;

@Component
public class VendorAuthContext extends VendorContext<VendorAuthFilter> {

    private static final long serialVersionUID = 8926326964205798155L;

    public interface VendorAuthFilter {
	public boolean filterVendorRequest(ApiRequestDetail apiRequest, CommonHttpRequest req, String traceId,
		String authToken);
    }

    @Autowired
    public VendorAuthContext(List<VendorAuthFilter> libs) {
	super(libs);
    }

    @Override
    public String getKey() {
	String vendor = VendorContext.getVendor();
	return vendor;
    };

    @Override
    public VendorAuthFilter get() {
	VendorAuthFilter vendorAuthFilter = super.get();
	if (vendorAuthFilter == null) {
	    return get("DEFAULT");
	}
	return vendorAuthFilter;
    };

}
