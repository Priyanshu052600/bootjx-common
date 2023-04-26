package com.boot.test;

import java.text.ParseException;

import com.boot.jx.dict.Language;
import com.boot.utils.ArgUtil;

public class EnumTest { // Noncompliant

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		Language l = (Language) ArgUtil.parseAsEnum("l", Language.EN,
				Language.class);
		System.out.println(l);
	}

}
