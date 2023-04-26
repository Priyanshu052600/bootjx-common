package com.boot.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundUtil {

	/**
	 * Responsible to make the round value
	 * 
	 * @param value
	 * @param places
	 * @return
	 */
	public static String round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		double val = (double) tmp / factor;
		int needToAdd = places - String.valueOf(val)
				.substring(String.valueOf(val).indexOf('.') + 1, String.valueOf(val).length()).length();

		if (needToAdd == 0) {
			double check = (double) tmp / factor;
			return String.valueOf(check);
		} else {
			String add = "";
			for (int i = 0; i < needToAdd; i++) {
				add = add + "0";
			}
			String str = String.valueOf((double) tmp / factor) + add;
			return str;
		}
	}

	public static BigDecimal roundBigDecimal(BigDecimal bd, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd;
	}
	
	public static BigDecimal roundToZeroDecimalPlaces(BigDecimal bd) {

		bd = bd.setScale(0, RoundingMode.HALF_UP);
		return bd;
	}

	public static void main(String[] args) {
		RoundUtil ruil = new RoundUtil();
		BigDecimal output = ruil.roundBigDecimal(new BigDecimal(11.65), 0);
	}
}
