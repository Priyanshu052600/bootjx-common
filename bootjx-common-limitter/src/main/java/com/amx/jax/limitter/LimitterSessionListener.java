package com.amx.jax.limitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@Service
@ConditionalOnProperty("app.ratelimit.refreshtoken.enable")
public class LimitterSessionListener implements HttpSessionListener {

    @Autowired
    private RefreshTokenData data;

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        if (data != null) {
            data.setSac(null);
        }
    }

}
