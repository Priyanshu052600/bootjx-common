package com.boot.jx.sample;

import org.springframework.stereotype.Component;

import com.boot.jx.scope.tnt.TenantSpecific;

@Component
@TenantSpecific({ "OMN", "KWT2" })
public class CalcLibOMN implements CalcLib {

	@Override
	public String getRSName() {
		return "TenantKwt_D is Here";
	}

}
