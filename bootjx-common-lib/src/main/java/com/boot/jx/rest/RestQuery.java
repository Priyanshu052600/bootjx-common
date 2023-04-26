package com.boot.jx.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.boot.jx.logger.LoggerService;
import com.boot.model.UtilityModels.Stringable;

public class RestQuery implements Stringable {

	private static Logger LOGGER = LoggerService.getLogger(RestQuery.class);

	private Map<String, String> map = null;

	@Override
	public void fromString(String testString) {
		try {
			this.map = splitQuery(testString);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("String to Query Conversion Exception", e);
		}
	}

	public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
					URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
		}
		return query_pairs;
	}

	public Map<String, String> toMap() {
		return map;
	}

}