package com.boot.jx.rest;

import com.boot.jx.http.CommonHttpRequest;

public interface AppRequestContextInFilter {
	public void appRequestContextInFilter(CommonHttpRequest localCommonHttpRequest);
}
