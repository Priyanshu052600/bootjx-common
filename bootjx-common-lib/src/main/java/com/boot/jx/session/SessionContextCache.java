package com.boot.jx.session;

import java.util.Map;

public interface SessionContextCache {
	public Map<String, Object> getContext();

	public void setContext(Map<String, Object> map);
}
