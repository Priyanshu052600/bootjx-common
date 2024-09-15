package com.javachinna.oauth2.user;

import java.util.Map;

import com.javachinna.oauth2.user.SocialEnums.ChannelProvider;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

	public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getProfileId() {
		return (String) attributes.get("sub");
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
		return (String) attributes.get("picture");
	}

	@Override
	public String getProvider() {
		return ChannelProvider.GOOGLE.getType();
	}
}