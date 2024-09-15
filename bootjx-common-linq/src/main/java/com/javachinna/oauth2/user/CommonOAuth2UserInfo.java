package com.javachinna.oauth2.user;

import java.util.HashMap;

import com.boot.utils.ArgUtil;
import com.javachinna.oauth2.user.SocialEnums.ChannelPartner;

public class CommonOAuth2UserInfo extends OAuth2UserInfo {

	private static final long serialVersionUID = 7647308019984179631L;

	public CommonOAuth2UserInfo() {
		super(new HashMap<String, Object>());
	}

	public CommonOAuth2UserInfo provider(String provider) {
		this.setProvider(provider);
		return this;
	}

	public CommonOAuth2UserInfo partner(ChannelPartner partner) {
		this.setPartner(ArgUtil.parseAsString(partner));
		return this;
	}

}