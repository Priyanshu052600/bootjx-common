package com.boot.jx.session.bc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.server.Session;

public interface HttpSessionStrategy {

	String getRequestedSessionId(HttpServletRequest request);

	void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response);

	void onInvalidateSession(HttpServletRequest request, HttpServletResponse response);

}
