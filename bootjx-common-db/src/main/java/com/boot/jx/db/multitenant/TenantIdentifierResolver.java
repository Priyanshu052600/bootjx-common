package com.boot.jx.db.multitenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import com.boot.jx.scope.tnt.TenantContextHolder;
import com.boot.jx.scope.tnt.Tenants;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

	@Override
	public String resolveCurrentTenantIdentifier() {
		String tenant = TenantContextHolder.currentSite();
		if (tenant != null) {
			return tenant.toString();
		}
		return Tenants.DEFAULT_STR;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}
}
