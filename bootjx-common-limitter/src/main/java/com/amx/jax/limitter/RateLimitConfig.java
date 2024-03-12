package com.amx.jax.limitter;

import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.caffeine.CaffeineProxyManager;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.distributed.remote.RemoteBucketState;

@Configuration
public class RateLimitConfig {

    @Value("${app.ratelimit.enable:false}")
    private Boolean rateLimitEnable;

    @Value("${app.ratelimit.capacitysec:35}")
    private Integer capacitySec;

    @Value("${app.ratelimit.capacitymin:1000}")
    private Integer capacityMin;

    @Value("#{'${app.ratelimit.whitelistedips:\\\"\\\"}'.split(',')}")
    private List<String> whiteListedIps;

    @Value("${app.ratelimit.refreshtoken.enable:false}")
    private Boolean refreshTokenEnable;

    @Value("${app.ratelimit.refreshtoken.interval:15}")
    private Integer refreshTokenInterval;

    private static final Logger log = LoggerFactory.getLogger(RateLimitConfig.class);

    public static final long CAFFEINE_CACHE_SIZE = 10_000;

    @Bean
    @ConditionalOnProperty("app.ratelimit.enable")
    public 	ProxyManager<String> caffeineProxyManager() {
        Caffeine<String, RemoteBucketState> builder = (Caffeine) Caffeine.newBuilder().maximumSize(CAFFEINE_CACHE_SIZE);
        return new CaffeineProxyManager<>(builder, Duration.ofMinutes(1));
    }

    @Bean
    public BucketConfiguration bucketConfiguration() {
        log.info("Creating local ratelimit bucket with initial capacity sec {} and capacity min {}", capacitySec,
                capacityMin);
        Bandwidth limitMin = Bandwidth.classic(capacityMin, Refill.intervally(capacityMin, Duration.ofMinutes(1)));
        Bandwidth limitSec = Bandwidth.classic(capacitySec, Refill.intervally(capacitySec, Duration.ofSeconds(1)));
        return BucketConfiguration.builder().addLimit(limitMin).addLimit(limitSec).build();
    }

//    @Bean
//    public ServletListenerRegistrationBean<LimitterSessionListener> limitterSessionListener() {
//        ServletListenerRegistrationBean<LimitterSessionListener> listenerRegBean = new ServletListenerRegistrationBean<>();
//
//        listenerRegBean.setListener(new LimitterSessionListener());
//        return listenerRegBean;
//    }

    public List<String> getWhiteListedIps() {
        return whiteListedIps;
    }

    public void setWhiteListedIps(List<String> whiteListedIps) {
        this.whiteListedIps = whiteListedIps;
    }

    public Boolean getRateLimitEnable() {
        return rateLimitEnable;
    }

    public void setRateLimitEnable(Boolean rateLimitEnable) {
        this.rateLimitEnable = rateLimitEnable;
    }

    public Integer getRefreshTokenInterval() {
        return refreshTokenInterval;
    }

    public void setRefreshTokenInterval(Integer refreshTokenInterval) {
        this.refreshTokenInterval = refreshTokenInterval;
    }

    public Boolean getRefreshTokenEnable() {
        return refreshTokenEnable;
    }

    public void setRefreshTokenEnable(Boolean refreshTokenEnable) {
        this.refreshTokenEnable = refreshTokenEnable;
    }

}
