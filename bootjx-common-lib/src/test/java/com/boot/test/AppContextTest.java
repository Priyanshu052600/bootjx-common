package com.boot.test;

import java.text.ParseException;

import com.boot.jx.AppContextUtil;
import com.boot.utils.StringUtils;

public class AppContextTest { // Noncompliant

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		AppContextUtil.loadTraceId("RPD-c8d0a-814KuAeqU2L-000000-000-814Kv6a1raN");

		System.out.println(AppContextUtil.getSessionId(false));

		String flowFix = AppContextUtil.getFlowfix();
		System.out.println(flowFix);
		long flowId = StringUtils.alpha62(flowFix);
		AppContextUtil.setFlowfix(StringUtils.pad(StringUtils.alpha62(++flowId), "000", 1));
		
		System.out.println(AppContextUtil.getTraceId(true, false));
		System.out.println(AppContextUtil.getTraceId(true, true));
	}

}
