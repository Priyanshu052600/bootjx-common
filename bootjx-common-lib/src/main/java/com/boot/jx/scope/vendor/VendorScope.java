package com.boot.jx.scope.vendor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class VendorScope implements Scope {

	private static final Logger LOGGER = LoggerFactory.getLogger(VendorScope.class);

	private Map<String, Object> scopedObjects = Collections.synchronizedMap(new HashMap<String, Object>());
	private Map<String, Runnable> destructionCallbacks = Collections.synchronizedMap(new HashMap<String, Runnable>());

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		String vendor = VendorContext.getVendor();
		String nameKey = name + "=" + vendor;
		if (!scopedObjects.containsKey(nameKey)) {
			scopedObjects.put(nameKey, this.assignValues(vendor, objectFactory.getObject()));
			LOGGER.info("Vendor bean registered {}", nameKey);
		}
		return scopedObjects.get(nameKey);
	}

	@Override
	public Object remove(String name) {
		String nameKey = getNameKey(name);
		destructionCallbacks.remove(nameKey);
		return scopedObjects.remove(nameKey);
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
		return name + "=" + VendorContext.getVendor();
	}

	@Override
	public String getConversationId() {
		return null;
	}

	private Object assignValues(String vendor, Object object) {
		return VendorProperties.assignValues(vendor, object);
	}

}