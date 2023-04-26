package com.boot.utils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class NumberUtil {

    private static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    /**
     * @param bigdecial to test
     * @return whether passed bigdecimal is integer or not
     * 
     */
    public static boolean isIntegerValue(BigDecimal bd) {
	if (bd == null) {
	    return false;
	}
	return bd.stripTrailingZeros().scale() <= 0;
    }

    public static boolean isNumeric(String strNum) {
	if (strNum == null) {
	    return false;
	}
	return pattern.matcher(strNum).matches();
    }

    public static long max(long... array) {
	// Validates input
	if (array == null) {
	    throw new IllegalArgumentException("The Array must not be null");
	} else if (array.length == 0) {
	    throw new IllegalArgumentException("Array cannot be empty.");
	}

	// Finds and returns max
	long max = array[0];
	for (int j = 1; j < array.length; j++) {
	    if (array[j] > max) {
		max = array[j];
	    }
	}
	return max;
    }

}
