package com.boot.utils;

import java.util.regex.Pattern;

import org.springframework.util.Assert;

public class PatternUtil {
	private static final int[] FLAG_LOOKUP = new int[Character.MAX_VALUE];
	private static final int FLAG_GLOBAL = 256;

	static {
		FLAG_LOOKUP['g'] = FLAG_GLOBAL;
		FLAG_LOOKUP['i'] = Pattern.CASE_INSENSITIVE;
		FLAG_LOOKUP['m'] = Pattern.MULTILINE;
		FLAG_LOOKUP['s'] = Pattern.DOTALL;
		FLAG_LOOKUP['c'] = Pattern.CANON_EQ;
		FLAG_LOOKUP['x'] = Pattern.COMMENTS;
		FLAG_LOOKUP['d'] = Pattern.UNIX_LINES;
		FLAG_LOOKUP['t'] = Pattern.LITERAL;
		FLAG_LOOKUP['u'] = Pattern.UNICODE_CASE;
	}

	/**
	 * Converts a regular expression modifier from the database into Java regular
	 * expression flags.
	 *
	 * @param c regular expression modifier
	 * @return the Java flags
	 * @throws IllegalArgumentException If sequence contains invalid flags.
	 */
	public static int regexFlag(final char c) {

		int flag = FLAG_LOOKUP[c];

		if (flag == 0) {
			throw new IllegalArgumentException(String.format("Unrecognized flag [%c]", c));
		}

		return flag;
	}

	/**
	 * Converts a sequence of regular expression modifiers from the database into
	 * Java regular expression flags.
	 *
	 * @param s regular expression modifiers
	 * @return the Java flags
	 * @throws IllegalArgumentException If sequence contains invalid flags.
	 */
	public static int regexFlags(final String s) {
		int flags = 0;

		if (s == null) {
			return flags;
		}

		for (final char f : s.toLowerCase().toCharArray()) {
			flags |= regexFlag(f);
		}

		return flags;
	}

	public static Pattern toPattern(String regex, String options) {

		Assert.notNull(regex, "Regex string must not be null!");

		return Pattern.compile(regex, options == null ? 0 : regexFlags(options));
	}

	public static Pattern equalsIgnoreCase(String string) {
		return PatternUtil.toPattern("^" + string + "$", "i");
	}

	public static Pattern contains(String string) {
		return PatternUtil.toPattern("" + string + "", "i");
	}
}