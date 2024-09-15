package com.boot.jx.sso.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.boot.jx.mongo.CommonDocInterfaces.TimeStampIndex;
import com.boot.jx.sso.SsoConstants.ASSOCIATION_TYPE;

public class Verification implements Serializable {

	private static final long serialVersionUID = -2509261092502765327L;

	protected String verificationId;

	private String verificationKey;
	private String verificationSecret;
	private String verificationType;
	private String verificationGroup;

	private String title;
	private String description;
	private String picture;

	private ASSOCIATION_TYPE associationType;

	private Set<String> profileTypes;

	private Map<String, Object> form;

	private TimeStampIndex created;
	private TimeStampIndex updated;
	private TimeStampIndex validated;

	private Map<String, Object> config;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<String> getProfileTypes() {
		return profileTypes;
	}

	public void setProfileTypes(Set<String> profileTypes) {
		this.profileTypes = profileTypes;
	}

	public TimeStampIndex getCreated() {
		return created;
	}

	public void setCreated(TimeStampIndex created) {
		this.created = created;
	}

	public TimeStampIndex getUpdated() {
		return updated;
	}

	public void setUpdated(TimeStampIndex updated) {
		this.updated = updated;
	}

	public String getVerificationId() {
		return verificationId;
	}

	public void setVerificationId(String verificationId) {
		this.verificationId = verificationId;
	}

	public Map<String, Object> getForm() {
		return form;
	}

	public void setForm(Map<String, Object> form) {
		this.form = form;
	}

	public String getVerificationKey() {
		return verificationKey;
	}

	public void setVerificationKey(String verificationKey) {
		this.verificationKey = verificationKey;
	}

	public String getVerificationSecret() {
		return verificationSecret;
	}

	public void setVerificationSecret(String verificationSecret) {
		this.verificationSecret = verificationSecret;
	}

	public ASSOCIATION_TYPE getAssociationType() {
		return associationType;
	}

	public void setAssociationType(ASSOCIATION_TYPE associationType) {
		this.associationType = associationType;
	}

	public String getVerificationType() {
		return verificationType;
	}

	public void setVerificationType(String verificationType) {
		this.verificationType = verificationType;
	}

	public String getVerificationGroup() {
		return verificationGroup;
	}

	public void setVerificationGroup(String verificationGroup) {
		this.verificationGroup = verificationGroup;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Map<String, Object> getConfig() {
		return config;
	}

	public void setConfig(Map<String, Object> config) {
		this.config = config;
	}

	public TimeStampIndex getValidated() {
		return validated;
	}

	public void setValidated(TimeStampIndex validated) {
		this.validated = validated;
	}
}
