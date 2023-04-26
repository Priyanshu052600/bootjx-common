package com.boot.jx.dict.payg;

import java.util.List;
import java.util.Map;

import com.boot.jx.dict.ResponseCodeESCROW;
import com.boot.utils.ArgUtil;

public class CashTrnxModel {

	private Long id;
	private String customerid;
	private String payid;
	private String docNo;
	private String amount;
	private String deposited;
	private String refunded;
	private String collected;
	private String onhold;
	private String traceid;
	private String action;
	private String createdDate;
	private String moneyDesc;
	private boolean status;
	private String moneyStatus;
	private String refundStatus;
	private String machineStatus;
	private String trnxStatus;
	private String bagRef;
	private String statusKey;
	private String currencyCode;
	private String refundlimit;
	private String maxPayableAmount;

	private List<KIOSKDenomination> lstTotalKIOSKDenomination;
	private List<KIOSKDenomination> lstTotalRefundKIOSKDenomination;
	private	Map<String, Object> paymentCaptureOutput;

	public String getPayid() {
		return payid;
	}

	public void setPayid(String payid) {
		this.payid = payid;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCollected() {
		return collected;
	}

	public void setCollected(String collected) {
		this.collected = collected;
	}

	public String getRefunded() {
		return refunded;
	}

	public void setRefunded(String refunded) {
		this.refunded = refunded;
	}

	/*
	 * public Date getCreatedDate() { return createdDate; }
	 * 
	 * public void setCreatedDate(Date createdDate) { this.createdDate =
	 * createdDate; }
	 */

	public String getMoneyDesc() {
		return moneyDesc;
	}

	public void setMoneyDesc(String moneyDesc) {
		this.moneyDesc = moneyDesc;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getDeposited() {
		return deposited;
	}

	public void setDeposited(String deposited) {
		this.deposited = deposited;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMoneyStatus() {
		return moneyStatus;
	}

	public void setMoneyStatus(String moneyStatus) {
		this.moneyStatus = moneyStatus;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getMachineStatus() {
		return machineStatus;
	}

	public void setMachineStatus(String machineStatus) {
		this.machineStatus = machineStatus;
	}

	public String getDocNo() {
		return docNo;
	}

	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	public String getOnhold() {
		return onhold;
	}

	public void setOnhold(String onhold) {
		this.onhold = onhold;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String time) {
		this.createdDate = time;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTraceid() {
		return traceid;
	}

	public void setTraceid(String traceid) {
		this.traceid = traceid;
	}

	public List<KIOSKDenomination> getLstTotalKIOSKDenomination() {
		return lstTotalKIOSKDenomination;
	}

	public void setLstTotalKIOSKDenomination(List<KIOSKDenomination> lstTotalKIOSKDenomination) {
		this.lstTotalKIOSKDenomination = lstTotalKIOSKDenomination;
	}

	public List<KIOSKDenomination> getLstTotalRefundKIOSKDenomination() {
		return lstTotalRefundKIOSKDenomination;
	}

	public void setLstTotalRefundKIOSKDenomination(List<KIOSKDenomination> lstTotalRefundKIOSKDenomination) {
		this.lstTotalRefundKIOSKDenomination = lstTotalRefundKIOSKDenomination;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBagRef() {
		return bagRef;
	}

	public void setBagRef(String bagRef) {
		this.bagRef = bagRef;
	}

	public String getStatusKey() {
		return ResponseCodeESCROW.ESCROW_WORKING.name().equals(this.machineStatus) ? (ArgUtil.is(this.trnxStatus) ? this.trnxStatus : this.moneyStatus)
				: this.machineStatus;
	}

	public void setStatusKey(String statusKey) {
		this.statusKey = statusKey;
	}

	public Map<String, Object> getPaymentCaptureOutput() {
		return paymentCaptureOutput;
	}

	public void setPaymentCaptureOutput(Map<String, Object> paymentCaptureOutpet) {
		this.paymentCaptureOutput = paymentCaptureOutpet;
	}

	public String getTrnxStatus() {
		return trnxStatus;
	}

	public void setTrnxStatus(String trnxStatus) {
		this.trnxStatus = trnxStatus;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getRefundlimit() {
		return refundlimit;
	}

	public void setRefundlimit(String refundlimit) {
		this.refundlimit = refundlimit;
	}
	
	public String getMaxPayableAmount() {
		return maxPayableAmount;
	}

	public void setMaxPayableAmount(String maxPayableAmount) {
		this.maxPayableAmount = maxPayableAmount;
	}
	
}
