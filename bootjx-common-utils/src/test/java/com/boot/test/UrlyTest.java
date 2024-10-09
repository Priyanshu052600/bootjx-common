package com.boot.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

import com.boot.utils.URLBuilder;
import com.boot.utils.Urly;

public class UrlyTest { // Noncompliant

	public static final String url = ("https://meherydata.s3.amazonaws.com/tathkarah/session/wa9659008856396522206222/ABGGllkAiFYAgo6ueesBvDnbOyx/1i1u0au2en6cf.jpeg");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws ParseException, IOException, URISyntaxException {
		URLBuilder x = Urly.parse("https://app.domain.xyz/agent");
		URI uri = new URI(x.getConnectionType(), null, x.getHost(), -1, null, null, null);
		System.out.println(x.getConnectionType() + ":// " + x.getHost() + x.getRelativeURL());

		// URI uri = new URI("https", null, x.getURL(), -1, null, null, null);
		x = Urly.parse("app.domain.xyz");
		uri = new URI(x.getConnectionType(), null, x.getHost(), -1, null, null, null);

		System.out.println(x.getConnectionType() + "://" + x.getHost() + x.getRelativeURL());
		System.out.println("SUBDOMAIN: " + x.getSubdomain());
		System.out.println("DOMAIN: " + x.getBasedomain());

		x = Urly.parse("app.domain.xyz/agent");

		uri = new URI(x.getConnectionType(), null, x.getHost(), -1, null, null, null);
		System.out.println(x.getConnectionType() + "://" + x.getHost() + x.getRelativeURL());
		System.out.println("SUBDOMAIN: " + x.getSubdomain());
		System.out.println("DOMAIN: " + x.getBasedomain());
		
		
		x = Urly.parse("domain.xyz/agent");

		uri = new URI(x.getConnectionType(), null, x.getHost(), -1, null, null, null);
		System.out.println(x.getConnectionType() + "://" + x.getHost() + x.getRelativeURL());
		System.out.println("SUBDOMAIN: " + x.getSubdomain());
		System.out.println("DOMAIN: " + x.getBasedomain());

	}

	public static void main2(String[] args) throws ParseException, IOException, URISyntaxException {
		URLBuilder x = Urly.parse(url);
		System.out.println(x.getURL());
		System.out.println(x.getRelativeURL());
		System.out.println("path " + x.toFile().getParent());
		System.out.println("file " + x.toFile().getName());
	}

}
