package com.boot.jx.http;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Kooky implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String value;
	private Date expires;
	private Integer maxAge;
	private String domain;
	private String path;
	private boolean secure;
	private boolean httpOnly;
	private String sameSite;

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append(name + "=" + value);

		if (expires != null) {
			SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
			s.append("; Expires=" + fmt.format(expires) + " GMT");
		}

		if (maxAge != null) {
			s.append("; Max-Age=" + maxAge);
		}

		if (domain != null) {
			s.append("; Domain=" + domain);
		}

		if (path != null) {
			s.append("; Path=" + path);
		}

		if (secure) {
			s.append("; Secure");
		}

		if (httpOnly) {
			s.append("; HttpOnly");
		}

		if (sameSite != null) {
			s.append("; SameSite=" + sameSite);
		}

		return s.toString();
	}

	public Kooky sameSite(String sameSite) {
		this.sameSite = sameSite;
		return this;
	}

	public Kooky name(String name) {
		this.name = name;
		return this;
	}

	public Kooky value(String value) {
		this.value = value;
		return this;
	}

	public Kooky path(String path) {
		this.path = path;
		return this;
	}

	public Kooky domain(String domain) {
		this.domain = domain;
		return this;
	}

	public Kooky maxAge(int ageInseconds) {
		Calendar timeout = Calendar.getInstance();
		timeout.add(Calendar.YEAR, 1);
		this.expires = timeout.getTime();
		return this;
	}

	public Kooky secure(boolean secure) {
		this.secure = secure;
		return this;
	}

	public Kooky httpOnly(boolean httpOnly) {
		this.httpOnly = httpOnly;
		return this;
	}

	public Kooky() {
		super();
		this.sameSite = "None";
		this.secure = true;
		this.httpOnly = true;
		this.path = "/";
	}
}