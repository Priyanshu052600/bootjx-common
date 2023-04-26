package com.boot.jx;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.boot.jx.dict.UserClient.UserDeviceClient;
import com.boot.jx.http.CommonHttpRequest.ApiRequestDetail;
import com.boot.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppContext implements Serializable {

	private static final long serialVersionUID = -6073379040253976816L;

	String tenant = null;
	String traceId = null;
	String contextId = null;
	String tranxId = null;
	String actorId = null;
	UserDeviceClient client;
	String apiRequestDetail;
	Map<String, Object> params = new HashMap<String, Object>();

	long traceTime = 0L;
	long tranxTime = 0L;

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getTranxId() {
		return tranxId;
	}

	public void setTranxId(String tranxId) {
		this.tranxId = tranxId;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public long getTraceTime() {
		return traceTime;
	}

	public void setTraceTime(long traceTime) {
		this.traceTime = traceTime;
	}

	public long getTranxTime() {
		return tranxTime;
	}

	public void setTranxTime(long tranxTime) {
		this.tranxTime = tranxTime;
	}

	public UserDeviceClient getClient() {
		return client;
	}

	public void setClient(UserDeviceClient client) {
		this.client = client;
	}

	/**
	 * Used for auth params
	 * 
	 * @return
	 */
	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public ApiRequestDetail apiRequestDetail() {
		return JsonUtil.fromJson(apiRequestDetail, ApiRequestDetail.class);
	}

	public void apiRequestDetail(ApiRequestDetail apiRequestDetail) {
		this.apiRequestDetail = JsonUtil.toJson(apiRequestDetail);
	}

	public String getApiRequestDetail() {
		return apiRequestDetail;
	}

	public void setApiRequestDetail(String apiRequestDetail) {
		this.apiRequestDetail = apiRequestDetail;
	}

	public void header(String key, Object value) {
		if (this.params == null) {
			this.params = new HashMap<String, Object>();
		}
		this.params.put(key, value);
	}

	public Object header(String key) {
		if (this.params == null) {
			this.params = new HashMap<String, Object>();
		}
		return this.params.get(key);
	}
}
