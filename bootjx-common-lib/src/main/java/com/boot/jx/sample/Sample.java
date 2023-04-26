package com.boot.jx.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sample {

	@Autowired
	CalcLibs calcLibs;

	public String getRSName() {
		return calcLibs.get().getRSName();
	}

}
