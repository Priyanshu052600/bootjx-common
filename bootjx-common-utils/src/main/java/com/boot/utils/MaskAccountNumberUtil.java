package com.boot.utils;

public class MaskAccountNumberUtil {

	public static String maskString(String strText, int start, int end, String maskChar) 
			throws Exception{

		if(strText == null || strText.equals(""))
			return "";

		if(start < 0)
			start = 0;

		if( end > strText.length() )
			end = strText.length();

		if(start > end)
			throw new Exception("End index cannot be greater than start index");

		int maskLength = end - start;

		if(maskLength == 0)
			return strText;

		StringBuilder sbMaskString = new StringBuilder(maskLength);

		for(int i = 0; i < maskLength; i++){
			sbMaskString.append(maskChar);
		}

		return strText.substring(0, start) 
				+ sbMaskString.toString() 
				+ strText.substring(start + maskLength);
	}

}
