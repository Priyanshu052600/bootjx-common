package com.boot.jx.sample;

import org.springframework.stereotype.Component;

import com.boot.jx.logger.client.AuditFilter;

@Component
public class DefaultAuditFilter implements AuditFilter<DefaultEvent> {

	@Override
	public void doFilter(DefaultEvent event) {
		
	}


}
