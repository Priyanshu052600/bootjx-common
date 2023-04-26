package com.boot.test;

import java.util.regex.Pattern;

import com.boot.libs.SixBitEnDec;
import com.boot.utils.CryptoUtil;

public class EncoderTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	public static final SixBitEnDec dec = new SixBitEnDec();

	public static class TestClass {
		private int num;
		private String str;

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public String getStr() {
			return str;
		}

		public void setStr(String str) {
			this.str = str;
		}
	}

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		TestClass p = new TestClass();
		p.setNum(3);
		p.setStr("Test");
		String ecd = new CryptoUtil.Encoder().message("23232").encrypt().encodeBase64().toString();
		String p2 = new CryptoUtil.Encoder().message(ecd).decodeBase64().decrypt().toString();
		System.out.println(ecd + "   " + p2);

		String testString = "0";
		String encr = new CryptoUtil.Encoder().message(testString).encrypt().encodeBase64().toString();
		System.out.println("B64   " + encr);
		System.out.println("B64   " + new CryptoUtil.Encoder().message("TkNldGxaMXBUWk9qWVdpQ1R5NHhkUT09").decodeBase64().decrypt().toString());
	}

}
