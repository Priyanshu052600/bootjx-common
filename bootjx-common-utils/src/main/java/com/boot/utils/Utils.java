package com.boot.utils;

import java.util.Iterator;
import java.util.List;


/**
 * The Class Utils.
 */
public class Utils {

	/** The comma. */
	public static String COMMA = ",";

	/**
	 * Concatenate.
	 *
	 * @param listOfItems the list of items
	 * @param separator the separator
	 * @return the string
	 */
	public static String concatenate(List<String> listOfItems, String separator) {
		if (ArgUtil.isEmpty(listOfItems)) {
			return Constants.BLANK;
		}
		StringBuilder sb = new StringBuilder();
		Iterator<String> stit = listOfItems.iterator();
		while (stit.hasNext()) {
			sb.append(stit.next());
			if (stit.hasNext()) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * Comma concat.
	 *
	 * @param listOfItems the list of items
	 * @return the string
	 */
	public static String commaConcat(List<String> listOfItems) {
		return concatenate(listOfItems, COMMA);
	}

}
