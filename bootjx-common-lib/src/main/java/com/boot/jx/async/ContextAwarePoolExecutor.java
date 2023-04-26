package com.boot.jx.async;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import com.boot.jx.AppContextUtil;

public class ContextAwarePoolExecutor extends ThreadPoolTaskExecutor {

	private static final long serialVersionUID = -3516799159622280946L;

	public <T> ContextAwareCallable<T> newContextAwareCallable(Callable<T> task) {
		return new ContextAwareCallable<T>(task, AppContextUtil.getContext());
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return super.submit(newContextAwareCallable(task));
	}

	@Override
	public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
		return super.submitListenable(newContextAwareCallable(task));
	}

}