package com.boot.loaderjs;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Map;

import com.boot.utils.FileUtil;
import com.boot.utils.IoUtils;
import com.boot.utils.JsonUtil;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

public class JsonDiffTest { // Noncompliant

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ParseException, IOException {
		InputStream leftStream = FileUtil.getExternalOrInternalResourceAsStream("dummy/json-1.json",
				JsonDiffTest.class);
		InputStream rightStream = FileUtil.getExternalOrInternalResourceAsStream("dummy/json-2.json",
				JsonDiffTest.class);

		String leftJson = IoUtils.inputstream_to_string(leftStream);

		String rightJson = IoUtils.inputstream_to_string(rightStream);

		Map<String, Object> leftMap = JsonUtil.fromJsonToMap(leftJson);

		Map<String, Object> rightMap = JsonUtil.fromJsonToMap(rightJson);

		MapDifference<String, Object> difference = Maps.difference(leftMap, rightMap);

		System.out.println(JsonUtil.toJson(difference.entriesDiffering()));

	}

}
