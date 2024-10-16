package com.boot.jx.sso.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.boot.jx.auth.AuthStateManager;
import com.boot.jx.auth.AuthStateManager.AuthState;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.rest.RestService;
import com.boot.model.MapModel;
import com.boot.utils.Urly;
import com.javachinna.oauth2.user.CommonOAuth2UserInfo;
import com.javachinna.oauth2.user.OAuth2UserInfo;
import com.javachinna.oauth2.user.SocialEnums.ChannelPartner;
import com.javachinna.oauth2.user.SocialEnums.ChannelProvider;

@Component
public class OultlookAuthenticator extends AbstractAuthenticator {

	private static final long serialVersionUID = 6686144045235586188L;

	private static final Logger LOGGER = LoggerFactory.getLogger(OultlookAuthenticator.class);

	private static final String AUTHORITY = "https://login.microsoftonline.com";
	private static final String AUTHORIZE_URL = AUTHORITY + "/common/oauth2/v2.0/authorize";
	private static final String AUTHORIZE_TOKEN = AUTHORITY + "/common/oauth2/v2.0/token";
	private static String scopes = "User.Read";

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Value("${outlook.app.client.id:}")
	String clientId;

	@Value("${outlook.app.client.secret:}")
	String clientSecret;

	@Autowired
	RestService restService;

	@PostConstruct
	public void init() {
		// FirebaseApp.initializeApp(firebaseOptions());
	}

	@Override
	public String createAuthUrl(String provider, ChannelPartner partner)
			throws MalformedURLException, URISyntaxException {
		return this.createAuthUrl(provider, partner, getUrl("/linq/app/v1/connect/" + provider + "/callback"));
	}

	@Override
	public String createAuthUrl(String provider, ChannelPartner partner, String redirectUri)
			throws URISyntaxException, MalformedURLException {
		/// &state=fooobar&scope=r_liteprofile%20r_emailaddress%20w_member_social
		AuthState state = authStateManager.createState();

		return Urly.parse(AUTHORIZE_URL).queryParam("response_type", "code") //
				.queryParam("client_id", clientId) //
				.queryParam("redirect_uri", redirectUri).queryParam("state", state.toString()) // State
				.queryParam("nonce", state.getNonce()) //
				.queryParam("scope", scopes) //
				.queryParam("response_mode", "form_post") //
				.getURL();

	}

	@Override
	public OAuth2UserInfo doAuthenticate(String provider, ChannelPartner partner, MapModel body) {
		String code = commonHttpRequest.getRequestParam("code");
		String idToken = commonHttpRequest.getRequestParam("idToken");
		String state = commonHttpRequest.getRequestParam("state");
		OAuth2UserInfo userinfo = new CommonOAuth2UserInfo();
		userinfo.setProvider(ChannelProvider.OUTLOOK.getType());

		// https://learn.microsoft.com/en-us/graph/auth-v2-user
		// System.out.println("Code===" + code);

		// https://learn.microsoft.com/en-us/graph/overview
		MapModel tokenResponse = restService.ajax(AUTHORIZE_TOKEN)//
				.field("grant_type", "authorization_code")//
				.field("code", code)//
				.field("client_id", clientId)//
				.field("client_secret", clientSecret)//
				.field("redirect_uri", getUrl("/linq/app/v1/connect/outlook/callback")).submit().asMapModel();

		// System.out.println("Token===" + tokenResponse.toJsonPretty());

		String accessToken = tokenResponse.keyEntry("access_token").asString();
		userinfo.setAccessToken(accessToken);

		MapModel profileResponse = restService.ajax("https://graph.microsoft.com/v1.0/me")
				.header("Authorization", "Bearer " + accessToken).get().asMapModel();

		// System.out.println("Token===" + profileResponse.toJsonPretty());
		userinfo.setProfileId(profileResponse.getString("id"));
		userinfo.setName(profileResponse.keyEntry("displayName").asString());
		userinfo.setEmail(profileResponse.keyEntry("mail").orKeyEntry("userPrincipalName").asString());
		userinfo.setPhone(profileResponse.keyEntry("mobilePhone").asString());
		userinfo.setJobTitle(profileResponse.keyEntry("jobTitle").asString());

		return userinfo;
	}

}
