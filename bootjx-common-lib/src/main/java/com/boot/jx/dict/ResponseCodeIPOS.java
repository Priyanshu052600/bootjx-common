package com.boot.jx.dict;

import java.util.Arrays;
import java.util.Map;

import com.boot.jx.dict.PayGCodes.CodeCategory;
import com.google.common.collect.Maps;

public enum ResponseCodeIPOS {

	
	FAIL_INTIT_PIN_PAD("FAIL_INTIT_PIN_PAD", "Fail to Initiate the pin pad initialization process", "0", CodeCategory.PAYMENT_ERR),
	ERROR_WHILE_PAYMENT("ERROR_WHILE_PAYMENT", "Error While Payment", "0", CodeCategory.PAYMENT_ERR),
	NET_AMOUNT_NOT_AVALIABLE("NET_AMOUNT_NOT_AVALIABLE", "Net Amount Not Avaliable", "0", CodeCategory.PAYMENT_ERR),
	AMOUNT_MISMATCH("AMOUNT_MISMATCH", "Amount Mismatch", "0", CodeCategory.PAYMENT_ERR),
	NO_AMOUNT_PAID("NO_AMOUNT_PAID", "No Amount Paid", "0", CodeCategory.PAYMENT_ERR),
	TRANSACTION_DECLINED("TRANSACTION_DECLINED", "Transaction Declined", "0", CodeCategory.PAYMENT_ERR),
	UNKNOWN("UNKNOWN", "Error code may not be mapped", "UNKNOWN", CodeCategory.UNKNOWN),
	
	WINEPTS_READY("WINEPTS_READY", "WINEPTS READY...", "0", CodeCategory.PAYMENT_ERR),
	INSERT_CARD("INSERT_CARD", "INSERT YOUR CARD", "0", CodeCategory.PAYMENT_ERR),
	INSERT_PIN("INSERT_PIN", "INSERT PIN", "0", CodeCategory.PAYMENT_ERR),
	TRNX_IN_PROGRESS("TRNX_IN_PROGRESS", "TRANSACTION IN PROGRESS", "0", CodeCategory.PAYMENT_ERR),
	PROCESSING_CARD("PROCESSING_CARD", "PROCESSING CARD", "0", CodeCategory.PAYMENT_ERR);
	
	private String responseCode;
	private String responseDesc;
	private String almullaErrorCode;
	private CodeCategory category;

	private static final Map<String, ResponseCodeIPOS> LOOKUP = Maps.uniqueIndex(
			Arrays.asList(ResponseCodeIPOS.values()),
			ResponseCodeIPOS::getResponseCode);

	ResponseCodeIPOS(String responseCode, String responseDesc, String almullaErrorCode, CodeCategory category) {
		this.responseCode = responseCode;
		this.responseDesc = responseDesc;
		this.almullaErrorCode = almullaErrorCode;
		this.setCategory(category);
	}

	ResponseCodeIPOS(String responseCode, String responseDesc) {
		this.responseCode = responseCode;
		this.responseDesc = responseDesc;
	}

	private ResponseCodeIPOS(String responseCode) {
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

		ResponseCodeIPOS respCode = getResponseCodeEnumByCode(responseCode);

		if (null == respCode) {
			return null;
		}

		return respCode.getCategory();
	}

	public static ResponseCodeIPOS getResponseCodeEnumByCode(String responseCode) {
		return LOOKUP.get(responseCode);
	}

	public static ResponseCodeIPOS getResponseCodeEnumByCode(String machineStatus, String moneyStatus) {
		return LOOKUP.get(machineStatus);
	}
}
