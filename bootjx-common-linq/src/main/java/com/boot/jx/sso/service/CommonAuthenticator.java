package com.boot.jx.sso.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.jx.sso.model.BasicAuthWebhookRespHolder;
import com.boot.jx.sso.model.SocialProfile;
import com.boot.model.MapModel;
import com.boot.utils.ArgUtil;
import com.javachinna.oauth2.user.OAuth2UserInfo;
import com.javachinna.oauth2.user.SocialEnums.ChannelPartner;
import com.javachinna.oauth2.user.SocialEnums.ChannelProvider;

@Component
public class CommonAuthenticator extends AbstractAuthenticator {

	private static final long serialVersionUID = 6686144045235586188L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonAuthenticator.class);

	@Autowired(required = false)
	private LinkedinAuthenticator linkedinAuthenticator;

	@Autowired(required = false)
	private OultlookAuthenticator oultlookAuthenticator;

	@Autowired(required = false)
	private FacebookAuthenticator facebookAuthenticator;

	@Autowired(required = false)
	private GoogleAuthenticator googleAuthenticator;

	@Autowired(required = false)
	private TwitterAuthenticator twitterAuthenticator;

	@Autowired(required = false)
	private OtplessAuthenticator otplessAuthenticator;

	@Autowired(required = false)
	private FirebaseAuthenticator firebaseAuthenticator;

	@Autowired(required = false)
	private TruecallerAuthenticator truecallerAuthenticator;

	@Autowired(required = false)
	private TelegramAuthenticator telegramAuthenticator;

	public BasicAuthenticator find(String provider, String partner) {
		if (ChannelProvider.LINKEDIN.getType().equalsIgnoreCase(provider)) {
			return linkedinAuthenticator;
		} else if (ChannelProvider.OUTLOOK.getType().equalsIgnoreCase(provider)) {
			return oultlookAuthenticator;
		} else if (ChannelProvider.FACEBOOK.getType().equalsIgnoreCase(provider)) {
			return facebookAuthenticator;
		} else if (ChannelProvider.GOOGLE.getType().equalsIgnoreCase(provider)) {
			return googleAuthenticator;
		} else if (ChannelProvider.TWITTER.getType().equalsIgnoreCase(provider)) {
			return twitterAuthenticator;
		} else if (ChannelProvider.WHATSAPP.getType().equalsIgnoreCase(provider)) {
			return otplessAuthenticator;
		} else if (ChannelProvider.TELEGRAM.getType().equalsIgnoreCase(provider)) {
			return telegramAuthenticator;
		} else if (ChannelPartner.TRUECALLER.name().equalsIgnoreCase(provider)
				|| (ChannelProvider.MOBILE.is(provider) && ChannelPartner.TRUECALLER.is(partner))) {
			return truecallerAuthenticator;
		} else {
			return firebaseAuthenticator;
		}
	}

	public BasicAuthenticator find(ChannelProvider provider, ChannelPartner partner) {
		return find(ArgUtil.parseAsString(provider), ArgUtil.parseAsString(partner));
	}

	private BasicAuthenticator find(String provider, ChannelPartner partner) {
		return find(provider, ArgUtil.parseAsString(partner));
	}

	@PostConstruct
	public void init() {
		// FirebaseApp.initializeApp(firebaseOptions());
	}

	@Override
	public String createAuthUrl(String provider, ChannelPartner partner) throws URISyntaxException, IOException {
		return find(provider, partner).createAuthUrl(provider, partner);
	}

	@Override
	public String createAuthUrl(String provider, ChannelPartner partner, String redirectUri)
			throws URISyntaxException, MalformedURLException {
		return find(provider, partner).createAuthUrl(provider, partner, redirectUri);
	}

	@Override
	public BasicAuthWebhookRespHolder webhook(ChannelProvider provider, ChannelPartner partner, MapModel body) {
		return find(provider, partner).webhook(provider, partner, body);
	}

	@Override
	public BasicAuthWebhookRespHolder message(ChannelProvider provider, ChannelPartner partner, MapModel body) {
		return find(provider, partner).message(provider, partner, body);
	}

	@Override
	public OAuth2UserInfo doAuthenticate(String provider, ChannelPartner partner, MapModel body) throws Exception {
		return find(provider, partner).doAuthenticate(provider, partner, body);
	}

	@Override
	public void promptConfirm(SocialProfile profile, String promptId, String orgName) {
		find(profile.getProvider(), profile.getPartner()).promptConfirm(profile, promptId, orgName);
	}

	public LinkedinAuthenticator getLinkedinAuthenticator() {
		return linkedinAuthenticator;
	}

	public OultlookAuthenticator getOultlookAuthenticator() {
		return oultlookAuthenticator;
	}

	public FacebookAuthenticator getFacebookAuthenticator() {
		return facebookAuthenticator;
	}

	public GoogleAuthenticator getGoogleAuthenticator() {
		return googleAuthenticator;
	}

	public OtplessAuthenticator getOtplessAuthenticator() {
		return otplessAuthenticator;
	}

	public FirebaseAuthenticator getFirebaseAuthenticator() {
		return firebaseAuthenticator;
	}

	public TruecallerAuthenticator getTruecallerAuthenticator() {
		return truecallerAuthenticator;
	}

}
