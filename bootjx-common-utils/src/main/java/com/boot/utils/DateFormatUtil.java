package com.boot.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DateUtil.
 */
// Needs further Customizations
public final class DateFormatUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateFormatUtil.class);

	private static final Map<String, DateFormat> JAVA_DATE_FORMAT_REGEXPS = new HashMap<String, DateFormat>() {
		private static final long serialVersionUID = -8521055413044649661L;
		{
			put("^\\d{8}$", new SimpleDateFormat("yyyyMMdd"));
			put("^\\d{1,2}-\\d{1,2}-\\d{4}$", new SimpleDateFormat("dd-MM-yyyy"));
			put("^\\d{4}-\\d{1,2}-\\d{1,2}$", new SimpleDateFormat("yyyy-MM-dd"));
			put("^\\d{1,2}/\\d{1,2}/\\d{4}$", new SimpleDateFormat("MM/dd/yyyy"));
			put("^\\d{4}/\\d{1,2}/\\d{1,2}$", new SimpleDateFormat("yyyy/MM/dd"));
			put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", new SimpleDateFormat("dd MMM yyyy"));
			put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", new SimpleDateFormat("dd MMMM yyyy"));
			put("^\\d{12}$", new SimpleDateFormat("yyyyMMddHHmm"));
			put("^\\d{8}\\s\\d{4}$", new SimpleDateFormat("yyyyMMdd HHmm"));
			put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", new SimpleDateFormat("dd-MM-yyyy HH:mm"));
			put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", new SimpleDateFormat("yyyy-MM-dd HH:mm"));
			put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", new SimpleDateFormat("MM/dd/yyyy HH:mm"));
			put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", new SimpleDateFormat("yyyy/MM/dd HH:mm"));
			put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", new SimpleDateFormat("dd MMM yyyy HH:mm"));
			put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", new SimpleDateFormat("dd MMMM yyyy HH:mm"));
			put("^\\d{14}$", new SimpleDateFormat("yyyyMMddHHmmss"));
			put("^\\d{8}\\s\\d{6}$", new SimpleDateFormat("yyyyMMdd HHmmss"));
			put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

			put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			put("^\\d{4}-\\d{1,2}-\\d{1,2}t\\d{1,2}:\\d{2}:\\d{2}\\.\\d{3}$",
					new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"));

			put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"));
			put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
			put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$",
					new SimpleDateFormat("dd MMM yyyy HH:mm:ss"));
			put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$",
					new SimpleDateFormat("dd MMMM yyyy HH:mm:ss"));
			put("^\\d{1,2}\\s[a-z]{4,},\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}\\s[a-z]{2}\\s[a-z]{3,}$",
					new SimpleDateFormat("dd MMMM, yyyy hh:mm:ss a z"));

		}
	};

	public static final String SQL_DATE_FORMAT_DEFAULT = "DD-MM-YYYY";
	public static final String SQL_TIME_FORMAT_DEFAULT = "DD-MM-YYYY HH24:MI:SS";
	private static final Map<String, String> SQL_DATE_FORMAT_REGEXPS = new HashMap<String, String>() {
		private static final long serialVersionUID = -8521055413044649661L;
		{
			put("YYYYMMDD", "^\\d{8}$");
			put(SQL_DATE_FORMAT_DEFAULT, "^\\d{1,2}-\\d{1,2}-\\d{4}$");
			put("DD/MM/YYYY", "^\\d{1,2}/\\d{1,2}/\\d{4}$");
			put("YYYY-MM-DD", "^\\d{4}-\\d{1,2}-\\d{1,2}$");
			put("YYYY/MM/DD", "^\\d{4}/\\d{1,2}/\\d{1,2}$");
			put("DD MMM YYYY", "^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$");
			put("DD MMMM YYYY", "^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$");
			put("YYYYMMDDHH24MI", "^\\d{12}$");
			put("yyyyMMdd HHmm", "^\\d{8}\\s\\d{4}$");
			put("dd-MM-yyyy HH:mm", "^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$");
			put("yyyy-MM-dd HH:mm", "^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$");
			put("MM/dd/yyyy HH:mm", "^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$");
			put("yyyy/MM/dd HH:mm", "^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$");
			put("dd MMM yyyy HH:mm", "^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$");
			put("dd MMMM yyyy HH:mm", "^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$");
			put("yyyyMMddHHmmss", "^\\d{14}$");
			put("yyyyMMdd HHmmss", "^\\d{8}\\s\\d{6}$");
			put(SQL_TIME_FORMAT_DEFAULT, "^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$");
			put("DD/MM/YYYY HH24:MI:SS", "^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$");

			put("yyyy-MM-dd HH24:MI:SS", "^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$");
			put("yyyy-MM-ddTHH24:MI:SS.FF3", "^\\d{4}-\\d{1,2}-\\d{1,2}t\\s\\d{1,2}:\\d{2}:\\d{2}\\.\\d{3}$");

			put("yyyy/MM/dd HH24:MI:SS", "^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$");
			put("dd MMM yyyy HH24:MI:SS", "^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$");
			put("dd MMMM yyyy HH24:MI:SS", "^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$");
		}
	};

	private DateFormatUtil() {
		throw new IllegalStateException("This is a class with static methods and should not be instantiated");
	}

	public static DateFormat determineDateFormat(String dateString) {
		for (String regexp : JAVA_DATE_FORMAT_REGEXPS.keySet()) {
			if (dateString.toLowerCase().matches(regexp)) {
				return JAVA_DATE_FORMAT_REGEXPS.get(regexp);
			}
		}
		return null; // Unknown format.
	}

	public static String determineSqlDateFormat(String dateString) {
		for (Entry<String, String> regexp : SQL_DATE_FORMAT_REGEXPS.entrySet()) {
			if (dateString.toLowerCase().matches(regexp.getValue())) {
				return regexp.getKey();
			}
		}
		return null; // Unknown format.
	}

	public static boolean isFormat(String dateString, String dateFormat) {
		if (SQL_DATE_FORMAT_REGEXPS.containsKey(dateFormat)) {
			return dateString.toLowerCase().matches(SQL_DATE_FORMAT_REGEXPS.get(dateFormat));
		} else if (JAVA_DATE_FORMAT_REGEXPS.containsKey(dateFormat)) {
			// return
			// dateString.toLowerCase().matches(JAVA_DATE_FORMAT_REGEXPS.get(dateFormat));
		}
		return false; // Unknown format.
	}

	public static boolean isTimeStamp(String str) {
		return ArgUtil.is(str) && str.length() > 12 && NumberUtil.isNumeric(str);
	}

	public static class MyDateConverter implements Converter {

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Object convert(Class type, Object value) {
			if (value == null) {
				return null;
			} else {
//		    parse your date format with date formatter
				try {
					DateFormat format = DateFormatUtil.determineDateFormat(ArgUtil.parseAsString(value));
					if (format != null) {
						return format.parse((String) value);
					}
					return null;
				} catch (ParseException e) {
					LOGGER.warn("Date Format Exception");
				}
				return null;
			}
		}

	}

}
