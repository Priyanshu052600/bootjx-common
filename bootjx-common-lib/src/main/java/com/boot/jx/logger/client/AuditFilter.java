package com.boot.jx.logger.client;

import com.boot.jx.logger.AuditEvent;

public interface AuditFilter<T extends AuditEvent<?>> {
	public void doFilter(T event);
}
