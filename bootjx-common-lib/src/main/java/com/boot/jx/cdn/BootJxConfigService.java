package com.boot.jx.cdn;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.boot.jx.AppConfig;
import com.boot.jx.AppContextUtil;
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

	public static class BootJxConfigProvider {
		String cdnUrl;
		String cdnApp;
		String cdnEntry;
		String webApp;
		String cdnContext;
		String cdnStatic;
		String cdnVersion;

		public String cdnUrlGet(String cdnUrl) {
			return ArgUtil.nonEmpty(this.cdnUrl, cdnUrl);
		}

		public BootJxConfigProvider cdnUrl(String cdnUrl) {
			this.cdnUrl = cdnUrl;
			return this;
		}

		public String cdnAppGet(String cdnApp) {
			return ArgUtil.nonEmpty(this.cdnApp, cdnApp);
		}

		public BootJxConfigProvider cdnApp(String cdnApp) {
			this.cdnApp = cdnApp;
			return this;
		}

		public String cdnEntryGet(String cdnEntry) {
			return ArgUtil.nonEmpty(this.cdnEntry, cdnEntry);
		}

		public BootJxConfigProvider cdnEntry(String cdnEntry) {
			this.cdnEntry = cdnEntry;
			return this;
		}

		public String webAppGet(String webApp) {
			return ArgUtil.nonEmpty(this.webApp, webApp);
		}

		public BootJxConfigProvider webApp(String webApp) {
			this.webApp = webApp;
			return this;
		}

		public String cdnContextGet(String cdnContext) {
			return ArgUtil.nonEmpty(this.cdnContext, cdnContext);
		}

		public BootJxConfigProvider cdnContext(String cdnContext) {
			this.cdnContext = cdnContext;
			return this;
		}

		public String cdnStaticGet(String cdnStatic) {
			return ArgUtil.nonEmpty(this.cdnStatic, cdnStatic);
		}

		public BootJxConfigProvider cdnStatic(String cdnStatic) {
			this.cdnStatic = cdnStatic;
			return this;
		}

		public String cdnVersionGet(String cdnVersion) {
			return ArgUtil.nonEmpty(this.cdnVersion, cdnVersion);
		}

		public BootJxConfigProvider cdnVersion(String cdnVersion) {
			this.cdnVersion = cdnVersion;
			return this;
		}

	}

	@Autowired
	private CommonHttpRequest commonHttpRequest;

	private BootJxConfigProvider defaultProfider = new BootJxConfigProvider();

	@Autowired(required = false)
	private BootJxConfigProvider bootJxConfigProvider;

	private BootJxConfigProvider provider() {
		Object provider = AppContextUtil.get("BootJxConfigProvider");
		if (ArgUtil.is(provider)) {
			return (BootJxConfigProvider) provider;
		}

		if (bootJxConfigProvider == null) {
			return defaultProfider;
		}
		return bootJxConfigProvider;
	}

	public BootJxConfigProvider provider(BootJxConfigProvider provider) {
		AppContextUtil.set("BootJxConfigProvider", provider);
		return provider;
	}

	public String getCdnUrl(boolean isSwagger) {
		String debugCdnUrl = commonHttpRequest.get("BOOTJX_CDN_URL");
		if (ArgUtil.is(debugCdnUrl) && !(debugCdnUrl.startsWith("http://") || debugCdnUrl.startsWith("https://"))) {
			debugCdnUrl = CryptoUtil.getEncoder().message(debugCdnUrl).decodeBase64().toString();
		}
		return ArgUtil.parseAsString(debugCdnUrl, isSwagger ? bootJxCdnSwagger : provider().cdnUrlGet(bootJxCdnUrl));
	}

	public String getCdnUrl() {
		return this.getCdnUrl(false);
	}

	public BootJxConfigModel bootJxAttributesModel(boolean isSwagger) {
		BootJxConfigModel model = new BootJxConfigModel();
		String cdnUrl = getCdnUrl(isSwagger);

		model.cdnUrl(cdnUrl);
		model.cdnApp(provider().cdnAppGet(bootJxCdnApp));
		model.cdnEntry(provider().cdnEntryGet("app-" + bootJxCdnApp));
		model.webApp(provider().webAppGet(bootJxCdnApp));
		model.cdnContext(provider().cdnContextGet(bootJxCdnContext));
		model.cdnStatic(provider().cdnStaticGet(bootJxCdnStatic));
		model.cdnVersion(provider().cdnVersionGet(bootJxCdnVersion));

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
			return this;
		}

		public BootJxConfigModel webApp(String bootJxWebApp) {
			map.put("BOOTJX_WEBAPP", bootJxWebApp);
			return this;
		}

		public BootJxConfigModel cdnEntry(String bootJxCdnEntry) {
			map.put("BOOTJX_CDN_ENTRY", bootJxCdnEntry);
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
