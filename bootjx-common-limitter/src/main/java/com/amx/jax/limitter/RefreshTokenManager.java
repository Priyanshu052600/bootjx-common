package com.amx.jax.limitter;

import java.security.Principal;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.amx.jax.limitter.exp.LimitterError;
import com.amx.jax.limitter.exp.LimitterException;
import com.amx.jax.limitter.model.RefreshTokenRequest;
import com.amx.jax.limitter.model.RefreshTokenResponse;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.utils.CryptoUtil;
import com.boot.utils.CryptoUtil.HashBuilder;
import com.boot.utils.JsonUtil;
import com.boot.utils.Random;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class RefreshTokenManager {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenManager.class);

    @Autowired
    private RateLimitConfig config;

    @Autowired
    private RefreshTokenData refreshTokenData;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private CommonHttpRequest httpService;

    public static final String SESSION_TOKEN_COOKIE = "SESSION_TOKEN_COOKIE";

    // in millis
    public static final long REFRESH_TOKEN_GENERATION_TIME_CHECK = 2 * 1000;

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request, HttpServletResponse httpServletResponse) {
        setTokenFromCookie(request);
        validateRequest(request);
        validateCurrentToken(request);
        String newToken = generateNewToken();
        // 5 sec of additional tolerance
        Integer tokenRetryTime = config.getRefreshTokenInterval() - 5;
        // set cookie
        Cookie sessionTokenCookie = new Cookie(RefreshTokenManager.SESSION_TOKEN_COOKIE, newToken);
        sessionTokenCookie.setMaxAge(config.getRefreshTokenInterval());
        sessionTokenCookie.setPath("/");
        sessionTokenCookie.setHttpOnly(false);
        httpServletResponse.addCookie(sessionTokenCookie);
        return new RefreshTokenResponse(newToken, tokenRetryTime);
    }

    private void setTokenFromCookie(RefreshTokenRequest request) {
        Cookie tokenCookie = WebUtils.getCookie(httpServletRequest, RefreshTokenManager.SESSION_TOKEN_COOKIE);
        if (tokenCookie != null && request.getToken() == null) {
            // set token from cookie
            request.setToken(tokenCookie.getValue());
        }
    }

    private void validateCurrentToken(RefreshTokenRequest request) {
        if (refreshTokenData.getSac() != null) {
            if (!isValidSessionToken(request.getToken())) {
                log.error("Invalid token in the request: {}", JsonUtil.toJson(request));
                invalidateSession();
                throw new LimitterException(LimitterError.INVALID_SESSION_TOKEN, "Invalid session token");
            }
        }
    }

    public boolean isValidSessionToken(String token) {
        HashBuilder tokenBuilder = getTokenBuilder();
        return tokenBuilder.validate(token);
    }

    public boolean isValidSessionToken(String token, Principal principle) {
        HashBuilder tokenBuilder = getTokenBuilder(principle);
        return tokenBuilder.validate(token);
    }

    private String generateNewToken() {
        String sac = Random.randomAlphaNumeric(6);
        refreshTokenData.setSac(sac);
        String newToken = getTokenBuilder().output();
        return newToken;
    }

    public HashBuilder getTokenBuilder() {
        Principal principle = httpServletRequest.getUserPrincipal();
        return getTokenBuilder(principle);
    }

    public HashBuilder getTokenBuilder(Principal principle) {
        return CryptoUtil.getHashBuilder().secret(principle.getName()).message(refreshTokenData.getSac())
                .interval(config.getRefreshTokenInterval()).tolerance(config.getRefreshTokenInterval()).toHMAC();
    }

    private void validateRequest(RefreshTokenRequest request) {

        if (httpServletRequest.getSession(false) == null) {
            throw new LimitterException(LimitterError.COMMON_LIMITTER_ERROR, "No session");
        }
        Principal principle = httpServletRequest.getUserPrincipal();
        if (principle == null) {
            throw new LimitterException(LimitterError.COMMON_LIMITTER_ERROR, "No session");
        }
        if (refreshTokenData.getSac() != null && request.getToken() == null) {
            throw new LimitterException(LimitterError.COMMON_LIMITTER_ERROR, "Session token can not be empty");
        }
    }

    private void invalidateSession() {
        // SecurityContextHolder.getContext().setAuthentication(null);
        HttpSession session = httpServletRequest.getSession(false);
        session.invalidate();
        // SecurityContextHolder.clearContext();
        httpService.clearSessionCookie();
    }

    /**
     * @return true if session token was generated within time window of
     *         <code>REFRESH_TOKEN_GENERATION_TIME_CHECK</code> millis post
     *         generation time
     */
    public boolean tokenGenerationTimeCheck() {
        boolean check = false;
        if (refreshTokenData.getSac() != null) {
            long now = Calendar.getInstance().getTimeInMillis();
            long diff = now - refreshTokenData.getSacGenerationTime();
            if (diff <= REFRESH_TOKEN_GENERATION_TIME_CHECK) {
                check = true;
            }
        }
        return check;
    }
}
