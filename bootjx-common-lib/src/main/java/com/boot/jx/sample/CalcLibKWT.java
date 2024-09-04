package com.boot.jx.sample;

import org.springframework.stereotype.Component;

import com.boot.jx.scope.tnt.TenantDefinations.TenantSpecific;

@Component
@TenantSpecific("KWT")
public class CalcLibKWT implements CalcLib {

	@Override
	public String getRSName() {
		return "TenantKwt is Here";
	}

}
