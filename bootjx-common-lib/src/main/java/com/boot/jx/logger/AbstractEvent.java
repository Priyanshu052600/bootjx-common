package com.boot.jx.logger;

import java.io.Serializable;

import com.boot.utils.ArgUtil;
import com.boot.utils.EnumType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractEvent implements Serializable {

	public static final String PROP_TRC_ID = "trcId";
	public static final String PROP_TRX_ID = "trxId";
	public static final String PROP_CATG = "catg";
	public static final String PROP_COMPONENT = "ms";
	public static final String PROP_TIMSTAMP = "ts";
	public static final String PROP_TYPE = "type";
	public static final String PROP_FLOW = "flow";
	private static final long serialVersionUID = -3042991299608634785L;

	public enum EventMarker implements EnumType {

		/**
		 * In case some alert is required SOS
		 */
		ALERT,

		/**
		 * Failure cases, usually for developers
		 */
		EXCEP,

		/**
		 * Event Important from business point of view, these event will be saved in
		 * DataBase
		 */
		AUDIT,

		/**
		 * Just information logs, NO DB, these are logs which can become candidate for
		 * AUDIT
		 */
		NOTICE,

		/**
		 * Events meant to be logged from method or event performance evaluation
		 * perspective
		 * 
		 */
		GAUGE,

		/**
		 * Events from tracking perspective, for example request/response coming/going
		 * in/out to/from service or
		 */
		TRACK,

		/**
		 * Application Performance logs
		 * 
		 */
		METER

	}

	public interface EventType extends EnumType {
		EventMarker marker();
	}

	@JsonProperty(PROP_COMPONENT)
	protected String component;

	@JsonProperty(PROP_CATG)
	protected String category = getClass().getSimpleName();

	@JsonProperty(PROP_TYPE)
	protected EventType type;

	@JsonProperty(PROP_TIMSTAMP)
	protected long timestamp;

	@JsonProperty(PROP_TRC_ID)
	protected String traceId;

	@JsonProperty(PROP_TRX_ID)
	protected String tranxId;

	@JsonProperty(PROP_FLOW)
	protected String flow;

	public AbstractEvent() {
		this.timestamp = System.currentTimeMillis();
		this.isCatureClient = true;
	}

	public AbstractEvent(EventType type) {
		this();
		this.type = type;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	@JsonIgnore
	public EventMarker getTypeMarker() {
		return ArgUtil.isEmpty(this.type) ? null : this.type.marker();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	abstract public void clean();

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

	@JsonIgnore
	protected boolean debugEvent;

	@JsonIgnore
	public boolean isDebugEvent() {
		return debugEvent;
	}

	@JsonIgnore
	public void setDebugEvent(boolean debugEvent) {
		this.debugEvent = debugEvent;
	}

	@JsonIgnore
	protected boolean isCatureClient;

	@JsonIgnore
	public boolean isCatureClient() {
		return isCatureClient;
	}

	@JsonIgnore
	public void setCatureClient(boolean isCatureClient) {
		this.isCatureClient = isCatureClient;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

}
