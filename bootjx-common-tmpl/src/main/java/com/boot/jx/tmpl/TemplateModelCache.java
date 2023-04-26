package com.boot.jx.tmpl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.boot.jx.cache.CacheBox;

/**
 * The Class LoggedInUsers.
 */
@Component
public class TemplateModelCache extends CacheBox<Map<String, Object>> {

	/**
	 * Instantiates a new logged in users.
	 */
	public TemplateModelCache() {
		super("TemplateModelCache");
	}

}
