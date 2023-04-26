package com.bootloaderjs;

import java.util.Base64;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.boot.utils.StringUtils;

public class SplitterTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");
	public static final Pattern LINK_CIVIL_ID = Pattern.compile("^LINK (.*)$");
	public static final Pattern LINKD_CIVIL_ID = Pattern.compile("^LINKD <(.*)>$");
	public static final Pattern ENCRYPTED_PROPERTIES = Pattern.compile("^ENC\\((.*)\\)$");

	public static final String SPLITTER_CHAR = ";";
	public static final String KEY_VALUE_SEPARATOR_CHAR = ":";

	public static final String PAYMENT_CAPTURE_CALLBACK_V2 = "/v2/capture/{paygCode}/{tenant}/{channel}/{product}/{uuid}";
	public static final String PAYMENT_CAPTURE_CALLBACK_V2_WILDCARD = "/v2/capture/{paygCode}/{tenant}/{channel}/{product}/{uuid}/*";
	public static final String PAYMENT_CAPTURE_CALLBACK_V2_REGEX = "/app/v2/capture/([a-zA-Z0-9_]+)/([a-zA-Z0-9_]+)/([a-zA-Z0-9_]+)/([a-zA-Z0-9_]+)/(?<trace>[0-9A-Za-z\\-]+)/?(.*)";
	public static final String PAYMENT_CAPTURE_CALLBACK_V2_REGEX2 = "/app/v2/capture/[\\w]/[\\w]/[\\w]/[\\w]/(?<traceid>[-a-zA-Z0-9])/(.*)";
	// #
	// ^/payg2/app/v2/capture/([a-zA-Z0-9_]+)/([a-zA-Z0-9_]+)/([a-zA-Z0-9_]+)/([a-zA-Z0-9_]+)/(?<traceid>[0-9A-Za-z\-]+)/?(.*)
	// #
	// /payg2/app/v2/capture/KNET2/KWT/ONLINE/REMIT/BKJ-7TSqm-7TSqmFsIe09-005218-prm-7TSqrAmaIl7
	public static final String PAYMENT_CAPTURE_CALLBACK_V2_REGEX_TEST = "/payg/app/v2/capture/KNET/KWT/ONLINE/REMIT/BKJ-79020-7TScwusMzwU-184466-prm-7TSd5hgYrtD";

	public static final String CDN_URL = "https://cdn.jsdelivr.net/gh/mehery-scoom/mehery-web-dist@1a4faca383289168e9d12836041fcd22b7fa6c3c/dist/";

	public static final String CDN_URL2 = "https://cdn.jsdelivr.net/gh/cherrybase/cherrybase.github.io@1a4faca383289168e9d12836041fcd22b7fa6c3c";

	public static void main4(String[] args) {
		System.out.println(UUID.nameUUIDFromBytes("gupshup:918750382050".getBytes()));
		System.out.println(Base64.getEncoder().encodeToString("gupshup:918750382050".getBytes()));;
	}

	public static void main(String[] args) {

		String[] codes = "wewe323232-2323-232-323232".split("\\-", 2);
		System.out.println(codes[0]);
		System.out.println(codes[1]);
	}

	public static void main5(String[] args) {

		testCDN(CDN_URL2);
	}

	public static void testCDN(String cdnUrl) {
		Pattern pattern = Pattern.compile(
				"(?<proto>.+)cdn.jsdelivr.net/gh/(?<org>.+)/(?<repo>.+)@(?<version>[-a-zA-Z0-9\\.]+)(?<path>.*)");
		Matcher matcher = pattern.matcher(cdnUrl);

		if (matcher.find()) {
			String protoV = matcher.group("proto");
			String orgV = matcher.group("org");
			String repoV = matcher.group("repo");
			String versionV = matcher.group("version");
			String pathV = matcher.group("path");
			String url = String.format("%scdn.jsdelivr.net/gh/%s/%s@%s%s", protoV, orgV, repoV,
					StringUtils.trim(versionV), pathV);
			if (cdnUrl.equals(url)) {
				System.out.println("Match");
				return;
			}
			System.out.println(url);
			System.out.println(cdnUrl);
		}

		System.out.println("No Match");
	}

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main2(String[] args) {

		Pattern pattern = Pattern.compile("^/payg" + PAYMENT_CAPTURE_CALLBACK_V2_REGEX);
		Matcher matcher = pattern.matcher(PAYMENT_CAPTURE_CALLBACK_V2_REGEX_TEST);

		if (matcher.find()) {
			System.out.println(matcher.group("traceid"));
		}

	}

	public static void linCivilIdCheck() {
		String testString = "ENC(uTSqb9grs1+vUv3iN8lItC0kl65lMG+8)";
		Matcher x = ENCRYPTED_PROPERTIES.matcher(testString);
		if (x.find()) {
			System.out.println(x.group(1));
		} else {
			System.out.println("No");
		}

	}

	public static void classNameCheck() {
		String testString = "com.amx.jax.mcq.SampleTask2$$EnhancerBySpringCGLIB$$82858f05";
		System.out.println(testString.split("\\$\\$")[0]);
	}

}
