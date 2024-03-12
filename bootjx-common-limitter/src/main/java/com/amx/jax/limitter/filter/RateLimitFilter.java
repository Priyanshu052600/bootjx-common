package com.amx.jax.limitter.filter;

import java.io.IOException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.amx.jax.limitter.RateLimitConfig;
import com.boot.utils.HttpUtils;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@ConditionalOnProperty("app.ratelimit.enable")
public class RateLimitFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);

    @Autowired
    RateLimitConfig rateLimitConfig;

    @Autowired
    ProxyManager<String> proxyManager;

    @Autowired
    BucketConfiguration bucketConfiguration;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        if (rateLimitConfig.getRateLimitEnable()) {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) resp;
            String ipAddress = HttpUtils.getIPAddress(request);
            if (ipAddress == null || ipAddress.isEmpty()) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing IP Address");
                return;
            }
            boolean checkFlag = true;
            if (CollectionUtils.isNotEmpty(rateLimitConfig.getWhiteListedIps())) {
                if (rateLimitConfig.getWhiteListedIps().contains(ipAddress)) {
                    checkFlag = false;
                }
            }
            if (checkFlag) {
                Bucket requestBucket = this.proxyManager.builder().build(ipAddress, bucketConfiguration);
                ConsumptionProbe probe = requestBucket.tryConsumeAndReturnRemaining(1);
                if (probe.isConsumed()) {
                    response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
                    chain.doFilter(req, resp);
                } else {
                    log.info("ipAddress {} exhausted api rate limit", ipAddress);
                    long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
                    response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
                    response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                            "You have exhausted your API Request Quota");
                    return;
                }
            } else {
                chain.doFilter(req, resp);
            }
        } else {
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}