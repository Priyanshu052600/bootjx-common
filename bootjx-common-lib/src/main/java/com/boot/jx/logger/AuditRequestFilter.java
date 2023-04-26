package com.boot.jx.logger;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.boot.jx.AppContextUtil;
import com.boot.utils.ArgUtil;

/**
 * The Class WebAuthFilter.
 */
@Component
@Order(AuditRequestFilter.AUDIT_PRECEDENCE)
public class AuditRequestFilter implements Filter {

	public static final int AUDIT_PRECEDENCE = Ordered.HIGHEST_PRECEDENCE - 1;

	@Autowired(required = false)
	private AuditDetailProvider auditDetailProvider;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// empty
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		if (auditDetailProvider != null && !ArgUtil.is(AppContextUtil.getActorId())) {
			AppContextUtil.setActorId(auditDetailProvider.getAuditUser());
		}
		chain.doFilter(req, resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// empty
	}
}
