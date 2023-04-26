package com.bootloaderjs;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import com.boot.utils.CryptoUtil;
import com.boot.utils.CryptoUtil.HashBuilder;

public class HmacTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.println(CryptoUtil.getSHA2Hash("some pass"));
	}

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main2(String[] args) {

		/*
		 * public static boolean validateHMAC(long currentTime, long interval, long
		 * tolerance, String secretKey, String message, String hash) {
		 * LOGGER.debug("validateHMAC I:{interval} S:{secretKey} M:{message}
		 * C:{currentTime} H:{hash} T:{tolerance}", interval, secretKey, message,
		 * currentTime, hash, tolerance);
		 * 
		 * I:28800 S:21276
		 * M:5F22C4A4F14D9102F124416059841D31C8E0896B5233986DA372C6B9FCC88865
		 * C:1574145958438
		 * H:58F9F20B4117BF8F824D3C122070C65BAD54C102B57B795AA4271081B19A46B2 T:28800
		 * I:28800 S:21276
		 * M:5F22C4A4F14D9102F124416059841D31C8E0896B5233986DA372C6B9FCC88865
		 * C:1574145958438 H:hash T:28800
		 * 
		 * I:60 S:HG85XJKFD3 M:21276 C:1574145958439
		 * H:A7E51403BB2C5C57BE14D27FE6BC219C8C39F275F01629204DD9FF8D239592B3 T:60 I:30
		 * S:HG85XJKFD3 M:21276 C:1574145958439
		 * H:A7E51403BB2C5C57BE14D27FE6BC219C8C39F275F01629204DD9FF8D239592B3 T:30
		 */

		// validateHMAC I:60
		// S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369 M:5KE3ZQ
		// C:1573114585707 H:3 T:60
		// validateHMAC I:60
		// S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369 M:5KE3ZQ
		// C:1573114585707 H:3 T:60

		// validateHMAC I:60
		// S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369 M:EET8AP
		// C:1573112719915 H:r T:60
//		System.out.println(CryptoUtil.validateComplexHMAC(1573114585707L, 60L, 60,
//				"S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369", "5KE3ZQ", "3"));

		/*
		 * System.out.println(CryptoUtil.validateHMAC(1574145993263L, 30, 30,
		 * "HG85XJKFD3", "21276",
		 * "A7E51403BB2C5C57BE14D27FE6BC219C8C39F275F01629204DD9FF8D239592B3"));
		 * 
		 * System.out.println( new
		 * HashBuilder().currentTime(1574145993263L).interval(30).secret("HG85XJKFD3").
		 * message("21276") .toHMAC().output());
		 */

		System.out.println(
				"==== " + new HashBuilder().currentTime(1574245195319L).interval(30).tolerance(60).secret("EJZQCU19H9")
						.message("21279").validate("E958E9E0847B0C06D462867BB33F63A40BC9F1B6629272E7F32354914A7F146D"));

		System.out.println(new HashBuilder().currentTime(1574244963435L - 0 * 1000).interval(30).secret("MYZK1GST1B")
				.message("21279").toHMAC().hash());

//		System.out.println(CryptoUtil.validateHMAC(1574145993263L, 60, 60,
//				"HG85XJKFD3", "21276", 
//				"A7E51403BB2C5C57BE14D27FE6BC219C8C39F275F01629204DD9FF8D239592B3"));

//		System.out.println(CryptoUtil.validateComplexHMAC(1573114585707L, 60L, 60,
//				"S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369", "5KE3ZQ", "zmBHL7"));
//		
//		System.out.println(CryptoUtil.validateNumHMAC(1573114585707L, 60L, 60,
//				"S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369", "5KE3ZQ", "zmBHL7"));
//		
//		System.out.println(CryptoUtil.validateComplexHMAC(1573114585707L, 60L, 60,
//				"S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369", "5KE3ZQ", "S59hL6"));
//		
//		System.out.println(CryptoUtil.validateNumHMAC(1573114585707L, 60L, 60,
//				"S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369", "5KE3ZQ", "S59hL6"));
	}

}
