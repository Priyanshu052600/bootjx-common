package com.boot.jx.sso.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.boot.jx.auth.AuthStateManager;
import com.boot.jx.auth.AuthStateManager.AuthState;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.rest.RestService;
import com.boot.utils.ArgUtil;
import com.javachinna.oauth2.user.SocialEnums.ChannelPartner;

public abstract class AbstractAuthenticator implements BasicAuthenticator {

	private static final long serialVersionUID = 6686144045235586188L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAuthenticator.class);

	@Value("${bootjx.linq.base.url:}")
	String linqBaseUrl;

	@Value("${bootjx.linq.base.callback.path:/linq/app/v1/connect/{provider}/callback}")
	String linqCallbackPath;

	@Autowired
	protected CommonHttpRequest commonHttpRequest;

	@Autowired
	protected AuthStateManager authStateManager;

	@Autowired
	protected RestService restService;

	public String getLinqBaseUrl() {
		if (ArgUtil.is(linqBaseUrl)) {
			return linqBaseUrl;
		}
		return commonHttpRequest.getServerHost() + "";
	}

	public AuthState createState() {
		return authStateManager.createState();
	}

	public String getUrl(String path) {
		return this.getLinqBaseUrl() + path;
	}

	public String redirectPath(String provider, ChannelPartner partner) {
		return linqCallbackPath.replace("{provider}", provider);
	}

	public String redirectUrl(String provider, ChannelPartner partner) {
		return this.getLinqBaseUrl() + redirectPath(provider, partner);
	}

}
