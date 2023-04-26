package com.boot.jx.exception;

import com.boot.jx.api.AResponse;
import com.boot.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmxApiError extends AResponse<Object> {

	private String body;
	private int rawStatusCode;

	/**
	 * errorKey is messageKey
	 */
	@Deprecated
	private String errorKey;

	public AmxApiError(String errorKey, String errorMessage) {
		super();
		this.errorKey = errorKey;
		this.message = errorMessage;
	}

	public AmxApiError(IExceptionEnum error, String errorKey, String errorMessage) {
		super();
		this.statusKey = ArgUtil.isEmpty(error) ? null : error.getStatusKey();
		this.errorKey = errorKey;
		this.messageKey = errorKey;
		this.message = errorMessage;
	}

	public AmxApiError(IExceptionEnum error) {
		super();
		this.statusKey = ArgUtil.isEmpty(error) ? null : error.getStatusKey();
	}

	public AmxApiError() {
		super();
	}

	public String getErrorKey() {
		return errorKey;
	}

	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	public AmxApiError meta(Object meta) {
		this.meta = meta;
		return this;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getRawStatusCode() {
		return rawStatusCode;
	}

	public void setRawStatusCode(int rawStatusCode) {
		this.rawStatusCode = rawStatusCode;
	}

}
