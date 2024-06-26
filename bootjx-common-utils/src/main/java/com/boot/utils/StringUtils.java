package com.boot.utils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class StringUtils {

	public static final String SPLITTER_CHAR = ";";
	public static final String KEY_VALUE_SEPARATOR_CHAR = ":";

	public static class StringMatcher {
		String str;
		Matcher m;

		public StringMatcher(String str) {
			this.str = str;
		}

		public boolean match(Pattern pattern) {
			this.m = pattern.matcher(str);
			return this.m != null;
		}

		public boolean find() {
			return this.m != null && this.m.find();
		}

		public boolean isMatch(Pattern pattern) {
			return this.match(pattern) && find();
		}

		public String group(int index) {
			return this.m.group(index);
		}

		public String toString() {
			return this.str;
		}

	}

	public static class DetailsBuilder {
		List<String> record;
		List<List<String>> records;

		public DetailsBuilder() {
			super();
			this.records = new ArrayList<List<String>>();
		}

		/**
		 * Create new Record
		 * 
		 * @return
		 */
		public DetailsBuilder create() {
			this.close();
			this.record = new ArrayList<String>();
			return this;
		}

		public DetailsBuilder close() {
			if (this.record != null) {
				this.records.add(this.record);
			}
			this.record = null;
			return this;
		}

		public DetailsBuilder add(Object... objects) {
			for (int i = 0; i < objects.length; i++) {
				this.record.add(parseAsString(objects[i]));
			}
			return this;
		}

		public List<String> get(int index) {
			if (index < records.size()) {
				return records.get(index);
			}
			return null;
		}

		public List<String> findOne(int queryCol, String queryValue) {
			for (List<String> record : records) {
				if (queryCol < record.size()) {
					if (queryValue.equals(record.get(queryCol))) {
						return record;
					}
				}
			}
			return null;
		}

		/**
		 *
		 * @param index - starts from 0
		 * @param col   - starts from 0
		 * @param value
		 * @return
		 */
		public DetailsBuilder update(int index, int col, Object value) {
			if (index < records.size()) {
				List<String> y = records.get(index);
				if (col < y.size()) {
					y.set(col, parseAsString(value));
				}
			}
			return this;
		}

		public DetailsBuilder updateQuery(int queryCol, String queryValue, int updateCol, String updateValue) {
			for (List<String> record : records) {
				if (queryCol < record.size() && updateCol < record.size()) {
					if (queryValue.equals(record.get(queryCol))) {
						record.set(updateCol, parseAsString(updateValue));
					}
				}
			}
			return this;
		}

		public String toString() {
			this.close();
			StringJoiner sjs = new StringJoiner(";");
			for (List<String> record : records) {
				StringJoiner sj = new StringJoiner(":");
				for (String string : record) {
					sj.add(string);
				}
				sjs.add(sj.toString());
			}
			return sjs.toString();
		}

		public DetailsBuilder parse(String detailString) {
			if (ArgUtil.is(detailString)) {
				String[] x = detailString.split(";");
				for (String string : x) {
					String[] y = string.split(":");
					List<String> record = Arrays.asList(y);
					this.records.add(record);
				}
			}
			return this;
		}

		private static String parseAsString(Object obj) {
			return ArgUtil.parseAsString(obj, Constants.BLANK);
		}

		public List<List<String>> getRecords() {
			return records;
		}
	}

	public static boolean isNotBlank(String str) {
		return !ArgUtil.isEmptyString(str);
	}

	/**
	 * Remove all characters which are not Alpha or Numeric
	 * 
	 * @param inputString
	 * @return
	 */
	public static String removeSpecialCharacter(String inputString) {
		return inputString.replaceAll("[^a-zA-Z0-9]+", "");
	}

	public static String removeSpaces(String inputString) {
		return inputString.replaceAll("[\\ ]+", "");
	}

	// public static Map<String, String> getMapFromString(String splitter_char,
	// String key_value_separator_char,
//			String data) {
	// return
	// Splitter.on(splitter_char).withKeyValueSeparator(key_value_separator_char).split(data);
//	}

	public static Map<String, String> getMapFromString(String splitter_char, String key_value_separator_char,
			String data) {
		Map<String, String> map = new HashMap<String, String>();
		String[] stubs = data.split(splitter_char);
		if (ArgUtil.is(stubs) && stubs.length > 0) {
			for (String string : stubs) {
				String[] stub = string.split(key_value_separator_char);
				if (ArgUtil.is(stub) && stub.length > 1) {
					map.put(trim(stub[0]), stub[1]);
				}
			}
		}
		return map;
	}

	public static Map<String, String> toMap(String data) {
		return getMapFromString(SPLITTER_CHAR, KEY_VALUE_SEPARATOR_CHAR, data);
	}

	public static Map<String, String> toMapFromQueryString(String data) {
		return getMapFromString("&", "=", data);
	}

	public static String toString(Map<String, String> data) {
		StringJoiner orCondition = new StringJoiner(SPLITTER_CHAR);
		for (Entry<String, String> c : data.entrySet()) {
			orCondition.add(c.getKey() + KEY_VALUE_SEPARATOR_CHAR + c.getValue());
		}
		return orCondition.toString();
	}

	public static int hash(String str, int max) {
		int hash = max;
		for (int i = 0; i < str.length(); i++) {
			hash = hash * 31 + str.charAt(i);
		}
		return (int) Math.abs(hash % max);
	}

	public static String trim(String str) {
		return (str == null) ? str : str.trim();
	}

	public static String trim(String s, char delimiter) {
		int sIndex;
		for (sIndex = 0; sIndex < s.length() - 1; sIndex++) {
			if (s.charAt(sIndex) != delimiter) {
				break;
			}
		}

		int eIndex;
		for (eIndex = s.length() - 1; eIndex > 0; eIndex--) {
			if (s.charAt(eIndex) != delimiter) {
				break;
			}
		}

		return s.substring(sIndex, Math.max(sIndex, eIndex + 1));
	}

	public static String ltrim(String s, char delimiter) {
		int sIndex;
		for (sIndex = 0; sIndex < s.length() - 1; sIndex++) {
			if (s.charAt(sIndex) != delimiter) {
				break;
			}
		}
		int eIndex = s.length();
		return s.substring(sIndex, Math.max(sIndex, eIndex + 1));
	}

	public static String trimLeadingZeroes(String value) {
		if (ArgUtil.is(value)) {
			return new Long(value).toString();
		}
		return value;
	}

	public static String wrap(String prefix, String str, String suffix) {
		if (ArgUtil.is(str))
			return prefix + str + suffix;
		return Constants.BLANK;
	}

	public static String prefix(String prefix, String str) {
		return wrap(prefix, str, Constants.BLANK);
	}

	public static String suffix(String str, String suffix) {
		return wrap(Constants.BLANK, str, suffix);
	}

	public static String normalizeSpace(String src) {
		return (src == null) ? src : src.trim().replaceAll(" +", " ");
	}

	public static String join(String delimter, String... strs) {
		StringJoiner sj = new StringJoiner(delimter);
		for (String string : strs) {
			if (ArgUtil.is(string)) {
				sj.add(string);
			}
		}
		return sj.toString();
	}

	/**
	 * 
	 * @param src
	 * @param pad
	 * @param flip => 0: left, 1:right
	 * @param trim : 1=max size will of pad, 0: min size will be of src;
	 * @return
	 */
	public static String pad(String src, String pad, int flip, int trim) {
		if (trim == 0) {
			return src;
		}

		int lendiff = pad.length() - src.length();
		if (flip == 1) {
			String fullPart = substring(pad, lendiff) + src;
			if (trim == 1) {
				return fullPart.substring(-1 * Math.min(lendiff, 0)); // fullPart.substring(Math.min(src.length(),
			} else {
				return lendiff >= 0 ? fullPart.substring(lendiff) : fullPart;
			}
		} else {
			String fullPart = src + ((lendiff >= 0) ? pad.substring(src.length()) : Constants.BLANK);
			if (trim == 0) {
				return fullPart.substring(0, src.length());
			} else {
				return fullPart.substring(0, pad.length());
			}
		}
	}

	/**
	 * 
	 * @param src
	 * @param pad
	 * @param flip => 0: left, 1:right
	 * @return
	 */
	public static String pad(String src, String pad, int flip) {
		return pad(src, pad, flip, 1);
	}

	public static String substring(String str, int length) {
		if (str == null || str.length() <= 0 || length <= 0) {
			return Constants.BLANK;
		} else if (str.length() <= length) {
			return str;
		} else {
			return str.substring(0, length);
		}
	}

	private static String mask(String strText, int start, int end, char maskChar) {

		if (strText == null || strText.equals(""))
			return "";

		if (start < 0)
			start = 0;

		if (end > strText.length())
			end = strText.length();

		if (start > end) {
			start = end;
		}

		int maskLength = end - start;

		if (maskLength == 0)
			return strText;

		StringBuilder sbMaskString = new StringBuilder(maskLength);

		for (int i = 0; i < maskLength; i++) {
			sbMaskString.append(maskChar);
		}

		return strText.substring(0, start) + sbMaskString.toString() + strText.substring(start + maskLength);
	}

	public static String mask(String strText) {
		if (ArgUtil.isEmpty(strText)) {
			return strText;
		}
		int len = strText.length();
		return mask(strText, len / 10 * 2, len / 10 * 7, '*');
	}

	public static boolean anyMatch(String val, String... matchers) {

		if (val == null)
			return false;

		return Stream.of(matchers).anyMatch(val::equalsIgnoreCase);
	}

	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B',
			'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z' };

	public static String alpha62(long i) {
		int radix = 62;
		char[] buf = new char[65];
		int charPos = 64;
		boolean negative = (i < 0);

		if (!negative) {
			i = -i;
		}

		while (i <= -radix) {
			buf[charPos--] = digits[(int) (-(i % radix))];
			i = i / radix;
		}
		buf[charPos] = digits[(int) (-i)];

		if (negative) {
			buf[--charPos] = '-';
		}

		return new String(buf, charPos, (65 - charPos));
	}

	private static int findIndex(char arr[], int t) {

		// if array is Null
		if (arr == null) {
			return -1;
		}

		// find length of array
		int len = arr.length;
		int i = 0;

		// traverse in the array
		while (i < len) {

			// if the i-th element is t
			// then return the index
			if (arr[i] == t) {
				return i;
			} else {
				i = i + 1;
			}
		}
		return -1;
	}

	public static long alpha62(final String encoded) {
		long ret = 0L;
		char c;
		if (ArgUtil.is(encoded)) {
			for (int index = 0; index < encoded.length(); index++) {
				c = encoded.charAt(index);
				ret *= digits.length;
				ret += findIndex(digits, c);
			}
		}
		return ret;
	}

	public static List<String> capitalize(List<String> input) {
		List<String> output = new ArrayList<>();
		input.forEach(i -> {
			output.add(capitalize(i));
		});
		return output;
	}

	public static String capitalize(String input) {
		if (!StringUtils.isNotBlank(input)) {
			return input;
		}
		if (input.length() == 1) {
			return input.substring(0, 1).toUpperCase();
		}
		return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
	}

	public static String decapitalize(String string) {
		if (string == null || string.length() == 0) {
			return string;
		}
		char c[] = string.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		return new String(c);
	}

	public static String toLowerCase(String str) {
		if (str == null) {
			return str;
		}
		return str.toLowerCase();
	}

	public static String toUpperCase(String str) {
		if (str == null) {
			return str;
		}
		return str.toUpperCase();
	}

	public static String sanitize(String str, String replaceWith) {
		if (str == null) {
			return Constants.BLANK;
		}
		if (str instanceof String) {
			return (ArgUtil.parseAsString(str, Constants.BLANK).replaceAll("[\\.@$:]", replaceWith));
		}
		return (String) str;
	}

	public static String sanitize(String str) {
		return sanitize(str, Constants.UNDERSCORE);
	}

	public static String slugify(String str) {
		if (str == null) {
			return Constants.BLANK;
		}
		// Convert to lowercase
		String slug = str.toLowerCase();

		// Normalize the string to remove diacritical marks (accents)
		slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
		slug = slug.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

		// Replace spaces with hyphens
		slug = slug.replaceAll("\\s+", "-");

		// Remove all non-alphanumeric characters except hyphens
		slug = slug.replaceAll("[^a-z0-9\\-]", "");

		// Remove multiple consecutive hyphens
		slug = slug.replaceAll("\\-+", "-");

		// Trim hyphens from start and end
		slug = slug.replaceAll("^-|-$", "");

		return slug;
	}

	public static String slugifyFileName(String fileName) {
		String fileExtension = Constants.BLANK;
		String fileBaseName = String.format("%s", fileName);
		int index = fileBaseName.lastIndexOf('.');

		if (index > 0) {
			fileExtension = "." + fileBaseName.substring(index + 1);
			fileBaseName = fileBaseName.substring(0, index);
		}
		return slugify(String.format("%s", fileBaseName)) + fileExtension;
	}

	public static String[] split(String str, String regex) {
		if (str == null) {
			return new String[0];
		}
		return str.split(regex);
	}

	public static String[] split(String str) {
		if (str == null) {
			return new String[0];
		}
		return str.split(KEY_VALUE_SEPARATOR_CHAR);
	}

	public static boolean contains(String str, String find) {
		if (str == null) {
			return false;
		}
		return str.contains(find);
	}

	public static String[] toArray(String str) {
		if (str == null) {
			return new String[0];
		}
		if (str.startsWith("[") && str.endsWith("]")) {
			str = str.trim().substring(1, str.length() - 1);
		}
		return str.split(",");
	}

	public static List<String> toList(String str) {
		return CollectionUtil.getList(toArray(str));
	}

	public static String getByIndex(String str, String regex, int index) {
		String[] x = split(str, regex);
		if (x.length > index) {
			return x[index];
		}
		return null;
	}

	public static boolean isEmpty(String str) {
		return ArgUtil.isEmptyString(str);
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static String maskIpAddress(String ipAddress) {
		String[] slots = ipAddress.split(":|\\.");
		return String.join("-", Arrays.copyOfRange(slots, 0, slots.length / 4 * 3));
	}

	public static String repeat(String repeat, int times) {
		return new String(new char[times]).replace("\0", repeat);
	}

}
