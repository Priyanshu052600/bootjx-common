package com.boot.jx.tunnel;

import java.io.Serializable;
import java.util.Map;

import com.boot.jx.AppContext;
import com.boot.utils.ArgUtil;
import com.boot.utils.JsonUtil;
import com.boot.utils.UniqueID;

public class TunnelMessage<M> implements Serializable {

	private static final long serialVersionUID = -638169239630153108L;

	long timestamp;
	String id;
	String topic;
	String appType;

	AppContext context;

	Map<String, Object> contextMap;

	M data;

	public TunnelMessage(M data) {
		this.id = UniqueID.generateString();
		this.timestamp = System.currentTimeMillis();
		this.data = data;
	}

	public TunnelMessage(M data, AppContext contextModel) {
		this(data);
		this.context = null;
		this.contextMap = JsonUtil.toJsonMap(contextModel);
	}

	public TunnelMessage() {
		this(null);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public M getData() {
		return data;
	}

	public void setData(M data) {
		this.data = data;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public AppContext getContext() {
		return ArgUtil.is(context) ? context : this.context();
	}

	public void setContext(AppContext context) {
		this.contextMap = JsonUtil.toJsonMap(context);
	}

	private AppContext context() {
		return JsonUtil.toObject(contextMap, AppContext.class);
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

}
