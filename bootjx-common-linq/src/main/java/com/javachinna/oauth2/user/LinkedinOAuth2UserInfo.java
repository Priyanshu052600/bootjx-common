package com.javachinna.oauth2.user;

import java.util.Map;

import com.javachinna.oauth2.user.SocialEnums.ChannelProvider;

public class LinkedinOAuth2UserInfo extends OAuth2UserInfo {

	public LinkedinOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getProfileId() {
		return (String) attributes.get("id");
	}

	@Override
	public String getName() {
		return ((String) attributes.get("localizedFirstName")).concat(" ")
				.concat((String) attributes.get("localizedLastName"));
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("emailAddress");
	}

	@Override
	public String getPicture() {
		return (String) attributes.get("pictureUrl");
	}

	@Override
	public String getProvider() {
		return ChannelProvider.LINKEDIN.getType();
	}
}