package com.amx.jax.grid;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "JAX_DYNAMIC_DISPLAY_FIELDS")
public class DynamicFieldDescriptorEntity implements GridFieldDescriptor {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	private BigDecimal id;

	@Column(name = "APPLICATION_COUNTRY_ID")
	private BigDecimal appCountryId;

	@Column(name = "DB_VIEW_NAME")
	private String dbViewName;

	@Column(name = "DB_COLUMN_NAME")
	private String dbColumnName;

	@Column(name = "FIELD_LABEL")
	private String fieldLabel;

	@Column(name = "FIELD_DATA_TYPE")
	private String fieldDataType;

	@Column(name = "FIELD_CATEGORY")
	private String fieldCategory;

	@Column(name = "FIELD_SUB_CATEGORY")
	private String fieldCategory2;

	@Column(name = "FIELD_ORDER")
	private String fieldOrder;
	
	@Column(name = "DISPLAY_ORDER")
	private String displayOrder;

	@Column(name = "ISACTIVE")
	private String isActive;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "CONDITION")
	private String condition;
	
	@Column(name = "FIELD_TYPE")
	private String fieldType;
	
	@Override
	public BigDecimal getId() {
		return id;
	}

	@Override
	public void setId(BigDecimal id) {
		this.id = id;
	}

	public BigDecimal getAppCountryId() {
		return appCountryId;
	}

	public void setAppCountryId(BigDecimal appCountryId) {
		this.appCountryId = appCountryId;
	}

	public String getDbViewName() {
		return dbViewName;
	}

	public void setDbViewName(String dbViewName) {
		this.dbViewName = dbViewName;
	}

	@Override
	public String getDbColumnName() {
		return dbColumnName;
	}

	@Override
	public void setDbColumnName(String dbColumnName) {
		this.dbColumnName = dbColumnName;
	}

	@Override
	public String getFieldLabel() {
		return fieldLabel;
	}

	@Override
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	@Override
	public String getFieldDataType() {
		return fieldDataType;
	}

	@Override
	public void setFieldDataType(String fieldDataType) {
		this.fieldDataType = fieldDataType;
	}

	@Override
	public String getFieldCategory() {
		return fieldCategory;
	}

	@Override
	public void setFieldCategory(String fieldCategory) {
		this.fieldCategory = fieldCategory;
	}

	@Override
	public String getFieldCategory2() {
		return fieldCategory2;
	}

	@Override
	public void setFieldCategory2(String fieldCategory2) {
		this.fieldCategory2 = fieldCategory2;
	}

	@Override
	public String getFieldOrder() {
		return fieldOrder;
	}

	@Override
	public void setFieldOrder(String fieldOrder) {
		this.fieldOrder = fieldOrder;
	}

	@Override
	public String getIsActive() {
		return isActive;
	}

	@Override
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Override
	public String getCondition() {
		// TODO Auto-generated method stub
		return condition;
	}

	@Override
	public void setCondition(String condition) {
		this.condition = condition;
		
	}

	@Override
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	@Override
	public String getFieldType() {
		return fieldType;
	}

}
