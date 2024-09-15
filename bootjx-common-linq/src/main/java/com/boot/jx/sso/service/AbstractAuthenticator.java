package com.boot.jx.sso.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.rest.RestService;
import com.boot.jx.sso.service.AuthStateManager.AuthState;

public abstract class AbstractAuthenticator implements BasicAuthenticator {

	private static final long serialVersionUID = 6686144045235586188L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAuthenticator.class);

	@Value("${bootjx.linq.base.url:http://localhost/linq}")
	String linqBaseUrl;

	@Autowired
	protected CommonHttpRequest commonHttpRequest;

	@Autowired
	protected AuthStateManager authStateManager;

	@Autowired
	protected RestService restService;

	public AuthState createState() {
		return authStateManager.createState();
	}

	public String getUrl(String path) {
		return this.linqBaseUrl + path;
	}

}
