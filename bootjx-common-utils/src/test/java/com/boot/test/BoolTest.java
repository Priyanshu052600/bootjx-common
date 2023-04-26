package com.boot.test;

import java.text.ParseException;
import java.util.regex.Pattern;

import com.boot.utils.ArgUtil;

public class BoolTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		checkAssert("=====true", ArgUtil.nullAsFalse(true), true);
		checkAssert("=====false", ArgUtil.nullAsFalse(false), false);
		checkAssert("=====null", ArgUtil.nullAsFalse(null), false);

		checkAssert("=====true", ArgUtil.nullAsTrue(true), true);
		checkAssert("=====false", ArgUtil.nullAsTrue(false), false);
		checkAssert("=====null", ArgUtil.nullAsTrue(null), true);

		checkAssert("=====TRUE", ArgUtil.nullAsFalse(Boolean.TRUE), true);
		checkAssert("=====FALSE", ArgUtil.nullAsFalse(Boolean.FALSE), false);
		checkAssert("=====null", ArgUtil.nullAsFalse(null), false);

		checkAssert("=====TRUE", ArgUtil.nullAsTrue(Boolean.TRUE), true);
		checkAssert("=====FALSE", ArgUtil.nullAsTrue(Boolean.FALSE), false);
		checkAssert("=====null", ArgUtil.nullAsTrue(null), true);
	}

	public static void checkAssert(String name, Object a, Object b) throws ParseException {
		if (ArgUtil.areEqual(a, b)) {
			System.out.println(name + " : PASS");
		} else {
			System.out.println(name + " : FAIL");
		}
	}

}
