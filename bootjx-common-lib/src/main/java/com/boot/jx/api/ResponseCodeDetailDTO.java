package com.boot.jx.api;

import java.io.Serializable;
import java.util.Map;

import com.boot.jx.dict.PayGCodes.CodeCategory;

public class ResponseCodeDetailDTO implements Serializable {

	private static final long serialVersionUID = -5279804951579408228L;

	private String responseCode; // client
	private String responseDesc; // client
	private String almullaErrorCode;
	private CodeCategory category;

	private String pgPaymentId;
	private String pgReferenceId;
	private String pgAuth;
	private String pgTransId;

	// Type
	private String type;
	// Status
	private String status;
	// Map <String, String> clientResp
	private Map<String, String> clientResponse;

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDesc() {
		return responseDesc;
	}

	public void setResponseDesc(String responseDesc) {
		this.responseDesc = responseDesc;
	}

	public String getAlmullaErrorCode() {
		return almullaErrorCode;
	}

	public void setAlmullaErrorCode(String almullaErrorCode) {
		this.almullaErrorCode = almullaErrorCode;
	}

	public CodeCategory getCategory() {
		return category;
	}

	public void setCategory(CodeCategory category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Map<String, String> getClientResponse() {
		return clientResponse;
	}

	public void setClientResponse(Map<String, String> clientResponse) {
		this.clientResponse = clientResponse;
	}

	public String getPgPaymentId() {
		return pgPaymentId;
	}

	public void setPgPaymentId(String pgPaymentId) {
		this.pgPaymentId = pgPaymentId;
	}

	public String getPgReferenceId() {
		return pgReferenceId;
	}

	public void setPgReferenceId(String pgReferenceId) {
		this.pgReferenceId = pgReferenceId;
	}

	public String getPgAuth() {
		return pgAuth;
	}

	public void setPgAuth(String pgAuth) {
		this.pgAuth = pgAuth;
	}

	public String getPgTransId() {
		return pgTransId;
	}

	public void setPgTransId(String pgTransId) {
		this.pgTransId = pgTransId;
	}

}
