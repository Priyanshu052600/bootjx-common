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

	@Value("${bootjx.cdn.version:}")
	String bootJxCdnVersion;

	@Value("${bootjx.cdn.app:}")
	String bootJxCdnApp;

	@Value("${bootjx.cdn.context:}")
	String bootJxCdnContext;

	@Value("${bootjx.app.title:}")
	String bootJxAppTitle;

	@Value("${bootjx.app.desc:}")
	String bootJxAppDesc;

	@Value("${bootjx.app.site:}")
	String bootJxAppSite;

	@Autowired
	private CommonHttpRequest commonHttpRequest;

	public String getCdnUrl() {
		String debugCdnUrl = commonHttpRequest.get("BOOTJX_CDN_URL");
		if (ArgUtil.is(debugCdnUrl) && !(debugCdnUrl.startsWith("http://") || debugCdnUrl.startsWith("https://"))) {
			debugCdnUrl = CryptoUtil.getEncoder().message(debugCdnUrl).decodeBase64().toString();
		}
		return ArgUtil.parseAsString(debugCdnUrl, bootJxCdnUrl);
	}

	public Map<String, Object> bootJxAttributes() {
		Map<String, Object> map = new HashMap<String, Object>();

		String cdnUrl = getCdnUrl();

		map.put("BOOTJX_CDN_URL", cdnUrl);
		map.put("BOOTJX_CDN_APP", bootJxCdnApp);
		map.put("BOOTJX_CDN_CONTEXT", bootJxCdnContext);
		map.put("BOOTJX_CDN_VERSION", bootJxCdnVersion);

		if (ArgUtil.is(cdnUrl) && (cdnUrl.contains("127.0.0.1") || cdnUrl.contains("localhost"))) {
			map.put("BOOTJX_CDN_DEBUG", ArgUtil.parseAsString(commonHttpRequest.get("BOOTJX_CDN_DEBUG"), "true"));
		} else {
			map.put("BOOTJX_CDN_DEBUG", ArgUtil.parseAsString(commonHttpRequest.get("BOOTJX_CDN_DEBUG"), "false"));
		}

		map.put("BOOTJX_APP_TITLE", bootJxAppTitle);
		map.put("BOOTJX_APP_DESC", bootJxAppDesc);
		map.put("BOOTJX_APP_SITE", bootJxAppSite);

		return map;
	}

}
