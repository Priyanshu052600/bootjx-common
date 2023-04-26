package com.boot.model;

import com.boot.model.MapModel.NodeEntry;
import com.boot.utils.StringUtils;

public class SafeString extends NodeEntry<String> {

	public SafeString(String str) {
		super(str);
	}

	public SafeString trim() {
		this.value = StringUtils.trim(value);
		return this;
	}

	public boolean startsWith(String prefix) {
		if (this.value == null) {
			return false;
		}
		return this.value.startsWith(prefix);
	}

	public boolean endsWith(String suffix) {
		if (value == null) {
			return false;
		}
		return value.endsWith(suffix);
	}

	public static SafeString from(String str) {
		return new SafeString(str);
	}
}