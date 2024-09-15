package com.boot.jx.sso.service;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boot.jx.sso.model.BasicAuthWebhookRespHolder;
import com.boot.jx.sso.model.SocialProfile;
import com.boot.model.MapModel;
import com.javachinna.oauth2.user.OAuth2UserInfo;
import com.javachinna.oauth2.user.SocialEnums.ChannelPartner;
import com.javachinna.oauth2.user.SocialEnums.ChannelProvider;

public interface BasicAuthenticator extends Serializable {

	public static final Logger LOGGER = LoggerFactory.getLogger(BasicAuthenticator.class);

	public OAuth2UserInfo doAuthenticate(String provider, ChannelPartner partner, MapModel body) throws Exception;

	default public OAuth2UserInfo authenticate(String provider, ChannelPartner partner, MapModel body) {
		try {
			return this.doAuthenticate(provider, partner, body);
		} catch (Exception e) {
			LOGGER.error("ERROR", e);
		}
		return null;
	}

	String createAuthUrl(String provider, ChannelPartner partner)
			throws MalformedURLException, URISyntaxException, IOException;

	String createAuthUrl(String provider, ChannelPartner partner, String redirectUrl)
			throws MalformedURLException, URISyntaxException;

	default BasicAuthWebhookRespHolder webhook(ChannelProvider provider, ChannelPartner partner, MapModel body) {
		BasicAuthWebhookRespHolder holder = new BasicAuthWebhookRespHolder();
		holder.setProvider(provider);
		holder.setPartner(partner);
		if (body != null) {
			holder.setBody(body.toMap());
		}
		return holder;
	}

	default public BasicAuthWebhookRespHolder message(ChannelProvider provider, ChannelPartner partner, MapModel body) {
		BasicAuthWebhookRespHolder holder = new BasicAuthWebhookRespHolder();
		holder.setProvider(provider);
		holder.setPartner(partner);
		if (body != null) {
			holder.setBody(body.toMap());
		}
		return holder;
	}

	default public void promptConfirm(SocialProfile profile, String promptId, String orgName) {
	}

	public static interface TelegramAuthenticator extends BasicAuthenticator {

	}

	public static interface TwitterAuthenticator extends BasicAuthenticator {

	}

}
