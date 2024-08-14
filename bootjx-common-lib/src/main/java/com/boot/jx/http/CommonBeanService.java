package com.boot.jx.http;

import java.io.Serializable;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.boot.utils.ArgUtil;

@Component
public class CommonBeanService implements Serializable {

	private static final long serialVersionUID = 8303255208018015687L;

	@Autowired
	private ApplicationContext applicationContext;

	public boolean isBeanPresentByName(String beanName) {

		// Check if the bean exists in the session/request scope
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {

			// Check for request-scoped bean
			Object requestBean = requestAttributes.getAttribute(beanName, RequestAttributes.SCOPE_REQUEST);
			if (requestBean != null) {
				return true;
			}

			// Check for session-scoped bean
			Object sessionBean = requestAttributes.getAttribute(beanName, RequestAttributes.SCOPE_SESSION);
			if (sessionBean != null) {
				return true;
			}
		}

		// Check if the bean exists in the application context
		if (applicationContext.containsBean(beanName)) {
			return true;
		}

		// Bean is not present in either the application context or the session scope
		return false;
	}

	public boolean isBeanPresentByType(Class<?> beanType) {

		// Check if the bean exists in the session scope by type
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {

			// Check for request-scoped bean by type
			for (String beanName : applicationContext.getBeanNamesForType(beanType)) {
				Object requestBean = requestAttributes.getAttribute(beanName, RequestAttributes.SCOPE_REQUEST);
				if (requestBean != null && beanType.isInstance(requestBean)) {
					return true;
				}
			}

			// Check for session-scoped bean by type
			for (String beanName : applicationContext.getBeanNamesForType(beanType)) {
				Object sessionBean = requestAttributes.getAttribute(beanName, RequestAttributes.SCOPE_SESSION);
				if (sessionBean != null && beanType.isInstance(sessionBean)) {
					return true;
				}
			}
		}

		// Check if the bean exists in the application context by type
		if (applicationContext.getBeanNamesForType(beanType).length > 0) {
			return true;
		}

		// Bean is not present in either the application context or the session scope
		return false;
	}

	public boolean isBeanPresent(Object bean) {
		if (!ArgUtil.is(bean)) {
			return false;
		}

		Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);

		String[] beanNames = applicationContext.getBeanNamesForType(clazz);

		// Check if the bean is present in the request or session scope
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			// Check for request-scoped bean
			for (String beanName : beanNames) {
				Object requestBean = requestAttributes.getAttribute(beanName, RequestAttributes.SCOPE_REQUEST);
				if (requestBean == bean) {
					return true;
				}
			}

			// Check for session-scoped bean
			for (String beanName : beanNames) {
				Object sessionBean = requestAttributes.getAttribute(beanName, RequestAttributes.SCOPE_SESSION);
				if (sessionBean == bean) {
					return true;
				}
			}
		}

		// Check if the bean is present in the application context
		for (String beanName : beanNames) {
			if (applicationContext.getBean(beanName) == bean) {
				return true;
			}
		}

		return false;
	}
}
