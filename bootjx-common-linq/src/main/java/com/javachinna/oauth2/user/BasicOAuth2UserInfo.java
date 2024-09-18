package com.javachinna.oauth2.user;

import java.io.Serializable;

import com.boot.utils.ArgUtil;
import com.boot.utils.StringUtils;
import com.javachinna.oauth2.user.SocialEnums.ChannelProvider;

public abstract class BasicOAuth2UserInfo implements Serializable {

	private static final long serialVersionUID = 6766553013282715387L;
	String name;
	String email;
	String phone;

	String jobTitle;
	String accessToken;

	String picture;
	String provider;
	String partner;
	String profileId;
	String profileHandle;
	String profileLink;
	Object record;

	public void importFrom(BasicOAuth2UserInfo profile) {
		this.name = profile.getName();
		this.email = profile.getEmail();
		this.phone = profile.getPhone();

		this.partner = profile.getPartner();
		this.provider = StringUtils.toLowerCase(profile.getProvider());
		this.profileId = profile.getProfileId();
		this.profileHandle = profile.getProfileHandle();
		this.profileLink = profile.getProfileLink();
		this.picture = profile.getPicture();

		this.jobTitle = profile.getJobTitle();
		this.accessToken = profile.getAccessToken();
		this.record = profile.getRecord();
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setProvider(String providerType) {
		this.provider = providerType;
	}

	public String getProvider() {
		return provider;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public Object getRecord() {
		return record;
	}

	public void setRecord(Object record) {
		this.record = record;
	}

	public String getProfileHandle() {
		return profileHandle;
	}

	public void setProfileHandle(String profileHandle) {
		this.profileHandle = profileHandle;
	}

	public String getProfileLink() {
		if (ChannelProvider.LINKEDIN.is(provider)) {
			if (ArgUtil.is(this.profileHandle)) {
				return "https://www.linkedin.com/in/" + this.profileHandle;
			}
		}
		return this.profileLink;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public void setProfileLink(String profileLink) {
		this.profileLink = profileLink;
	}
}