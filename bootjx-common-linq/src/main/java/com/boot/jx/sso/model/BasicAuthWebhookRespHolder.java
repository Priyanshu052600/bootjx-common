package com.boot.jx.sso.model;

import java.util.HashMap;
import java.util.Map;

import com.javachinna.oauth2.user.SocialEnums.ChannelPartner;
import com.javachinna.oauth2.user.SocialEnums.ChannelProvider;

public class BasicAuthWebhookRespHolder {

	public static enum WebhookRespStatus {
		ACCEPTED, REJECTED
	}

	protected String id;
	protected String nonce;
	protected WebhookRespStatus status;
	protected String answer;
	protected Map<String, Object> body;
	protected ChannelProvider provider;
	protected ChannelPartner partner;
	protected Map<String, Object> data;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getBody() {
		return body;
	}

	public void setBody(Map<String, Object> body) {
		this.body = body;
	}

	public ChannelProvider getProvider() {
		return provider;
	}

	public void setProvider(ChannelProvider provider) {
		this.provider = provider;
	}

	public ChannelPartner getPartner() {
		return partner;
	}

	public void setPartner(ChannelPartner partner) {
		this.partner = partner;
	}

	public Map<String, Object> data() {
		if (this.data == null) {
			this.data = new HashMap<String, Object>();
		}
		return this.data;
	}

	public BasicAuthWebhookRespHolder data(String key, Object value) {
		this.data().put(key, value);
		return this;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public WebhookRespStatus getStatus() {
		return status;
	}

	public void setStatus(WebhookRespStatus status) {
		this.status = status;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
