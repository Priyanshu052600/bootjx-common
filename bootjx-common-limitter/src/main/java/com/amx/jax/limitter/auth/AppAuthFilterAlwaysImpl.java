package com.amx.jax.limitter.auth;

import java.io.IOException;
import java.security.Principal;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.WebUtils;

import com.amx.jax.limitter.RateLimitConfig;
import com.amx.jax.limitter.RefreshTokenData;
import com.amx.jax.limitter.RefreshTokenManager;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.http.CommonHttpRequest.ApiRequestDetail;
import com.boot.jx.rest.AppRequestInterfaces.AppAuthFilterAlways;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@Component
public class AppAuthFilterAlwaysImpl implements AppAuthFilterAlways {

    @Autowired
    private RateLimitConfig rateLimitConfig;

    @Autowired
    private RefreshTokenData refreshTokenData;

    @Autowired
    private RefreshTokenManager refreshTokenManager;

    @Override
    public boolean filterAppRequest(ApiRequestDetail apiRequest, CommonHttpRequest req, String traceId) {
        boolean isApplicable = true;
        if (CollectionUtils.isNotEmpty(apiRequest.getRules())) {
            if (apiRequest.getRules().contains("BYPASS_SESSION_TOKEN")) {
                isApplicable = false;
            }
        }
        if (rateLimitConfig.getRefreshTokenEnable() && isApplicable) {

            HttpServletRequest request = req.getRequest();
            HttpServletResponse response = req.getResponse();
            Principal principal = request.getUserPrincipal();
            boolean isLoggedIn = (principal != null);
            if (refreshTokenData != null && isLoggedIn) {
                try {
                    if (refreshTokenData.getSac() == null) {

                        response.sendError(HttpStatus.FORBIDDEN.value(), "Session token cookie not generated");
                        return false;
                    }
                    // validate that session token cookie is present and valid
                    Cookie tokenCookie = WebUtils.getCookie(request, RefreshTokenManager.SESSION_TOKEN_COOKIE);
                    if (tokenCookie == null) {
                        response.sendError(HttpStatus.FORBIDDEN.value(), "Session token cookie not present");
                        return false;
                    }
                    String sessionToken = tokenCookie.getValue();
                    if (!refreshTokenManager.isValidSessionToken(sessionToken)) {
                        response.sendError(HttpStatus.FORBIDDEN.value(),
                                "Session token invalid or expired, try logging out");
                        return false;
                    }
                } catch (IOException e) {

                    return false;
                }
            }
        }
        return true;
    }

}
