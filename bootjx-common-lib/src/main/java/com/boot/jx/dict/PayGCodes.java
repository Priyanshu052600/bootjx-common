package com.boot.jx.dict;

import com.boot.utils.ArgUtil;

public class PayGCodes {

	public static enum CodeTypes {
		TECHNICAL, VALIDATION, UNCERTAIN
	}

	public static enum CodeStatus {
		APPROVED, DECLINED, UNKNOWN
	}

	public static enum CodeCategory {
		CONN_FAILURE(CodeTypes.TECHNICAL, CodeStatus.DECLINED,
				"The transaction could not be completed due to connectivity issue"),

		FORMT_ERR(CodeTypes.TECHNICAL, CodeStatus.DECLINED,
				"The transaction could not be completed due to ISO Message Formatter error"),

		TXN_SUCCESS(CodeTypes.VALIDATION, CodeStatus.APPROVED, "The transaction is successfully completed"),

		UNKNOWN(CodeTypes.UNCERTAIN, CodeStatus.APPROVED, "Not able to determine"),

		TXN_DATA(CodeTypes.VALIDATION, CodeStatus.DECLINED,
				"The transaction could not be completed due to invalid/missing transaction data"),

		TXN_AUTH(CodeTypes.VALIDATION, CodeStatus.DECLINED,
				"The transaction could not be completed due to authentication failure"),

		TXN_CANCEL_SUCC(CodeTypes.TECHNICAL, CodeStatus.DECLINED, "The transaction has been cancelled"),
		
		INST_ISSUES(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to missing/invalid institution details"),
		LOG_NOT_FOUND(CodeTypes.UNCERTAIN, CodeStatus.DECLINED, "The transaction could not be completed due to missing log details"),
		MRCH_ERR(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to missing/invalid merchant details"),
		PAYMENT_ERR(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to missing/invalid payment details"),
		TERML_ERR(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to invalid/missing terminal details"),
		TXN_OTP_LIM(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to OTP attempt limit exceeded"),
		TXN_OTP_VLDT(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to OTP validity expired"),
		TXN_AUTH_PIN(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to invalid PIN"),
		TXN_BANK(CodeTypes.TECHNICAL, CodeStatus.DECLINED, "The transaction could not be completed due to some technical error"),
		TXN_CANCEL_FAIL(CodeTypes.TECHNICAL, CodeStatus.DECLINED, "The transaction could not be cancelled"),
		TXN_CARD(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to invalid/missing card details"),
		TXN_CARD_VLDT(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to card validity expiry"),
		TXN_DATA_CURR(CodeTypes.TECHNICAL, CodeStatus.DECLINED, "The transaction could not be completed due to currency conversion failure"),
		TXN_DN_RISK(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to risk check failure"),
		TXN_LIMIT_FUNDS(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to insufficient funds"),
		TXN_LIMIT(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to the withdrwal limit exceeded"),
		TXN_REFUND_ISSUE(CodeTypes.UNCERTAIN, CodeStatus.DECLINED, "The refund request could not be completed due to technical error"),
		TXN_REQ_FAILURE(CodeTypes.UNCERTAIN, CodeStatus.DECLINED, "The transaction could not be processed"),
		TXN_SEC_FAILURE(CodeTypes.UNCERTAIN, CodeStatus.DECLINED, "The transaction could not be completed due to unsecured network"),
		TXN_SESSION(CodeTypes.UNCERTAIN, CodeStatus.DECLINED, "Transaction timeout occurred"),
		TXN_TRNP(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to invalid/missing transportal details"),
		TXN_UDF(CodeTypes.VALIDATION, CodeStatus.DECLINED, "The transaction could not be completed due to invalid/missing user-defined details"),
		TXN_URL(CodeTypes.TECHNICAL, CodeStatus.DECLINED, "The transaction could not be completed due to invalid/missing URL response"),
		
		WIRE_TRANSFER_APPLICATION_SUCCESS(CodeTypes.VALIDATION, CodeStatus.DECLINED, "Wire Transfer trnx is successfully completed"),
		
		// -- additional code category added for Oman
		CARD_NOT_REGISTERED_FOR_OTP(CodeTypes.VALIDATION, CodeStatus.DECLINED, "Card not registered for OTP"),
		INVALID_CARD_NUMBER_01(CodeTypes.VALIDATION, CodeStatus.DECLINED, "Invalid card number"),
		INVALID_CARD_NUMBER_03(CodeTypes.VALIDATION, CodeStatus.DECLINED, "Transaction denied due to card number length error"),
		OTP_TRIES_EXCEED(CodeTypes.VALIDATION, CodeStatus.DECLINED, "OTP Tries Exceeded"),
		TRANX_DECLN_EXCEED_OTP_ATTMPT(CodeTypes.VALIDATION, CodeStatus.DECLINED, "Transaction declined due to exceeding OTP attempts"),
		TIME_EXCEEDS_FOR_OTP(CodeTypes.VALIDATION, CodeStatus.DECLINED, "Time exceeds for OTP"),
		TRANX_DN_MISSING_CARD_NUMBER(CodeTypes.VALIDATION, CodeStatus.DECLINED, "Transaction denied due to missing card number"),
		TRANX_DN_RISK_MIN_TRANX(CodeTypes.VALIDATION, CodeStatus.DECLINED, "Transaction denied due to Risk : Minimum Transaction Amount processing"),
		TRANX_DN_RISK_MAX_PROC_AMT(CodeTypes.VALIDATION, CodeStatus.DECLINED, "Transaction denied due to Risk : Maximum processing amount"),
		TRANX_DN_RISK_MAX_AMT(CodeTypes.VALIDATION, CodeStatus.DECLINED, "Transaction denied due to Risk : Maximum processing amount"),
		TRANX_TIME_LMT_EXCEEDS(CodeTypes.UNCERTAIN, CodeStatus.DECLINED, "Transaction time limit exceeds");
		
		CodeTypes type;
		CodeStatus status;
		String message;

		CodeCategory(CodeTypes type, CodeStatus status, String message) {
			this.type = type;
			this.message = message;
			this.status = status;
		}

		public CodeTypes getType() {
			return type;
		}

		public CodeStatus getStatus() {
			return status;
		}

	}

	public interface IPayGCode {
		public String getDescription();

		public CodeCategory getCategory();
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public static Enum getPayGCode(Object code, Enum defaultCode) {
		if (ArgUtil.isEmpty(code)) {
			return defaultCode;
		}
		return ArgUtil.parseAsEnum(code, defaultCode);
	}

}
