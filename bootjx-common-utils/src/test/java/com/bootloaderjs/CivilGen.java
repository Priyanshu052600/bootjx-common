package com.bootloaderjs;

public class CivilGen {
	public static void main(String[] args) {
		System.out.println("Hello World");
		long i = Long.parseLong("100000000000");
		while (i != Long.parseLong("999999999999")) {
			i = i + 1;
			if (isValid(String.valueOf(i))) {
				System.out.println(i);
			}
		}

	}

	public static boolean isValid(String civilId) {
		// if (StringUtils.isEmpty(civilId)) {
		// return false;
		// }
		if (civilId.length() != 12) {
			return false;
		}
		int idcheckdigit = Character.getNumericValue(civilId.charAt(11));
		int[] multiFactor = { 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		int cal = 0;
		for (int i = 0; i < 11; i++) {
			cal += multiFactor[i] * Character.getNumericValue(civilId.charAt(i));
		}
		//System.out.println(cal + "");
		int rem = cal % 11;
		int calcheckdigit = 11 - rem;
		if (calcheckdigit == 0 || calcheckdigit == 10) {

			return false;
		} else {
			if (calcheckdigit != idcheckdigit) {
				return false;
			}
		}
		return true;
	}
}
