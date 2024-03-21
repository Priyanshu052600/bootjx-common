package com.boot.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;

public class ClazzUtil {

	private final static Map<String, Class<?>> cache = new HashMap<String, Class<?>>();

	@SuppressWarnings("unchecked")
	public static <T> Class<T> fromName(String clzName) {
		Class<?> clz = cache.get(clzName);
		if (clz != null)
			return (Class<T>) clz;
		try {
			clz = Class.forName(clzName);
			cache.put(clzName, clz);
		} catch (Exception e) {
		}
		return (Class<T>) clz;
	}

	public static Pattern getGenericTypePattern(Class<?> clazz) {
		return Pattern.compile("^" + clazz.getName() + "<(.*)>$");
	}

	public static String getClassName(Object target) {
		return target.getClass().getName().split("\\$")[0];
	}

	public static String getSimpleClassName(Object target) {
		return target.getClass().getSimpleName().split("\\$")[0];
	}

	public static <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> annotationClass) {
		final A annotation = clazz.getAnnotation(annotationClass);
		if (annotation == null && (clazz.isSynthetic())) { // OK, this is probably proxy
			return getAnnotation(clazz.getSuperclass(), annotationClass);
		} else {
			return annotation;
		}
	}

	public static <A extends Annotation> A getAnnotationFromBean(Object candidate, Class<A> annotationClass) {
		Class<?> c = AopProxyUtils.ultimateTargetClass(candidate);
		return getAnnotation(c, annotationClass);
	}

	public static String getUltimateClassName(Object target) {
		Class<?> c = AopProxyUtils.ultimateTargetClass(target);
		return c.getName().split("\\$")[0];
	}

	public static Class<?> getUltimateClass(Object target) {
		Class<?> c = AopProxyUtils.ultimateTargetClass(target);
		return c;
	}

	public static List<Field> getAllFields(Class<?> clazz, List<Field> fields) {
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

		if (clazz.getSuperclass() != null) {
			getAllFields(clazz.getSuperclass(), fields);
		}
		return fields;
	}

	public static Field[] getAllFields(Class<?> clazz) {
		List<Field> list = getAllFields(clazz, new LinkedList<Field>());
		return list.toArray(new Field[list.size()]);
	}

	public static <A extends Annotation> A findMethodAnnotation(Object target, Method method,
			Class<A> annotationClass) {
		A annotation = AnnotatedElementUtils.getMergedAnnotation(method, annotationClass);
		if (annotation != null) {
			return annotation;
		} else {
			// Try to find annotation on proxied class
			Class<?> targetClass = AopUtils.getTargetClass(target);
			if (targetClass != null && !target.getClass().equals(targetClass)) {
				try {
					Method methodOnTarget = targetClass.getMethod(method.getName(), method.getParameterTypes());
					return AnnotatedElementUtils.getMergedAnnotation(methodOnTarget, annotationClass);
				} catch (NoSuchMethodException e) {
					return null;
				}
			} else {
				return null;
			}
		}
	}
}
