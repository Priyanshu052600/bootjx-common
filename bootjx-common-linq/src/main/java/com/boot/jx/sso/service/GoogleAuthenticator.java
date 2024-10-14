package com.boot.jx.sso.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.jx.auth.AuthStateManager;
import com.boot.jx.auth.AuthStateManager.AuthState;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.rest.RestService;
import com.boot.model.MapModel;
import com.boot.utils.ArgUtil;
import com.boot.utils.FileUtil;
import com.boot.utils.IoUtils;
import com.boot.utils.Urly;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.javachinna.oauth2.user.CommonOAuth2UserInfo;
import com.javachinna.oauth2.user.OAuth2UserInfo;
import com.javachinna.oauth2.user.SocialEnums.ChannelPartner;
import com.javachinna.oauth2.user.SocialEnums.ChannelProvider;

@Component
public class GoogleAuthenticator extends AbstractAuthenticator {

	private static final long serialVersionUID = 6686144045235586188L;

	private static final Logger LOGGER = LoggerFactory.getLogger(GoogleAuthenticator.class);

	private static final String GOOGLE = "https://accounts.google.com";
	private static final String GOOGLE_OAUTH_URL = GOOGLE + "/o/oauth2/v2/auth";
	private static final String FACEBOOK_GRAPHAPI = "https://oauth2.googleapis.com";
	private static final String AUTHORIZE_TOKEN = FACEBOOK_GRAPHAPI + "/token";

	private static final GsonFactory jacksonFactory = new GsonFactory();
	private static final NetHttpTransport netHttpTransport = new NetHttpTransport();

	@Autowired
	CommonHttpRequest commonHttpRequest;

	private MapModel googleCreds;

	public MapModel googleCreds() {
		if (this.googleCreds == null) {
			try {
				InputStream leftStream = FileUtil.getExternalOrInternalResourceAsStream("providers/google-creds.json",
						GoogleAuthenticator.class);
				String leftJson = IoUtils.inputstream_to_string(leftStream);
				this.googleCreds = MapModel.from(leftJson);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return googleCreds;
	}

	private static GoogleIdTokenVerifier verifier;

	// From:
	// https://developers.google.com/identity/sign-in/android/backend-auth#using-a-google-api-client-library
	// If you retrieved the token on Android using the Play Services 8.3 API or
	// newer, set
	// the issuer to "https://accounts.google.com". Otherwise, set the issuer to
	// "accounts.google.com". If you need to verify tokens from multiple sources,
	// build
	// a GoogleIdTokenVerifier for each issuer and try them both.

	public GoogleIdTokenVerifier getVerifier() {
		if (verifier == null) {

			String clientId = googleCreds().pathEntry("web.client_id").asString();

			verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory)
					.setAudience(Collections.singletonList(clientId)).build();
		}
		return verifier;
	}

	@Autowired
	AuthStateManager ssoSessionBean;

	@Autowired
	RestService restService;

	@Override
	public String createAuthUrl(String provider, ChannelPartner partner)
			throws MalformedURLException, URISyntaxException {
		return this.createAuthUrl(provider, partner, redirectUrl(provider, partner));
	}

	@Override
	public String createAuthUrl(String provider, ChannelPartner partner, String redirectUrl)
			throws MalformedURLException, URISyntaxException {
		AuthState state = ssoSessionBean.createState();
		state.setRedirectUrl(redirectUrl);

		MapModel googleCreds = googleCreds();
		String clientId = googleCreds.pathEntry("web.client_id").asString();

		return Urly.parse(GOOGLE_OAUTH_URL).queryParam("response_type", "code").queryParam("client_id", clientId)
				.queryParam("scope", "openid email profile").queryParam("redirect_uri", redirectUrl)
				.queryParam("state", state.toString()).queryParam("nonce", state.getNonce()).getURL();
	}

	@Override
	public OAuth2UserInfo doAuthenticate(String provider, ChannelPartner partner, MapModel body) {
		String token = commonHttpRequest.getRequestParam("credential");
		String stateStr = commonHttpRequest.getRequestParam("state");
		String code = commonHttpRequest.getRequestParam("code");
		String scope = commonHttpRequest.getRequestParam("scope");

		String redirectUrl = redirectPath(provider, partner);
		if (ArgUtil.is(stateStr)) {
			AuthState state = ssoSessionBean.fromState(stateStr);
			redirectUrl = state.getRedirectUrl();
		}

		if (ArgUtil.is(code)) {
			String clientId = googleCreds.pathEntry("web.client_id").asString();
			String clientSecret = googleCreds.pathEntry("web.client_secret").asString();
			MapModel tokenResponse = restService.ajax(AUTHORIZE_TOKEN)//
					.field("code", code)//
					.field("client_id", clientId)//
					.field("client_secret", clientSecret)//
					.field("grant_type", "authorization_code")//
					.field("redirect_uri", redirectUrl)//
					.submit().asMapModel();
			// System.out.println("Token===" + tokenResponse.toJsonPretty());
			// token = tokenResponse.keyEntry("access_token").asString();
			token = tokenResponse.keyEntry("id_token").asString();
		}

		if (ArgUtil.is(token)) {
			GoogleIdToken idToken = null;
			try {
				idToken = getVerifier().verify(token);
				if (idToken != null) {
					GoogleIdToken.Payload payload = idToken.getPayload();
					OAuth2UserInfo userinfo = new CommonOAuth2UserInfo();
					userinfo.setProvider(ChannelProvider.GOOGLE.getType());
					// Get profile information from payload
					userinfo.setProfileId(payload.getSubject());
					userinfo.setEmail(payload.getEmail());
					userinfo.setName(ArgUtil.parseAsString(payload.get("name")));
					userinfo.setPicture(ArgUtil.parseAsString(payload.get("picture")));
					
					return userinfo;

				} else {
					LOGGER.warn("Invalid Google ID token.");
				}
			} catch (GeneralSecurityException e) {
				LOGGER.warn(e.getLocalizedMessage());
			} catch (IOException e) {
				LOGGER.warn(e.getLocalizedMessage());
			}
		}
		return null;
	}

}
