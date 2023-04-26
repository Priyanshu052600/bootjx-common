package com.boot.jx.exception;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

public class ExceptionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionFactory.class);

    private static Map<String, AmxApiException> map = new HashMap<String, AmxApiException>();

    private static Map<String, AmxApiException> clasmap = new HashMap<String, AmxApiException>();

    public static void register(AmxApiException exc) {
	clasmap.put(exc.getClass().getName(), exc);
    }

    public static void register(String key, AmxApiException exc) {
	clasmap.put(key, exc);
    }

    public static AmxApiException get(String key) {
	return clasmap.get(key);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void readExceptions() {

	ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
	provider.addIncludeFilter(new AssignableTypeFilter(AmxApiException.class));

	Set<BeanDefinition> components = provider.findCandidateComponents("com/amx");
	AmxApiError amxApiError = new AmxApiError();
	for (BeanDefinition component : components) {
	    try {
		Class cls = Class.forName(component.getBeanClassName());
		Constructor<?> ctor = cls.getConstructor(AmxApiError.class);
		Object object = ctor.newInstance(new Object[] { amxApiError });
		if (object != null) {
		    register((AmxApiException) object);
		}
	    } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
		    | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		LOGGER.error("No Default Constructor {}(AmxApiError apiError)", component.getBeanClassName(), e);
	    }
	}

    }

}
