package com.boot.jx.mongo.logger;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.boot.jx.tunnel.ZQueueDefs.ZQueueElement;

@Document(collection = QueueElementDoc.COLLECTION_NAME)
@TypeAlias("QueueElement")
public class QueueElementDoc implements Serializable, ZQueueElement {

	private static final long serialVersionUID = -1916969779141145310L;

	public static final String COLLECTION_NAME = "Z_QUEUE";

	@Id
	private String docId;

	@Indexed
	private String queueType;

	@Indexed
	private String queueId;

	@Indexed
	private String itemId;

	@Indexed
	private long itemOrder;

	@Indexed
	private String appVenv;

	@Indexed
	private String appType;

	@Indexed
	private String batchId;

	@Indexed
	private long timestamp;

	private Map<String, Object> item;

	@Override
	public String getQueueType() {
		return queueType;
	}

	@Override
	public void setQueueType(String queueType) {
		this.queueType = queueType;
	}

	@Override
	public String getQueueId() {
		return queueId;
	}

	@Override
	public void setQueueId(String queueId) {
		this.queueId = queueId;
	}

	@Override
	public String getItemId() {
		return itemId;
	}

	@Override
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@Override
	public long getItemOrder() {
		return itemOrder;
	}

	@Override
	public void setItemOrder(long itemOrder) {
		this.itemOrder = itemOrder;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public Map<String, Object> getItem() {
		return item;
	}

	@Override
	public void setItem(Map<String, Object> item) {
		this.item = item;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getAppVenv() {
		return appVenv;
	}

	public void setAppVenv(String appVenv) {
		this.appVenv = appVenv;
	}

}
