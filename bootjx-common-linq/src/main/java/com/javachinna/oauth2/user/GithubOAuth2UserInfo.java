package com.javachinna.oauth2.user;

import java.util.Map;

import com.javachinna.oauth2.user.SocialEnums.ChannelProvider;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

	public GithubOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getProfileId() {
		return ((Integer) attributes.get("id")).toString();
	}

	@Override
	public String getName() {
		return (String) attributes.get("name");
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getPicture() {
		return (String) attributes.get("avatar_url");
	}
	
	@Override
	public String getProvider() {
		return ChannelProvider.GITHUB.getType();
	}
}