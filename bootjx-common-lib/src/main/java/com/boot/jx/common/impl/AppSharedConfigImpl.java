package com.boot.jx.common.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.jx.AppConfig;
import com.boot.jx.AppConfigPackage.AppSharedConfig;

@Component
public class AppSharedConfigImpl implements AppSharedConfig {

    @Autowired
    private AppConfig appConfig;

    @Override
    public Map<String, Object> getExternalConfig(Map<String, Object> config) {
	return config;
    };
}
