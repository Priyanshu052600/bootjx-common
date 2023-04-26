package com.boot.jx.scope.tnt;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.boot.jx.dict.Language;
import com.boot.jx.scope.tnt.Tenants.Tenant;
import com.boot.model.UtilityModels.Stringable;
import com.boot.utils.ArgUtil;
import com.boot.utils.Constants;
import com.boot.utils.FileUtil;

@TenantScoped
@Component
public class TenantProperties {

	private static final Logger LOGGER = LoggerFactory.getLogger(TenantProperties.class);
	private static TenantProperties obj = new TenantProperties();
	private static Environment ENV;
	private static Properties APP_ENV;
	private static String APP_ENV_VALUE;
	public static final Pattern ENCRYPTED_PROPERTIES = Pattern.compile("^ENC\\((.*)\\)$");
	public static BasicTextEncryptor textEncryptor = new BasicTextEncryptor();

	private Properties properties = null;

	TenantProperties() {
	}

	@Autowired
	public TenantProperties(Environment env) {
		if (ENV == null) {
			setEnviroment(env);
		}
	}

	public static void setEnviroment(Environment env) {
		ENV = env;
		String privateKey = ENV.getProperty("jasypt.encryptor.password");
		if (!ArgUtil.isEmpty(env) && !ArgUtil.isEmpty(privateKey)) {
			textEncryptor.setPasswordCharArray(privateKey.toCharArray());
		}
	}

	public static String decryptProp(Object propertyValue) {
		// return ArgUtil.parseAsString(propertyValue);
		String propertyValueStr = ArgUtil.parseAsString(propertyValue);
		Matcher x = ENCRYPTED_PROPERTIES.matcher(propertyValueStr);
		if (x.find()) {
			try {
				return textEncryptor.decrypt(x.group(1));
			} catch (Exception e) {
				return propertyValueStr;
			}

		}
		return propertyValueStr;
	}

	public static Environment getEnviroment() {
		return ENV;
	}

	public static String getEnvProperties(String key) {
		String value = null;
		if (getEnviroment() != null) {
			value = getEnviroment().getProperty(key);
		}
		if (ArgUtil.isEmpty(value)) {
			value = System.getProperty(key);
		}
		return value;
	}

	public static String getAppEnvProperties(String key) {
		String value = getEnvProperties(key);
		if (ArgUtil.isEmpty(value) && !ArgUtil.isEmpty(APP_ENV)) {
			if (ArgUtil.isEmpty(APP_ENV_VALUE)) {
				APP_ENV_VALUE = ArgUtil.parseAsString(getEnvProperties("app.env"), Constants.BLANK).toLowerCase();
			}
			value = APP_ENV.getProperty(key.replace("@env", APP_ENV_VALUE));
			if (ArgUtil.isEmpty(value)) {
				value = APP_ENV.getProperty(key.replace("@env", "default"));
			}
		}
		return value;
	}

	@SuppressWarnings("serial")
	public static Map<String, String> loadPropertiesMap(InputStream s) throws IOException {
		final Map<String, String> ordered = new LinkedHashMap<String, String>();
		// Hack to use properties class to parse but our map for preserved order
		Properties bp = new Properties() {
			@Override
			public synchronized Object put(Object key, Object value) {
				ordered.put((String) key, (String) value);
				return super.put(key, value);
			}
		};
		bp.load(s);
		final Map<String, String> resolved = new LinkedHashMap<String, String>(ordered.size());
		StrSubstitutor sub = new StrSubstitutor(new StrLookup() {
			@Override
			public String lookup(String key) {
				String value = resolved.get(key);

				if (ArgUtil.isEmpty(value)) {
					value = getAppEnvProperties(key);
				}
				return value;
			}
		});
		for (String k : ordered.keySet()) {
			String value = sub.replace(ordered.get(k));
			resolved.put(k, value);
		}
		return resolved;
	}

	private static Properties getPropertiesInternal(String tenant, Object object) {
		LOGGER.debug("getPropertiesInternal:START");
		Properties tenantProperties = new Properties();
		String propertyFile = "application." + tenant + ".properties";
		InputStream inSideInputStream = null;
		InputStream outSideInputStream = null;

		try {
			LOGGER.debug("Loading Properties from classpath: {}", propertyFile);
			URL ufile = FileUtil.getResource(propertyFile, object.getClass());
			if (ufile != null) {
				inSideInputStream = ufile.openStream();
				// tenantProperties.load(inSideInputStream);
				tenantProperties.putAll(loadPropertiesMap(inSideInputStream));
				LOGGER.info("Loaded Properties from classpath: {}", ufile.getPath());
			}

		} catch (IllegalArgumentException | IOException e) {
			LOGGER.error("Fail:inSideInputStream:getResource", e);
		} finally {
			try {
				if (inSideInputStream != null) {
					inSideInputStream.close();
				}
			} catch (IOException e) {
				LOGGER.debug("Silent_Fail_of_File_Closing_InSideInputStream");
			}
		}

		try {
			LOGGER.debug("Loading Properties from jarpath: {}", propertyFile);
			outSideInputStream = FileUtil.getExternalResourceAsStream(propertyFile, object.getClass());
			if (outSideInputStream != null) {
				// tenantProperties.load(outSideInputStream);
				tenantProperties.putAll(loadPropertiesMap(outSideInputStream));
				LOGGER.info("Loaded Properties from jarpath: {}", propertyFile);
			}

		} catch (IllegalArgumentException | IOException e) {
			LOGGER.error("Fail:outSideInputStream:getExternalResourceAsStream", e);
		} finally {
			try {
				if (outSideInputStream != null) {
					outSideInputStream.close();
				}
			} catch (IOException e) {
				LOGGER.debug("Silent_Fail_of_File_Closing_OutSideInputStream");
			}
		}
		LOGGER.debug("getPropertiesInternal:END");
		return tenantProperties;
	}

	public static Properties getProperties(String tenant, Object object) {
		if (APP_ENV == null) {
			APP_ENV = getPropertiesInternal("env", obj);
		}
		return getPropertiesInternal(tenant, object);
	}

	public Properties getProperties() {
		if (properties == null) {
			properties = getProperties(TenantContextHolder.currentSite().toString().toLowerCase(), obj);
		}
		return properties;
	}

	public static Object assignValues(String tenant, Object object) {
		Properties tenantProperties = getProperties(tenant, object);
		String currentPropertyName = null;
		try {
			Class<?> clazz = AopProxyUtils.ultimateTargetClass(object);

			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(TenantValue.class)) {
					TenantValue annotation = field.getAnnotation(TenantValue.class);
					String propertyName = annotation.value().replace("${", "").replace("}", "");
					currentPropertyName = propertyName;
					Object propertyValue = tenantProperties.getProperty(propertyName);

					if (propertyValue == null) {
						propertyValue = getAppEnvProperties(propertyName);
					}

					if (propertyValue != null) {
						// ArgUtil.parseAsT(propertyValue, defaultValue, required)
						Type type = field.getGenericType();
						String typeName = type.getTypeName();
						field.setAccessible(true);

						if ("java.lang.String".equals(typeName)) {
							field.set(object, decryptProp(propertyValue));
						} else if ("int".equals(typeName) || "java.lang.Integer".equals(typeName)) {
							field.set(object, ArgUtil.parseAsInteger(propertyValue));
						} else if ("boolean".equals(typeName) || "java.lang.Boolean".equals(typeName)) {
							field.set(object, ArgUtil.parseAsBoolean(propertyValue));
						} else if (Language.class.getName().equals(typeName)) {
							field.set(object, ArgUtil.parseAsEnum(propertyValue, Language.DEFAULT));
						} else if (Tenant.class.getName().equals(typeName)) {
							field.set(object, Tenants.from(propertyValue, Tenants.DEFAULT));
						} else if ("java.lang.String[]".equals(typeName)) {
							field.set(object, ArgUtil.parseAsStringArray(propertyValue));
						} else if (type instanceof Class && ((Class<?>) type).isArray()
								&& ((Class<?>) type).getComponentType().isEnum()) {
							Class<?> componentType = ((Class<?>) type).getComponentType();
							field.set(object, ArgUtil.parseAsEnumArray(propertyValue, componentType));
						} else if (type instanceof Class && ((Class<?>) type).isEnum()) {
							field.set(object, ArgUtil.parseAsEnum(propertyValue, type));
						} else if (Stringable.class.isAssignableFrom((Class<?>) type)
								|| ((Class<?>) type).isAssignableFrom(Stringable.class)) {
							Class<?> cl = Class.forName(typeName);
							Constructor<?> cons = cl.getConstructor();
							Stringable o = (Stringable) cons.newInstance();
							o.fromString(ArgUtil.parseAsString(propertyValue));
							field.set(object, o);
						} else {
							try {
								field.set(object, ArgUtil.parseAsObject((Class<?>) type, propertyValue, true));
							} catch (IllegalArgumentException e) {
								LOGGER.warn("********** Property Type Undefined *****  {} {} = {}", typeName,
										propertyName, propertyValue);
							}
						}
					}
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | ClassCastException | ClassNotFoundException
				| NoSuchMethodException | SecurityException | InstantiationException | InvocationTargetException e) {
			LOGGER.error("readPropertyException {}", currentPropertyName, e);
		}
		return object;
	}

}
