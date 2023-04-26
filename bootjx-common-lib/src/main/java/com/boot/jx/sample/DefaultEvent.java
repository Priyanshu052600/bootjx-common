package com.boot.jx.sample;

import com.boot.jx.logger.AuditEvent;
import com.boot.utils.EnumType;

public class DefaultEvent extends AuditEvent<DefaultEvent> {

    private static final long serialVersionUID = 8827531092425201809L;

    public static enum Type implements EnumType {
	DEFAULT_EVENT;

	public static final EnumType DEFAULT = DEFAULT_EVENT;
    }

}
