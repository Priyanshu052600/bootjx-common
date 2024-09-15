package com.boot.jx.sso.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.rest.RestService;
import com.boot.jx.sso.service.AuthStateManager.AuthState;
import com.boot.model.MapModel;
import com.boot.utils.ArgUtil;
import com.boot.utils.Urly;
import com.javachinna.oauth2.user.CommonOAuth2UserInfo;
import com.javachinna.oauth2.user.OAuth2UserInfo;
import com.javachinna.oauth2.user.SocialEnums.ChannelPartner;
import com.javachinna.oauth2.user.SocialEnums.ChannelProvider;

@Component
public class FacebookAuthenticator extends AbstractAuthenticator {

	private static final long serialVersionUID = 6686144045235586188L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FacebookAuthenticator.class);

	private static final String FACEBOOK = "https://www.facebook.com";
	private static final String FACEBOOK_OAUTH_URL = FACEBOOK + "/v16.0/dialog/oauth";
	private static final String FACEBOOK_GRAPHAPI = "https://graph.facebook.com";
	private static final String AUTHORIZE_TOKEN = FACEBOOK_GRAPHAPI + "/v16.0/oauth/access_token";
	private static final String GRAPH_API_ME = FACEBOOK_GRAPHAPI + "/v2.3/me";
	private static String scopes = "User.Read";

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Value("${facebook.app.client.id}")
	String clientId;

	@Value("${facebook.app.client.secret}")
	String clientSecret;

	@Autowired
	AuthStateManager authStateManager;

	@Autowired
	RestService restService;

	@PostConstruct
	public void init() {
		// FirebaseApp.initializeApp(firebaseOptions());
	}

	@Override
	public String createAuthUrl(String provider, ChannelPartner partner)
			throws MalformedURLException, URISyntaxException {
		return this.createAuthUrl(provider, partner, getUrl("/linq/app/v1/connect/facebook/callback"));
	}

	@Override
	public String createAuthUrl(String provider, ChannelPartner partner, String redirectUrl)
			throws MalformedURLException, URISyntaxException {
		/// &state=fooobar&scope=r_liteprofile%20r_emailaddress%20w_member_social
		AuthState state = authStateManager.createState();
		state.setRedirectUrl(redirectUrl);
		return Urly.parse(FACEBOOK_OAUTH_URL).queryParam("response_type", "code granted_scopes") //
				.queryParam("client_id", clientId) //
				.queryParam("redirect_uri", redirectUrl).queryParam("state", state.toString()) // State
				.queryParam("nonce", state.getNonce()) //
				.queryParam("scope", "public_profile email") //
				// .queryParam("response_mode", "form_post") //
				.getURL();
	}

	@Override
	public OAuth2UserInfo doAuthenticate(String provider, ChannelPartner partner, MapModel body) {
		String code = commonHttpRequest.getRequestParam("code");
		String idToken = commonHttpRequest.getRequestParam("idToken");
		String stateStr = commonHttpRequest.getRequestParam("state");

		String redirectUrl = "/linq/app/v1/connect/facebook/callback";
		if (ArgUtil.is(stateStr)) {
			AuthState state = authStateManager.fromState(stateStr);
			redirectUrl = state.getRedirectUrl();
		}

		OAuth2UserInfo userinfo = new CommonOAuth2UserInfo();
		userinfo.setProvider(ChannelProvider.FACEBOOK.getType());

		// https://learn.microsoft.com/en-us/graph/auth-v2-user
		// System.out.println("Code===" + code);

		// https://learn.microsoft.com/en-us/graph/overview
		MapModel tokenResponse = restService.ajax(AUTHORIZE_TOKEN)//
				.field("client_id", clientId)//
				.field("redirect_uri", redirectUrl).submit().field("client_secret", clientSecret)//
				.field("code", code)//
				// .field("grant_type", "authorization_code")//
				.asMapModel();

		// System.out.println("Token===" + tokenResponse.toJsonPretty());

		String accessToken = tokenResponse.keyEntry("access_token").asString();
		userinfo.setAccessToken(accessToken);

		MapModel profileResponse = restService.ajax(GRAPH_API_ME).queryParam("fields", "id,name,picture,email")
				.queryParam("access_token", accessToken).get().asMapModel();

		// System.out.println("profileResponse===" + profileResponse.toJsonPretty());
		userinfo.setProfileId(profileResponse.getString("id"));
		userinfo.setName(profileResponse.keyEntry("name").asString());
		userinfo.setEmail(profileResponse.keyEntry("email").orKeyEntry("userPrincipalName").asString());
		userinfo.setPicture(profileResponse.pathEntry("picture.data.url").asString());
		userinfo.setProfileLink(profileResponse.getString("link"));

		return userinfo;
	}

}
