package com.boot.utils;

import com.boot.jx.exception.AmxException;

public class AssertUtil {
	private AssertUtil() {
		throw new IllegalStateException("This is a utility class with static methods and should not be instantiated");
	}

	public static void asNotNull(Object obj, String message) {
		if (ArgUtil.isEmpty(obj)) {
			throw new AmxException(message);
		}
	}
}
