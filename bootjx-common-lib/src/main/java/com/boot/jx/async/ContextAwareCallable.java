package com.boot.jx.async;

import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.MDC;

import com.boot.jx.AppContext;
import com.boot.jx.AppContextUtil;
import com.boot.jx.logger.LoggerService;

public class ContextAwareCallable<T> implements Callable<T> {

	Logger LOGGER = LoggerService.getLogger(ContextAwareCallable.class);

	private Callable<T> task;
	private AppContext context = null;
	Map<String, String> contextMap;

	public ContextAwareCallable(Callable<T> task, AppContext context) {
		this.task = task;
		this.context = context;
		this.contextMap = MDC.getCopyOfContextMap();
	}

	@Override
	public T call() throws Exception {
		MDC.setContextMap(contextMap);
		if (context != null) {
			AppContextUtil.setContext(context);
			AppContextUtil.init();
		}
		try {
			return task.call();
		} catch (Exception e) {
			LOGGER.error("task.call", e);
			throw new Exception(e);
		} finally {
			AppContextUtil.clear();
			MDC.clear();
		}
	}
}
