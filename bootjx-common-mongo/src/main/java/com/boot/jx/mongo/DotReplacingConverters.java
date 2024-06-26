package com.boot.jx.mongo;

import java.util.Map;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

public class DotReplacingConverters {

	public static final String DOT_REPLACEMENT = "#dot#";

	@WritingConverter
	public static class DotReplacingWriter implements Converter<Map<String, Object>, Document> {

		@Override
		public Document convert(Map<String, Object> source) {
			Document document = new Document();
			for (Map.Entry<String, Object> entry : source.entrySet()) {
				document.put(entry.getKey().replace(".", DOT_REPLACEMENT), entry.getValue());
			}
			return document;
		}
	}

	@ReadingConverter
	public static class DotReplacingReader implements Converter<Document, Map<String, Object>> {

		@Override
		public Map<String, Object> convert(Document source) {
			Document document = new Document();
			for (Map.Entry<String, Object> entry : source.entrySet()) {
				document.put(entry.getKey().replace(DOT_REPLACEMENT, "."), entry.getValue());
			}
			return document;
		}
	}
}
