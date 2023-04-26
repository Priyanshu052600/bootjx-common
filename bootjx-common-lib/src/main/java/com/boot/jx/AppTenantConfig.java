package com.boot.jx;

import org.springframework.stereotype.Component;

import com.boot.jx.scope.tnt.TenantScoped;
import com.boot.jx.scope.tnt.TenantValue;

@Component
@TenantScoped
public class AppTenantConfig {

	@TenantValue("${encrypted.tenant.property}")
	String tenantSpecifcDecryptedProp;

	public String getTenantSpecifcDecryptedProp() {
		return tenantSpecifcDecryptedProp;
	}

	@TenantValue("${encrypted.tenant.property2}")
	String tenantSpecifcDecryptedProp2;

	public String getTenantSpecifcDecryptedProp2() {
		return tenantSpecifcDecryptedProp2;
	}
}
