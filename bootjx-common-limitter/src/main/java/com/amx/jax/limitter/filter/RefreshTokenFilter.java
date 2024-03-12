package com.amx.jax.limitter.filter;

import java.io.IOException;
import java.security.Principal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.amx.jax.limitter.RateLimitConfig;
import com.amx.jax.limitter.RefreshTokenData;
import com.amx.jax.limitter.RefreshTokenManager;
import com.amx.jax.limitter.constant.ApiPath;
import com.amx.jax.limitter.exp.LimitterError;
import com.amx.jax.limitter.model.AppData;
import com.amx.jax.limitter.util.FilterUtil;
import com.boot.jx.AppConfig;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RefreshTokenFilter implements Filter {

    @Autowired
    private RateLimitConfig rateLimitConfig;

    @Autowired
    private RefreshTokenData refreshTokenData;

    @Autowired
    private RefreshTokenManager refreshTokenManager;

    @Autowired
    private AppConfig appConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        boolean isApplicable = isApplicable(req);

        if (rateLimitConfig.getRefreshTokenEnable() && isApplicable) {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) resp;
            Principal principal = request.getUserPrincipal();
            boolean isLoggedIn = (principal != null);
            String errorMessage = null;
            String acceptHeader = request.getHeader("Accept");
            if (refreshTokenData != null && isLoggedIn) {
                Cookie tokenCookie = WebUtils.getCookie(request, RefreshTokenManager.SESSION_TOKEN_COOKIE);
                if (refreshTokenData.getSac() == null) {
                    errorMessage = "Session token cookie not generated";
                }
                // validate that session token cookie is present and valid
                else if (tokenCookie == null) {
                    errorMessage = "Session token cookie not present";
                } else {
                    String sessionToken = tokenCookie.getValue();
                    if (!refreshTokenManager.isValidSessionToken(sessionToken, principal)) {
                        errorMessage = "Session token invalid or expired";
                    }
                }
                if (errorMessage != null) {
                    FilterUtil.setErrorResponse(request, response, LimitterError.INVALID_SESSION_TOKEN.name(),
                            errorMessage);
                    AppData appData = new AppData();
                    if (StringUtils.isNotBlank(appConfig.getLogoutPath())) {
                        appData.setLogoutPath(appConfig.getLogoutPath());
                    } else {
                        appData.setLogoutPath("#");
                    }
                    request.setAttribute("appData", appData);
                    return;
                }
            }

        }
        chain.doFilter(req, resp);
    }

    private boolean isApplicable(ServletRequest req) {
        HttpServletRequest request = (HttpServletRequest) req;
        boolean isApplicable = true;
        String path = request.getRequestURI();
        if (StringUtils.isNotBlank(path)) {
            if (isLogout(request)) {
                isApplicable = false;
            }
            if (path.contains(ApiPath.REFRESH_TOKEN)) {
                isApplicable = false;
            }
        }
        // skip check for very recent generated token . This is to circumvent javascript
        // sync issue in UI
        if (refreshTokenManager.tokenGenerationTimeCheck()) {
            isApplicable = false;
        }
        return isApplicable;
    }

    private boolean isLogout(ServletRequest req) {
        HttpServletRequest request = (HttpServletRequest) req;
        String path = request.getRequestURI();
        if (StringUtils.isNotBlank(path)) {
            if (path.contains("logout")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

}
