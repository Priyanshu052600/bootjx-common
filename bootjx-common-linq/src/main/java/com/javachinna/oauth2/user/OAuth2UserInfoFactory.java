package com.javachinna.oauth2.user;

import java.util.Map;

import com.javachinna.exception.OAuth2AuthenticationProcessingException;
import com.javachinna.oauth2.user.SocialEnums.ChannelProvider;

public class OAuth2UserInfoFactory {
	public static BasicOAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
		if (registrationId.equalsIgnoreCase(ChannelProvider.GOOGLE.getType())) {
			return new GoogleOAuth2UserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase(ChannelProvider.FACEBOOK.getType())) {
			return new FacebookOAuth2UserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase(ChannelProvider.GITHUB.getType())) {
			return new GithubOAuth2UserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase(ChannelProvider.LINKEDIN.getType())) {
			return new LinkedinOAuth2UserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase(ChannelProvider.TWITTER.getType())) {
			return new GithubOAuth2UserInfo(attributes);
		} else {
			throw new OAuth2AuthenticationProcessingException(
					"Sorry! Login with " + registrationId + " is not supported yet.");
		}
	}
}