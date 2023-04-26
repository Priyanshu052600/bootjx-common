package com.boot.jx.scope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import com.boot.utils.ContextUtil;

public class ThreadScope implements Scope {

	private Map<String, Runnable> destructionCallbacks = Collections.synchronizedMap(new HashMap<String, Runnable>());

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		Map<String, Object> map = ContextUtil.map();
		if (!map.containsKey(name)) {
			map.put(name, objectFactory.getObject());
		}
		return map.get(name);
	}

	@Override
	public Object remove(String name) {
		destructionCallbacks.remove(name);
		return ContextUtil.map().remove(name);
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		String nameKey = getNameKey(name);
		destructionCallbacks.put(nameKey, callback);
	}

	@Override
	public Object resolveContextualObject(String key) {
		return null;
	}

	private String getNameKey(String name) {
		return getConversationId() + name;
	}

	@Override
	public String getConversationId() {
		return ContextUtil.getTraceId(false);
	}

}