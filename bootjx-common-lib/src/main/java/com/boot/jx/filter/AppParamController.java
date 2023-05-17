package com.boot.jx.filter;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boot.jx.AppConfig;
import com.boot.jx.AppConfigPackage;
import com.boot.jx.AppParam;
import com.boot.jx.AppTenantConfig;
import com.boot.jx.api.ApiResponse;
import com.boot.jx.api.ApiResponseUtil;
import com.boot.jx.api.BoolRespModel;
import com.boot.jx.def.IndicatorListner;
import com.boot.jx.def.IndicatorListner.GaugeIndicator;
import com.boot.jx.exception.AmxApiError;
import com.boot.jx.http.ApiRequest;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.http.RequestType;
import com.boot.jx.model.UserDevice;
import com.boot.jx.scope.tnt.TenantContextHolder;
import com.boot.jx.scope.tnt.TenantProperties;
import com.boot.jx.scope.vendor.VendorAuthService;
import com.boot.jx.scope.vendor.VendorContext;
import com.boot.jx.scope.vendor.VendorContext.ApiVendorHeaders;
import com.boot.utils.ArgUtil;
import com.boot.utils.CryptoUtil.HashBuilder;
import com.boot.utils.HttpUtils;
import com.boot.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

@RestController
public class AppParamController {

	public static final String EXT_PUB_CONFIG_CLIENT = "/ext/pub/config/client";
	private static final Logger LOGGER = LoggerFactory.getLogger(AppParamController.class);
	public static final String PUB_AMX_PREFIX = "/pub/boot";
	public static final String PUBG_AMX_PREFIX = "/pubg/";
	public static final String PARAM_URL = PUB_AMX_PREFIX + "/params";
	public static final String FEATURE_URL = PUB_AMX_PREFIX + "/features";
	public static final String METRIC_URL = PUB_AMX_PREFIX + "/metric";

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Autowired
	AppConfig appConfig;

	@Autowired
	AppTenantConfig appTenantConfig;

	@Autowired(required = false)
	VendorAuthService vendorAuthConfig;

	@Autowired(required = false)
	List<IndicatorListner> listners;

	@ApiRequest(type = RequestType.NO_TRACK_PING)
	@RequestMapping(value = { PUB_AMX_PREFIX + "/ping" }, method = RequestMethod.GET)
	public ApiResponse<Object, Object> intPubPing() {
		return ApiResponse.build().message("pong");
	}

	@ApiRequest(type = RequestType.NO_TRACK_PING)
	@RequestMapping(value = { "/int/pub/boot/metric", METRIC_URL }, method = RequestMethod.GET)
	public ApiResponse<Object, Object> intPubMetric() {
		ApiResponse<Object, Object> response = ApiResponse.build();
		for (AppParam eachAppParam : AppParam.values()) {
			response.addResult(eachAppParam);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		GaugeIndicator gaugeIndicator = new GaugeIndicator();
		if (!ArgUtil.isEmpty(listners)) {
			for (IndicatorListner eachListner : listners) {
				map.putAll(eachListner.getIndicators(gaugeIndicator));
			}
		}
		return response.data(map);
	}

	@ApiRequest(type = RequestType.NO_TRACK_PING)
	@RequestMapping(value = PARAM_URL, method = RequestMethod.GET)
	public AppParam[] geoLocation(@RequestParam(required = false) AppParam id) {
		if (id != null) {
			id.setEnabled(!id.isEnabled());
			LOGGER.info("App Param {} changed to {}", id, id.isEnabled());
		}
		return AppParam.values();
	}

	@ApiVendorHeaders
	@ApiRequest(type = RequestType.NO_TRACK_PING)
	@RequestMapping(value = FEATURE_URL, method = RequestMethod.GET)
	public Object[] features() {
		if (vendorAuthConfig != null) {
			return vendorAuthConfig.getFeaturesList().toArray();
		}
		return null;
	}

	@Autowired
	TenantProperties tenantProperties;

	/** The env. */
	@Autowired
	private Environment env;

	public String prop(String key) {
		String value = tenantProperties.getProperties().getProperty(key);
		if (ArgUtil.isEmpty(value)) {
			value = env.getProperty(key);
		}
		return ArgUtil.parseAsString(value);
	}

	@Autowired
	private AppConfigPackage appConfigPackage;

	@RequestMapping(value = "/pub/amx/config/shared/clear", method = RequestMethod.GET)
	public ApiResponse<BoolRespModel, Object> clearSharedConfig() {
		appConfigPackage.clear(null);
		return ApiResponse.buildData(new BoolRespModel(true));
	}

	@Autowired(required = false)
	VendorAuthService appVendorConfigForAuth;

	@RequestMapping(value = "/pub/amx/device", method = { RequestMethod.GET, RequestMethod.POST })
	public ApiResponse<UserDevice, Map<String, Object>> userDevice(@RequestParam(required = false) String key,
			@RequestParam(required = false) String vendor, HttpSession httpSession, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("getAppSpecifcDecryptedProp", appConfig.getAppSpecifcDecryptedProp());
		map.put("getTenantSpecifcDecryptedProp2", appTenantConfig.getTenantSpecifcDecryptedProp2());
		map.put("getTenantSpecifcDecryptedProp", appTenantConfig.getTenantSpecifcDecryptedProp());
		map.put("defaultTenant", appConfig.getDefaultTenant());
		map.put(TenantContextHolder.TENANT, TenantContextHolder.currentSite(false));

		VendorContext.setVendor(vendor);

		map.put("getBasicAuthPassword", appVendorConfigForAuth.getBasicAuthPassword());
		map.put("getBasicAuthUser", appVendorConfigForAuth.getBasicAuthUser());
		map.put("commonHttpRequest.getIPAddress()", commonHttpRequest.getIPAddress());

		map.put("httpSession.getId", httpSession.getId());
		map.put("request.getRequestURL", request.getRequestURL().toString());
		map.put("request.getServerName", request.getServerName());
		map.put("request.getRequestURI()", request.getRequestURI());
		map.put("request.getRemoteHost()", request.getRemoteHost());
		map.put("request.getRemoteAddr()", request.getRemoteAddr());
		map.put("request.getLocalAddr()", request.getLocalAddr());
		map.put("request.getScheme()", request.getScheme());
		map.put("HttpUtils.getSubdomain()", HttpUtils.getSubDomain(request));
		map.put("HttpUtils.getScheme()", HttpUtils.getScheme(request));

		if (!ArgUtil.isEmpty(key)) {
			map.put(key, prop(key));
		}

		ApiResponseUtil.addWarning("THis is a warning for no reason");
		ApiResponse<UserDevice, Map<String, Object>> resp = new ApiResponse<UserDevice, Map<String, Object>>();
		resp.setMeta(map);
		resp.setData(commonHttpRequest.getUserDevice().toSanitized());
		return resp;
	}

	@RequestMapping(value = "/pub/amx/hmac", method = RequestMethod.GET)
	public Map<String, String> hmac(@RequestParam Long interval, @RequestParam String secret,
			@RequestParam String message, @RequestParam Integer length,
			@RequestParam(required = false) Long currentTime, @RequestParam(required = false) String complexHash) {
		Map<String, String> map = new HashMap<String, String>();
		HashBuilder builder = new HashBuilder().interval(interval).secret(secret).message(message);
		if (!ArgUtil.isEmpty(currentTime)) {
			builder.currentTime(currentTime);
		}
		map.put("hmac", builder.toHMAC().output());
		map.put("numeric", builder.toNumeric(length).output());
		map.put("complex", builder.toComplex(length).output());
		if (!ArgUtil.isEmpty(complexHash)) {
			map.put("valid", "" + builder.validateComplexHMAC(complexHash));
		}

		return map;
	}

	@RequestMapping(value = "/pub/amx/encrypt", method = RequestMethod.GET)
	public Map<String, String> encrypt(@RequestParam String secret, @RequestParam String message) {
		Map<String, String> map = new HashMap<String, String>();
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPasswordCharArray(secret.toCharArray());
		map.put("decrypted", message);
		map.put("encrypted", textEncryptor.encrypt(message));
		return map;
	}

	@RequestMapping(value = "/pub/amx/decrypt", method = RequestMethod.GET)
	public Map<String, String> decrypt(@RequestParam String secret, @RequestParam String message) {
		Map<String, String> map = new HashMap<String, String>();
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPasswordCharArray(secret.toCharArray());
		map.put("encrypted", message);
		map.put("decrypted", textEncryptor.decrypt(message));
		return map;
	}

	@RequestMapping(value = "/pub/amx/json/decode/b64", method = RequestMethod.POST)
	public Map<String, Object> jsonDecodeB64(@RequestParam String jsond) {
		byte[] decodedBytes = Base64.getDecoder().decode(jsond);
		String requestParamsJson = new String(decodedBytes);
		if (!ArgUtil.isEmpty(requestParamsJson)) {
			return JsonUtil.fromJson(requestParamsJson, new TypeReference<Map<String, Object>>() {
			});
		}
		return null;
	}

	@RequestMapping(value = "/pub/amx/json/encode/b64", method = RequestMethod.POST)
	public String jsonEncodeB64(@RequestBody Map<String, Object> json) {
		String callbackUrl = JsonUtil.toJson(json);
		return Base64.getEncoder().encodeToString(callbackUrl.getBytes());
	}

	@RequestMapping(value = "/pub/error/{exception}/{statusKey}", method = RequestMethod.GET)
	public AmxApiError jsonEncodeB64(@RequestParam String status, @RequestParam String exception) {
		AmxApiError error = new AmxApiError(status, status);
		error.setException(exception);
		return error;
	}

	@RequestMapping(value = "/ext/pub/logger", method = RequestMethod.GET)
	public ApiResponse<Object, Object> toggleLogger(@RequestParam(required = false) String loggerName,
			@RequestParam(required = false, defaultValue = "info") String level) {

		if (ArgUtil.is(loggerName)) {
			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
			ch.qos.logback.classic.Logger logger = loggerName.equalsIgnoreCase("root")
					? loggerContext.getLogger(loggerName)
					: loggerContext.exists(loggerName);
			if (logger != null) {
				logger.setLevel(Level.toLevel(level));
				LOGGER.info("Changed logger: " + loggerName + " to level : " + level);
			} else {
				LOGGER.info("Logger Not Found Make Sure that logger name is correct");
			}
		} else {
			AppRequestUtil.isLocalEnable();
		}
		return ApiResponse.build().message("logger" + AppRequestUtil.isLocal());
	}

	@RequestMapping(value = EXT_PUB_CONFIG_CLIENT, method = RequestMethod.GET)
	public ApiResponse<Map<String, Object>, Object> extPubConfig() {
		return ApiResponse.buildData(appConfigPackage.getExternalConfig());
	}

}
