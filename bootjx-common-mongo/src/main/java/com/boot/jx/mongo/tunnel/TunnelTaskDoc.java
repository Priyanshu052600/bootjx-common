package com.boot.jx.mongo.tunnel;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.boot.jx.mongo.CommonDocInterfaces.IDocument;
import com.boot.jx.mongo.CommonDocInterfaces.TimeStampIndex.TimeStampDoc;

@Document(collection = "SYS_TASK_HANDLER")
@TypeAlias("TunnelTaskDoc")
public class TunnelTaskDoc extends TimeStampDoc implements IDocument {

	@Id
	private String id;

	private String topic;
	private String platform;

	private String service;

	private boolean active;

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

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
