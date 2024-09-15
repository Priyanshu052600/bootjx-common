package com.boot.jx.sso.model;

import com.javachinna.oauth2.user.BasicOAuth2UserInfo;

public class SocialProfile extends BasicOAuth2UserInfo {

	private static final long serialVersionUID = -6129557955513055322L;

	protected String profileUUId;

	String contactId;

	private String userId;

	boolean verified;

	public String getProfileUUId() {
		return profileUUId;
	}

	public void setProfileUUId(String profileUUId) {
		this.profileUUId = profileUUId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public void profileUUId(BasicOAuth2UserInfo info) {
		this.profileUUId = String.format("%s:%s", info.getProvider(), info.getProfileId()).toLowerCase();
	}
}
