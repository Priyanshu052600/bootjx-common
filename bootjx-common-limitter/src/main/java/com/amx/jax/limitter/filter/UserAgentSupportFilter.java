package com.amx.jax.limitter.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.limitter.constant.LimitterConstant;
import com.amx.jax.limitter.constant.LimitterError;
import com.amx.jax.limitter.manager.UserAgentManager;
import com.amx.jax.limitter.util.FilterUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserAgentSupportFilter implements Filter {

    @Autowired
    UserAgentManager userAgentManager;

    public static List<String> EXCLUDE_URLS = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String filterExcludeUrlParam = filterConfig
                .getInitParameter(LimitterConstant.USER_AGENT_FILTER_EXCLUDE_URL_PARAM);
        if (StringUtils.isNotBlank(filterExcludeUrlParam)) {
            String[] excludeUrls = filterExcludeUrlParam.split(LimitterConstant.USER_AGENT_INIT_PARAM_SEPARATER);
            EXCLUDE_URLS = Arrays.asList(excludeUrls);
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String path = request.getRequestURI().substring(request.getContextPath().length());
        boolean isExclude = FilterUtil.isExclude(path, EXCLUDE_URLS);
        if (!isExclude) {
            String userAgent = request.getHeader("User-Agent");
            boolean isSupported = userAgentManager.isSupportedUserAgent(userAgent);
            boolean isUnSupported = userAgentManager.isUnsupportedUserAgent(userAgent);
            if (!isSupported || isUnSupported) {
                String message = userAgentManager.getUserAgentNotSupportMsg(userAgent, isUnSupported);
                FilterUtil.setErrorResponse(request, response, LimitterError.USER_AGENT_NOT_SUPPORTED, message);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
