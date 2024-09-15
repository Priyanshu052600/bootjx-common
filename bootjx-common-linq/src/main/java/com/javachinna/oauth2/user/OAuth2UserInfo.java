package com.javachinna.oauth2.user;

import java.util.Map;

public abstract class OAuth2UserInfo extends BasicOAuth2UserInfo {
	private static final long serialVersionUID = -250485978851007935L;
	protected Map<String, Object> attributes;

	public OAuth2UserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}
}