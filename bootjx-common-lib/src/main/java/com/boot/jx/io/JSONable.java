package com.boot.jx.io;

import com.boot.utils.JsonUtil;

public interface JSONable {

	default String toJSON() {
		return JsonUtil.toJson(this);
	}

}
