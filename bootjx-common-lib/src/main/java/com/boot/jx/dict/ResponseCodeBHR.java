package com.boot.jx.dict;

import java.util.Arrays;
import java.util.Map;

import com.boot.jx.dict.PayGCodes.CodeCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.google.common.collect.Maps;

@JsonFormat(shape=Shape.OBJECT)
public enum  ResponseCodeBHR {

	// @formatter:off

	CUSTOMER_CARD_EXPIRED("54","The customer card is expired", "100002", CodeCategory.TXN_CARD_VLDT),
	CARD_EXPIRED("33","The customer card is expired", "AMX-000001", CodeCategory.TXN_CARD_VLDT),
	
	ISSUE_IN_CARD("05","The customer should contact the bank to resolve the Card issue", "100004", CodeCategory.TXN_CARD),
	INVALID_CARD_NUMBER_02("PY20006","Invalid Brand", "100007", CodeCategory.TXN_CARD),
	RESTRICTED_CARD("36","Restricted card", "100033", CodeCategory.TXN_CARD),
	INVALID_CARD_NUMBER("14","Invalid card number", "100015", CodeCategory.TXN_CARD),
	
	PIN_EXCEED("38","Allowable PIN tries exceeded", "100031", CodeCategory.TXN_OTP_LIM),
	PIN_NUMBER_EXCEED("75","Allowable number PIN tries exceeded", "100034", CodeCategory.TXN_OTP_LIM),
	
	EXCEEDS_WITHDRAW_LIMIT("61","The card exceeds withdrawal amount limit", "100006", CodeCategory.TXN_LIMIT),
	
	INSUFFICIENT_FUNDS("51","Insufficient funds", "100008", CodeCategory.TXN_LIMIT_FUNDS),
	
	INCORRECT_PIN_NUMBER("55","Incorrect pin number", "100005", CodeCategory.TXN_AUTH_PIN),
    
	BANK_DISCON("91","The bank is disconnected at the moment", "100003", CodeCategory.TXN_BANK),
	
	DENIED_BY_RISK("71","Risk denied the transaction","100147", CodeCategory.TXN_DN_RISK),
	
	HOST_TIMEOUT("8","The authorization system did not respond within the timeout limit.", "100256", CodeCategory.CONN_FAILURE),
	
	REFER_ISSUE("78","Refer to Issuer", "100248", CodeCategory.MRCH_ERR),
	
	INELIGIBLE_ACCOUNT("76","Ineligible account", "100211", CodeCategory.PAYMENT_ERR),
	
	RESTRICT_CARD("62","Restricted card", "100035", CodeCategory.TXN_AUTH),
	
	EXCEEDS_WITHDRAW_FREQUENCY_LIMIT("65","Exceeds withdrawal frequency limit", "100102", CodeCategory.TXN_DATA),
	
	CAPTURED("CAPTURED","Transaction was approved", "100045", CodeCategory.TXN_SUCCESS),
	NOT_CAPTURED("NOT CAPTURED","Transaction was not approved", "100035", CodeCategory.TXN_AUTH),
	VOIDED("VOIDED","Transaction was voided", "100079", CodeCategory.TXN_DATA),
	GENERIC_ERROR("GENERIC ERROR","Generic Error", "100060", CodeCategory.TXN_DATA),
	CANCELLED("CANCELED","Canceled Transaction", "100080", CodeCategory.TXN_CANCEL_SUCC),
	
	UNKNOWN("UNKNOWN","Error code may not be mapped","UNKNOWN",CodeCategory.UNKNOWN);

	// @formatter:on
	
	private String responseCode;
	private String responseDesc;
	private String almullaErrorCode;
	private CodeCategory category;

	private static final Map<String, ResponseCodeBHR> LOOKUP = Maps.uniqueIndex(Arrays.asList(ResponseCodeBHR.values()),
			ResponseCodeBHR::getResponseCode);

	ResponseCodeBHR(String responseCode, String responseDesc, String almullaErrorCode, CodeCategory category) {
		this.responseCode = responseCode;
		this.responseDesc = responseDesc;
		this.almullaErrorCode = almullaErrorCode;
		this.setCategory(category);
	}

	ResponseCodeBHR(String responseCode, String responseDesc) {
		this.responseCode = responseCode;
		this.responseDesc = responseDesc;
	}

	private ResponseCodeBHR(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseCode() {
		return this.responseCode;
	}

	public String getResponseDesc() {
		return this.responseDesc;
	}

	public String getAlmullaErrorCode() {
		return almullaErrorCode;
	}

	public CodeCategory getCategory() {
		return category;
	}

	public void setCategory(CodeCategory category) {
		this.category = category;
	}

	public static CodeCategory getCodeCategoryByResponseCode(String responseCode) {

		ResponseCodeBHR respCode = getResponseCodeEnumByCode(responseCode);

		if (null == respCode) {
			return null;
		}

		return respCode.getCategory();
	}

	public static ResponseCodeBHR getResponseCodeEnumByCode(String responseCode) {
		return LOOKUP.get(responseCode);
	}

}
