package com.boot.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boot.libs.SixBitEnDec;
import com.boot.model.MapModel;

public final class CryptoUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(CryptoUtil.class);

	private final static int INTERVAL = 30; // 30 secs
	private final static int TOLERANCE = 30; // 30 secs
	// private final static int INTERVAL_MILLIS = INTERVAL * 1000;
	// private final static String ALGO_SHA1 = "SHA1";
	private final static String PASS_DELIMITER = "#";
	private final static String DEFAULT_ENCODING = "UTF-8";
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	/** The Constant PAD_ZERO. */
	private static final String PAD_ZERO = "0";

	/** The Constant MD5. */
	private static final String MD5 = "MD5";

	/** The Constant SHA1. */
	private static final String SHA1 = "SHA1";

	/** The Constant SHA2. */
	private static final String SHA2 = "SHA-256";

	/**
	 * 
	 * @param interval    : In Seconds
	 * @param secretKey
	 * @param message
	 * @param currentTime : In MilliSeconds
	 * @return
	 */
	public static String generateHMAC(long interval, String secretKey, String message, long currentTime) {
		try {

			// Long epoch = Math.round(currentTime / 1000.0);

			String elapsed = Long.toString(currentTime / (interval * 1000));
			String password = String.join(PASS_DELIMITER, elapsed, secretKey, message);
			// System.out.println(interval + " " + secretKey + " " + message + " " +
			// currentTime + " " + password);
			MessageDigest md = MessageDigest.getInstance(SHA2);
			ByteArrayOutputStream pwsalt = new ByteArrayOutputStream();
			pwsalt.write(password.getBytes(DEFAULT_ENCODING));
			byte[] unhashedBytes = pwsalt.toByteArray();
			byte[] digestVonPassword = md.digest(unhashedBytes);
			return bytesToHex(digestVonPassword);
		} catch (NoSuchAlgorithmException | IOException e) {
			LOGGER.error("HMAC Error", e);
		}
		return null;
	}

	/**
	 * 
	 * @param interval  in seconds
	 * @param secretKey
	 * @param message
	 * @return
	 */
	public static String generateHMAC(long interval, String secretKey, String message) {
		String publicToken = generateHMAC(interval, secretKey, message, System.currentTimeMillis());
		return publicToken;
	}

	public static String generateHMAC(String secretKey, String message, long currentTime) {
		return generateHMAC(INTERVAL, secretKey, message, currentTime);
	}

	public static String generateHMAC(String secretKey, String message) {
		return generateHMAC(secretKey, message, System.currentTimeMillis());
	}

	public static boolean validateHMAC(long currentTime, long interval, long tolerance, String secretKey,
			String message, String hash) {

		LOGGER.debug("validateHMAC C:{} I:{} T:{} S:{} M:{} H:{}", currentTime, interval, tolerance, secretKey, message,
				hash);

		if (generateHMAC(interval, secretKey, message, currentTime).equals(hash)) {
			return true;
		} else if (generateHMAC(interval, secretKey, message, currentTime - tolerance * 1000).equals(hash)) {
			return true;
		} else if (generateHMAC(interval, secretKey, message, currentTime + tolerance * 1000).equals(hash)) {
			return true;
		}
		return false;
	}

	public static boolean validateNumHMAC(long currentTime, long interval, long tolerance, String secretKey,
			String message, String numHash, int length) {

		LOGGER.debug("validateHMAC C:{} I:{} T:{} S:{} M:{} H:{}", currentTime, interval, tolerance, secretKey, message,
				numHash);

		if (toNumeric(length, generateHMAC(interval, secretKey, message, currentTime)).equals(numHash)) {
			return true;
		} else if (toNumeric(length, generateHMAC(interval, secretKey, message, currentTime - tolerance * 1000))
				.equals(numHash)) {
			return true;
		} else if (toNumeric(length, generateHMAC(interval, secretKey, message, currentTime + tolerance * 1000))
				.equals(numHash)) {
			return true;
		}
		return false;
	}

	public static boolean validateNumHMAC(long currentTime, long interval, long tolerance, String secretKey,
			String message, String numHash) {
		return validateNumHMAC(currentTime, interval, tolerance, secretKey, message, numHash, numHash.length());
	}

	public static boolean validateComplexHMAC(long currentTime, long interval, long tolerance, String secretKey,
			String message, String complexHash, int length) {

		LOGGER.debug("validateHMAC C:{} I:{} T:{} S:{} M:{} H:{}", currentTime, interval, tolerance, secretKey, message,
				complexHash);

		if (toComplex(length, generateHMAC(interval, secretKey, message, currentTime)).equals(complexHash)) {
			return true;
		} else if (toComplex(length, generateHMAC(interval, secretKey, message, currentTime - tolerance * 1000))
				.equals(complexHash)) {
			return true;
		} else if (toComplex(length, generateHMAC(interval, secretKey, message, currentTime + tolerance * 1000))
				.equals(complexHash)) {
			return true;
		}
		return false;
	}

	public static boolean validateComplexHMAC(long currentTime, long interval, long tolerance, String secretKey,
			String message, String complexHash) {
		return validateComplexHMAC(currentTime, interval, tolerance, secretKey, message, complexHash,
				complexHash.length());
	}

	@Deprecated
	public static boolean validateHMAC(long interval, String secretKey, String message, long currentTime, String hash) {
		return validateHMAC(currentTime, interval, interval, secretKey, message, hash);
	}

	public static boolean validateHMAC(long interval, long tolerance, String secretKey, String message, String hash) {
		return validateHMAC(System.currentTimeMillis(), interval, tolerance, secretKey, message, hash);
	}

	@Deprecated
	public static boolean validateHMAC(long interval, String secretKey, String message, String hash) {
		return validateHMAC(interval, secretKey, message, System.currentTimeMillis(), hash);
	}

	public static boolean validateHMAC(String secretKey, String message, String publicToken) {
		return validateHMAC(INTERVAL, TOLERANCE, secretKey, message, publicToken);
	}

	public static String toNumeric(int length, String hash) {
		char[] hashChars = hash.toCharArray();
		int totalInt = 0;
		for (int i = 0; i < hashChars.length; i++) {
			int cint = hashChars[i];
			totalInt = (cint * cint * i) + totalInt;
		}
		long hashCode = Math.max(totalInt % Math.round(Math.pow(10, length)), 2);
		int passLenDiff = (length - String.valueOf(hashCode).length());
		long passLenFill = Math.max(Math.round(Math.pow(10, passLenDiff)) - 1, 1);
		return ArgUtil.parseAsString(hashCode * passLenFill);
	}

	// private static final String COMPLEX_CHARS =
	// "!@#$%^&*?0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String COMPLEX_CHARS = "0123456789abcdefghijkLmnopqrstuvwxyzABCDEFGHiJKLMNOPQRSTUVWXYZ";
	public static final String ALPHA_CHARS = "abcdefghijklmnopqrstuvwxyz";

	public static class ComplexString {
		private String str;

		public ComplexString(String str) {
			this.str = str;
		}

		public String toString() {
			return this.str;
		}

		public boolean equals(String str) {
			if (this.str.equals(str)) {
				return true;
			}
			str = str.replace("l", "L").replace("I", "i");
			if (this.str.equals(str)) {
				return true;
			}
			return this.str.replace("L", "i").equals(str.replace("L", "i"));
		}
	}

	public static ComplexString toCharSet(int length, String hash, String charSet) {
		char[] hashChars = hash.toCharArray();
		int totalInt = 0;
		for (int i = 0; i < hashChars.length; i++) {
			int cint = hashChars[i];
			totalInt = (cint * cint * i) + totalInt;
		}
		int vlength = length * 2;
		long hashCode = Math.max(totalInt % Math.round(Math.pow(10, vlength)), 2);
		int passLenDiff = (vlength - String.valueOf(hashCode).length());
		long passLenFill = Math.max(Math.round(Math.pow(10, passLenDiff)) - 1, 1);
		long next = hashCode * passLenFill;
		StringBuilder complexHash = new StringBuilder();
		while (next > 0) {
			long thisIndex = next % 100;
			next = (next - thisIndex) / 100;
			thisIndex = thisIndex % charSet.length();
			complexHash.append(charSet.charAt((int) thisIndex));
		}
		return new ComplexString(complexHash.toString());
	}

	public static ComplexString toComplex(int length, String hash) {
		return toCharSet(length, hash, COMPLEX_CHARS);
	}

	public static ComplexString toAlpha(int length, String hash) {
		return toCharSet(length, hash, ALPHA_CHARS);
	}

	public static String toHex(int length, String hash) {
		// length = length*2;
		char[] hashChars = hash.toCharArray();
		int totalInt = 0;
		for (int i = 0; i < hashChars.length; i++) {
			int cint = hashChars[i];
			totalInt = (cint * cint * i) + totalInt;
		}
		long hashCode = Math.max(totalInt % Math.round(Math.pow(16, length)), 2);
		int passLenDiff = (length - String.valueOf(Long.toHexString(hashCode)).length());
		long passLenFill = Math.max(Math.round(Math.pow(16, passLenDiff)) - 1, 1);
		// return (Long.toHexString(hashCode * passLenFill));
		return (Long.toHexString(hashCode * passLenFill) + "FFFFFF").substring(0, length);
	}

	private static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * Gets the m d5 hash.
	 *
	 * @param str the str
	 * @return md5 hashed string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public static String getMD5Hash(String str) throws NoSuchAlgorithmException {
		return getMD5Hash(str.getBytes());
	}

	/**
	 * Gets the m d5 hash.
	 *
	 * @param byteArray the byte array
	 * @return md5 hashed string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public static String getMD5Hash(byte[] byteArray) throws NoSuchAlgorithmException {
		return getHashedStrFor(byteArray, MD5);
	}

	/**
	 * Gets the SH a1 hash.
	 *
	 * @param str the str
	 * @return sha1 hashed string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public static String getSHA1Hash(String str) throws NoSuchAlgorithmException {
		return getSHA1Hash(str.getBytes());
	}

	/**
	 * Gets the SH a1 hash.
	 *
	 * @param byteArray the byte array
	 * @return sha1 hashed string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public static String getSHA1Hash(byte[] byteArray) throws NoSuchAlgorithmException {
		return getHashedStrFor(byteArray, SHA1);
	}

	/**
	 * Gets the SH a2 hash.
	 *
	 * @param str the str
	 * @return sha2 hashed string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public static String getSHA2Hash(String str) throws NoSuchAlgorithmException {
		return getSHA2Hash(str.getBytes());
	}

	/**
	 * Gets the SH a2 hash.
	 *
	 * @param byteArray the byte array
	 * @return sha2 hashed string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public static String getSHA2Hash(byte[] byteArray) throws NoSuchAlgorithmException {
		return getHashedStrFor(byteArray, SHA2);
	}

	/**
	 * Gets the hashed str for.
	 *
	 * @param byteArray the byte array
	 * @param algorithm the algorithm
	 * @return the hashed str for
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	private static String getHashedStrFor(byte[] byteArray, String algorithm) throws NoSuchAlgorithmException {

		MessageDigest msgDigest = MessageDigest.getInstance(algorithm);
		msgDigest.reset();
		msgDigest.update(byteArray);
		byte[] digest = msgDigest.digest();
		BigInteger bigInt = new BigInteger(1, digest);
		String hashtext = bigInt.toString(16);
		// Pad it to get full 32 chars.
		while (hashtext.length() < 32) {
			hashtext = PAD_ZERO + hashtext;
		}

		return hashtext;

	}

	/**
	 * hash_hmac in java
	 * 
	 * @param byteArray
	 * @param algorithm
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String getHashHmac(String baseString, String keyString)
			throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKey secretKey = null;
		byte[] keyBytes = keyString.getBytes();
		secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(secretKey);
		byte[] text = baseString.getBytes();
		return new String(Base64.getEncoder().encode(mac.doFinal(text))).trim();
	}

	public static String getHashHmac(String algo, String data, String key)
			throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKey secretKey = null;
		byte[] keyBytes = key.getBytes();
		secretKey = new SecretKeySpec(keyBytes, algo);
		Mac mac = Mac.getInstance(algo);
		mac.init(secretKey);
		byte[] text = data.getBytes();
		return new String(Base64.getEncoder().encode(mac.doFinal(text))).trim();
	}

	public static class HashBuilder implements Serializable {
		private static final long serialVersionUID = 3866060536613924880L;
		private long interval;
		private long tolerance;
		private String secret;
		private String message;
		private long currentTime;
		private String output;
		private String hash;
		private int length;
		private boolean isToleranceSet;

		public HashBuilder() {
			this.currentTime = System.currentTimeMillis();
			this.interval = INTERVAL;
			this.tolerance = TOLERANCE;
			this.length = 0;
			this.isToleranceSet = false;
		}

		/**
		 * Interval in seconds
		 * 
		 * @param interval
		 * @return
		 */
		public HashBuilder interval(long interval) {
			this.interval = interval;
			return this;
		}

		public HashBuilder tolerance(long tolerance) {
			this.tolerance = tolerance;
			this.isToleranceSet = true;
			return this;
		}

		/**
		 * Current Time stamp in milliseconds default taken from
		 * System.currentTimeMillis()
		 * 
		 * @param currentTime
		 * @return
		 */
		public HashBuilder currentTime(long currentTime) {
			this.currentTime = currentTime;
			return this;
		}

		public HashBuilder secret(String secret) {
			this.secret = secret;
			return this;
		}

		public HashBuilder hash(String hash) {
			this.hash = hash;
			return this;
		}

		public HashBuilder length(int length) {
			this.length = length;
			return this;
		}

		public HashBuilder message(String message) {
			this.message = message;
			return this;
		}

		/**
		 * Generates SHA2 based HASH from params
		 * 
		 * @return
		 */
		public HashBuilder toSHA2() {
			try {
				this.hash = CryptoUtil.getSHA2Hash(this.message);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return this;
		}

		/**
		 * Generates time based HASH from params, with given interval in SECONDS
		 * 
		 * @return
		 */
		public HashBuilder toHmac(long interval) {
			this.hash = CryptoUtil.generateHMAC(this.interval, this.secret, this.message, this.currentTime);
			return this;
		}

		public HashBuilder toHMAC() {
			return this.toHmac(this.interval);
		}

		public HashBuilder toHmac() {
			return this.toHmac(this.interval);
		}

		/**
		 * Generates timeless HASH from params, with given halgo
		 * 
		 * @param algo
		 * @return
		 * @throws InvalidKeyException
		 * @throws NoSuchAlgorithmException
		 */
		public HashBuilder toHmac(String algo) {
			try {
				this.hash = CryptoUtil.getHashHmac(algo, this.message, this.secret);
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return this;
		}

		public HashBuilder toHmacSHA256() {
			return this.toHmac("HmacSHA256");
		}

		/**
		 * Converts generated has to Numeric Representation. Should be called post Any
		 * Hash Function only.
		 * 
		 * @param length
		 * @return
		 */
		public HashBuilder toNumeric(int length) {
			this.output = CryptoUtil.toNumeric(length, this.hash);
			return this;
		}

		public HashBuilder toNumeric() {
			this.output = CryptoUtil.toNumeric(this.length, this.hash);
			return this;
		}

		public HashBuilder toComplex(int length) {
			this.output = CryptoUtil.toComplex(length, this.hash).toString();
			return this;
		}

		public HashBuilder toComplex() {
			this.output = CryptoUtil.toComplex(this.length, this.hash).toString();
			return this;
		}

		public HashBuilder toHex(int length) {
			this.output = CryptoUtil.toHex(length, this.hash);
			return this;
		}

		public HashBuilder toHex() {
			this.output = CryptoUtil.toHex(this.length, this.hash);
			return this;
		}

		public String hash() {
			return hash;
		}

		public String output() {
			return ArgUtil.isEmpty(this.output) ? this.hash : this.output;
		}

		public boolean equals(String hash) {
			return hash.equals(this.output());
		}

		public boolean validate(String hash) {
			// return CryptoUtil.validateHMAC(this.interval, this.secret, this.message,
			// this.currentTime, hash);
			if (isToleranceSet) {
				return CryptoUtil.validateHMAC(this.currentTime, this.interval, this.tolerance, this.secret,
						this.message, hash);
			} else {
				return CryptoUtil.validateHMAC(this.currentTime, this.interval, this.interval, this.secret,
						this.message, hash);
			}
		}

		public boolean validateNumHMAC(String numHash) {
			int lengthThis = this.length;
			if (lengthThis == 0) {
				lengthThis = numHash.length();
			}
			return CryptoUtil.validateNumHMAC(this.currentTime, this.interval, this.tolerance, this.secret,
					this.message, numHash, lengthThis);
		}

		public boolean validateComplexHMAC(String complexHash) {
			int lengthThis = this.length;
			if (lengthThis == 0) {
				lengthThis = complexHash.length();
			}
			return CryptoUtil.validateComplexHMAC(this.currentTime, this.interval, this.tolerance, this.secret,
					this.message, complexHash, lengthThis);
		}

	}

	private static BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
	public static final SixBitEnDec SIX_BIT_ENDEC = new SixBitEnDec();
	static {
		textEncryptor.setPasswordCharArray("ZNEAYuVTsC".toCharArray());
	}

	public static class CrypToken {
		public String message;
		public long expireAt;

		public boolean isExpired() {
			return System.currentTimeMillis() > expireAt;
		}
	}

	public static class TokenExpiredException extends RuntimeException {
		private static final long serialVersionUID = -6558235413712013564L;
	}

	public static class Encoder {
		private String output;
		private CrypToken token;

		public Encoder message(String message) {
			this.output = message;
			return this;
		}

		public <T> Encoder obzect(T obj) {
			this.output = JsonUtil.toJson(obj);
			return this;
		}

		public Encoder decodeBase64() {
			this.output = new String(Base64.getDecoder().decode(this.output));
			return this;
		}

		public Encoder encodeBase64() {
			this.output = Base64.getEncoder().encodeToString(this.output.getBytes());
			return this;
		}

		public Encoder encodeSixBit() {
			this.output = new String(SIX_BIT_ENDEC.encode(this.output, SixBitEnDec.SIX_BIT));
			return this;
		}

		public Encoder decodeSixBit() {
			this.output = new String(SIX_BIT_ENDEC.decode(this.output.getBytes(), SixBitEnDec.SIX_BIT));
			return this;
		}

		public Encoder encodeURL() {
			try {
				this.output = URLEncoder.encode(output, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return this;
		}

		public Encoder decodeURL() {
			try {
				this.output = URLDecoder.decode(output, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return this;
		}

		public Encoder encrypt() {
			this.output = textEncryptor.encrypt(this.output);
			return this;
		}

		public Encoder decrypt() {
			try {
				this.output = textEncryptor.decrypt(this.output);
			} catch (Exception e) {
				LOGGER.error("invalid message from decryption", e.getMessage());
			}
			return this;
		}

		/**
		 * 
		 * @param validity in seconds
		 * @return
		 */
		public Encoder tokenize(long validity) {
			token = new CrypToken();
			token.message = this.output;
			token.expireAt = System.currentTimeMillis() + validity * 1000;
			this.output = JsonUtil.toJson(token);
			return this;
		}

		public Encoder detokenize() {
			token = JsonUtil.fromJson(this.output, CrypToken.class, true);
			this.output = token.message;
			return this;
		}

		public Encoder validate() {
			if (token != null) {
				if (token.isExpired()) {
					throw new TokenExpiredException();
				}
			}
			return this;
		}

		public Encoder sha1() {
			try {
				this.output = CryptoUtil.getSHA1Hash(this.output);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return this;
		}

		public Encoder sha2() {
			try {
				this.output = CryptoUtil.getSHA2Hash(this.output);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return this;
		}

		public Encoder md5() {
			try {
				this.output = CryptoUtil.getMD5Hash(this.output);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return this;
		}

		public <T> T toObzect(Class<T> type) {
			return JsonUtil.fromJson(this.output, type);
		}

		public CrypToken toToken() {
			return JsonUtil.fromJson(this.output, CrypToken.class, true);
		}

		public String toString() {
			return this.output;
		}

		public String hash() {
			return this.output;
		}

		public boolean is(String compaeTo) {
			return ArgUtil.is(this.output, compaeTo);
		}

		public MapModel toMapModel() {
			return MapModel.from(output);
		}

	}

	public static Encoder getEncoder() {
		return new Encoder();
	}

	public static HashBuilder getHashBuilder() {
		return new HashBuilder();
	}

}
