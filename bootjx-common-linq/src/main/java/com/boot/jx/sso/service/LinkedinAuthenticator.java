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
import com.boot.model.MapModel;
import com.boot.utils.Urly;
import com.javachinna.oauth2.user.CommonOAuth2UserInfo;
import com.javachinna.oauth2.user.OAuth2UserInfo;
import com.javachinna.oauth2.user.SocialEnums.ChannelPartner;
import com.javachinna.oauth2.user.SocialEnums.ChannelProvider;

@Component
public class LinkedinAuthenticator extends AbstractAuthenticator {

	private static final long serialVersionUID = 6686144045235586188L;

	private static final Logger LOGGER = LoggerFactory.getLogger(LinkedinAuthenticator.class);

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Value("${linkedin.app.client.id}")
	String linkedinAppClientId;

	@Value("${linkedin.app.client.secret}")
	String linkedinAppClientSecret;

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
		return this.createAuthUrl(provider, partner, getUrl("/linq/app/v1/connect/" + provider + "/callback"));
	}

	@Override
	public String createAuthUrl(String provider, ChannelPartner partner, String redirectUri)
			throws URISyntaxException, MalformedURLException {
		/// &state=fooobar&scope=r_liteprofile%20r_emailaddress%20w_member_social
		authStateManager.refreshCsrfToken();
		String state = MapModel.createInstance().put("csrfToken", authStateManager.getCsrfToken()).encoder().encrypt()
				.tokenize(10).encodeBase64().toString();

		/*
		 * https://medium.com/@ErincYigit/linkedin-authentication-and-fetching-user-data
		 * -from-api-b820835471c8
		 */
		return Urly.parse("https://www.linkedin.com/oauth/v2/authorization").queryParam("response_type", "code")
				.queryParam("client_id", linkedinAppClientId).queryParam("redirect_uri", redirectUri)
				.queryParam("state", state)
				/*
				 * https://learn.microsoft.com/en-us/linkedin/shared/authentication/
				 * authorization-code-flow?context=linkedin%2Fcontext&tabs=HTTPS1
				 */
				.queryParam("scope", "r_liteprofile r_emailaddress profile email openid r_basicprofile")

				.getURL();

	}

	@Override
	public OAuth2UserInfo doAuthenticate(String provider, ChannelPartner partner, MapModel body) {
		String code = commonHttpRequest.getRequestParam("code");
		OAuth2UserInfo userinfo = new CommonOAuth2UserInfo();

		MapModel resp = restService.ajax("https://www.linkedin.com/oauth/v2/accessToken")
				.queryParam("grant_type", "authorization_code").queryParam("code", code)
				.queryParam("client_id", linkedinAppClientId).queryParam("client_secret", linkedinAppClientSecret)
				.queryParam("redirect_uri", getUrl("/linq/app/v1/connect/linkedin/callback")).get().asMapModel();
		String accessToken = resp.keyEntry("access_token").asString();
		userinfo.setAccessToken(accessToken);

		MapModel profile = restService.ajax("https://api.linkedin.com/v2/me")
				.header("Authorization", "Bearer " + accessToken)
				.queryParam("projection",
						"(id,firstName,lastName,headline,vanityName,profilePicture(displayImage~:playableStreams))")
				.get().asMapModel();

		// System.out.println("===" + profile.toJson());

		userinfo.setProfileId(profile.getString("id"));
		userinfo.setName(profile.pathEntry("firstName.localized.en_US").asString() + " "
				+ profile.pathEntry("lastName.localized.en_US").asString());
		userinfo.setPicture(
				profile.pathEntry("profilePicture.displayImage~.elements.[0].identifiers.[0].identifier").asString());
		userinfo.setProfileHandle(profile.keyEntry("vanityName").asString());

		MapModel email = restService.ajax("https://api.linkedin.com/v2/emailAddress")
				.header("Authorization", "Bearer " + accessToken).queryParam("q", "members")
				.queryParam("projection", "(elements*(handle~))").get().asMapModel();

		userinfo.setEmail(email.pathEntry("elements.[0].handle~.emailAddress").asString());
		userinfo.setProvider(ChannelProvider.LINKEDIN.getType());

		return userinfo;
	}

}
