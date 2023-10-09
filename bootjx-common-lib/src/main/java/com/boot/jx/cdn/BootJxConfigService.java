package com.boot.jx.cdn;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.boot.jx.AppConfig;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.utils.ArgUtil;
import com.boot.utils.CryptoUtil;

@Component
public class BootJxConfigService {

	@Autowired
	AppConfig appConfig;

	@Value("${bootjx.cdn.url:}")
	String bootJxCdnUrl;

	@Value("${bootjx.cdn.swagger:}")
	String bootJxCdnSwagger;

	@Value("${bootjx.cdn.version:}")
	String bootJxCdnVersion;

	@Value("${bootjx.cdn.app:}")
	String bootJxCdnApp;

	@Value("${bootjx.cdn.context:}")
	String bootJxCdnContext;

	@Value("${bootjx.cdn.static:}")
	String bootJxCdnStatic;

	@Value("${bootjx.app.title:}")
	String bootJxAppTitle;

	@Value("${bootjx.app.desc:}")
	String bootJxAppDesc;

	@Value("${bootjx.app.site:}")
	String bootJxAppSite;

	@Autowired
	private CommonHttpRequest commonHttpRequest;

	public String getCdnUrl(boolean isSwagger) {
		String debugCdnUrl = commonHttpRequest.get("BOOTJX_CDN_URL");
		if (ArgUtil.is(debugCdnUrl) && !(debugCdnUrl.startsWith("http://") || debugCdnUrl.startsWith("https://"))) {
			debugCdnUrl = CryptoUtil.getEncoder().message(debugCdnUrl).decodeBase64().toString();
		}
		return ArgUtil.parseAsString(debugCdnUrl, isSwagger ? bootJxCdnSwagger : bootJxCdnUrl);
	}

	public String getCdnUrl() {
		return this.getCdnUrl(false);
	}

	public BootJxConfigModel bootJxAttributesModel(boolean isSwagger) {
		BootJxConfigModel model = new BootJxConfigModel();
		String cdnUrl = getCdnUrl(isSwagger);

		model.cdnUrl(cdnUrl);
		model.cdnApp(bootJxCdnApp);
		model.cdnContext(bootJxCdnContext);
		model.cdnStatic(bootJxCdnStatic);
		model.cdnVersion(bootJxCdnVersion);

		if (ArgUtil.is(cdnUrl) && (cdnUrl.contains("127.0.0.1") || cdnUrl.contains("localhost"))) {
			model.put("BOOTJX_CDN_DEBUG", ArgUtil.parseAsString(commonHttpRequest.get("BOOTJX_CDN_DEBUG"), "true"));
		} else {
			model.put("BOOTJX_CDN_DEBUG", ArgUtil.parseAsString(commonHttpRequest.get("BOOTJX_CDN_DEBUG"), "false"));
		}

		model.put("BOOTJX_APP_TITLE", bootJxAppTitle);
		model.put("BOOTJX_APP_DESC", bootJxAppDesc);
		model.put("BOOTJX_APP_SITE", bootJxAppSite);

		model.put("BOOTJX_UPGRADE_INSECURE_REQUESTS", "true");

		return model;
	}

	public BootJxConfigModel bootJxAttributesModel() {
		return this.bootJxAttributesModel(false);
	}

	public Map<String, Object> bootJxAttributes() {
		BootJxConfigModel model = this.bootJxAttributesModel();
		return model.map();
	}

	public static class BootJxConfigModel {
		private Map<String, Object> map;

		public BootJxConfigModel(Map<String, Object> map) {
			super();
			this.map = map;
		}

		public BootJxConfigModel() {
			this(new HashMap<String, Object>());
		}

		public BootJxConfigModel cdnUrl(String cdnUrl) {
			map.put("BOOTJX_CDN_URL", cdnUrl);
			return this;
		}

		public BootJxConfigModel cdnStatic(String cdnStatic) {
			map.put("BOOTJX_CDN_STATIC", cdnStatic);
			return this;
		}

		public BootJxConfigModel cdnContext(String bootJxCdnContext) {
			map.put("BOOTJX_CDN_CONTEXT", bootJxCdnContext);
			return this;
		}

		public BootJxConfigModel cdnApp(String bootJxCdnApp) {
			map.put("BOOTJX_CDN_APP", bootJxCdnApp);
			this.cdnAEntry(bootJxCdnApp);
			return this;
		}

		public BootJxConfigModel cdnAEntry(String bootJxCdnApp) {
			map.put("BOOTJX_CDN_ENTRY", bootJxCdnApp);
			return this;
		}

		public BootJxConfigModel cdnVersion(String bootJxCdnVersion) {
			map.put("BOOTJX_CDN_VERSION", bootJxCdnVersion);
			return this;
		}

		public BootJxConfigModel preventUpgradeInsecureRequest() {
			map.put("BOOTJX_UPGRADE_INSECURE_REQUESTS", "false");
			return this;
		}

		public Map<String, Object> map() {
			return this.map;
		}

		public BootJxConfigModel put(String key, Object value) {
			this.map.put(key, value);
			return this;
		}

	}
}
