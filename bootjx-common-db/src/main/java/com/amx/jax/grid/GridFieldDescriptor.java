package com.amx.jax.grid;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boot.utils.ArgUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = GridFieldDescriptorDTO.class)
public interface GridFieldDescriptor extends Serializable {

	void setDbColumnName(String dbColumnName);

	String getDbColumnName();

	BigDecimal getId();

	void setId(BigDecimal id);

	void setFieldOrder(String fieldOrder);

	String getFieldOrder();

	void setFieldCategory2(String fieldCategory2);

	void setFieldCategory(String fieldCategory);

	String getFieldCategory2();

	String getFieldCategory();

	void setFieldDataType(String fieldDataType);

	String getFieldDataType();

	void setFieldLabel(String fieldLabel);

	String getFieldLabel();

	void setIsActive(String isActive);

	String getIsActive();

	void setCondition(String condition);

	String getCondition();

	void setFieldType(String fieldType);

	String getFieldType();

	public default boolean isDBColumn() {
		return ArgUtil.is(this.getFieldDataType());
	}
	
	String getFilterType();
	
	void setFilterType(String filterType);
	
	String getFilterOptionsView();
	
	void setFilterOptionsView(String filterOptionsView);
	
	String getFilterRequired();
	
	void setFilterRequired(String filterRequired);
	
	String getFilterCondition();
	
	void setFilterCondition(String filterCondition);

	String getFilterGroupName();
	
	void setFilterGroupName(String filterGroupName);
	
	String getFilterGroupStrategy();
	
	void setFilterGroupStrategy(String filterGroupStrategy);

}
