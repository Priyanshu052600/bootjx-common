package com.boot.utils;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

public class OTPUtils {

	public static class OTPDetails implements Serializable {
		public OTPDetails() {
			super();
			this.timestamp = System.currentTimeMillis();
		}

		private static final long serialVersionUID = -2043308399129271937L;
		private long timestamp;
		private String id;
		private String yin;
		private String yang;
		private String key;
		private String hash;
		private String otp;
		private String prefix;

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getOtp() {
			return otp;
		}

		public void setOtp(String otp) {
			this.otp = otp;
		}

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}

		public String getHash() {
			return hash;
		}

		public void setHash(String hash) {
			this.hash = hash;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public boolean isValid(String otp) {
			try {
				return this.getHash().equals(CryptoUtil.getSHA1Hash(otp));
			} catch (NoSuchAlgorithmException e) {
				return false;
			}
		}

		public boolean validate(String otp, String otpHash) {
			try {
				return this.getHash().equals(CryptoUtil.getSHA1Hash(otp)) && this.getHash().equals(otpHash);
			} catch (NoSuchAlgorithmException e) {
				return false;
			}
		}

		public String getYin() {
			return yin;
		}

		public void setYin(String yin) {
			this.yin = yin;
		}

		public String getYang() {
			return yang;
		}

		public void setYang(String yang) {
			this.yang = yang;
		}

		public OTPDetails yin(String yin) {
			this.yin = yin;
			return this;
		}

		public OTPDetails id(String id) {
			this.id = id;
			return this;
		}

		public OTPDetails yang(String yang) {
			this.yang = yang;
			return this;
		}

		public OTPDetails genrate(String authid, String context) {
			this.id = ArgUtil.nonEmpty(this.id, this.yin + this.yang);
			String saltedDetailString = String.join("#", this.id, authid, context);
			this.setPrefix(CryptoUtil.toAlpha(3, saltedDetailString).toString().toUpperCase());
			this.setOtp(CryptoUtil.toNumeric(6, saltedDetailString).toString().toUpperCase());
			try {
				this.setHash(CryptoUtil.getSHA1Hash(this.getOtp()));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return this;
		}

	}

	public static String genrateUniqueKey(String authid, String context) {
		return String.join("#", authid, context);
	}

	public static OTPDetails getBasicOTP(String id, String authid, String context) {
		OTPDetails details = new OTPDetails();
		details.setId(id);
		int partlength = id.length() / 2;
		details.setYin(id.substring(0, partlength));
		details.setYang(id.substring(partlength, id.length()));
		details.setKey(genrateUniqueKey(authid, context));
		return details.genrate(authid, context);
	}

	public static OTPDetails genrateBasicOTP(String authid, String context) {
		return getBasicOTP(Random.randomAlphaNumeric(16), authid, context);
	}

	public static boolean validateBasicOTP(String id, String authid, String context, String otp) {
		return getBasicOTP(id, authid, context).isValid(otp);
	}

}
