package com.boot.jx;

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.boot.jx.dict.Language;
import com.boot.jx.dict.Project;
import com.boot.jx.dict.UserClient.AppType;
import com.boot.jx.dict.UserClient.Channel;
import com.boot.jx.dict.UserClient.ClientType;
import com.boot.jx.dict.UserClient.DeviceType;
import com.boot.jx.filter.AppClientErrorHanlder;
import com.boot.jx.filter.AppClientInterceptor;
import com.boot.jx.scope.tnt.TenantProperties;
import com.boot.jx.scope.tnt.Tenants;
import com.boot.utils.ArgUtil;
import com.boot.utils.Constants;
import com.boot.utils.CryptoUtil;
import com.boot.utils.JsonUtil.JsonUtilConfigurable;
import com.boot.utils.UniqueID;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import jakarta.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:application-lib.properties")
@EnableEncryptableProperties
public class AppConfig {

	private Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

	private static final String PROP_PREFIX = "${";
	private static final String PROP_SUFFIX = "}";
	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");
	public static final String APP_ENV = "${app.env}";
	public static final String APP_VENV = "${app.venv}";
	public static final String APP_GROUP = "${app.group}";
	public static final String APP_NAME = "${app.name}";
	public static final String APP_TYPE = "${app.type}";
	public static final String APP_DESCRIPTION = "${app.description}";
	public static final String APP_ID = "${app.id}";
	public static final String APP_VERSION = "${app.version}";
	public static final String APP_BUILDTIMESTAMP = "${app.buildtimestamp}";

	public static final String APP_PROD = "${app.prod}";
	public static final String APP_SWAGGER = "${app.swagger}";
	public static final String APP_DEBUG = "${app.debug}";
	public static final String APP_CACHE = "${app.cache}";
	public static final String APP_LOGGER = "${app.audit}";
	public static final String APP_MONITOR = "${app.monitor}";

	public static final String APP_CONTEXT_PREFIX = "${server.contextPath}";
	public static final String SPRING_APP_NAME = "${spring.application.name}";

	public static final String APP_AUTH_KEY = "${app.auth.key}";
	public static final String APP_AUTH_TOKEN = "${app.auth.token}";
	public static final String APP_AUTH_ENABLED = "${app.auth.enabled}";

	public static final String DEFAULT_TENANT_KEY = "default.tenant";

	public static final String DEFAULT_TENANT_EXP = PROP_PREFIX + DEFAULT_TENANT_KEY + PROP_SUFFIX;

	public static final String JAX_CDN_URL = "${jax.cdn.url}";
	public static final String JAX_CDN_CONTEXT = "${jax.cdn.context}";
	public static final String JAX_APP_URL = "${jax.app.url}";
	public static final String JAX_POSTMAN_URL = "${jax.postman.url}";
	public static final String JAX_AUTH_URL = "${jax.auth.url}";
	public static final String JAX_CASHIER_URL = "${jax.cashier.url}";
	public static final String JAX_SERVICE_URL = "${jax.service.url}";

	public static final String JAX_SSO_URL = "${jax.sso.url}";

	public static final String SPRING_REDIS_HOST = "${spring.redis.host}";
	public static final String SPRING_REDIS_PORT = "${spring.redis.port}";

	@Value(APP_ENV)
	@AppParamKey(AppParam.APP_ENV)
	private String appEnv;

	@Value(APP_VENV)
	@AppParamKey(AppParam.APP_VENV)
	private String appVenv;

	@Value(APP_GROUP)
	@AppParamKey(AppParam.APP_GROUP)
	private String appGroup;

	@Value(APP_NAME)
	@AppParamKey(AppParam.APP_NAME)
	private String appName;

	@Value(APP_TYPE)
	@AppParamKey(AppParam.APP_TYPE)
	private String appType;

	@Value(APP_DESCRIPTION)
	@AppParamKey(AppParam.APP_DESCRIPTION)
	private String appDescription;

	@Value(SPRING_APP_NAME)
	@AppParamKey(AppParam.SPRING_APP_NAME)
	private String springAppName;

	@Value(APP_ID)
	@AppParamKey(AppParam.APP_ID)
	private String appId;

	@Value(APP_VERSION)
	@AppParamKey(AppParam.APP_VERSION)
	private String appVersion;

	@Value(APP_BUILDTIMESTAMP)
	@AppParamKey(AppParam.APP_BUILDTIMESTAMP)
	private String appAppBuildStamp;

	@Value(APP_PROD)
	@AppParamKey(AppParam.APP_PROD)
	private Boolean prodMode;

	@Value(APP_SWAGGER)
	@AppParamKey(AppParam.APP_SWAGGER)
	private boolean swaggerEnabled;

	@Value(APP_DEBUG)
	@AppParamKey(AppParam.APP_DEBUG)
	private Boolean debug;

	@Value(APP_LOGGER)
	@AppParamKey(AppParam.APP_LOGGER)
	private boolean logger;

	@Value(APP_MONITOR)
	@AppParamKey(AppParam.APP_MONITOR)
	private boolean monitor;

	@Value(APP_AUTH_KEY)
	private String appAuthKey;

	@Value(APP_AUTH_TOKEN)
	private String appAuthToken;

	@Value(APP_AUTH_ENABLED)
	@AppParamKey(AppParam.APP_AUTH_ENABLED)
	private boolean appAuthEnabled;

	@Value(APP_CACHE)
	@AppParamKey(AppParam.APP_CACHE)
	private Boolean cache;

	@Value("${app.title}")
	private String appTitle;

	@Value(DEFAULT_TENANT_EXP)
	@AppParamKey(AppParam.DEFAULT_TENANT)
	private String defaultTenant;

	@Value("${default.lang}")
	private Language defaultLang;

	@Value("${default.channel}")
	private Channel defaultChannel;

	@Value("${default.client.type}")
	private ClientType defaultClientType;

	@Value("${default.device.type}")
	private DeviceType defaultDeviceType;

	@Value("${default.app.type}")
	private AppType defaultAppType;

	@Value(JAX_CDN_URL)
	@AppParamKey(AppParam.JAX_CDN_URL)
	private String cdnURL;

	@Value(JAX_CDN_CONTEXT)
	private String cdnContext;

	@Value(JAX_POSTMAN_URL)
	@AppParamKey(AppParam.JAX_POSTMAN_URL)
	private String postmapURL;

	@Value(JAX_SSO_URL)
	@AppParamKey(AppParam.JAX_SSO_URL)
	private String ssoURL;

	@Value(SPRING_REDIS_HOST)
	@AppParamKey(AppParam.SPRING_REDIS_HOST)
	private String redisSpringHost;

	@Value(SPRING_REDIS_PORT)
	@AppParamKey(AppParam.SPRING_REDIS_PORT)
	private String redisSpringPort;

	@Value(APP_CONTEXT_PREFIX)
	@AppParamKey(AppParam.APP_CONTEXT_PREFIX)
	private String appPrefix;
	
	@Value(JAX_AUTH_URL)
	@AppParamKey(AppParam.JAX_AUTH_URL)
	private String authURL;
	
	@Value(JAX_CASHIER_URL)
	@AppParamKey(AppParam.JAX_CASHIER_URL)
	private String cashierURL;
	
	@Value(JAX_SERVICE_URL)
	@AppParamKey(AppParam.JAX_SERVICE_URL)
	private String jaxServiceURL;

	@Value("${app.response.ok}")
	private boolean appResponseOK;

	@Value("${app.session}")
	private boolean appSessionEnabled;

	private String originApp;

	public boolean isAppSessionEnabled() {
		return appSessionEnabled;
	}

	@Value("${server.servlet.session.cookie.http-only}")
	private boolean cookieHttpOnly;

	@Value("${server.servlet.session.cookie.name:JSESSIONID}")
	private String sessionCookieName;

	@Value("${server.servlet.session.cookie.secure}")
	private boolean cookieSecure;

	@Value("${spring.profiles.active}")
	private String[] springProfile;

	@Value("${app.audit.file.print}")
	String[] printableAuditMarkers;

	@Value("${app.audit.file.skip}")
	String[] skipAuditMarkers;

	@Value("${encrypted.app.property}")
	String appSpecifcDecryptedProp;

	public boolean isCookieHttpOnly() {
		return cookieHttpOnly;
	}

	public boolean isCookieSecure() {
		return cookieSecure;
	}

	public String getAppName() {
		return appName;
	}

	public Boolean isProdMode() {
		return prodMode;
	}

	public Boolean isSwaggerEnabled() {
		return swaggerEnabled;
	}

	public Boolean isDebug() {
		return debug;
	}

	public Boolean isCache() {
		return cache;
	}

	public String getCdnURL() {
		return cdnURL;
	}

	public String getCdnContext() {
		return cdnContext;
	}

	public String getPostmapURL() {
		return postmapURL;
	}

	public String getAuthURL() {
		return authURL;
	}
	@Bean
	public AppParam loadAppParams() {

		LOGGER.info("Loading loadAppParams");

		for (Field field : AppConfig.class.getDeclaredFields()) {
			AppParamKey s = field.getAnnotation(AppParamKey.class);
			Value v = field.getAnnotation(Value.class);
			if (s != null && v != null) {
				Matcher match = pattern.matcher(v.value());
				if (match.find()) {
					s.value().setProperty(match.group(1));
				}

				String typeName = field.getGenericType().getTypeName();
				Object value = null;
				try {
					value = field.get(this);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

				if ("java.lang.String".equals(typeName)) {
					s.value().setValue(ArgUtil.parseAsString(value, Constants.BLANK).trim());
				} else if ("boolean".equals(typeName) || "java.lang.Boolean".equals(typeName)) {
					s.value().setEnabled(ArgUtil.parseAsBoolean(value));
				}
			}
		}

		try {
			String appInstanceId = String.format("%s#%s#%s#%s#%s", AppParam.APP_ENV.getValue(),
					AppParam.APP_VENV.getValue(), AppParam.APP_GROUP.getValue(), AppParam.APP_NAME.getValue(),
					AppParam.APP_ID.getValue());
			AppParam.APP_INSTANCE_ID.setValue(appInstanceId);
			AppParam.APP_INSTANCE_HASH.setValue(CryptoUtil.getMD5Hash(appInstanceId));
			AppParam.APP_INSTANCE_UID.setValue(appInstanceId + "#" + UniqueID.PREF);
			AppParam.APP_INSTANCE_TYPE.setValue(AppParam.APP_VENV.getValue() + "/" + AppParam.APP_TYPE.getValue());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder, AppClientErrorHanlder errorHandler,
			AppClientInterceptor appClientInterceptor) {
		builder.rootUri("https://localhost.com");
		RestTemplate restTemplate = builder.build();
		restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
		restTemplate.setInterceptors(Collections.singletonList(appClientInterceptor));
		restTemplate.setErrorHandler(errorHandler);
		return restTemplate;
	}

	// @Bean
	public JsonUtilConfigurable jsonUtilConfigurable(ObjectMapper objectMapper) {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return new JsonUtilConfigurable(objectMapper);
	}

	@Bean
	public Project project(@Value("${app.project}") Project project) {
		ProjectConfig.PROJECT = project;
		return project;
	}

	public String getSsoURL() {
		return ssoURL;
	}

	public String getAppAuthKey() {
		return appAuthKey;
	}

	public boolean isAppAuthEnabled() {
		return appAuthEnabled;
	}

	public String getAppEnv() {
		return appEnv;
	}

	public String getAppGroup() {
		return appGroup;
	}

	public String getAppId() {
		return appId;
	}

	public String[] getPrintableAuditMarkers() {
		return printableAuditMarkers;
	}

	public String[] getSkipAuditMarkers() {
		return skipAuditMarkers;
	}

	public boolean isAudit() {
		return logger;
	}

	public String getAppPrefix() {
		return appPrefix;
	}

	@Autowired
	private Environment environment;

	@Autowired
	TenantProperties tenantProperties;

	public String prop(String key) {
		String value = tenantProperties.getProperties().getProperty(key);
		if (ArgUtil.isEmpty(value)) {
			value = environment.getProperty(key);
		}
		return ArgUtil.parseAsString(value);
	}

	@PostConstruct
	public void init() {
		TenantProperties.setEnviroment(environment);
		if (defaultTenant != null) {
			Tenants.setDefault(defaultTenant);
		}
		this.loadAppParams();
	}

	public String getDefaultTenant() {
		return defaultTenant;
	}

	public String getSpringAppName() {
		return springAppName;
	}

	public String getAppSpecifcDecryptedProp() {
		return appSpecifcDecryptedProp;
	}

	public String getAppAuthToken() {
		return appAuthToken;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public boolean isAppResponseOK() {
		return appResponseOK;
	}

	public Language getDefaultLang() {
		return defaultLang;
	}

	public Channel getDefaultChannel() {
		return defaultChannel;
	}

	public ClientType getDefaultClientType() {
		return defaultClientType;
	}

	public DeviceType getDefaultDeviceType() {
		return defaultDeviceType;
	}

	public AppType getDefaultAppType() {
		return defaultAppType;
	}

	public String getAppAppBuildStamp() {
		return appAppBuildStamp;
	}

	public String getAppTitle() {
		return appTitle;
	}

	public String getAppType() {
		return appType;
	}

	public String getSessionCookieName() {
		return sessionCookieName;
	}

	public String getAppVenv() {
		return appVenv;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}
	
	public String getAppInstanceType() {
		return AppParam.APP_INSTANCE_TYPE.getValue();
	}

	public String getCashierURL() {
		return cashierURL;
	}

	public void setCashierURL(String cashierURL) {
		this.cashierURL = cashierURL;
	}

	public String getJaxURL() {
		return jaxServiceURL;
	}

	public void getJaxURL(String jaxServiceURL) {
		this.jaxServiceURL = jaxServiceURL;
	}

}
