package com.amx.jax.grid;

import java.math.BigDecimal;


public class GridFieldDescriptorDTO implements GridFieldDescriptor {

	private static final long serialVersionUID = 1L;
	private BigDecimal id;

	private String dbViewName;

	private String dbColumnName;

	private String fieldLabel;

	private String fieldDataType;

	private String fieldCategory;

	private String fieldCategory2;

	private String fieldOrder;

	private String isActive;
	
	private String condition;
	
	private String fieldType;
	
	private String filterDependsOn;

	private String filterType;

	private String filterOptionsView;
	
	private String filterRequired;
	
	private String filterCondition;
	
	private String filterGroupName;
	
	private String filterGroupStrategy;

	@Override
	public BigDecimal getId() {
		return id;
	}

	@Override
	public void setId(BigDecimal id) {
		this.id = id;
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

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Override
	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public String getCondition() {
		return condition;
	}

	@Override
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	@Override
	public String getFieldType() {
		return fieldType;
	}
	
	@Override
	public String getFilterDependsOn() {
		return filterDependsOn;
	}

	@Override
	public void setFilterDependsOn(String filterDependsOn) {
		this.filterDependsOn = filterDependsOn;
	}


	@Override
	public String getFilterType() {
		return filterType;
	}

	@Override
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	@Override
	public String getFilterOptionsView() {
		return filterOptionsView;
	}

	@Override
	public void setFilterOptionsView(String filterOptionsView) {
		this.filterOptionsView = filterOptionsView;
	}

	@Override
	public String getFilterRequired() {
		return filterRequired;
	}

	@Override
	public void setFilterRequired(String filterRequired) {
		this.filterRequired = filterRequired;
	}

	@Override
	public String getFilterCondition() {
		return filterCondition;
	}

	@Override
	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}

	@Override
	public String getFilterGroupName() {
		return filterGroupName;
	}

	@Override
	public void setFilterGroupName(String filterGroupName) {
		this.filterGroupName = filterGroupName;
	}

	@Override
	public String getFilterGroupStrategy() {
		return filterGroupStrategy;
	}

	@Override
	public void setFilterGroupStrategy(String filterGroupStrategy) {
		this.filterGroupStrategy = filterGroupStrategy;
	}


	
}