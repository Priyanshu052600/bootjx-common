package com.boot.utils;

public class Random {

    /**
     * Instantiates a new random.
     */
    private Random() {
	// private constructor to hide the implicit public one.
    }

    /** The Constant ALPHA_NUMERIC_STRING. */
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /** The Constant HEXA_STRING. */
    private static final String HEXA_STRING = "ABCDEF0123456789";

    /** The Constant ALPHA_STRING. */
    private static final String ALPHA_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /** The Constant NUMERIC_STRING. */
    private static final String NUMERIC_STRING = "0123456789";

    /** The Constant SPECIALCHARACTER_STRING. */
    private static final String SPECIALCHARACTER_STRING = "!@#$%^&*_=+-/.?<>)";

    /** The Constant COMBINED_STRING. */
    private static final String PASSWORD_STRING = ALPHA_NUMERIC_STRING + SPECIALCHARACTER_STRING;

    /**
     * Gets the int.
     *
     * @param min the min
     * @param max the max
     * @return the int
     */
    public static int getInt(int min, int max) {
	return (int) (Math.random() * (max - min + 1) + min);
    }

    /**
     * Random alpha.
     *
     * @param count       the count
     * @param alphaString the alpha string
     * @return the string
     */
    public static String randomAlpha(int count, String alphaString) {
	StringBuilder builder = new StringBuilder();
	while (count-- != 0) {
	    int character = (int) (Math.random() * alphaString.length());
	    builder.append(alphaString.charAt(character));
	}
	return builder.toString();
    }

    /**
     * Random alpha.
     *
     * @param count the count
     * @return the string
     */
    public static String randomAlpha(int count) {
	return randomAlpha(count, ALPHA_STRING);
    }

    /**
     * Random numeric.
     *
     * @param count the count
     * @return the string
     */
    public static String randomNumeric(int count) {
	return randomAlpha(count, NUMERIC_STRING);
    }

    /**
     * Random alpha numeric.
     *
     * @param count the count
     * @return the string
     */
    public static String randomAlphaNumeric(int count) {
	return randomAlpha(count, ALPHA_NUMERIC_STRING);
    }

    public static String randomHexa(int count) {
	return randomAlpha(count, HEXA_STRING);
    }

    public static String randomPassword(int count) {
	String password = randomAlpha(count, PASSWORD_STRING);
	return password;
    }

}
