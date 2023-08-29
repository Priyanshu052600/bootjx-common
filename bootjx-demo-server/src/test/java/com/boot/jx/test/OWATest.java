package com.boot.jx.test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionException;

public class OWATest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");

	private static Logger LOGGER = LoggerFactory.getLogger(OWATest.class);

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws ExpressionException
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */

	public static void main(String[] args) throws MalformedURLException, URISyntaxException {
		String[] keys = "32care:64c15721d8be2e37457633f9-6b383d0c-26cc-4741-b308-463e14d3b33f".split("\\-",2);
		
		String[] apiIndex = keys[0].split("\\:", 2);

		String clientId = apiIndex[0];
		String apiId = apiIndex[1];
		String apiSecret = keys[1];
		
		System.out.println("clientId : " + clientId);
		System.out.println("apiId : " + apiId);
		System.out.println("apiSecret : " + apiSecret);
		
		
		//System.out.println(CryptoUtil.getEncoder().message("pass4").sha2().toString());
	}
}
