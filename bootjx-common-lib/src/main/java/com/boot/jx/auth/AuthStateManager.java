package com.boot.jx.auth;

import java.io.Serializable;

import com.boot.jx.AppContextUtil;
import com.boot.model.MapModel;
import com.boot.utils.Random;
import com.boot.utils.UniqueID;

public abstract class AuthStateManager implements Serializable {
	private static final long serialVersionUID = -8304930738022690453L;

	public static class AuthState implements Serializable {
		private static final long serialVersionUID = -6534068662179480068L;

		private String csrfToken;
		private String nonce;
		private String redirectUrl;
		private long timestamp;
		private String domain;

		public String getCsrfToken() {
			return csrfToken;
		}

		public void setCsrfToken(String csrfToken) {
			this.csrfToken = csrfToken;
		}

		public String getNonce() {
			return nonce;
		}

		public void setNonce(String nonce) {
			this.nonce = nonce;
		}

		public String toString() {
			return MapModel.createInstance().put("csrfToken", getCsrfToken()).put("nonce", nonce).put("domain", domain)
					.put("redirectUrl", redirectUrl).encoder().encrypt().tokenize(10).encodeBase64().toString();
		}

		public String getRedirectUrl() {
			return redirectUrl;
		}

		public void setRedirectUrl(String redirectUrl) {
			this.redirectUrl = redirectUrl;
		}

		public AuthState redirectUrl(String redirectUrl) {
			this.redirectUrl = redirectUrl;
			return this;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}

	}

	private String csrfToken;
	private AuthState state;

	public String getCsrfToken() {
		return csrfToken;
	}

	public void setCsrfToken(String csrfToken) {
		this.csrfToken = csrfToken;
	}

	public String refreshCsrfToken() {
		this.csrfToken = Random.randomAlphaNumeric(10);
		return this.csrfToken;
	}

	public AuthState createState() {
		refreshCsrfToken();
		state = new AuthState();
		state.setTimestamp(System.currentTimeMillis());
		state.setNonce(UniqueID.generateString62());
		state.setCsrfToken(getCsrfToken());
		state.setDomain(AppContextUtil.getTenant());
		return state;
	}

	public AuthState fromState(String stateStr) {
		return MapModel.decoder(stateStr).decodeBase64().detokenize().decrypt().toObzect(AuthState.class);
	}

	public AuthState getState() {
		return state;
	}

	public void setState(AuthState state) {
		this.state = state;
	}

}
