package com.boot.jx.sso.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.jx.auth.AuthStateManager;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.rest.RestService;
import com.boot.model.MapModel;
import com.boot.utils.ArgUtil;
import com.boot.utils.CryptoUtil;
import com.boot.utils.UniqueID;
import com.boot.utils.Urly;
import com.javachinna.oauth2.user.CommonOAuth2UserInfo;
import com.javachinna.oauth2.user.OAuth2UserInfo;
import com.javachinna.oauth2.user.SocialEnums.ChannelPartner;
import com.javachinna.oauth2.user.SocialEnums.ChannelProvider;

@Component
public class OtplessAuthenticator extends AbstractAuthenticator {

	private static final long serialVersionUID = 6686144045235586188L;

	private static final Logger LOGGER = LoggerFactory.getLogger(OtplessAuthenticator.class);

	private static final String AUTHORITY = "https://login.microsoftonline.com";
	private static String scopes = "User.Read";

	@Autowired
	CommonHttpRequest commonHttpRequest;

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
		String nonce = UniqueID.generateString62();
		String state = MapModel.createInstance().put("csrfToken", authStateManager.getCsrfToken()).put("nonce", nonce)
				.encoder().encrypt().tokenize(10).encodeBase64().toString();
		return Urly.parse(getUrl("/linq/pub/v1/connect/whatsapp"))//
				.queryParam("response_type", "code") //
				// .queryParam("client_id", clientId) //
				.queryParam("redirect_uri", redirectUri).queryParam("state", state) // State
				.queryParam("nounce", nonce) //
				.queryParam("scope", scopes) //
				.queryParam("response_mode", "form_post") //
				.getURL();

	}

	@Override
	public OAuth2UserInfo doAuthenticate(String provider, ChannelPartner partner, MapModel body) {
		String credential = commonHttpRequest.getRequestParam("credential");
		String idToken = commonHttpRequest.getRequestParam("idToken");
		String state = commonHttpRequest.getRequestParam("state");
		OAuth2UserInfo userinfo = new CommonOAuth2UserInfo();
		userinfo.setProvider(ChannelProvider.WHATSAPP.getType());
		if (ArgUtil.is(credential)) {
			MapModel creds = CryptoUtil.getEncoder().message(credential).decodeBase64().toMapModel();
			userinfo.setAccessToken(creds.keyEntry("token").asString());
			userinfo.setProfileId(creds.pathEntry("waNumber").asString());
			userinfo.setName(creds.keyEntry("waName").orPathEntry("email.name").orPathEntry("mobile.name").asString());
			userinfo.setEmail(creds.pathEntry("email.email").asString());
			userinfo.setPhone(creds.keyEntry("waNumber").orPathEntry("mobile.number").asString());
			userinfo.setRecord(creds);
		}
		return userinfo;
	}

}
