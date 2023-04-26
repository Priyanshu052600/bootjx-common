package com.boot.jx.rbac.session;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

public class SessionConfigInitializer extends AbstractHttpSessionApplicationInitializer {
	public SessionConfigInitializer() {
		super(RedisSessionConfig.class);
	}

}
