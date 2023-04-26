package com.boot.jx.session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

import com.boot.utils.ArgUtil;

//@Configuration
//@EnableSpringHttpSession
public class SpringHttpSessionConfig {

	@Value("${server.session.cookie.name:JSESSIONID}")
	String cookieName;

	@Value("${server.session.cookie.path:}")
	String cookiePath;

	@Value("${server.session.cookie.http-only:true}")
	boolean useHttpOnlyCookie;

	@Value("${server.session.cookie.domain:}")
	String domainNamePattern;

	@Value("${server.session.cookie.secure:true}")
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
