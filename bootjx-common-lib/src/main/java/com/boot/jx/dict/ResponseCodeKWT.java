package com.boot.jx.dict;

import java.util.Arrays;
import java.util.Map;

import com.boot.jx.dict.PayGCodes.CodeCategory;
import com.google.common.collect.Maps;

public enum ResponseCodeKWT {
	
	CAPTURED("CAPTURED","Transaction was approved", "100045", CodeCategory.TXN_SUCCESS),
	NOT_CAPTURED("NOT+CAPTURED","Transaction was not approved", "100035", CodeCategory.TXN_AUTH),
	VOIDED("VOIDED","Transaction was voided", "100079", CodeCategory.TXN_DATA),
	GENERIC_ERROR("GENERIC ERROR","Generic Error", "100060", CodeCategory.TXN_DATA),
	CANCELED("CANCELED","Canceled Transaction", "100080", CodeCategory.TXN_CANCEL_SUCC),
	CANCELLED("CANCELLED","Cancelled Transaction", "100080", CodeCategory.TXN_CANCEL_SUCC),
	UNKNOWN("UNKNOWN","Error code may not be mapped","UNKNOWN",CodeCategory.UNKNOWN);
	
	private String responseCode;
	private String responseDesc;
	private String almullaErrorCode;
	private CodeCategory category;

	private static final Map<String, ResponseCodeKWT> LOOKUP = Maps.uniqueIndex(Arrays.asList(ResponseCodeKWT.values()),
			ResponseCodeKWT::getResponseCode);

	ResponseCodeKWT(String responseCode, String responseDesc, String almullaErrorCode, CodeCategory category) {
		this.responseCode = responseCode;
		this.responseDesc = responseDesc;
		this.almullaErrorCode = almullaErrorCode;
		this.setCategory(category);
	}

	ResponseCodeKWT(String responseCode, String responseDesc) {
		this.responseCode = responseCode;
		this.responseDesc = responseDesc;
	}

	private ResponseCodeKWT(String responseCode) {
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

		ResponseCodeKWT respCode = getResponseCodeEnumByCode(responseCode);

		if (null == respCode) {
			return null;
		}

		return respCode.getCategory();
	}

	public static ResponseCodeKWT getResponseCodeEnumByCode(String responseCode) {
		return LOOKUP.get(responseCode);
	}	
}
