package com.amx.jax.limitter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.limitter.constant.ApiPath;
import com.amx.jax.limitter.exp.LimitterError;
import com.amx.jax.limitter.exp.LimitterException;
import com.amx.jax.limitter.model.RefreshTokenRequest;
import com.amx.jax.limitter.model.RefreshTokenResponse;
import com.boot.jx.api.ApiResponse;
import com.boot.utils.JsonUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
public class LimitterController {

    @Autowired
    private RefreshTokenManager refreshTokenManager;
    @Autowired
    private RateLimitConfig rateLimitConfig;

    private static final Logger log = LoggerFactory.getLogger(LimitterController.class);

    @RequestMapping(value = ApiPath.REFRESH_TOKEN, method = RequestMethod.POST)
    public ApiResponse<RefreshTokenResponse, Object> refreshToken(@RequestBody @Valid RefreshTokenRequest request,
            HttpServletResponse httpServletResponse) throws IOException {
        log.debug("request refreshToken: {} ", JsonUtil.toJson(request));
        RefreshTokenResponse response = null;
        if (rateLimitConfig.getRefreshTokenEnable()) {
            response = refreshTokenManager.refreshToken(request, httpServletResponse);
        } else {
            throw new LimitterException(LimitterError.REFRESH_TOKEN_DISABLED, "Refresh token feature is not enabled");
        }

        return ApiResponse.buildResult(response);
    }

}
