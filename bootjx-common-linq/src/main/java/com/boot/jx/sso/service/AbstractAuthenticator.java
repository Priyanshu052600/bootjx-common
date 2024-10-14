package com.boot.jx.sso.service;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.boot.jx.auth.AuthStateManager;
import com.boot.jx.auth.AuthStateManager.AuthState;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.rest.RestService;
import com.boot.jx.sso.SSOUrlUtility;
import com.boot.utils.ArgUtil;
import com.boot.utils.URLBuilder;
import com.boot.utils.Urly;
import com.javachinna.oauth2.user.SocialEnums.ChannelPartner;

public abstract class AbstractAuthenticator implements BasicAuthenticator {

	private static final long serialVersionUID = 6686144045235586188L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAuthenticator.class);

	@Autowired
	protected CommonHttpRequest commonHttpRequest;

	@Autowired
	protected AuthStateManager authStateManager;

	@Autowired
	protected RestService restService;

	@Autowired
	private SSOUrlUtility urlService;

	public URLBuilder linkBase() {
		URLBuilder linkBase;
		try {
			linkBase = Urly.parse(commonHttpRequest.getServerHost());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			linkBase = null;
		}
		return linkBase;
	}

	public String getOrigin() {
		return urlService.getOrigin(linkBase());
	}

	private String getLinqBaseUrl() {
		if (ArgUtil.is(urlService.getLinqBaseUrl())) {
			return urlService.getLinqBaseUrl();
		}
		return this.getOrigin();
		// return commonHttpRequest.getServerHost() + "";
	}

	public AuthState createState() {
		return authStateManager.createState();
	}

	public String getUrl(String path) {
		return this.getLinqBaseUrl() + path;
	}

	public String redirectPath(String provider, ChannelPartner partner) {
		return urlService.redirectPath(provider, partner);
	}

	public String redirectUrl(String provider, ChannelPartner partner) {
		return this.getLinqBaseUrl() + redirectPath(provider, partner);
	}

	public String getVerificationLink(String verificationId) {
		return urlService.getUrl("/linq/app/v/" + verificationId);
	}

	public String getHomeLink(String subdomain) {
		return urlService.getUrl(subdomain, "/linq/home");
	}

}
