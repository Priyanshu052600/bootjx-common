package com.boot.jx.dict;

import java.util.Arrays;
import java.util.Map;

import com.boot.jx.dict.PayGCodes.CodeCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.google.common.collect.Maps;

@JsonFormat(shape=Shape.OBJECT)
public enum ResponseCodeOMN {
    //-- CONN_FAILURE
	HOST_TIMEOUT("IPAY0100158","Host SWITCH timeout", "100180", CodeCategory.CONN_FAILURE),
	EXT_MSG_SYS_ERR("IPAY0100159","External message system error","100181",CodeCategory.CONN_FAILURE),
	UNABLE_PROCESS_TRANX("IPAY0100160","Unable to process the transaction","100182",CodeCategory.CONN_FAILURE),
	PROBLEM_OCCURRED_DEFAULT_INS("IPAY0200028","Problem occurred while loading default institution configuration", "100184", CodeCategory.CONN_FAILURE),
    PROBLEM_OCCUR_EXT_CONN("IPAY0200029","Problem occurred while getting external connection details", "100185", CodeCategory.CONN_FAILURE),   
    NO_EXT_CONN_DEATILS_01("IPAY0200030","No external connection details for Extr Conn id", "100186", CodeCategory.CONN_FAILURE),
    NO_EXT_CONN_DEATILS_02("IPAY0200031","Alternate external connection details not found for the alt Extr Conn id", "100187", CodeCategory.CONN_FAILURE),
    PROBLEM_OOCCUR_EXT_CONN_DETAILS("IPAY0200032","Problem occurred while getting external connection details for Extr Conn id", "100188",CodeCategory.CONN_FAILURE),
    EXT_CONN_NOT_ENABLED("IPAY0100255","External connection not enabled", "100189", CodeCategory.CONN_FAILURE),
    
    //-- FORMT_ERR
    PROBLEM_OCCUR_FORMAT_REVRS_REQ("IPAY0100234","Problem occurred while formatting Reverse completion request in ISO Message Formatter", "100170", CodeCategory.FORMT_ERR), 
    PROBLEM_OCCUR_FORMAT_REVRS_REFUND_REQ("IPAY0100235","Problem occurred while formatting Reverse refund request in ISO Message Formatter", "100171", CodeCategory.FORMT_ERR), 
    PROBLEM_OCCUR_FORMT_ISO_MSG("IPAY0100236","Problem occurred while formatting Capture request in ISO Message Formatter", "100172", CodeCategory.FORMT_ERR), 
    PROBLEM_OCCUR_FORMT_PURCH_REQ("IPAY0100237","Problem occurred while formatting Reverse purchase request in ISO Message Formatter", "100173", CodeCategory.FORMT_ERR),
    PROBLEM_OCCUR_FORMT_CAP_REQ("IPAY0100238","Problem occurred while formatting Capture request in ISO Message Formatter", "100174", CodeCategory.FORMT_ERR),  
    PROBLEM_OCCUR_FORMT_AUTH_REQ("IPAY0100239","Problem occurred while formatting authorization request in ISO Message Formatter", "100175", CodeCategory.FORMT_ERR), 
    PROBLEM_OCCUR_FORMT_RFND_REQ("IPAY0100240","Problem occurred while formatting refund request in ISO Message Formatter", "100176", CodeCategory.FORMT_ERR), 
    PROBLEM_OCCUR_PURCH_REQ("IPAY0100241","Problem occurred while formatting purchase request in ISO Message Formatter", "100177", CodeCategory.FORMT_ERR),    
    PROBLEM_OCCUR_ISO_MSG("IPAY0100245","Problem occurred while sending/receiving ISO message", "100178", CodeCategory.FORMT_ERR),
    ISO_MSG_NULL("IPAY0100144","ISO MSG is null. See log for more details!", "100179", CodeCategory.FORMT_ERR),
    PROBLEM_OCCURRED_DEFAULT_MSG("IPAY0100145","Problem occurred while loading default messages in ISO Formatter", "100270", CodeCategory.FORMT_ERR),
    PROBLEM_OCCURRED_FORMT_PURCH_RQT("IPAY0100147","Problem occurred while formatting purchase request in B24 ISO Message Formatter", "100271", CodeCategory.FORMT_ERR), 
    PROBLEM_OCCURRED_FORMT_RVRS_RQT("IPAY0100150","Problem occurred while formatting Reverse purchase request in B24 ISO Message Formatter", "100272", CodeCategory.FORMT_ERR),
    PROBLEM_OCCUR_FORMT_REFUND("IPAY0100151","Problem occurred while formatting Refund request in B24 ISO Message Formatter", "100273" , CodeCategory.FORMT_ERR),
    PROBLEM_OCCURRED_FORMT_AUTH_RQT("IPAY0100152","Problem occurred while formatting authorization request in B24 ISO Message Formatter", "100274", CodeCategory.FORMT_ERR),
    PROBLEM_OCCURRED_FORMT_CAP_RQT("IPAY0100153","Problem occurred while formatting Capture request in B24 ISO Message Formatter", "100275", CodeCategory.FORMT_ERR),
    PROBLEM_OCCUR_REVRS_MSG_FORMT("IPAY0100154","Problem occurred while formatting Reverse Refund request in B24 ISO Message Formatter", "100276", CodeCategory.FORMT_ERR),
    PROBLEM_OCCURRED_FORMT_RVRS_AUTH_RQT("IPAY0100155","Problem occurred while formatting reverse authorization request in B24 ISO Message Formatter", "100277", CodeCategory.FORMT_ERR), 
    PROBLEM_OCCURRED_FORMT_RVRS_CAP_RQT("IPAY0100156","Problem occurred while formatting Reverse Capture request in B24 ISO Message Formatter", "100278", CodeCategory.FORMT_ERR),
    
    //-- INST_ISSUES
    INST_NOT_ENABLED("IPAY0100009","Institution not enabled", "100190", CodeCategory.INST_ISSUES),
    INS_NOT_ENABLED_ENCYPT("IPAY0100010","Institution has not enabled for the encryption process", "100191", CodeCategory.INST_ISSUES),
    PAY_INS_NOT_ENABLED("IPAY0100125","Payment instrument not enabled", "100192", CodeCategory.INST_ISSUES),
    SORRY_INS_NOT_HANDLE("IPAY0100094","Sorry, this instrument is not handled", "100193", CodeCategory.INST_ISSUES),
    
    //-- LOG_NOT_FOUND
    PROBLEM_OCCURRED_IP_BLOCK("IPAY0200011","Problem occurred while getting IP block details", "100259", CodeCategory.LOG_NOT_FOUND),
    PROBLEM_OCCUR_PAY_LOG_ID("IPAY0200012","Problem occurred while updating payment log IP details", "100260", CodeCategory.LOG_NOT_FOUND),
    PROBLEM_OCCUR_UPDATE_DSC_PAY_LOG("IPAY0200013","Problem occurred while updating description details in payment log", "100261", CodeCategory.LOG_NOT_FOUND),
    
    //-- MRCH_ERR
    PROBLEM_OCCUR_MRCHT_PROC("IPAY0100035","Problem occurred during merchant hashing process", "100246", CodeCategory.MRCH_ERR),
    MERCHANT_ID_MISMATCH("IPAY0100129","Transaction denied due to Merchant ID mismatch", "100247", CodeCategory.MRCH_ERR),
    MRCH_NOT_ALLOW_ENCRYPT_PROC("IPAY0100161","Merchant is not allowed for encryption process", "100248", CodeCategory.MRCH_ERR),
    MRCH_ENCRPYT_ENABLED("IPAY0100178","Merchant encryption enabled", "100249", CodeCategory.MRCH_ERR),
    MRCH_RES_URL_DWN("IPAY0100249","Merchant response url is down", "100250", CodeCategory.MRCH_ERR),
    MERCH_NOT_ENABLED("IPAY0100254","Merchant not enabled",  "100251", CodeCategory.MRCH_ERR),
    PROBLEM_OCCUR_MRCHT_DETAILS("IPAY0200003","Problem occurred while getting merchant details", "100252", CodeCategory.MRCH_ERR),
    PROBLEM_OCCUR_MERCH_RESPONSE("IPAY0200014","Problem occurred during merchant response", "100253", CodeCategory.MRCH_ERR),
    ERR_OCCUR_MRCH_ID("IPAY0200037","Error occurred while getting Merchant ID", "100254", CodeCategory.MRCH_ERR),
    MRCHT_NOT_ENABLED_ENCYPT_PROC("IPAY0100011","Merchant has not enabled for encryption process", "100345", CodeCategory.MRCH_ERR),
    
    //PAYMENT_ERR	
    PROBLEM_OCCUR_PROCESS_DRT_DEBIT("IPAY0100053","Problem occurred while processing direct debit", "100194", CodeCategory.PAYMENT_ERR),  
    PAY_DETAILS_NOT_AVAL("IPAY0100054","Payment details not available", "100195", CodeCategory.PAYMENT_ERR),
    INVLD_PAY_STATUS("IPAY0100055","Invalid Payment Status", "100196", CodeCategory.PAYMENT_ERR),
    INS_NOT_ALLOW_TRM_BRND("IPAY0100056","Instrument not allowed in Terminal and Brand", "100197", CodeCategory.PAYMENT_ERR), 
    PAY_OPTION_NOT_ENABLED("IPAY0100046","Payment option not enabled", "100198", CodeCategory.PAYMENT_ERR),
    PAY_PAGE_VALID_FAILED_INVLD_STS("IPAY0100047","Payment Page validation failed due to invalid Order Status", "100199", CodeCategory.PAYMENT_ERR),
    PAY_ID_MISSING("IPAY0100037","Payment id missing", "100200", CodeCategory.PAYMENT_ERR),
    INVALID_PAY_ID("IPAY0100039","Invalid payment id", "100201", CodeCategory.PAYMENT_ERR),
    PAY_DETAILS_MISSING("IPAY0100041","Payment details missing", "100202", CodeCategory.PAYMENT_ERR),
    MISSING_PAY_INS("IPAY0100069","Missing payment instrument", "100203", CodeCategory.PAYMENT_ERR),
    INVLD_PAY_INS("IPAY0100106","Invalid payment instrument", "100204", CodeCategory.PAYMENT_ERR),
    INS_NOT_ENABLED("IPAY0100107","Instrument not enabled", "100205", CodeCategory.PAYMENT_ERR),
    TRANX_PAY_INS_MISMATCH("IPAY0100131","Transaction denied due to Payment Instrument mismatch", "100206", CodeCategory.PAYMENT_ERR),
    ERR_OCCUR_DTR_PAY_INS("IPAY0100202","Error occurred in Determine Payment Instrument", "100207", CodeCategory.PAYMENT_ERR),
    MISSING_PAY_DETAILS("IPAY0100204","Missing payment details", "100208", CodeCategory.PAYMENT_ERR),
    NOT_SUPP_PAY_INS("IPAY0100243","NOT SUPPORTED IPAY0100244 Payment Instrument Not Configured", "100209", CodeCategory.PAYMENT_ERR),
    PAY_DETAILS_VERF_FAILURE("IPAY0100250","Payment details verification failed", "100210", CodeCategory.PAYMENT_ERR), 
    INVALID_PAY_DATA("IPAY0100251","Invalid payment data", "100211", CodeCategory.PAYMENT_ERR), 
    PAY_ENCRPYT_FAIL("IPAY0100256","Payment encryption failed", "100212", CodeCategory.PAYMENT_ERR), 
    PAY_OPT_NOT_ENABLE("IPAY0100260","Payment option(s) not enabled", "100213", CodeCategory.PAYMENT_ERR),
    PROBLEM_OCCUR_VAL_PAY_DEATILS("IPAY0200007","Problem occurred while validating payment details", "100214", CodeCategory.PAYMENT_ERR), 
    PROBLEM_OCCUR_PAY_DETAILS("IPAY0200008","Problem occurred while verifying payment details", "100215", CodeCategory.PAYMENT_ERR),
    PROBLEM_OCCURRED_PAY_DETAILS("IPAY0200009","Problem occurred while getting payment details", "100216", CodeCategory.PAYMENT_ERR),
    PROBLEM_OCCUR_UPDT_PAY_DETAILS("IPAY0200010","Problem occurred while updating payment details", "100217", CodeCategory.PAYMENT_ERR),
    PROBLEM_OCCUR_PAY_INS("IPAY0200016","Problem occurred while getting payment instrument", "100218", CodeCategory.PAYMENT_ERR),
    PROBLEM_OCCUR_PAY_INS_LST("IPAY0200017","Problem occurred while getting payment instrument list", "100219", CodeCategory.PAYMENT_ERR),
    PROBLEM_OCCUR_TRANX_DETAILS("IPAY0200018","Problem occurred while getting transaction details", "100220", CodeCategory.PAYMENT_ERR),
    PROBLEM_OCCUR_CURR("IPAY0200022","Problem occurred while getting currency", "100221", CodeCategory.PAYMENT_ERR),
    PROBLEM_OCCUR_DETM_PAY_INS("IPAY0200023","Problem occurred while determining payment instrument", "100222", CodeCategory.PAYMENT_ERR),
    PROBLEM_OCCUR_BRAND_RULE("IPAY0200024","Problem occurred while getting brand rules details", "100223", CodeCategory.PAYMENT_ERR),
    PROBLEM_OCCUR_INS_DFEATILS("IPAY0200002","Problem occurred while getting institution details", "100224", CodeCategory.PAYMENT_ERR),
    BRAND_RULE_NOT_ENABLE("IPAY0100257","Brand rules not enabled", "100225", CodeCategory.PAYMENT_ERR),
    CURR_CODE_NOT_ENABLE("IPAY0100034","Currency code not enabled", "100226", CodeCategory.PAYMENT_ERR),
    BRAND_NOT_ENABLED("IPAY0100126","Brand not enabled", "100227", CodeCategory.PAYMENT_ERR),
    INAVLID_RESULT_CODE("IPAY0100134","Transaction denied due to invalid Result Code", "100228", CodeCategory.PAYMENT_ERR),
    PROBLEM_OCCURRED_PERFORM_ACTION("IPAY0100135","Problem occurred while doing perform action code reference id", "100229", CodeCategory.PAYMENT_ERR),
    PROBLEM_OCCUR_CURR_MINOR("IPAY0100206","Problem occurred while getting currency minor digits", "100230", CodeCategory.PAYMENT_ERR),
    BIN_RANGE_NOT_ENABLED("IPAY0100207","","100231", CodeCategory.PAYMENT_ERR),
    ACTION_NOT_ENABLED("IPAY0100208","Action not enabled", "100232", CodeCategory.PAYMENT_ERR),   
    INST_CONFIG_NOT_ENABLED("IPAY0100209","Institution config not enabled", "100233", CodeCategory.PAYMENT_ERR),
    
    //TERML_ERR
    INVLD_TERMINAL("IPAY0100017","Inactive terminal", "100153", CodeCategory.TERML_ERR), 
    TERML_PASSWD_EXPRD("IPAY0100018","Terminal password expired", "100154", CodeCategory.TERML_ERR),
    INVLD_LOG_IN_ATTMPT("IPAY0100019","Invalid log in attempt", "100155", CodeCategory.TERML_ERR),
    INVALID_ACTION_TYPE("IPAY0100020","Invalid action type", "100156", CodeCategory.TERML_ERR),  
    EMPPYT_TRML_KEY("IPAY0100012","Empty terminal key", "100157", CodeCategory.TERML_ERR),
    TREMNL_ACTION_NOT_ENABLED("IPAY0100033","Terminal action not enabled", "100158", CodeCategory.TERML_ERR),
    //100159 duplicate 100057
    TERMNL_AUTH_REQ_INVLD_TRANPT_ID("IPAY0100014","Terminal Authentication requested with invalid tranportal ID data", "100160", CodeCategory.TERML_ERR),
    TERMINL_NOT_ENABLED("IPAY0100008","Terminal not enabled", "100161", CodeCategory.TERML_ERR), 
    TERMINAL_INACTIVE("IPAY0100095","Terminal inactive", "100162", CodeCategory.TERML_ERR),
    TRM_ACTION_NOT_ENABLED("IPAY0100098","Terminal Action not enabled for Transaction request, Terminal", "100163", CodeCategory.TERML_ERR), 
    TRM_PAY_INS_NOT_ENABLED("IPAY0100099","Terminal Payment Instrument not enabled for Transaction request, Terminal", "100164", CodeCategory.TERML_ERR),
    PROBLEM_OCCUR_AUTH("IPAY0100100","Problem occurred while authorize", "100165", CodeCategory.TERML_ERR),
    TERMINALT_ID_MISMATCH("IPAY0100130","Transaction denied due to Terminal ID mismatch", "100166", CodeCategory.TERML_ERR),
    PROBLEM_OCCUR_TERMINL("IPAY0200001","Problem occurred while getting terminal", "100167", CodeCategory.TERML_ERR), 
    PROBLEM_OCCUR_UPDT_TERML("IPAY0200005","Problem occurred while updating terminal details", "100168", CodeCategory.TERML_ERR),
    PROBLEM_OCCUR_TRM_DETAILS("IPAY0200015","Problem occurred while getting terminal details", "100169", CodeCategory.TERML_ERR),
    
    //TXN_AUTH
    AUTH_NOT_AVAL("IPAY0100180","Authentication not available", "100035", CodeCategory.TXN_AUTH),
    EMPTY_OTP_NUMBER("IPAY0100092","Empty OTP number", "100036", CodeCategory.TXN_AUTH),
	INVALID_OTP_NUMBER("IPAY0100093","Invalid OTP number", "100037", CodeCategory.TXN_AUTH),
	INVALID_TRM_KEY("IPAY0100050","Invalid terminal key", "100038", CodeCategory.TXN_AUTH), 
    MISSING_TRM_KEY("IPAY0100051","Missing terminal key", "100039", CodeCategory.TXN_AUTH),
    PROBLEM_OCCUR_MERCH_RESP_ENCRYPT("IPAY0100052","Problem occurred during merchant response encryption", "100040", CodeCategory.TXN_AUTH),
    PASSWD_SEC_NOT_ENABLED("IPAY0100016","Password security not enabled", "100041", CodeCategory.TXN_AUTH), 
    PROBLEM_OCCUR_PASSWD_SEC("IPAY0200004","Problem occurred while getting password security rules", "100042", CodeCategory.TXN_AUTH),
    IVR_NOT_ENABLED("IPAY0100179","IVR not enabled", "100043", CodeCategory.TXN_AUTH),
    ENCRPTY_ENABLED("IPAY0100186","Encryption enabled", "100044", CodeCategory.TXN_AUTH), 
    
    //TXN_CARD
    
    
	CARD_HOLDERNAME_NOT_PRESENT("IPAY0100081","Card holder name is not present", "100009", CodeCategory.TXN_CARD),
	// 100010 duplicate of 100009
	CARD_ADDR_NOT_PRESENT("IPAY0100082","Card address is not present", "100011", CodeCategory.TXN_CARD),
    CARD_POST_CODE_NOT_PRST("IPAY0100083","Card postal code is not present", "100012", CodeCategory.TXN_CARD),
    CARD_DECRYPT_FAIL("IPAY0100111","Card decryption failed", "100013", CodeCategory.TXN_CARD),
    
    INVALID_CARD_NUMBER_DN("IPAY0100119","Transaction denied due to invalid card number", "100015", CodeCategory.TXN_CARD),
	INVALID_CARD_NUMBER_04("IPAY0100121","Transaction denied due to invalid card holder name", "100016", CodeCategory.TXN_CARD),
	MISSING_CARD_EXPY_YR("IPAY0100225","Missing card expiry year", "100017", CodeCategory.TXN_CARD),  
    INVALID_CARD_EXPY_YR("IPAY0100226","Invalid card expiry year", "100018", CodeCategory.TXN_CARD),
    MISSING_CARD_EXPY_MON("IPAY0100227","Missing card expiry month", "100019", CodeCategory.TXN_CARD),    
    INVALID_CARD_EXPY_MON("IPAY0100228","Invalid card expiry month", "100020", CodeCategory.TXN_CARD),     
    INVALID_CARD_EXPY_DAY("IPAY0100229","Invalid card expiry day", "100021", CodeCategory.TXN_CARD), 
    MISSING_ENCRYPT_CARD_NUMBER("IPAY0200027","Missing encrypted card number", "100022", CodeCategory.TXN_CARD),
    MISSING_CARD_NUMBER("IPAY0100219","Missing card number", "100023", CodeCategory.TXN_CARD), 
    INVLD_CARD_NUMBER("IPAY0100220","Invalid card number", "100024", CodeCategory.TXN_CARD),
    INVALID_CARD_NUMBER_05("IPAY0100133","Transaction denied due to Card Number mismatch", "100025", CodeCategory.TXN_CARD),
    MISSING_CARD_HLD_NAME("IPAY0100221","Missing card holder name", "100026", CodeCategory.TXN_CARD),
    INVLD_CARD_HLD_NAME("IPAY0100222","Invalid card holder name", "100027", CodeCategory.TXN_CARD), 
    MISSING_CVV("IPAY0100223","Missing cvv", "100028", CodeCategory.TXN_CARD), 
    INVALID_CVV("IPAY0100224","Invalid cvv", "100029", CodeCategory.TXN_CARD),
    CARD_ENCRPTY_FAIL("IPAY0100181","Card encryption failed", "100030", CodeCategory.TXN_CARD),
    
    //TXN_OTP_LIM
    
	
    
    //TXN_OTP_VLDT
	
    
    //TXN_CANCEL_FAIL
	PROBLEM_OCCUR_CANCEL_TRANX("IPAY0100253","Problem occurred while cancelling the transaction", "100081", CodeCategory.TXN_CANCEL_FAIL),
    
    //TXN_CANCEL_SUCC
	CANCELLED("IPAY0100048","Cancelled", "100080", CodeCategory.TXN_CANCEL_SUCC),
    
    //-- TXN_CARD_VLDT
    CARD_EXPIRED_01("IPAY0100230","Card expired", "AMX-000001", CodeCategory.TXN_CARD_VLDT),
    
    //TXN_DATA_CURR
    PROBLEM_OCCURRED_CONV_AMT( "IPAY0100172","Problem occurred while converting amount", "100058", CodeCategory.TXN_DATA_CURR), 
    
    //TXN_DATA
    MISSING_TGRANS_DATA("IPAY0100007","Missing transaction data", "100050", CodeCategory.TXN_DATA),
    MISSING_CURR("IPAY0100021","Missing currency", "100051", CodeCategory.TXN_DATA),
    INVALID_CURRENCY("IPAY0100022","Invalid currency", "", CodeCategory.TXN_DATA),
    MISSING_AMOUNT("IPAY0100023","Missing amount", "100053", CodeCategory.TXN_DATA),
    INVALID_AMOUNT("IPAY0100024","Invalid amount", "100054", CodeCategory.TXN_DATA),
    INVLD_AMT_CURR("IPAY0100025","Invalid amount or currency", "1000055", CodeCategory.TXN_DATA),
    INVLD_LANG_ID("IPAY0100026","Invalid language id", "100056", CodeCategory.TXN_DATA),
    INAVLID_TRACK_ID("IPAY0100027","Invalid track id", "100057", CodeCategory.TXN_DATA),
    INVLD_TRANS_DATA("IPAY0100013","Invalid transaction data", "100059", CodeCategory.TXN_DATA),
    PROBLEM_VALID_TRANX("IPAY0100124","Problem occurred while validating transaction data", "1000060", CodeCategory.TXN_DATA),
    INVLD_TRAN_ID("IPAY0100215","Invalid tranportal id", "100061", CodeCategory.TXN_DATA),    
    INVLD_DATA_REC("IPAY0100216","Invalid data received", "100062", CodeCategory.TXN_DATA),
    INVLD_PAY_DETAILS("IPAY0100217","Invalid payment detail", "100063", CodeCategory.TXN_DATA), 
    INVLD_BRAND_ID("IPAY0100218","Invalid brand id", "1000064", CodeCategory.TXN_DATA),
    PROBLEM_OCCURRED_VAL_ORG_TRANX("IPAY0100142","Problem occurred while validating original transaction", "100065", CodeCategory.TXN_DATA),
    TRANX_ACTION_NULL("IPAY0100143","Transaction action is null", "100066", CodeCategory.TXN_DATA),
    DECRPTY_TRANX_DATA_FAILED("IPAY0100176","Decrypting transaction data failed", "1000067", CodeCategory.TXN_DATA), 
    INVLD_TRANX_PAY_NULL("IPAY0100109","Invalid subsequent transaction, payment id is null or empty", "100068", CodeCategory.TXN_DATA), 
    INVLD_SUBQ_TRANX_NULL_EMPTY("IPAY0100110","Invalid subsequent transaction, Tran Ref id is null or empty", "100069", CodeCategory.TXN_DATA),
    SUBSEQ_TRANX_ORGN_INVLD("IPAY0100113","subsequent transaction, but original transaction id is invalid", "100070", CodeCategory.TXN_DATA),
    DUP_RECORD_TRANX_ID_EXIST("IPAY0100114","Duplicate Record, transaction ID already exist", "100071", CodeCategory.TXN_DATA),
    //1000071 Duplicate of 1000065
    MISSING_ORG_TRANX_ID("IPAY0100232","Missing original transaction id", "100073", CodeCategory.TXN_DATA),
    INVLD_ORGN_TRANX_ID("IPAY0100233","Invalid original transaction id", "100074", CodeCategory.TXN_DATA),
    TRANX_NOT_PROC_EMPTY("IPAY0100192","Transaction Not Processed due to Empty XID ", "100075", CodeCategory.TXN_DATA),
    TRANX_NOT_PROC_INVALID_XID("IPAY0100193","Transaction Not Processed due to invalid XID", "100076", CodeCategory.TXN_DATA),
    //1000077 Duplicate of 100228
    PROBLEM_OCCUR_ORGIN_TRANX("IPAY0100112","Problem occurred in method loading original transaction data(card number, exp month / year)", "1000078", CodeCategory.TXN_DATA),
    INVALID_INPUT_DATA_REC("IPAY0100177","Invalid input data received", "100079", CodeCategory.TXN_DATA),
    TRANX_DN_INVALID("IPAY0100057","Transaction denied due to invalid processing option action code", "100082", CodeCategory.TXN_DATA),
    TRANX_DN_INLD_INS("IPAY0100058","Transaction denied due to invalid instrument", "100083", CodeCategory.TXN_DATA), 
    TRANX_DN_INLD_CURR_CODE("IPAY0100059","Transaction denied due to invalid currency code", "100084", CodeCategory.TXN_DATA), 
    TRANX_DN_MISSING_AMT("IPAY0100060","Transaction denied due to missing amount", "100085", CodeCategory.TXN_DATA),
    TRANX_DN_INVLD_AMT("IPAY0100061","Transaction denied due to invalid amount", "100086", CodeCategory.TXN_DATA), 
    TRANX_DN_INVLD_CURR("IPAY0100062","Transaction denied due to invalid Amount/Currency", "100087", CodeCategory.TXN_DATA),
    TRANX_DENIED_INVALID_CVV("IPAY0100073","Transaction denied due to invalid CVV", "100088", CodeCategory.TXN_DATA),
	TRANX_DENIED_INVALID_EXPIRY_YEAR("IPAY0100075","Transaction denied due to invalid expiry year", "100089", CodeCategory.TXN_DATA),
	TRANX_DENIED_INVALID_EXPIRY_MONTH("IPAY0100077","Transaction denied due to invalid expiry month", "100090", CodeCategory.TXN_DATA),
	TRANX_DENIED_INVALID_EXPIRY_DAY("IPAY0100079","Transaction denied due to invalid expiry day", "100091", CodeCategory.TXN_DATA),
	TRANX_DN_EXP_DATE("IPAY0100080","Transaction denied due to expiration date", "100092", CodeCategory.TXN_DATA),
	TRANX_DN_FAILED_CARD_DIGIT_CAL("IPAY0100070","Transaction denied due to failed card check digit calculation", "100093", CodeCategory.TXN_DATA),
    TRANX_DN_MISSING_CVD2("IPAY0100071","Transaction denied due to missing CVD2", "100094", CodeCategory.TXN_DATA),
    TRANX_DN_INVLD_CVD2("IPAY0100072","Transaction denied due to invalid CVD2", "100095", CodeCategory.TXN_DATA),
    TRANX_DN_DUE_ORIGIN_TRANX_ID("IPAY0100115","Transaction denied due to missing original transaction id", "100096", CodeCategory.TXN_DATA),
	TRANX_DN_DUE_INVALID_TRANX_ID("IPAY0100116","Transaction denied due to invalid original transaction id", "100097", CodeCategory.TXN_DATA),
	
	TRANX_DN_MISSING_CVV("IPAY0100086","Transaction denied due to missing CVV", "100099", CodeCategory.TXN_DATA),
    TRANX_DN_PRV_CAPTUTRE_FAILURE("IPAY0100136","Transaction denied due to previous capture check failure", "100100", CodeCategory.TXN_DATA),
    TRANX_DN_AMT_AUTH_CHECK_FAILURE("IPAY0100137","Transaction denied due to refund amount greater than auth amount check failure", "100101", CodeCategory.TXN_DATA),
    TRANX_DN_CAPTURE_AMOUNT_FAILURE("IPAY0100138","Transaction denied due to capture amount versus auth amount check failure", "100102", CodeCategory.TXN_DATA),
    TRANX_DN_VOID_AMOUNT_FAILURE("IPAY0100139","Transaction denied due to void amount versus original amount check failure", "100103", CodeCategory.TXN_DATA),
    TRANX_DN_PRV_VOID_FAILURE("IPAY0100140","Transaction denied due to previous void check failure", "100104", CodeCategory.TXN_DATA),
    TRANX_DN_AUTH_ALREADY_CAPTURE("IPAY0100141","Transaction denied due to authorization already captured", "1000105", CodeCategory.TXN_DATA),
    TRANX_DN_INVALID_PAY_INS("IPAY0100120","Transaction denied due to invalid payment instrument", "100106", CodeCategory.TXN_DATA),
	TRANX_DN_INVALID_ADDRESS("IPAY0100122","Transaction denied due to invalid address", "100107", CodeCategory.TXN_DATA),
	TRANX_DN_INVALID_POSTCODE("IPAY0100123","Transaction denied due to invalid postal code", "100108", CodeCategory.TXN_DATA),
	TRANX_DN_CURRENCY_MISMATCH("IPAY0100132","Transaction denied due to Currency Code mismatch", "100109", CodeCategory.TXN_DATA),
    TRANX_DN_INS_ID_MISMATCH("IPAY0100128","Transaction denied due to Institution ID mismatch", "100110", CodeCategory.TXN_DATA),
    PROBLEM_OCCUR_TRANX_LOG_DETAILS("IPAY0200026","Problem occurred while getting transaction log details", "100311", CodeCategory.TXN_DATA),
    
    //TXN_DN_RISK
    
    TRANX_DN_RISK_MAX_REFND_PROC("IPAY0100195","Transaction denied due to Risk : Maximum refund processing amount", "100126", CodeCategory.TXN_DN_RISK), 
     
    TRANX_DN_MAX_DBT_AMT("IPAY0100197","Transaction denied due to Risk : Maximum debit amount", "100128", CodeCategory.TXN_DN_RISK),  
    TRANX_DN_RISK_TRANX_CNT_LMT("IPAY0100198","Transaction denied due to Risk : Transaction count limit exceeded for the IP", "100129", CodeCategory.TXN_DN_RISK), 
    TRANX_DN_PRV_CHECK_FAILURE("IPAY0100199","Transaction denied due to previous refund check failure ( Validate Original Transaction", "100130", CodeCategory.TXN_DN_RISK),
    DN_RISK_NEG_BIN("IPAY0100200","Denied by risk : Negative BIN check - Fail", "100131", CodeCategory.TXN_DN_RISK),
    DN_RISK_DECL_CARD("IPAY0100201","Denied by risk : Declined Card check – Fail", "100132", CodeCategory.TXN_DN_RISK),
    TRANX_DN_BRAND_DIRC("IPAY0100189","Transaction denied due to brand directory unavailable", "100133", CodeCategory.TXN_DN_RISK),  
    TRANX_DN_RISK_MAX("IPAY0100190","Transaction denied due to Risk : Maximum transaction count", "100134", CodeCategory.TXN_DN_RISK), 
    TRANX_DN_RISK_MAX_CONT("IPAY0100103","Transaction denied due to Risk : Maximum transaction count", "100135", CodeCategory.TXN_DN_RISK),
    
    DN_RISK_PROF_NOT_EXIST("IPAY0100101","Denied by risk : Risk Profile does not exist", "100137", CodeCategory.TXN_DN_RISK), 
    DN_RISK_MAX_LIMIT("IPAY0100102","Denied by risk : Maximum Floor Limit Check - Fail", "100138", CodeCategory.TXN_DN_RISK),
    //100139 duplicate of 100135
    //100140 duplicate of 100136
    DN_RISK_NEG_CARD("IPAY0100191","Denied by risk : Negative Card check - Fail", "100141", CodeCategory.TXN_DN_RISK), 
    PROBLEM_OCCUR_PERFORM_RISK_CHECK("IPAY0100246","Problem occurred while doing perform ip risk check", "100142", CodeCategory.TXN_DN_RISK),
    PERFORM_RISK_CHECK_FAIL("IPAY0100108","Perform risk check : Failed", "100143", CodeCategory.TXN_DN_RISK),
    PROBLEM_OCCUR_RISK_PROFILE("IPAY0200019","Problem occurred while getting risk profile details", "100144", CodeCategory.TXN_DN_RISK),
    PROBLEM_OCCUR_PROFM_TRANX("IPAY0200020","Problem occurred while performing transaction risk check", "100145", CodeCategory.TXN_DN_RISK), 
    PROBLEM_OCCUR_PROF_RISK("IPAY0200021","Problem occurred while performing risk check", "100146", CodeCategory.TXN_DN_RISK),
    DN_BY_RISK("IPAY0100045","Denied by Risk", "100147", CodeCategory.TXN_DN_RISK),
    
    //TXN_REFUND_ISSUE
    PROBLEM_OCCURRED_BUILD_RFND_RQT("IPAY0100173","Problem occurred while building refund request", "100244", CodeCategory.TXN_REFUND_ISSUE),   
    PROBLEM_OCCURED_RFND_PROC("IPAY0100175","Problem occurred in refund process", "100245", CodeCategory.TXN_REFUND_ISSUE), 
    
    //TXN_REQ_FAILURE
    PROBLEM_OCCURRED_TRANX("IPAY0100163","Problem occurred during transaction", "100234", CodeCategory.TXN_REQ_FAILURE),
    TRANX_NOT_PROCESS_EMPTY_AUTH_STS("IPAY0100166","Transaction Not Processed due to Empty Authentication Status", "100235", CodeCategory.TXN_REQ_FAILURE), 
    TRANX_NOT_PROCESS_INVALID_AUTH_STS("IPAY0100167","Transaction Not Processed due to Invalid Authentication Status", "100236", CodeCategory.TXN_REQ_FAILURE),   
    TRANX_NOT_PROCESS_EMPTY_ENRL_STS("IPAY0100168","Transaction Not Processed due to Empty Enrollment Status", "100237", CodeCategory.TXN_REQ_FAILURE), 
    TRANX_NOT_PROCESS_INVALID_ENRL_STS("IPAY0100169","Transaction Not Processed due to Invalid Enrollment Status", "100238", CodeCategory.TXN_REQ_FAILURE),
    TRANX_NOT_PROCESS_INVALID_CAVV("IPAY0100170","Transaction Not Processed due to invalid CAVV", "100239", CodeCategory.TXN_REQ_FAILURE),
    TRANX_NOT_PROCESS_EMPTY_CAVV("IPAY0100171","Transaction Not Processed due to Empty CAVV", "100240", CodeCategory.TXN_REQ_FAILURE), 
    PROBLEM_OCCUR_PRFM_TRANX("IPAY0100203","Problem occurred while doing perform transaction", "100241", CodeCategory.TXN_REQ_FAILURE),
    PROBLEM_OCCUR_PROC_TRANX("IPAY0100213","Problem occurred while processing the hosted transaction request", "100242", CodeCategory.TXN_REQ_FAILURE),
    UNABLED_PROC_REQ("IPAY0100038","Unable to process the request", "100243", CodeCategory.TXN_REQ_FAILURE),
    
    //TXN_SEC_FAILURE
    IP_ADDR_BLOCK_ALREADY("IPAY0100043","IP address is blocked already", "100258", CodeCategory.TXN_SEC_FAILURE),
    
    //TXN_SESSION
    TRANX_PROG_TAB("IPAY0100040","Transaction in progress in another tab/window", "100255", CodeCategory.TXN_SESSION),
    
    PROBLEM_OCCUR_LOAD_PAY_PAGE("IPAY0100044","Problem occurred while loading payment page", "100257", CodeCategory.TXN_SESSION),
    
    //TXN_TRNP
    MISSING_TRANPT_ID("IPAY0100005","Missing tranportal id", "100148", CodeCategory.TXN_TRNP),
    INVLD_TRANPT_ID("IPAY0100006","Invalid tranportal ID", "100149", CodeCategory.TXN_TRNP),
    INVLD_TRANPT_PASSWD("IPAY0100015","Invalid tranportal password", "100150", CodeCategory.TXN_TRNP),
    PROBLEM_OCCUR_VERFY_TRANPT("IPAY0200006","Problem occurred while verifying tranportal password", "100151", CodeCategory.TXN_TRNP),
    PROBLEM_OCCUR_VER_TRAN("IPAY0100214","Problem occurred while verifying tranportal id", "100152", CodeCategory.TXN_TRNP),
    
    //TXN_UDF
    TRANX_DN_INVLD_TRACK_ID("IPAY0100063","Transaction denied due to invalid track ID", "100111", CodeCategory.TXN_UDF),   
    TRANX_DN_INVLD_UDF1("IPAY0100064","Transaction denied due to invalid UDF1", "100112", CodeCategory.TXN_UDF),
    TRANX_DN_INVLD_UDF2("IPAY0100065","Transaction denied due to invalid UDF2", "100113", CodeCategory.TXN_UDF),
    TRANX_DN_INVLD_UDF3("IPAY0100066","Transaction denied due to invalid UDF3", "100114", CodeCategory.TXN_UDF),
    //100115 duplicate of 100114
    TRANX_DN_INVLD_UDF4("IPAY0100067","Transaction denied due to invalid UDF4", "100116", CodeCategory.TXN_UDF),
    TRANX_DN_INVLD_UDF5("IPAY0100068","Transaction denied due to invalid UDF5", "100117", CodeCategory.TXN_UDF),
    INVLD_USER_DEF_FD1("IPAY0100028","Invalid user defined field1", "100118", CodeCategory.TXN_UDF), 
    INVLD_USER_DEF_FD2("IPAY0100029","Invalid user defined field2", "100119", CodeCategory.TXN_UDF),
    INVALID_USER_DEF_FIELD3("IPAY0100030","Invalid user defined field3", "100120", CodeCategory.TXN_UDF), 
    INVLD_USER_DEF_FD4("IPAY0100031","Invalid user defined field4", "100121", CodeCategory.TXN_UDF),
    INVLD_USER_DEF_FD5("IPAY0100032","Invalid user defined field5", "100122", CodeCategory.TXN_UDF),
    UDF_MISMATCHED("IPAY0100036","UDF Mismatched", "100123", CodeCategory.TXN_UDF),
    INVALID_USER_DEFINE_FIELD("IPAY0100231","Invalid user defined field", "100124", CodeCategory.TXN_UDF),
    
    //TXN_URL
    MISSING_ERROR_URL("IPAY0100001","Missing error URL", "100046", CodeCategory.TXN_URL),
    INVLD_ERR_URL("IPAY0100002","Invalid error URL","100047", CodeCategory.TXN_URL),
    MISSING_RES_URL("IPAY0100003","Missing response URL", "100048", CodeCategory.TXN_URL), 
    INVLD_RES_URL("IPAY0100004","Invalid response URL", "100049", CodeCategory.TXN_URL),
    
    CAPTURED("CAPTURED","Transaction was approved", "100045", CodeCategory.TXN_SUCCESS),
	NOT_CAPTURED("NOT CAPTURED","Transaction was not approved", "100035", CodeCategory.TXN_AUTH),
	VOIDED("VOIDED","Transaction was voided", "100079", CodeCategory.TXN_DATA),
	GENERIC_ERROR("GENERIC ERROR","Generic Error", "100060", CodeCategory.TXN_DATA),
    
    UNKNOWN("UNKNOWN","Error code may not be mapped","UNKNOWN",CodeCategory.UNKNOWN),
    
    // -- additional ResponseCode added for Oman
    CARD_NOT_REGISTERED_FOR_OTP("IPAY0200085","CARD_NOT_REGISTERED_FOR_OTP", "100033", CodeCategory.CARD_NOT_REGISTERED_FOR_OTP),
    INVALID_CARD_NUMBER_01("Invalid card number.","Invalid card number", "100007", CodeCategory.INVALID_CARD_NUMBER_01),
    INVALID_CARD_NUMBER_03("IPAY0100118","Transaction denied due to card number length error", "100014", CodeCategory.INVALID_CARD_NUMBER_03),
    OTP_TRIES_EXCEED("IPAY0200075","OTP Tries Exceeded", "100031", CodeCategory.OTP_TRIES_EXCEED),
    TRANX_DECLN_EXCEED_OTP_ATTMPT("IPAY0100049","Transaction declined due to exceeding OTP attempts", "100034", CodeCategory.TRANX_DECLN_EXCEED_OTP_ATTMPT),
    TIME_EXCEEDS_FOR_OTP("IPAY0200079","TIME_EXCEEDS_FOR_OTP", "100032", CodeCategory.TIME_EXCEEDS_FOR_OTP),
    TRANX_DN_MISSING_CARD_NUMBER("IPAY0100117","Transaction denied due to missing card number", "100098", CodeCategory.TRANX_DN_MISSING_CARD_NUMBER),
    TRANX_DN_RISK_MIN_TRANX("IPAY0100194","Transaction denied due to Risk : Minimum Transaction Amount processing", "100125", CodeCategory.TRANX_DN_RISK_MIN_TRANX),
    TRANX_DN_RISK_MAX_PROC_AMT("IPAY0100196","Transaction denied due to Risk : Maximum processing amount", "100127", CodeCategory.TRANX_DN_RISK_MAX_PROC_AMT),
    TRANX_DN_RISK_MAX_AMT("IPAY0100104","Transaction denied due to Risk : Maximum processing amount", "100136", CodeCategory.TRANX_DN_RISK_MAX_AMT),
    TRANX_TIME_LMT_EXCEEDS("IPAY0100042","Transaction time limit exceeds", "100256", CodeCategory.TRANX_TIME_LMT_EXCEEDS);
    
    //OTHERS
    //VALIDATE_ORG_TRANX("IPAY0100142","Problem occurred while validating original transaction"),
	//TRANX_DENIED_INVALID_UDF3("IPAY0100066","Transaction denied due to invalid UDF3"),
	//UNABLE_TO_TRANX("IPAY0100160","Unable to process the transaction"), 
    //CARD_HOLDER_NOT_PRESENT("IPAY0100081","Card holder name is not present"),
     
    private String responseCode;
	private String responseDesc;
	private String almullaErrorCode;
	CodeCategory category;
	
	private static final Map<String, ResponseCodeOMN> LOOKUP = Maps.uniqueIndex(Arrays.asList(ResponseCodeOMN.values()),
			ResponseCodeOMN::getResponseCode);
	
	ResponseCodeOMN(String responseCode, String responseDesc, String almullaErrorCode, CodeCategory category) {
		this.responseCode = responseCode;
		this.responseDesc = responseDesc;
		this.almullaErrorCode = almullaErrorCode;
		this.category = category;
	}

	ResponseCodeOMN(String responseCode,String responseDesc) {
		this.responseCode = responseCode;
		this.responseDesc = responseDesc;
	}
	
	private ResponseCodeOMN(String responseCode) {
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

		ResponseCodeOMN respCode = getResponseCodeEnumByCode(responseCode);

		if (null == respCode) {
			return null;
		}

		return respCode.getCategory();
	}

	public static ResponseCodeOMN getResponseCodeEnumByCode(String responseCode) {
		return LOOKUP.get(responseCode);
	}
	

}
