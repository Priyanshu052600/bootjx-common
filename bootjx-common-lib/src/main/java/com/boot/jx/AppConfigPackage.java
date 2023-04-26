package com.boot.jx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.utils.ArgUtil;
import com.boot.utils.ClazzUtil;

@Component
public class AppConfigPackage {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public interface AppCommonConfig {

		public Map<String, Object> configAttributes();

		public Map<String, Object> appAttributes();

	}

	public interface AppSharedConfig {
		default void clear(Map<String, String> map) {
			// DO NOTHING
		};

		default String name() {
			return null;
		};

		default Map<String, Object> getExternalConfig(Map<String, Object> config) {
			return config;
		};
	}

	@Autowired(required = false)
	private List<AppSharedConfig> listAppSharedConfig;

	public void clear(Map<String, String> map) {
		if (ArgUtil.is(listAppSharedConfig)) {
			for (AppSharedConfig appSharedConfig : listAppSharedConfig) {
				appSharedConfig.clear(map);
				LOGGER.info("for class {}", ClazzUtil.getUltimateClassName(appSharedConfig));
			}
		}
	}

	public Map<String, Object> getExternalConfig() {
		Map<String, Object> config = new HashMap<String, Object>();
		if (ArgUtil.is(listAppSharedConfig)) {
			for (AppSharedConfig appSharedConfig : listAppSharedConfig) {
				appSharedConfig.getExternalConfig(config);
			}
		}
		return config;
	}
}