package com.boot.jx.logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.boot.jx.AppContextUtil;
import com.boot.utils.EnumType;

public class AuditData<T extends AuditData<T>> implements Serializable {

	private static final long serialVersionUID = -5071113192606436075L;

	public static enum Type implements EnumType {
		TRACE, TRANX, CUSTOM
	}

	private String context;
	private long timestamp;
	private Map<String, Object> data;
	protected Type type;

	public AuditData(String context) {
		super();
		this.context = context;
		this.timestamp = System.currentTimeMillis();
		this.data = new HashMap<String, Object>();
		this.type = Type.CUSTOM;
	}

	@SuppressWarnings("unchecked")
	public T put(String key, Object data) {
		this.data.put(key, data);
		return (T) this;
	}

	public static class TraceAuditData extends AuditData<TraceAuditData> {

		private static final long serialVersionUID = 8715268507886242466L;
		public String traceId;

		public TraceAuditData(String context) {
			super(context);
			this.type = Type.TRACE;
			this.traceId = AppContextUtil.getTraceId();
		}
	}

	public static class FlowAuditData extends AuditData<FlowAuditData> {

		private static final long serialVersionUID = -6230592496360253825L;
		public String tranxId;

		public FlowAuditData(String context) {
			super(context);
			this.type = Type.TRANX;
			this.tranxId = AppContextUtil.getTranxId();
		}
	}
}
