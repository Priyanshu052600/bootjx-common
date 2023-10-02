package com.boot.test;

import java.io.IOException;
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
		URLBuilder x = Urly.parse(url);
		System.out.println(x.getURL());
		System.out.println(x.getRelativeURL());
		System.out.println("path " + x.toFile().getParent());
		System.out.println("file " + x.toFile().getName());
	}

}
