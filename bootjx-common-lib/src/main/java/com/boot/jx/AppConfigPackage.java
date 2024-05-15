package com.boot.jx;

import java.io.Serializable;
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
		default void clear(AppSharedConfigChange change) {
			// DO NOTHING
		};

		@Deprecated
		default void clear(Map<String, String> data) {
			// DO NOTHING
		};

		default String name() {
			return null;
		};

		default Map<String, Object> getExternalConfig(Map<String, Object> config) {
			return config;
		}
	}

	public static class AppSharedConfigChange implements Serializable {
		private static final long serialVersionUID = 6496200861213027301L;
		String configType;
		String configId;
		Map<String, String> details;

		public String getConfigType() {
			return configType;
		}

		public void setConfigType(String configType) {
			this.configType = configType;
		}

		public String getConfigId() {
			return configId;
		}

		public void setConfigId(String configId) {
			this.configId = configId;
		}

		public Map<String, String> getDetails() {
			return details;
		}

		public void setDetails(Map<String, String> details) {
			this.details = details;
		}

		public Map<String, String> details() {
			if (this.details == null) {
				this.details = new HashMap<String, String>();
			}
			return details;
		}

		public AppSharedConfigChange put(String key, String value) {
			this.details().put(key, value);
			return this;
		}
	}

	@Autowired(required = false)
	private List<AppSharedConfig> listAppSharedConfig;

	public void clear(AppSharedConfigChange change) {
		if (ArgUtil.is(listAppSharedConfig)) {
			for (AppSharedConfig appSharedConfig : listAppSharedConfig) {
				appSharedConfig.clear(change);
				LOGGER.debug("for class {}", ClazzUtil.getUltimateClassName(appSharedConfig));
			}
		}
	}

	public void clear() {
		if (ArgUtil.is(listAppSharedConfig)) {
			this.clear(new AppSharedConfigChange());
		}
	}

	@Deprecated
	public void clear(Map<String, String> data) {
		if (ArgUtil.is(listAppSharedConfig)) {
			for (AppSharedConfig appSharedConfig : listAppSharedConfig) {
				appSharedConfig.clear(data);
				LOGGER.debug("for class {}", ClazzUtil.getUltimateClassName(appSharedConfig));
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