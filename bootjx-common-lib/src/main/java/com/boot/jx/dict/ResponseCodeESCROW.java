package com.boot.jx.dict;

import java.util.Arrays;
import java.util.Map;

import com.boot.jx.dict.PayGCodes.CodeCategory;
import com.google.common.collect.Maps;

public enum ResponseCodeESCROW {

	EQUAL_AMOUNT("EQUAL_AMOUNT", "Transaction was approved", "0", CodeCategory.TXN_SUCCESS),
	MCHN_CASSETTE_IS_EMPTY("MCHN_CASSETTE_IS_EMPTY", "Cassette is empty", "0", CodeCategory.TXN_SUCCESS),
	MCHN_BAG_IS_DISABLED("BAG_IS_DISABLED", "Bag is disabled", "0", CodeCategory.PAYMENT_ERR),
	MCHN_BAG_IS_JAMMED("BAG_IS_JAMMED", "Bag is jammed", "0", CodeCategory.PAYMENT_ERR),
	MCHN_BAG_IS_OFFLINE("MCHN_BAG_IS_OFFLINE", "Bag is Offline", "0", CodeCategory.PAYMENT_ERR),
	MCHN_DISPENSER_IS_JAMMED("MCHN_DISPENSER_IS_JAMMED", "Dispenser is jammed", "0", CodeCategory.PAYMENT_ERR),
	MCHN_CASSETTE_IS_LOW("MCHN_CASSETTE_IS_LOW", "Cassette is low", "0", CodeCategory.TXN_SUCCESS),
	ESCROW_IS_DISABLED("ESCROW_IS_DISABLED", "Escrow is disabled", "0", CodeCategory.PAYMENT_ERR),
	ESCROW_IS_JAMMED("ESCROW_IS_JAMMED", "Escrow is jammed", "0", CodeCategory.PAYMENT_ERR),
	ESCROW_IS_OFFLINE("ESCROW_IS_OFFLINE", "Escrow is offline", "0", CodeCategory.PAYMENT_ERR),
	FAIL_GENERATE_BAG_REF("FAIL_GENERATE_BAG_REF", "Fail To Generate New Bag Ref.", "0", CodeCategory.PAYMENT_ERR),
	ESCROW_WORKING("ESCROW_WORKING", "Escrow Working", "0", CodeCategory.TXN_SUCCESS),
	ESCROW_TIMEOUT("ESCROW_TIMEOUT", "Waiting Response from KIOSK MACHINE FAIL", "0", CodeCategory.PAYMENT_ERR),
	NET_AMOUNT_NOT_AVAILABLE("NET_AMOUNT_NOT_AVAILABLE", "Net Amount Not Available", "0", CodeCategory.PAYMENT_ERR),
	NOTE_JAM("NOTE_JAM", "Note Jam in machine", "0", CodeCategory.PAYMENT_ERR),
	PAUSE_BAG_NOT_WORKING("PAUSE_BAG_NOT_WORKING", "Pause Bag is not open", "0", CodeCategory.PAYMENT_ERR),
	LESSER_AMOUNT("LESSER_AMOUNT", "Lesser Amount", "0", CodeCategory.PAYMENT_ERR),
	NO_CASH_ENTERED("NO_CASH_ENTERED", "No Cash Entered", "0", CodeCategory.PAYMENT_ERR),
	MONEY_IN_HANG_STAGE("MONEY_IN_HANG_STAGE", "Money In Hang Stage", "0", CodeCategory.TXN_SUCCESS),
	GREATER_AMOUNT_MONEY_IN_HANG_STAGE("GREATER_AMOUNT_MONEY_IN_HANG_STAGE", "Greater Amount Money In Hang Stage", "0", CodeCategory.TXN_SUCCESS),
	GREATER_THAN_MAX_REFUND("GREATER_THAN_MAX_REFUND", "Greater Than Max Refund Amount", "0", CodeCategory.PAYMENT_ERR),
	GREATER_THAN_MAX_REFUND_LOWER_NOTES("GREATER_THAN_MAX_REFUND_LOWER_NOTES", "Greater Than Max Refund Amount, Lower Notes", "0", CodeCategory.PAYMENT_ERR),
	GREATER_AMOUNT("GREATER_AMOUNT", "Greater Amount", "0", CodeCategory.PAYMENT_ERR),
	MONEY_REJECTED("MONEY_REJECTED", "Money Rejected", "0", CodeCategory.PAYMENT_ERR),
	FEED_CASH_FAIL("FEED_CASH_FAIL", "Feed Cash Fail", "0", CodeCategory.PAYMENT_ERR),
	CONNECTION_FAIL_MACHINE("CONNECTION_FAIL_MACHINE", "Connection Fail Machine", "0", CodeCategory.PAYMENT_ERR),
	REFUND_DONE("REFUND_DONE", "Refund Done", "0", CodeCategory.TXN_SUCCESS),
	REFUND_NOT_DONE("REFUND_NOT_DONE", "Refund Not Done", "0", CodeCategory.TXN_SUCCESS),
	REFUND_FAIL("REFUND_FAIL", "Refund Fail", "0", CodeCategory.PAYMENT_ERR),
	FAIL_REJECT("FAIL_REJECT", "Fail To Reject", "0", CodeCategory.PAYMENT_ERR),
	REJECTED("REJECTED", "Rejected", "0", CodeCategory.PAYMENT_ERR),
	LESSER_NOTES("LESSER_NOTES", "Lesser Notes", "0", CodeCategory.TXN_SUCCESS),
	FAIL_ACCEPT_MONEY("FAIL_ACCEPT_MONEY", "Fail To Accept Money", "0", CodeCategory.PAYMENT_ERR),
	ACCEPT_MONEY("ACCEPT_MONEY", " Accepted Money", "0", CodeCategory.TXN_SUCCESS),
	GREATER_THAN_REFUND_LIMIT_AND_NOT_DONE("GREATER_THAN_REFUND_LIMIT_AND_NOT_DONE", "GREATER THAN REFUND LIMIT ,REFUND NOT DONE", "0", CodeCategory.TXN_SUCCESS),
	PAYMENT_CAPTURED("PAYMENT_CAPTURED", "Payment Captured", "0", CodeCategory.TXN_SUCCESS),
	MONEY_REJECTED_LESSER_AMOUNT("MONEY_REJECTED_LESSER_AMOUNT", "Money rejected Lesser Amount", "0", CodeCategory.PAYMENT_ERR),
	MONEY_REJECTED_GREATER_AMOUNT("MONEY_REJECTED_GREATER_AMOUNT", "Money rejected Greater Amount", "0", CodeCategory.PAYMENT_ERR),
	PAYMENT_NOT_CAPTURED("PAYMENT_NOT_CAPTURED", "Payment Not Captured", "0", CodeCategory.PAYMENT_ERR),
	UNKNOWN("UNKNOWN", "Error code may not be mapped", "UNKNOWN", CodeCategory.UNKNOWN);

	private String responseCode;
	private String responseDesc;
	private String almullaErrorCode;
	private CodeCategory category;

	private static final Map<String, ResponseCodeESCROW> LOOKUP = Maps.uniqueIndex(
			Arrays.asList(ResponseCodeESCROW.values()),
			ResponseCodeESCROW::getResponseCode);

	ResponseCodeESCROW(String responseCode, String responseDesc, String almullaErrorCode, CodeCategory category) {
		this.responseCode = responseCode;
		this.responseDesc = responseDesc;
		this.almullaErrorCode = almullaErrorCode;
		this.setCategory(category);
	}

	ResponseCodeESCROW(String responseCode, String responseDesc) {
		this.responseCode = responseCode;
		this.responseDesc = responseDesc;
	}

	private ResponseCodeESCROW(String responseCode) {
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

		ResponseCodeESCROW respCode = getResponseCodeEnumByCode(responseCode);

		if (null == respCode) {
			return null;
		}

		return respCode.getCategory();
	}

	public static ResponseCodeESCROW getResponseCodeEnumByCode(String responseCode) {
		return LOOKUP.get(responseCode);
	}

	public static ResponseCodeESCROW getResponseCodeEnumByCode(String machineStatus, String moneyStatus) {
		return LOOKUP.get(machineStatus);
	}
}
