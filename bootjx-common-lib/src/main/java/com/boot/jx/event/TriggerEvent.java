package com.boot.jx.event;

public class TriggerEvent {

	private String event;
	private String id;
	private String value;

	public TriggerEvent(String event, String id, String value) {
		super();
		this.event = event;
		this.id = id;
		this.value = value;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
