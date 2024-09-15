package com.boot.jx.sso.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.boot.jx.sso.model.BasicAuthWebhookRespHolder;
import com.boot.jx.sso.service.AuthStateManager.AuthState;
import com.boot.model.MapModel;
import com.boot.utils.ArgUtil;
import com.boot.utils.Urly;
import com.javachinna.oauth2.user.CommonOAuth2UserInfo;
import com.javachinna.oauth2.user.OAuth2UserInfo;
import com.javachinna.oauth2.user.SocialEnums.ChannelPartner;
import com.javachinna.oauth2.user.SocialEnums.ChannelProvider;

@Component
public class TruecallerAuthenticator extends AbstractAuthenticator {

	private static final long serialVersionUID = 6686144045235586188L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TruecallerAuthenticator.class);

	private static final String AUTHORITY = "https://login.microsoftonline.com";
	private static String scopes = "User.Read";

	@Value("${truecaller.app.name}")
	String appName;

	@Value("${truecaller.app.key}")
	String appKey;

	@Value("${truecaller.app.domain}")
	String appDomain;

	@Value("${truecaller.callback.url}")
	String callbackUrl;

	@Override
	public String createAuthUrl(String provider, ChannelPartner partner)
			throws MalformedURLException, URISyntaxException {
		return this.createAuthUrl(provider, partner,
				getUrl("/linq/app/v1/connect/" + provider + "/callback?=" + ChannelPartner.TRUECALLER));
	}

	@Override
	public String createAuthUrl(String provider, ChannelPartner partner, String redirectUri)
			throws MalformedURLException, URISyntaxException {
		AuthState state = createState();
		return Urly.parse(getUrl("/linq/pub/v1/connect/firebase/" + provider))//
				.queryParam("response_type", "code") //
				// .queryParam("client_id", clientId) //
				.queryParam("redirect_uri", redirectUri).queryParam("state", state.toString()) // State
				.queryParam("nonce", state.getNonce()) //
				// .queryParam("scope", scopes) //
				.queryParam("response_mode", "form_post") //
				.getURL();
	}

	@Override
	public BasicAuthWebhookRespHolder webhook(ChannelProvider provider, ChannelPartner partner, MapModel body) {
		String requestId = body.getString("requestId");
		// String accessToken = body.getString("accessToken");
		BasicAuthWebhookRespHolder doc = super.webhook(provider, partner, body);
		doc.setNonce(requestId);
		return doc;
	}

	@Override
	public OAuth2UserInfo doAuthenticate(String provider, ChannelPartner partner, MapModel body) {
		String requestId = body.getString("requestId");
		String accessToken = body.getString("accessToken");
		String endPoint = body.getString("endpoint", "https://profile4-noneu.truecaller.com/v1/default");

		try {
			MapModel profile = restService.ajax(endPoint).header("Authorization", "Bearer " + accessToken).get()
					.asMapModel();

			OAuth2UserInfo userinfo = new CommonOAuth2UserInfo().provider(provider).partner(partner);
			// System.out.println("===" + profile.toJson());

			userinfo.setProfileId(profile.getString("userId"));
			userinfo.setName(
					profile.pathEntry("name.first").asString() + " " + profile.pathEntry("name.last").asString());
			userinfo.setPicture(profile.pathEntry("avatarUrl").asString());

			userinfo.setAccessToken(accessToken);
			userinfo.setEmail(profile.pathEntry("onlineIdentities.email").asString());
			userinfo.setPhone(profile.pathEntry("phoneNumbers.[0]").asString());
			userinfo.setRecord(profile.toMap());
			return userinfo;
		} catch (Exception e) {
			LOGGER.error("TrueCallerApiErrror", e);
		}
		return null;
	}

	public String getTruecallerPublicOptions() {
		return MapModel.createInstance().put("production", false).put("truecaller", MapModel.createInstance() //
				.put("appKey", this.appKey) //
				.put("appName", this.appName) //
				.put("appDomain", this.appDomain) //
				.put("callbackUrl", this.callbackUrl) //
				.toMap()).toJson();
	}

}
