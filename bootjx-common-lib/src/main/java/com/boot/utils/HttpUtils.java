package com.boot.utils;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public final class HttpUtils {

    private static final PolicyFactory POLICY = new HtmlPolicyBuilder().allowStandardUrlProtocols().toFactory();

    public static String sanitze(String str) {
	return POLICY.sanitize(str);
    }

    public static String getIPAddress(HttpServletRequest request) {
	String remoteAddr = null;
	if (request != null) {
	    remoteAddr = request.getHeader("X-FORWARDED-FOR");
	    if (remoteAddr == null || "".equals(remoteAddr)) {
		remoteAddr = request.getRemoteAddr();
	    }
	}
	return remoteAddr;
    }

    public static String getServerName(HttpServletRequest request) {
	return request.getServerName();
    }

    public static String getHostName(HttpServletRequest request) {
	String scheme = request.getScheme();
	String serverName = request.getServerName();
	int portNumber = request.getServerPort();
	String portStr = "";
	if (portNumber != 80 && portNumber != 443) {
	    portStr = ":" + portNumber;
	}
	return scheme + "://" + serverName + portStr;
    }

    public static String getDomain(HttpServletRequest request) {
	String scheme = request.getScheme();
	String serverName = request.getServerName();
	int portNumber = request.getServerPort();
	String portStr = "";
	if (portNumber != 80 && portNumber != 443) {
	    portStr = ":" + portNumber;
	}
	return scheme + "://" + serverName + portStr;
    }

    public static String getSubDomain(HttpServletRequest request) {
	try {
	    if (Urly.isIPAddress(request.getServerName())) {
		return null;
	    }
	    return Urly.getSubDomainName(request.getServerName());
	} catch (MalformedURLException e) {
	    return null;
	}
    }

}
