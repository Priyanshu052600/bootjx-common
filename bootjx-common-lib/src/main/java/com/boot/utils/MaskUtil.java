package com.boot.utils;

public final class MaskUtil {

	/**
	 * Masks input string as per lenght and maskstring from left
	 * 
	 * @param input
	 * @param unmaskLength
	 *            - length of chars which need not be masked
	 * @param maskString
	 * @return
	 */
	public static final String leftMask(String input, final int unmaskLength, final String maskString) {

		String regex = ".(?=.{" + unmaskLength + "})";
		return input.replaceAll(regex, maskString);
	}

	/**
	 * Mask email id from left side
	 * 
	 * @param email
	 * @param unmaskLength
	 * @param maskString
	 * @return
	 */
	public static final String maskEmail(String emailId, final int unmaskLength, final String maskString) {
		String email = emailId.split("@")[0];
		String domain = emailId.split("@")[1];
		StringBuilder maskedEmail = new StringBuilder();
		maskedEmail.append(leftMask(email, unmaskLength, maskString));
		maskedEmail.append("@");
		maskedEmail.append(domain);
		return maskedEmail.toString();
	}
}
