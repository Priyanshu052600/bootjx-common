package com.boot.test;

import java.text.ParseException;
import java.util.regex.Pattern;

import com.boot.utils.ArgUtil;
import com.boot.utils.StringUtils.StringMatcher;

public class ArgUtilTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("index:\\ ([a-zA-Z0-9_]+)\\ dup key");
	public static final Pattern pattern2 = Pattern.compile(
			"duplicate key error collection: [a-zA-Z0-9_\\.]+ index:\\ ([a-zA-Z0-9_]+)('; nested exception|\\ dup\\ key)");

	public static final Pattern PROXY = Pattern.compile("\\/proxy\\ ([a-zA-Z0-9_\\-]+)$");

	public static void main(String[] args) throws ParseException {
		System.out.println(ArgUtil.any("e".equals("v"), "e".equals("e")));
		System.out.println("- "+ArgUtil.anyOf(true,5));
	}

	public static void main5(String[] args) throws ParseException {
		String x = "/proxy aert$rip-a";

		StringMatcher matcher = new StringMatcher(x);
		if (matcher.isMatch(PROXY)) {
			System.out.println(matcher.group(1));
		} else {
			System.out.println("ss");
		}

	}

	public static void main4(String[] args) throws ParseException {
		String x = "Write failed with error code 11000 and error message 'E11000 duplicate key error collection: localbot.AGENTS index: agent_email dup key: { : \"lalit.tanwar.cherrybase@gmail.com\" }'; nested exception is com.mongodb.DuplicateKeyException: Write failed with error code 11000 and error message 'E11000 duplicate key error collection: localbot.AGENTS index: agent_email dup key: { : \"lalit.tanwar.cherrybase@gmail.com\" }'";
		String x2 = "Write failed with error code 11000 and error message 'E11000 duplicate key error collection: CONFIG_CLIENT_KEY index: keyName'; nested exception is com.mongodb.DuplicateKeyException: Write failed with error code 11000 and error message 'E11000 duplicate key error collection: CONFIG_CLIENT_KEY index: keyName'";
		StringMatcher matcher = new StringMatcher(x2);
		if (matcher.isMatch(pattern2)) {
			System.out.println(matcher.group(1));
		} else {
			System.out.println("ss");
		}

	}

	public static void main3(String[] args) throws ParseException {
		checkAssert("=====true", ArgUtil.isEqual(null), false);
		checkAssert("=====true", ArgUtil.isEqual(null, null), true);
		checkAssert("=====false", ArgUtil.isEqual("", null, null), false);
		checkAssert("=====false", ArgUtil.isEqual(null, "", null), true);
		checkAssert("=====false", ArgUtil.isEqual(null, "x", null), true);
	}

	public static void main2(String[] args) throws ParseException {
		checkAssert("=====true", ArgUtil.isEmptyValue(0), true);
		checkAssert("=====false", ArgUtil.isEmptyValue(0L), true);
		checkAssert("=====null", ArgUtil.isEmptyValue(null), true);
		checkAssert("=====null", ArgUtil.isEmptyValue(""), true);
		checkAssert("=====null", ArgUtil.isEmptyValue(3), false);
		checkAssert("=====null", ArgUtil.isEmptyValue("3"), false);
		checkAssert("=====null", ArgUtil.isEmptyValue("0"), false);
		checkAssert("=====null", ArgUtil.isEmptyValue(false), true);
		checkAssert("=====null", ArgUtil.isEmptyValue(Boolean.FALSE), true);
	}

	public static void checkAssert(String name, Object a, Object b) throws ParseException {
		if (ArgUtil.areEqual(a, b)) {
			System.out.println(name + " : PASS");
		} else {
			System.out.println(name + " : FAIL");
		}
	}

}
