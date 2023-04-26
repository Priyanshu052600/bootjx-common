package com.boot.jx.drools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.aop.framework.AopProxyUtils;

import com.boot.utils.ArgUtil;
import com.boot.utils.EnumType;
import com.boot.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DEvent implements Serializable {

	public static enum Types implements EnumType {
		BEFORE, ON, AFTER
	}

	public static enum State {
		TRIGGERED, SENT, RECEIVED, PROCESSED, COMPLETED
	}

	public static enum Status {
		CREATED, SUCCESS, SKIPPED, FAILED
	}


	
	
	private static final long serialVersionUID = -5524423920614420872L;

	private Map<String, Object> data;
	private Map<String, DAction> actions;
	private State state;
	private Status status;

	private String eventName;
	private Types eventType;

	@JsonIgnore
	private Map<String, Object> localData;

	public DEvent() {
		super();
		this.state = State.TRIGGERED;
		this.status = Status.CREATED;
		this.data = new HashMap<String, Object>();
		this.localData = new HashMap<String, Object>();
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public DEvent data(String key, Object value) {
		this.data.put(key, value);
		return this;
	}

	public DEvent local(String key, Object value) {
		this.localData.put(key, value);
		return this;
	}

	public DEvent local(Object value) {
		Class<?> c = AopProxyUtils.ultimateTargetClass(value);
		return local(c.getName(), value);
	}

	@SuppressWarnings("unchecked")
	public <T> T readLocal(Class<T> c) {
		return (T) localData.get(c.getName());
	}

	@SuppressWarnings("unchecked")
	public <T> T readLocal(String key) {
		return (T) localData.get(key);
	}

	public void action(String action, String... args) {
		if (ArgUtil.isEmpty(this.actions)) {
			this.actions = new HashMap<String, DAction>();
		}
		DAction dAction = this.actions.get(action);

		if (!ArgUtil.is(dAction)) {
			dAction = new DAction();
			dAction.setAction(action);
			this.actions.put(action, dAction);
		}
		Map<String, Object> tempOptions = new HashMap<String, Object>();
		for (String arg : args) {
			String[] x = StringUtils.split(arg, "=");
			if (x.length > 1) {
				dAction.getOptions().put(x[0], x[1]);
				tempOptions.put(x[0], x[1]);
			} else if (x.length > 0) {
				dAction.getOptions().put(x[0], true);
				tempOptions.put(x[0], true);
			}

		}
		dAction.getRules().add(tempOptions);

	}

	public Map<String, DAction> getActions() {
		return actions;
	}

	public void setActions(Map<String, DAction> actions) {
		this.actions = actions;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Types getEventType() {
		return eventType;
	}

	public void setEventType(Types eventType) {
		this.eventType = eventType;
	}

	@JsonIgnore
	public boolean is(String eventName) {
		return ArgUtil.areEqual(this.eventName, eventName);
	}

	@JsonIgnore
	public boolean is(Types eventType) {
		return ArgUtil.areEqual(this.eventType, eventType);
	}

}
