package com.boot.jx.rest;

import java.util.HashMap;
import java.util.Map;

import com.boot.utils.EntityDtoUtil;

public class RequestMetaInfo<T extends RequestMetaInfo<T>> extends ARequestMetaInfo {

	private static final long serialVersionUID = 3249496088642457400L;

	public static class CommonRequestMetaInfo extends RequestMetaInfo<CommonRequestMetaInfo> {
		private static final long serialVersionUID = -2579877382913638573L;
		public Map<String, Object> map;

		public Map<String, Object> getMap() {
			return map;
		}

		public void setMap(Map<String, Object> map) {
			this.map = map;
		}

		public Map<String, Object> map() {
			if (this.map == null) {
				this.map = new HashMap<String, Object>();
			}
			return this.map;
		}

		public CommonRequestMetaInfo set(String key, Object value) {
			this.map().put(key, value);
			return this;
		}

		public CommonRequestMetaInfo get(String key) {
			this.map().get(key);
			return this;
		}
	}

	@SuppressWarnings("unchecked")
	public T copyFrom(T requestMetaInfo) {
		EntityDtoUtil.copyProperties(this, requestMetaInfo);
		return (T) this;
	}

	public T copyTo(T requestMetaInfo) {
		EntityDtoUtil.copyProperties(requestMetaInfo, this);
		return requestMetaInfo;
	}

	@SuppressWarnings("unchecked")
	public T copy() {
		try {
			return this.copyTo((T) this.getClass().newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Class<T> classT() {
		return (Class<T>) this.getClass();
	}

}
