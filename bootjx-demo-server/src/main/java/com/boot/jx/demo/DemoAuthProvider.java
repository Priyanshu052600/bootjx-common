package com.boot.jx.demo;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.boot.utils.ArgUtil;

@Component
public class DemoAuthProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {

		final String username = auth.getName();
		if (ArgUtil.isEmpty(username)) {
			throw new BadCredentialsException("invalid login details");
		}

		String password = null;
		if (auth.getCredentials() != null) {
			password = auth.getCredentials().toString();
		} else if (ArgUtil.isEmpty(password)) {
			throw new BadCredentialsException("invalid login details");
		}

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password,
				Collections.emptyList());
		token.setDetails(auth.getDetails());
		return token;
//		return auth;
	}

	@Override
	public boolean supports(Class<?> auth) {
		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}

}
