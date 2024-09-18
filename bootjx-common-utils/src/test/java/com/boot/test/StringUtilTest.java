package com.boot.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.boot.model.MapModel.NodeEntry;
import com.boot.utils.StringUtils;
import com.boot.utils.StringUtils.DetailsBuilder;

public class StringUtilTest {

	@Test
	public void NodeEntryBoolean() {
		assertEquals("TRUE", new NodeEntry<Object>().value("true").asBoolean(), true);
		assertEquals("FALSE", new NodeEntry<Object>().value("false").asBoolean(), false);
	}

	//@Test
	public void normalizedString() {
		assertEquals("N1", "Lalit Tanwar", "Lalit Tanwar");
		assertEquals("N2", "Lalit    Tanwar", "Lalit Tanwar");
		assertEquals("N3", "   Lalit   Tanwar", "Lalit Tanwar");
		assertEquals("N4", " Lalit  Tanwar  ", "Lalit Tanwar");
	}

	@Test
	public void removeSpaces() {
		assertEquals("N1", StringUtils.removeSpaces("Lalit Tanwar"), "LalitTanwar");
		assertEquals("N2", StringUtils.removeSpaces("Lalit    Tanwar"), "LalitTanwar");
		assertEquals("N3", StringUtils.removeSpaces("   Lalit   Tanwar"), "LalitTanwar");
		assertEquals("N4", StringUtils.removeSpaces(" Lalit  Tanwar  "), "LalitTanwar");
	}

	@Test
	public void replaceDot() {
		System.out.println("3e3.343.34343.343".replaceAll("\\.", "/"));
	}

	@Test
	public void testTrim() {
		assertEquals("t1", StringUtils.trim("/abc/def/ghij", '/'), "abc/def/ghij");
		assertEquals("t2", StringUtils.trim("/abc/def/ghij/", '/'), "abc/def/ghij");
		assertEquals("t3", StringUtils.trim("abc/def/ghij/", '/'), "abc/def/ghij");
		assertEquals("t4", StringUtils.trim("////abc/def/ghij/", '/'), "abc/def/ghij");
		assertEquals("t5", StringUtils.trim("////abc/def/ghij/////", '/'), "abc/def/ghij");
		assertEquals("t6", StringUtils.trim("abc/def/ghij/////", '/'), "abc/def/ghij");
		assertEquals("t7", StringUtils.trim("//abc/def/ghij/////", '/'), "abc/def/ghij");
		assertEquals("t7", StringUtils.trim("aaaaaaa", 'a'), "");
	}

	@Test
	public void testSplit() {
		assertEquals(StringUtils.getByIndex("10.28.42.255", ",", 0), "10.28.42.255");
		assertEquals(StringUtils.getByIndex("10.28.42.109,10.28.42.255", ",", 0), "10.28.42.109");
	}

	@Test
	public void testDetails() {
		DetailsBuilder db = new DetailsBuilder();
		for (int i = 0; i < 5; i++) {
			db.create();
			db.add(i, i * 2, i * 5);
		}
		String x = db.toString();
		// System.out.println(x);
		DetailsBuilder db2 = new DetailsBuilder().parse(x);
		// System.out.println(db2.toString());
		assertEquals(x, db2.toString());
	}

	@Test
	public void testCapitalize() {
		String s1 = "target";
		String op = StringUtils.capitalize(s1);
		assertEquals("Target", op);

		String s2 = "p";
		String op2 = StringUtils.capitalize(s2);
		assertEquals("P", op2);
		long val = 9999999999L;// 999999999999999L * 248L;
		// System.out.println("======" + val);
		// System.out.println("======" + Long.toString(val, 36));
		// System.out.println("======" + StringUtils.alpha62(val));

		String custId = StringUtils.alpha62(0); // StringUtils.alpha62(9999999999L);
		// KWT-JBQ-7R3Ri-7R3RiGe70zB-aUKYOz-bmt-7R3RiIZQkgH
		String nowTrace = "xxxx-7R3RiIZQkgH";
		// System.out.println(custId);
		// System.out.println(nowTrace);
		// System.out.println(StringUtils.pad(custId, nowTrace, 0, 1));
		// System.out.println(UniqueID.generateRequestId("7R3Ri-7R3RiGe70zB", "bmt"));
	}

	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B',
			'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z' };

	@Test
	public void tesyAlpha62() {
		long original = 2232323L;
		String originalStr = StringUtils.alpha62(original);
		long result = StringUtils.alpha62(originalStr);
		// System.out.println(original + " == " + originalStr + " == " + result);
		assertEquals(original, result);
	}

}
