package com.boot.jx.scope.tnt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.jx.AppContextUtil;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.http.CommonHttpRequest.ApiRequestDetail;
import com.boot.jx.scope.tnt.TenantAuthContext.TenantAuthFilter;

@Component
public class TenantAuthContext extends TenantContext<TenantAuthFilter> {

	private static final long serialVersionUID = 8926326964205798155L;

	public interface TenantAuthFilter {
		public boolean filterTenantRequest(ApiRequestDetail apiRequest, CommonHttpRequest req, String traceId);
	}

	@Autowired
	public TenantAuthContext(List<TenantAuthFilter> libs) {
		super(libs);
	}

	@Override
	public String getKey() {
		String tnt = AppContextUtil.getTenant();
		return tnt;
	};

	@Override
	public TenantAuthFilter get() {
		TenantAuthFilter tenantAuthFilter = super.get();
		if (tenantAuthFilter == null) {
			return get("DEFAULT");
		}
		return tenantAuthFilter;
	};

}
