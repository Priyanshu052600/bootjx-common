package com.boot.jx.swagger;

import java.util.List;

/**
 * 
 * @author lalittanwar
 *
 */
public class MockParamBuilder {

    public static final String HEADER_VALUE = "header";

    public static enum MockParamType {
	HEADER, COOKIE, BODY, QUERY
    }

    public static class MockParam {
	MockParamType type;
	String id;
	String name;
	String description;
	String defaultValue;
	boolean required = false;
	boolean hidden = false;
	String securityScheme;
	List<String> values = null;
	public String valueType;

	public String getDefaultValue() {
	    return defaultValue;
	}

	public String getId() {
	    return id;
	}

	public String getName() {
	    return name;
	}

	public String getDescription() {
	    return description;
	}

	public MockParamType getType() {
	    return type;
	}

	public boolean isRequired() {
	    return required;
	}

	public List<String> getValues() {
	    return values;
	}

	public String getValueType() {
	    return valueType;
	}

	public boolean isHidden() {
	    return hidden;
	}

	public void setHidden(boolean hidden) {
	    this.hidden = hidden;
	}

	public String getSecurityScheme() {
	    return securityScheme;
	}

	public void setSecurityScheme(String securityScheme) {
	    this.securityScheme = securityScheme;
	}

    }

    MockParam mockParam;

    public MockParamBuilder() {
	this.mockParam = new MockParam();
    }

    public MockParamBuilder id(String id) {
	this.mockParam.id = id;
	return this;
    }

    public MockParamBuilder name(String name) {
	this.mockParam.name = name;
	return this;
    }

    public MockParamBuilder description(String description) {
	this.mockParam.description = description;
	return this;
    }

    public MockParamBuilder defaultValue(String defaultValue) {
	this.mockParam.defaultValue = defaultValue;
	return this;
    }

    public MockParamBuilder required(boolean required) {
	this.mockParam.required = required;
	return this;
    }

    public MockParamBuilder hidden(boolean hidden) {
	this.mockParam.hidden = hidden;
	return this;
    }

    public MockParamBuilder parameterType(MockParamType type) {
	this.mockParam.type = type;
	return this;
    }

    public MockParamBuilder securityScheme(String securityScheme) {
	this.mockParam.securityScheme = securityScheme;
	return this;
    }

    public MockParamBuilder allowableValues(List<String> values, String valueType) {
	this.mockParam.values = values;
	this.mockParam.valueType = valueType;
	return this;
    }

    public MockParam build() {
	return mockParam;
    }

}
