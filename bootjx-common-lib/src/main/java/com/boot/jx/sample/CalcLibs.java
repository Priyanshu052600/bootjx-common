package com.boot.jx.sample;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.jx.scope.tnt.TenantContext;

@Service
public class CalcLibs extends TenantContext<CalcLib> {

	@Autowired
	public CalcLibs(List<CalcLib> libs) {
		super(libs);
	}

}
