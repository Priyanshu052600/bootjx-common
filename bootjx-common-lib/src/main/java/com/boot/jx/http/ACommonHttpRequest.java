package com.boot.jx.http;

import com.boot.utils.HttpUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
}
