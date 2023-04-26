package com.bootloaderjs;

import com.boot.utils.BashUtil;

public class ArgumentTokenizerTest {
	public static void main(String[] args) {
		for (String s : BashUtil
				.tokenize("-s -d \"String with space\" -d \"string with \\\" escape \\n the next line\"")) {
			System.out.println(s);
		}

		for (String s : BashUtil.tokenize("-u demo:password -O ftp://test.rebex.net/readme.txt")) {
			System.out.println(s);
		}

		for (String s : BashUtil.tokenize("-o hello.zip ftp://speedtest.tele2.net/1MB.zip")) {
			System.out.println(s);
		}
	}
}
