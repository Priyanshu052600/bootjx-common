package com.boot.jx.dict.payg;

public class KioskReciept {

	private CashTrnxModel cashmodel;
	private IPosReceipt iposReceipt;
	private String serviceCode;
	
	
	public CashTrnxModel getCashmodel() {
		return cashmodel;
	}
	public void setCashmodel(CashTrnxModel cashmodel) {
		this.cashmodel = cashmodel;
	}
	public IPosReceipt getIposReceipt() {
		return iposReceipt;
	}
	public void setIposReceipt(IPosReceipt iposReceipt) {
		this.iposReceipt = iposReceipt;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
	
	
	
}
