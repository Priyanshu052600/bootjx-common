package com.bootloaderjs;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

import com.boot.utils.FileUtil;

public class FileTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws ParseException
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ParseException, IOException {
		Date sysDate = new Date(0L);
//		System.out.println("====="+
//				FileUtil.normalize("/Users/Animesh/Documents/adapter_client/application.env.properties")
//				);
		
		System.out.println(
				FileUtil.getExternalResourceAsStream("application.env.properties", FileTest.class));
	}

}
