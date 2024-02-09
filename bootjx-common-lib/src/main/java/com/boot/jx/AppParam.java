package com.boot.jx;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonPropertyOrder({ "param", "enabled", "property", "value" })
public enum AppParam {

	PRINT_TRACK_BODY(false), DEBUG_INFO,

	APP_ENV, APP_VENV, APP_GROUP, APP_NAME, APP_ID,

	// APP_INSTANCE_ID
	APP_INSTANCE_TYPE, APP_INSTANCE_ID, APP_INSTANCE_HASH, APP_INSTANCE_UID, APP_TYPE,

	APP_VERSION, APP_BUILDTIMESTAMP, APP_CONTEXT_PREFIX, APP_DESCRIPTION,

	APP_PROD, APP_SWAGGER, APP_DEBUG, APP_CACHE, APP_AUTH_ENABLED, APP_LOGGER, APP_MONITOR,

	// Defaults
	DEFAULT_TENANT,

	JAX_CDN_URL, JAX_APP_URL, JAX_POSTMAN_URL, JAX_LOGGER_URL, JAX_SSO_URL, JAX_AUTH_URL, JAX_CASHIER_URL,
	JAX_SERVICE_URL, JAX_PRICER_URL,

	SPRING_REDIS_HOST, SPRING_REDIS_PORT, SPRING_APP_NAME;

	@JsonProperty("_id")
	String param;

	boolean enabled;
	String value;
	String property;

	AppParam() {
		this(false);
	}

	AppParam(boolean enabled) {
		this.param = this.name();
		this.enabled = enabled;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}
