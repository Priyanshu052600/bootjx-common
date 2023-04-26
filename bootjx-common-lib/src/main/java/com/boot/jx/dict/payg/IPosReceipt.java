package com.boot.jx.dict.payg;

import java.util.List;
import java.util.Map;

public class IPosReceipt {
	
	private int msgLength;
	private String msgCode;
	private String postId;
	private String currencyType;
	private String amount;
	private String typeOfPrint;
	private String storeCode;
	private String terminalCode;
	private String transId;
	private String authorizationCode;
	private String receiptDate;
	private String receiptTime;
	private String panLength;
	private String pan;
	private int receiptlength;
	private String receiptData;
	private int extraDataLength;
	private String extraData;
	private int errorCode;
	private String errorDescription;
	private String customerId;
	private String docNo;
	private String machineStatus;
	private String moneyStatus;
	private List<String> crc;
	private String iposError;
	private	Map<String, Object> paymentCaptureOutput;
	private String trnxStatus;
	private String payId;
	private String currencyCode;
	private String maxPayableAmount;
	
	public IPosReceipt() {
		super();
	}

	public IPosReceipt(int msgLength, String msgCode, String postId, String currencyType, String amount,
			String typeOfPrint, String storeCode, String terminalCode, String transId, String authorizationCode,
			String receiptDate, String receiptTime, String panLength, String pan, int receiptlength, String receiptData,
			int extraDataLength, String extraData, int errorCode, String errorDescription, String customerId,
			String docNo, String machineStatus, List<String> crc, String iposError,
			Map<String, Object> paymentCaptureOutput, String trnxStatus, String payId, String currencyCode) {
		super();
		this.msgLength = msgLength;
		this.msgCode = msgCode;
		this.postId = postId;
		this.currencyType = currencyType;
		this.amount = amount;
		this.typeOfPrint = typeOfPrint;
		this.storeCode = storeCode;
		this.terminalCode = terminalCode;
		this.transId = transId;
		this.authorizationCode = authorizationCode;
		this.receiptDate = receiptDate;
		this.receiptTime = receiptTime;
		this.panLength = panLength;
		this.pan = pan;
		this.receiptlength = receiptlength;
		this.receiptData = receiptData;
		this.extraDataLength = extraDataLength;
		this.extraData = extraData;
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
		this.customerId = customerId;
		this.docNo = docNo;
		this.machineStatus = machineStatus;
		this.crc = crc;
		this.iposError = iposError;
		this.paymentCaptureOutput = paymentCaptureOutput;
		this.trnxStatus = trnxStatus;
		this.payId = payId;
		this.currencyCode = currencyCode;
	}

	public int getMsgLength() {
		return msgLength;
	}

	public void setMsgLength(int msgLength) {
		this.msgLength = msgLength;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTypeOfPrint() {
		return typeOfPrint;
	}

	public void setTypeOfPrint(String typeOfPrint) {
		this.typeOfPrint = typeOfPrint;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getTerminalCode() {
		return terminalCode;
	}

	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public String getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getReceiptTime() {
		return receiptTime;
	}

	public void setReceiptTime(String receiptTime) {
		this.receiptTime = receiptTime;
	}

	public String getPanLength() {
		return panLength;
	}

	public void setPanLength(String panLength) {
		this.panLength = panLength;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public int getReceiptlength() {
		return receiptlength;
	}

	public void setReceiptlength(int receiptlength) {
		this.receiptlength = receiptlength;
	}

	public String getReceiptData() {
		return receiptData;
	}

	public void setReceiptData(String receiptData) {
		this.receiptData = receiptData;
	}

	public int getExtraDataLength() {
		return extraDataLength;
	}

	public void setExtraDataLength(int extraDataLength) {
		this.extraDataLength = extraDataLength;
	}

	public String getExtraData() {
		return extraData;
	}

	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public List<String> getCrc() {
		return crc;
	}
	
	public void setCrc(List<String> crc) {
		this.crc = crc;
	}

	public String getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getDocNo() {
		return docNo;
	}
	
	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	public String getMachineStatus() {
		return machineStatus;
	}
	
	public void setMachineStatus(String machineStatus) {
		this.machineStatus = machineStatus;
	}

	public String getIposError() {
		return iposError;
	}
	
	public void setIposError(String iposError) {
		this.iposError = iposError;
	}

	public Map<String, Object> getPaymentCaptureOutput() {
		return paymentCaptureOutput;
	}
	
	public void setPaymentCaptureOutput(Map<String, Object> paymentCaptureOutput) {
		this.paymentCaptureOutput = paymentCaptureOutput;
	}

	public String getTrnxStatus() {
		return trnxStatus;
	}
	
	public void setTrnxStatus(String trnxStatus) {
		this.trnxStatus = trnxStatus;
	}

	public String getPayId() {
		return payId;
	}
	
	public void setPayId(String payId) {
		this.payId = payId;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	public String getMoneyStatus() {
		return moneyStatus;
	}

	public void setMoneyStatus(String moneyStatus) {
		this.moneyStatus = moneyStatus;
	}
	
	public String getMaxPayableAmount() {
		return maxPayableAmount;
	}

	public void setMaxPayableAmount(String maxPayableAmount) {
		this.maxPayableAmount = maxPayableAmount;
	}
}
