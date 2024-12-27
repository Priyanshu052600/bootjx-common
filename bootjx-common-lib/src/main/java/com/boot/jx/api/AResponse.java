package com.boot.jx.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.boot.jx.api.AmxResponseSchemes.ApiMetaDetailsResponse;
import com.boot.jx.exception.ApiHttpExceptions.ApiStatusCodes;
import com.boot.jx.exception.IExceptionEnum;
import com.boot.jx.swagger.ApiMockModelProperty;
import com.boot.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AResponse<M> implements ApiMetaDetailsResponse<M> {

	protected Long timestamp;
	protected String serverVersion;

	// Spring Norms
	protected String status = "200"; // 400
	protected String error; // Bad Request
	protected String exception; // org.springframework.http.converter.HttpMessageNotReadableException
	protected String message;// JSON parse error
	protected String warningKey;

	@ApiMockModelProperty(example = "/postman/email/send")
	protected String path;

	@ApiMockModelProperty(example = "/go/to/some/other/url.html")
	protected String redirectUrl;

	public static enum Target {
		_BLANK, _SELF, _PARENT, _TOP, _IFRAME
	}

	protected String messageKey;

	/** The status key. */
	protected String statusKey = ApiStatusCodes.SUCCESS.toString();

	// Amx Specs
	protected M meta;
	protected List<M> details;
	protected List<ApiFieldError> errors = null;
	protected List<ApiFieldError> warnings = null;
	protected List<Object> traces = null;
	protected List<String> logs = null;
	protected Map<String, Object> params = null;
	protected Map<String, Object> extra = null;
	protected ApiPagination pagination;

	public AResponse() {
		this.timestamp = System.currentTimeMillis();
		this.meta = null;
	}

	/**
	 * target="_blank|_self|_parent|_top|framename"
	 * 
	 * @param redirectUrl
	 */
	@JsonIgnore
	public void setTargetUrl(String redirectUrl, Target target) {
		this.redirectUrl = target + ":" + redirectUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	@Override
	public Long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 *
	 * @param timestamp the new timestamp
	 */
	@Override
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * HTTP Status Code : 400
	 * 
	 * @return
	 */
	@Override
	public String getStatus() {
		return status;
	}

	/**
	 * HTTP Status Code : 400
	 * 
	 * @param status
	 */
	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonIgnore
	public void setStatusEnum(IExceptionEnum status) {
		if (!ArgUtil.isEmpty(status)) {
			this.statusKey = status.getStatusKey();
			this.status = ArgUtil.parseAsString(status.getStatusCode());
		}
	}

	/**
	 * Gets the status key.
	 *
	 * @return the status key
	 */
	@Override
	public String getStatusKey() {
		return statusKey;
	}

	/**
	 * Sets the status key.
	 *
	 * @param statusKey the new status key
	 */
	@Override
	public void setStatusKey(String statusKey) {
		// this.setStatusEnum(ApiStatusCodes.NO_STATUS);
		this.statusKey = statusKey;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	@JsonIgnore
	public void setHttpStatus(HttpStatus status) {
		if (status.is5xxServerError() || status.is4xxClientError() || status.is3xxRedirection()) {
			this.statusKey = status.series().name();
			this.error = status.getReasonPhrase();
		}
		this.status = ArgUtil.parseAsString(status.value());

	}

	/**
	 * Error Type
	 * 
	 * @return
	 */
	public String getError() {
		return error;
	}

	/**
	 * Error Type
	 * 
	 * @return
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Get Exception class of error (Spring Norms)
	 * 
	 * @return
	 */
	public String getException() {
		return exception;
	}

	/**
	 * Set Exception class of error (Spring Norms)
	 * 
	 * @return
	 */
	public void setException(String exception) {
		this.exception = exception;
	}

	/**
	 * Get Exception Message (Spring Norms)
	 * 
	 * @return
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * Set Exception Message (Spring Norms)
	 * 
	 * @param message
	 */
	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	public String getWarningKey() {
		if (this.warningKey == null && ArgUtil.is(this.warnings)) {
			if (this.warnings.size() > 0) {
				this.warningKey = this.warnings.get(0).getCodeKey();
			}
		}
		return this.warningKey;
	}

	public void setWarningKey(String warningKey) {
		this.warningKey = warningKey;
	}

	/**
	 * API url
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * API url
	 * 
	 * @return
	 */
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public M getMeta() {
		return meta;
	}

	@Override
	public void setMeta(M meta) {
		this.meta = meta;
	}

	/**
	 * Gets the errors.
	 *
	 * @return the errors
	 */
	public List<ApiFieldError> getErrors() {
		return errors;
	}

	/**
	 * Sets the errors.
	 *
	 * @param errors the new errors
	 */
	public void setErrors(List<ApiFieldError> errors) {
		this.errors = errors;
	}

	@Override
	public String getMessageKey() {
		return messageKey;
	}

	@Override
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public List<ApiFieldError> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<ApiFieldError> warnings) {
		this.warnings = warnings;
	}

	protected AResponse<M> warnings() {
		if (this.warnings == null) {
			this.warnings = new ArrayList<ApiFieldError>();
		}
		return this;
	}

	protected AResponse<M> logs() {
		if (this.logs == null) {
			this.logs = new ArrayList<String>();
		}
		return this;
	}

	protected AResponse<M> traces() {
		if (this.traces == null) {
			this.traces = new ArrayList<Object>();
		}
		return this;
	}

	public void addWarning(ApiFieldError warning) {
		if (ArgUtil.is(warning)) {
			if (!ArgUtil.is(this.warningKey) && ArgUtil.is(warning.getCodeKey())) {
				this.warningKey = warning.getCodeKey();
			}
			for (ApiFieldError amxWarning : this.warnings().getWarnings()) {
				if (warning.toString().equals(amxWarning.toString())) {
					return;
				}
			}
			this.warnings.add(warning);
		}
	}

	public void addWarning(String warning) {
		ApiFieldError w = new ApiFieldError();
		w.setDescription(warning);
		addWarning(w);
	}

	public void addLog(String log) {
		this.logs().getLogs().add(log);
	}

	public void addTrace(Object log) {
		this.traces().getTraces().add(log);
	}

	public List<String> getLogs() {
		return logs;
	}

	public void setLogs(List<String> logs) {
		this.logs = logs;
	}

	public List<M> getDetails() {
		return details;
	}

	public void setDetails(List<M> details) {
		this.details = details;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}

	protected AResponse<M> param(String paramKey, Object paramValue) {
		if (this.params == null) {
			this.params = new HashMap<String, Object>();
		}
		this.params.put(paramKey, paramValue);
		return this;
	}

	protected AResponse<M> extra(String paramKey, Object paramValue) {
		if (this.extra == null) {
			this.extra = new HashMap<String, Object>();
		}
		this.extra.put(paramKey, paramValue);
		return this;
	}

	public String getServerVersion() {
		return serverVersion;
	}

	public void setServerVersion(String serverVersion) {
		this.serverVersion = serverVersion;
	}

	public List<Object> getTraces() {
		return traces;
	}

	public void setTraces(List<Object> traces) {
		this.traces = traces;
	}

	public ApiPagination getPagination() {
		return pagination;
	}

	public void setPagination(ApiPagination pagination) {
		this.pagination = pagination;
	}

	public ApiPagination pagination() {
		if (this.pagination == null) {
			this.pagination = new ApiPagination();
		}
		return this.pagination;
	}
}
