package com.boot.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * The Class Urly.
 */
public class Urly {

	private static final Pattern IP_PATTERN = Pattern
			.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

	public static boolean isIPAddress(final String ip) {
		return IP_PATTERN.matcher(ip).matches();
	}

	/**
	 * Gets the domain name.
	 *
	 * @param url the url
	 * @return the domain name
	 * @throws MalformedURLException the malformed URL exception
	 */
	public static String getDomainName(String url) throws MalformedURLException {
		if (!url.startsWith("http") && !url.startsWith("https")) {
			url = "http://" + url;
		}
		URL netUrl = new URL(url);
		String host = netUrl.getHost();
		if (host.startsWith("www")) {
			host = host.substring("www".length() + 1);
		}
		return host;
	}

	/**
	 * Gets the sub domain name.
	 *
	 * @param url the url
	 * @return the sub domain name
	 * @throws MalformedURLException the malformed URL exception
	 */
	public static String getSubDomainName(String url) throws MalformedURLException {
		String[] names = url.split("\\.");
		if (names.length < 3) {
			return null;
		} else {
			return names[0];
		}
	}

	/**
	 * Gets the builder.
	 *
	 * @return the builder
	 */
	public static URLBuilder getBuilder() {
		return new URLBuilder();
	}

	/**
	 * Parses the.
	 *
	 * @param urlString the url string
	 * @return the URL builder
	 * @throws MalformedURLException the malformed URL exception
	 */
	public static URLBuilder parse(String urlString) throws MalformedURLException {
		return URLBuilder.parse(urlString);
	}
}
