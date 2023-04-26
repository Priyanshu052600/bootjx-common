package com.bootloaderjs;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import com.boot.utils.StringUtils;
import com.boot.utils.Urly;

public class App { // Noncompliant

    public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

    /**
     * This is just a test method
     * 
     * @param args
     * @throws URISyntaxException
     * @throws MalformedURLException
     */

    public static void main(String[] args) throws MalformedURLException, URISyntaxException {

	System.out.println(String.format("https://app.%s/partner/auth/verify-link?code=%s&account=%s", "mehery.com",
		"7aefe142-e449-44f9-8bf0-73a449882979", "613e1acb142e532cb46fa51c"));

    }

    public static void main4(String[] args) throws MalformedURLException, URISyntaxException {

	System.out.println(Urly.getBuilder().path("/pub/v2/user/meta").queryParam("S", "vs")
		.queryParam("milestone", "vm").queryParam("lang", "en").getRelativeURL());

    }

    public static void main3(String[] args) {
	System.out.println(StringUtils.trimLeadingZeroes("01064858690"));
	System.out.println(StringUtils.trimLeadingZeroes(""));
	System.out.println(StringUtils.trimLeadingZeroes(null));
	System.out.println(StringUtils.trimLeadingZeroes("78797861"));
    }

    public static void main2(String[] args) {
	String pad = "FffffffffffffffffffffffffffffffG";
	String src = "SssssssssssssT";
	System.out.println(pad);
	System.out.println(src);
	System.out.println(StringUtils.pad(src, pad, 1, 0));
	System.out.println(StringUtils.pad(src, pad, 1, 1));

	System.out.println("Set 1");
	printAndCheck(StringUtils.pad("SssssssssssssT", "FffffffffffffffffffffffffffffffG", 0, 0), "SssssssssssssT");
	printAndCheck(StringUtils.pad("SssssssssssssT", "FffffffffffffffffffffffffffffffG", 0, 1),
		"SssssssssssssTfffffffffffffffffG");
	printAndCheck(StringUtils.pad("SssssssssssssT", "FffffffffffffffffffffffffffffffG", 1, 0), "SssssssssssssT");
	printAndCheck(StringUtils.pad("SssssssssssssT", "FffffffffffffffffffffffffffffffG", 1, 1),
		"FfffffffffffffffffSssssssssssssT");

	System.out.println("Set 2");
	printAndCheck(StringUtils.pad("SssssssssssssT", "", 0, 0), "SssssssssssssT");
	printAndCheck(StringUtils.pad("SssssssssssssT", "", 0, 1), "");
	printAndCheck(StringUtils.pad("SssssssssssssT", "", 1, 0), "SssssssssssssT");
	printAndCheck(StringUtils.pad("SssssssssssssT", "", 1, 1), "");

	System.out.println("Set 3");
	printAndCheck(StringUtils.pad("abdefg", "xxxx", 0, 0), "abdefg");
	printAndCheck(StringUtils.pad("abdefg", "xxxx", 0, 1), "abde");
	printAndCheck(StringUtils.pad("abdefg", "xxxx", 1, 0), "abdefg");
	printAndCheck(StringUtils.pad("abdefg", "xxxx", 1, 1), "defg");

	System.out.println("Set 4");
	printAndCheck(StringUtils.pad("abdefg", "xxxxxxx", 0, 0), "abdefg");
	printAndCheck(StringUtils.pad("abdefg", "xxxxxxx", 0, 1), "abdefgx");
	printAndCheck(StringUtils.pad("abdefg", "xxxxxxx", 1, 0), "abdefg");
	printAndCheck(StringUtils.pad("abdefg", "xxxxxxx", 1, 1), "xabdefg");

	System.out.println("Set 5");
	printAndCheck(StringUtils.pad("", "xxxxxxx", 0, 0), "");
	printAndCheck(StringUtils.pad("", "xxxxxxx", 0, 1), "xxxxxxx");
	printAndCheck(StringUtils.pad("", "xxxxxxx", 1, 0), "");
	printAndCheck(StringUtils.pad("", "xxxxxxx", 1, 1), "xxxxxxx");

    }

    public static void printAndCheck(String str, String check) {
	System.out.println(String.format("%15s === %15s %15s", str.equals(check), check, str));
    }

    private static long rotateTime(long millis, int i) {
	return (System.currentTimeMillis() / (millis)) & i;
    }

    private static long rotateTimeReverse(long millis, int i) {
	return i - (System.currentTimeMillis() / (millis)) & i;
    }

}
