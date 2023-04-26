package com.boot.jx.dict;

import java.util.HashMap;
import java.util.Map;

import com.boot.jx.exception.IMessageKey;
import com.boot.utils.ArgUtil;

public class MessageDictionary {

	public static final Map<String, Map<String, IMessageKey>> MAP = new HashMap<String, Map<String, IMessageKey>>();

	public static <T extends IMessageKey> void map(IMessageKey messageKey, String... matchings) {

		String className = messageKey.getClass().getName();
		Map<String, IMessageKey> dict = MAP.get(className);
		if (ArgUtil.isEmpty(dict)) {
			dict = new HashMap<String, IMessageKey>();
			MAP.put(className, dict);
		}

	}

}
