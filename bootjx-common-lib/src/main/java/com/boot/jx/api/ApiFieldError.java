package com.boot.jx.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.boot.jx.exception.ExceptionMessageKey;
import com.boot.jx.exception.IExceptionEnum;
import com.boot.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ResponseError.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiFieldError implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3253269120058295762L;

	/** The obzect. */
	String obzect = null;
	String field = null;
	String description = null;
	String descriptionKey = null;
	String code = null;
	String codeKey = null;
	Object body;
	// Enum Codes
	// IExceptionEnum enumCode;
	/**
	 * Added but yet to be used for parameterized descriptionKey
	 */
	private Object[] descriptionArgs;
	private Object[] possibleValues;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	List<String> codes = null;

	public ApiFieldError() {
		this.codes = new ArrayList<String>();
	}

	public List<String> getCodes() {
		return codes;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}

	/**
	 * Gets the obzect.
	 *
	 * @return the obzect
	 */
	public String getObzect() {
		return obzect;
	}

	/**
	 * Sets the obzect.
	 *
	 * @param obzect the new obzect
	 */
	public void setObzect(String obzect) {
		this.obzect = obzect;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the field.
	 *
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * Sets the field.
	 *
	 * @param field the new field
	 */
	public void setField(String field) {
		this.field = field;
	}

	public String toString() {
		return code + codeKey + field + obzect + descriptionKey + description;
	}

	public String getDescriptionKey() {
		if (!ArgUtil.is(descriptionKey) && ArgUtil.is(descriptionArgs)) {
			this.descriptionKey = ExceptionMessageKey.build(descriptionKey, descriptionArgs);
		}
		return descriptionKey;
	}

	public void setDescriptionKey(String descriptionKey) {
		this.descriptionKey = descriptionKey;
	}

	public String getCodeKey() {
		return codeKey;
	}

	public void setCodeKey(String codeKey) {
		this.codeKey = codeKey;
	}

	public ApiFieldError descriptionKey(Object key) {
		this.descriptionKey = ArgUtil.parseAsString(key);
		return this;
	}

	public ApiFieldError descriptionArgs(Object... key) {
		this.descriptionArgs = key;
		return this;
	}

	public ApiFieldError description(String description) {
		this.description = description;
		return this;
	}

	public ApiFieldError code(IExceptionEnum enumCode) {
		this.code = ArgUtil.parseAsString(enumCode.getStatusCode());
		this.codeKey = enumCode.getStatusKey();
		return this;
	}

	public ApiFieldError codeKey(String codeKey) {
		this.codeKey = codeKey;
		return this;
	}

	public ApiFieldError code(int code) {
		this.code = ArgUtil.parseAsString(code);
		return this;
	}

	public ApiFieldError code(String code) {
		this.code = code;
		return this;
	}

	public ApiFieldError field(String field) {
		this.field = field;
		return this;
	}

	public ApiFieldError obzect(String obzect) {
		this.obzect = obzect;
		return this;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public Object[] getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(Object[] possibleValues) {
		this.possibleValues = possibleValues;
	}

	public ApiFieldError possibleValues(Object... possibleValues) {
		this.possibleValues = possibleValues;
		return this;
	}

	/*
	 * public IExceptionEnum getEnumCode() { return enumCode; }
	 * 
	 * public void setEnumCode(IExceptionEnum enumCode) { this.enumCode = enumCode;
	 * this.code = String.valueOf(enumCode.getStatusCode()); this.codeKey =
	 * enumCode.getStatusKey(); }
	 */

}
