package com.boot.jx.logger;

import com.boot.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysAuditEvent extends AuditEvent<SysAuditEvent> {

	private static final long serialVersionUID = 340515142629212154L;

	public static enum Type implements EventType {

		REDIS_CONNECTIVITY(EventMarker.ALERT),

		DEFAULT;

		EventMarker marker;

		@Override
		public EventMarker marker() {
			return this.marker;
		}

		Type() {
			this.marker = EventMarker.AUDIT;
		}

		Type(EventMarker marker) {
			this.marker = marker;
		}
	}

	public SysAuditEvent(Type type) {
		super(type);
	}

	@Override
	public String getDescription() {
		return this.type + ":" + this.result;
	}

	public static String getMergedString(String oldStr, String newStr) {
		if (!ArgUtil.isEmpty(newStr)) {
			oldStr = ArgUtil.isEmpty(oldStr) ? newStr
					: String.format("%s;%s", oldStr, newStr);
		}
		return oldStr;
	}

}
