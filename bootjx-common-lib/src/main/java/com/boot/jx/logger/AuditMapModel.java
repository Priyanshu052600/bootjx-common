package com.boot.jx.logger;

import java.math.BigDecimal;
import java.util.Map;

import com.boot.model.MapModel;
import com.boot.utils.JsonPath;

public class AuditMapModel extends MapModel {

	private static final JsonPath TYPE = new JsonPath("type");
	private static final JsonPath DESC = new JsonPath("desc");
	private static final JsonPath TARGET_ID = new JsonPath("targetId");
	private static final JsonPath CUSTOMER_ID = new JsonPath("cust/id");
	private static final JsonPath CLIENT_IP = new JsonPath("client/ip");
	private static final JsonPath CLIENT_FP = new JsonPath("client/fp");
	private static final JsonPath CLIENT_CT = new JsonPath("client/ct");
	private static final JsonPath AGENT_OS = new JsonPath("agent/operatingSystem");
	private static final JsonPath AGENT_BROWSER = new JsonPath("agent/browser");

	public AuditMapModel(Map<String, Object> event) {
		super(event);
	}

	public String getType() {
		return TYPE.load(this.map, null);
	}

	public String getDescription() {
		return DESC.load(this.map, null);
	}

	public BigDecimal getTargetId() {
		return this.entry(TARGET_ID).asBigDecimal();
	}

	public BigDecimal getCustomerId() {
		return this.entry(CUSTOMER_ID).asBigDecimal();
	}

	public String getClientIp() {
		return this.entry(CLIENT_IP).asString();
	}

	public String getClientFp() {
		return this.entry(CLIENT_FP).asString();
	}

	public String getClientType() {
		return this.entry(CLIENT_CT).asString();
	}

	public String getClientOs() {
		return this.entry(AGENT_OS).asString();
	}

	public String getClientBrowser() {
		return this.entry(AGENT_BROWSER).asString();
	}
}