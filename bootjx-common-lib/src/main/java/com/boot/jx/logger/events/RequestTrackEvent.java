package com.boot.jx.logger.events;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import com.boot.jx.AppConstants;
import com.boot.jx.AppContext;
import com.boot.jx.exception.ApiHttpExceptions.ApiStatusCodes;
import com.boot.jx.logger.AbstractEvent;
import com.boot.jx.logger.AuditEvent;
import com.boot.jx.tunnel.TunnelEventXchange;
import com.boot.jx.tunnel.TunnelMessage;
import com.boot.model.UtilityModels.JsonObject;
import com.boot.utils.ArgUtil;
import com.boot.utils.ContextUtil;
import com.boot.utils.HttpUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ AuditEvent.PROP_DESC, "status", AuditEvent.PROP_MSG, AbstractEvent.PROP_TYPE,
		AuditEvent.PROP_RESULT, AbstractEvent.PROP_TIMSTAMP })
public class RequestTrackEvent extends AuditEvent<RequestTrackEvent> implements JsonObject {

	private static final long serialVersionUID = -8735500343787196557L;
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestTrackEvent.class);

	public static enum Type implements EventType {
		REQT_IN(">="), RESP_OUT("<="), REQT_OUT("=>"), RESP_IN("=<"), PUB_OUT("P="), SUB_IN("S="), HTTP_IN("≡>"),
		HTTP_OUT(">≡");

		String shortCode;

		Type() {
			this.shortCode = this.name();
		}

		Type(String shortcode) {
			this.shortCode = shortcode;
		}

		public String toString() {
			return this.shortCode;
		}

		@Override
		public EventMarker marker() {
			return null;
		}
	}

	@JsonIgnore
	private AppContext context;

	@JsonProperty("rspTym")
	private Long responseTime;

	private String ip;
	private String status;
	private String uri;

	private Map<String, String> topic;

	public RequestTrackEvent(Type type) {
		super(type);
	}

	public <T> RequestTrackEvent(Type type, TunnelEventXchange xchange, TunnelMessage<T> message) {
		super(type);
		this.topic = new HashMap<String, String>();
		this.description = String.format("%s %s", this.type, xchange.getTopic(message.getTopic()));
		topic.put("id", message.getId());
		topic.put("name", message.getTopic());
		this.context = message.getContext();
	}

	public RequestTrackEvent(HttpRequest request) {
		super(Type.REQT_OUT);
		this.track(request);
	}

	public RequestTrackEvent(ClientHttpResponse response, HttpRequest request) {
		super(Type.RESP_IN);
		this.track(response, request.getURI());
	}

	public RequestTrackEvent(HttpServletRequest request) {
		super(Type.REQT_IN);
		this.track(request);
	}

	public RequestTrackEvent(HttpServletResponse response, HttpServletRequest request) {
		super(Type.RESP_OUT);
		this.track(response, request);
	}

	public RequestTrackEvent(HttpServletResponse resp, HttpServletRequest req, long responseTime) {
		this(resp, req);
		this.responseTime = responseTime;
	}

	/**
	 * 
	 * FOR RESP_OUT Event;
	 * 
	 * @param response
	 * @param request
	 * @return
	 */
	@Deprecated
	public RequestTrackEvent track(HttpServletResponse response, HttpServletRequest request) {
		this.uri = request.getRequestURI();
		this.description = String.format("%s%s=%s", this.type, response.getStatus(), this.uri);

		ApiAuditEvent apiEventObject = (ApiAuditEvent) ContextUtil.map().getOrDefault("api_event", null);

		if (!ArgUtil.isEmpty(apiEventObject)) {
			this.result = apiEventObject.getResult();
			this.message = apiEventObject.getMessage();
			this.details = apiEventObject.getDetails();
			this.errorCode = apiEventObject.getErrorCode();
			this.exception = apiEventObject.getException();
			this.exceptionType = apiEventObject.getExceptionType();
			this.resolveResult();
		}

		return this;
	}

	@Deprecated
	public RequestTrackEvent track(HttpServletRequest request) {
		this.uri = request.getRequestURI();
		this.description = String.format("%s%s=%s", this.type, request.getMethod(), this.uri);
		this.ip = HttpUtils.getIPAddress(request);
		return this;
	}

	@Deprecated
	public RequestTrackEvent track(HttpRequest request) {
		this.uri = ArgUtil.parseAsString(request.getURI());
		this.description = String.format("%s%s=%s", this.type, request.getMethod(), this.uri);
		return this;
	}

	@Deprecated
	public RequestTrackEvent track(ClientHttpResponse response, URI uri) {
		this.uri = ArgUtil.parseAsString(uri);
		String statusCode = "000";

		try {
			String exceptionType = response.getHeaders().getFirst(AppConstants.EXCEPTION_HEADER_KEY);
			String errorCode = response.getHeaders().getFirst(AppConstants.EXCEPTION_HEADER_CODE_KEY);

			if (!ArgUtil.isEmpty(exceptionType)) {
				this.result = Result.ERROR;
				if (ArgUtil.is(errorCode)) {
					this.errorCode = errorCode;
				} else {
					this.exceptionType = exceptionType;
				}
			}

		} catch (Exception e) {
			LOGGER.error("RequestTrackEvent.track while fetching header in", e);
		}

		try {
			HttpStatus status = response.getStatusCode();
			statusCode = ArgUtil.parseAsString(status);
			if (ArgUtil.is(status) && (status.is5xxServerError() || status.is4xxClientError())) {
				this.result = Result.ERROR;
			}

		} catch (IOException e) {
			LOGGER.error("RequestTrackEvent.track while logging response in", e);
			this.description = String.format("%s%s=%s", this.type, "EXCEPTION", this.uri);
		}

		this.description = String.format("%s%s=%s", this.type, statusCode, this.uri);
		return this;
	}

	private void resolveResult() {
		if (ArgUtil.is(this.exceptionType)
				&& this.exceptionType.equals("org.springframework.web.client.ResourceAccessException")) {
			if (ArgUtil.is(this.exceptionType) && this.exception.contains("Connection timed out")) {
				this.result = Result.TIMEOUT;
			}
		}
	}

	public Long getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Long responseTime) {
		this.responseTime = responseTime;
	}

	public AppContext getContext() {
		return context;
	}

	public void setContext(AppContext context) {
		this.context = context;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public RequestTrackEvent debug(boolean isDebugOnly) {
		this.debugEvent = isDebugOnly;
		return this;
	}

	public RequestTrackEvent responseTime(long responseTime) {
		this.responseTime = responseTime;
		return this;
	}

	public void clean() {
	}

	public Map<String, String> getTopic() {
		return topic;
	}

	public void setTopic(Map<String, String> topic) {
		this.topic = topic;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	// Ignore Porps
	@Override
	public String getCategory() {
		return null;
	}

	@Override
	public String getComponent() {
		return null;
	}

	@Override
	public String getFlow() {
		if (ArgUtil.areEqual(this.flow, this.uri)) {
			return null;
		}
		return this.flow;
	}

	public RequestTrackEvent inbound(HttpServletResponse response, HttpServletRequest request) {
		this.uri = request.getRequestURI();
		this.description = String.format("%s%s=%s", this.type, request.getMethod(), this.uri);
		this.ip = HttpUtils.getIPAddress(request);
		this.status = ArgUtil.parseAsString(response.getStatus());

		ApiAuditEvent apiEventObject = (ApiAuditEvent) ContextUtil.map().getOrDefault("api_event", null);
		if (!ArgUtil.isEmpty(apiEventObject)) {
			this.result = apiEventObject.getResult();
			this.message = apiEventObject.getMessage();
			this.details = apiEventObject.getDetails();
			this.errorCode = apiEventObject.getErrorCode();
			this.exception = apiEventObject.getException();
			this.exceptionType = apiEventObject.getExceptionType();
			this.resolveResult();
		}

		return this;
	}

	public RequestTrackEvent outbound(ClientHttpResponse response, HttpRequest request) {
		this.uri = ArgUtil.parseAsString(request.getURI());
		this.description = String.format("%s%s=%s", this.type, request.getMethod(), this.uri);

		this.status = "000";

		try {
			String exceptionType = response.getHeaders().getFirst(AppConstants.EXCEPTION_HEADER_KEY);
			String errorCode = response.getHeaders().getFirst(AppConstants.EXCEPTION_HEADER_CODE_KEY);

			if (!ArgUtil.isEmpty(exceptionType)) {
				this.result = Result.ERROR;
				if (ArgUtil.is(errorCode)) {
					this.errorCode = errorCode;
				} else {
					this.exceptionType = exceptionType;
				}
			}

		} catch (Exception e) {
			LOGGER.error("RequestTrackEvent.track while fetching header in", e);
		}

		try {
			HttpStatus status = ApiStatusCodes.getHttpStatus(response);
			this.status = ArgUtil.parseAsString(status);
			if (ArgUtil.is(status) && (status.is5xxServerError() || status.is4xxClientError())) {
				this.result = Result.ERROR;
			}
		} catch (IOException e) {
			LOGGER.error("RequestTrackEvent.track while logging response in", e);
		}

		return this;
	}

	@Override
	public Object jsonObject() {
		this.setCategory(null);
		this.setResult(Result.DONE.equals(this.result) ? null : this.result);
		if (Type.REQT_IN.equals(this.type) || Type.REQT_OUT.equals(this.type)) {
			RequestTrackEvent re = new RequestTrackEvent((Type) this.type);
			re.setResult(null);
			re.setCategory(null);
			re.setTimestamp(this.timestamp);
			re.setDescription(this.description);
			return re;
		} else if (Type.RESP_IN.equals(this.type)) {
			RequestTrackEvent re = new RequestTrackEvent((Type) this.type);
			re.setResult(null);
			re.setCategory(null);
			re.setTimestamp(this.timestamp);
			re.setDescription(this.description);
			return re;
		}
		return this;
	}

}
