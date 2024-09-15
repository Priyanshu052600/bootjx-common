package com.boot.jx.sso.model;

import java.io.Serializable;

import com.boot.jx.mongo.CommonDocInterfaces.TimeStampIndex.TimeStampDoc;

public class SsoUser extends TimeStampDoc implements Serializable {

	private static final long serialVersionUID = 1150855436045147691L;

	protected String userId;

	private String email;

	private String phone;

	private String name;

	private String password;

	private SocialProfile profile;

	private String providerProfileId;

	public String getProviderProfileId() {
		return providerProfileId;
	}

	public void setProviderProfileId(String providerUserIdId) {
		this.providerProfileId = providerUserIdId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public SocialProfile getProfile() {
		return profile;
	}

	public void setProfile(SocialProfile profile) {
		this.profile = profile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
