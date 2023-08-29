package com.boot.jx.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boot.utils.HttpUtils;

public abstract class ACommonHttpRequest {

	public abstract HttpServletRequest getRequest();

	public abstract HttpServletResponse getResponse();

	/**
	 * 
	 * @see HttpServletRequest#getRequestURI()
	 * @return
	 */
	public String getRequestURI() {
		return getRequest().getRequestURI();
	}

	public String getServerHost() {
		return HttpUtils.getHostName(getRequest());
	}

	public String getServerName() {
		return HttpUtils.getServerName(getRequest());
	}

	public String getSubDomain() {
		return HttpUtils.getSubDomain(getRequest());
	}

	public String getBaseDomain() {
		return getServerName().replaceFirst(getSubDomain() + ".", "");

	}
}
