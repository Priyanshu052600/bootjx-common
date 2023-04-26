package com.boot.jx.http;

import javax.servlet.http.HttpServletRequest;

public interface ApiRequestConfig {

	public RequestType from(HttpServletRequest req, RequestType reqType);

}
