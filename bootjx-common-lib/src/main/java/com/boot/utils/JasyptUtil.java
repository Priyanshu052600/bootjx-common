package com.boot.utils;

import org.jasypt.util.text.BasicTextEncryptor;

public class JasyptUtil {
	public static BasicTextEncryptor textEncryptor = new BasicTextEncryptor();

	public static void setPassword(String privateKey) {
		textEncryptor.setPasswordCharArray(privateKey.toCharArray());
	}

	public static String encrypt(String data) {
		return textEncryptor.encrypt(data);
	}

	public static String decrypt(String privateKey, String data) {
		textEncryptor.setPasswordCharArray(privateKey.toCharArray());
		return textEncryptor.decrypt(data);
	}
}
