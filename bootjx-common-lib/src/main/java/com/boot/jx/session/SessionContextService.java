package com.boot.jx.session;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.model.MapModel;
import com.boot.utils.JsonUtil;

@Component
public class SessionContextService {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired(required = false)
	private SessionContextCache sessionContextCache;

	public MapModel getContext() {
		if (sessionContextCache != null) {
			return new MapModel(sessionContextCache.getContext());
		}
		return new MapModel();
	}

	public <T extends MapModel> MapModel getContext(T map) {
		if (sessionContextCache != null) {
			map.fromMap(sessionContextCache.getContext());
		}
		return map;
	}

	public <T> T getContext(Class<T> toValueType) {
		if (sessionContextCache != null) {
			try {
				return JsonUtil.toObject(sessionContextCache.getContext(), toValueType);
			} catch (Exception e) {
				log.error("Unable to fetch from Redis Session Context");
			}
		}
		return JsonUtil.toObject(new HashMap<String, Object>(), toValueType);
	}

	public void setContext(Object model) {
		if (sessionContextCache != null) {
			sessionContextCache.setContext(JsonUtil.toMap(model));
		}
	}

	public void setContext(MapModel model) {
		if (sessionContextCache != null) {
			sessionContextCache.setContext(model.toMap());
		}
	}
}
