package com.amx.jax.grid;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GridParams implements Serializable {

	private static final long serialVersionUID = 4565076394225819716L;
	
	private BigDecimal customerId;
	private BigDecimal customerReference;
	private BigDecimal docFinYear;
	private BigDecimal docNo;
	private BigDecimal collectionDocFinYear;
	private BigDecimal collectionDocNo;
	private BigDecimal branchId;

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getDocFinYear() {
		return docFinYear;
	}

	public void setDocFinYear(BigDecimal docFinYear) {
		this.docFinYear = docFinYear;
	}

	public BigDecimal getDocNo() {
		return docNo;
	}

	public void setDocNo(BigDecimal docNo) {
		this.docNo = docNo;
	}

	public BigDecimal getBranchId() {
		return branchId;
	}

	public void setBranchId(BigDecimal branchId) {
		this.branchId = branchId;
	}

	public BigDecimal getCollectionDocFinYear() {
		return collectionDocFinYear;
	}

	public void setCollectionDocFinYear(BigDecimal collectionDocFinYear) {
		this.collectionDocFinYear = collectionDocFinYear;
	}

	public BigDecimal getCollectionDocNo() {
		return collectionDocNo;
	}

	public void setCollectionDocNo(BigDecimal collectionDocNo) {
		this.collectionDocNo = collectionDocNo;
	}

	public BigDecimal getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}
	
	

}
