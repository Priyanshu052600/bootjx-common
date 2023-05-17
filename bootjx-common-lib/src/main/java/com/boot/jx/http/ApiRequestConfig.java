package com.boot.jx.http;

import jakarta.servlet.http.HttpServletRequest;

public interface ApiRequestConfig {

	public RequestType from(HttpServletRequest req, RequestType reqType);

}
