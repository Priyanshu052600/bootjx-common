package com.boot.jx.session.custom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.boot.jx.session.bc.CookieHttpSessionStrategy;
import com.boot.jx.session.bc.DefaultCookieSerializer;
import com.boot.jx.session.bc.HeaderHttpSessionStrategy;
import com.boot.jx.session.bc.HttpSessionStrategy;
import com.boot.jx.session.bc.MapSessionRepository;
import com.boot.utils.ArgUtil;

//@Configuration
//@EnableSpringHttpSession
public class SpringHttpSessionConfig {

	@Value("${server.servlet.session.cookie.name:JSESSIONID}")
	String cookieName;

	@Value("${server.servlet.session.cookie.path:}")
	String cookiePath;

	@Value("${server.servlet.session.cookie.http-only:true}")
	boolean useHttpOnlyCookie;

	@Value("${server.servlet.session.cookie.domain:}")
	String domainNamePattern;

	@Value("${server.servlet.session.cookie.secure:true}")
	boolean useSecureCookie;

	@Bean
	public MapSessionRepository sessionRepository() {
		return new MapSessionRepository();
	}

	@Bean
	public HttpSessionStrategy httpSessionStrategy() {
		HeaderHttpSessionStrategy headerSession = new HeaderHttpSessionStrategy();
		CookieHttpSessionStrategy cookieSession = new CookieHttpSessionStrategy();

		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		if (ArgUtil.is(cookieName))
			serializer.setCookieName(cookieName);
		if (ArgUtil.is(cookiePath))
			serializer.setCookiePath(cookiePath);
		if (ArgUtil.is(domainNamePattern))
			serializer.setDomainNamePattern(domainNamePattern);
		serializer.setUseHttpOnlyCookie(useHttpOnlyCookie);
		serializer.setUseSecureCookie(useSecureCookie);

		cookieSession.setCookieSerializer(serializer);
		headerSession.setHeaderName("x-auth-token");
		return new SmartHttpSessionStrategy(cookieSession, headerSession);
	}
}
