package com.boot.jx.exception;

import java.util.HashMap;
import java.util.Map;

import com.boot.jx.api.ApiFieldError;
import com.boot.jx.types.Dnum;
import com.boot.utils.ArgUtil;

public class ExceptionMessageKey extends Dnum<ExceptionMessageKey> implements IMessageKey {

	public static class MessageKey implements IMessageKey {

		String output;

		public MessageKey(String output) {
			this.output = output;
		}

		@Override
		public String toString() {
			return this.output;
		}
	}

	public static final Map<String, String> MAP = new HashMap<String, String>();

	public static final ExceptionMessageKey NULL_NOT_ALLOWED = new ExceptionMessageKey("NULL_NOT_ALLOWED", 1,
			"may not be null", "NotNUll");

	public static int ordinalCounter = 0;

	int argCount = 0;
	String messageKeyFormat;

	public ExceptionMessageKey(String messageKeyName, int argCount, String... matchings) {
		super(messageKeyName, ordinalCounter++);
		this.argCount = argCount;
		for (String matching : matchings) {
			MAP.put(matching.toLowerCase(), messageKeyName);
		}
		StringBuilder builder = new StringBuilder(messageKeyName);
		for (int i = 0; i < argCount; i++) {
			builder.append(":%s");
		}
		messageKeyFormat = builder.toString();
	}

	public ExceptionMessageKey(IExceptionEnum exceptionEnum, int argCount, String... matchings) {
		super(exceptionEnum.getStatusKey(), ordinalCounter++);
	}

	public int getArgCount() {
		return argCount;
	}

	public String get(Object... args) {
		if (args.length == this.argCount) {
			return String.format(this.messageKeyFormat, args);
		} else {
			return build(this.name(), args);
		}
	}

	public static String build(String exceptionEnumString, Object... values) {
		final String DELIM = ":";
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(exceptionEnumString).append(DELIM);
		for (int i = 0; i < values.length; i++) {
			if (i == (values.length - 1)) {
				sbuf.append(values[i]);
			} else {
				sbuf.append(values[i]).append(DELIM);
			}
		}
		return sbuf.toString();
	}

	public static IMessageKey build(IExceptionEnum exceptionEnum, Object... values) {
		return new MessageKey(build(exceptionEnum.getStatusKey(), values));
	}

	@Override
	public String toString() {
		return this.messageKeyFormat;
	}

	public static <E> Dnum<? extends Dnum<?>>[] values() {
		return values(ExceptionMessageKey.class);
	}

	public static ExceptionMessageKey valueOf(String name) {
		if (ArgUtil.isEmpty(name)) {
			return null;
		}
		String exceptionMessageKeyStr = MAP.get(name.toLowerCase());
		if (!ArgUtil.isEmpty(exceptionMessageKeyStr)) {
			return fromString(ExceptionMessageKey.class, exceptionMessageKeyStr);
		}
		return null;
	}

	public static void resolveLocalMessage(AmxApiError apiError) {
		if (apiError.getErrors() != null && apiError.getErrors().size()>0) {
			ApiFieldError fieldError = apiError.getErrors().get(0);
			if (fieldError != null) {
				if (ArgUtil.isEmpty(apiError.getMessageKey())) {
					ExceptionMessageKey exceptionMessageKey = ExceptionMessageKey.valueOf(fieldError.getDescription());
					if (exceptionMessageKey != null) {
						apiError.setMessageKey(exceptionMessageKey
								.get(ArgUtil.nonEmpty(fieldError.getField(), fieldError.getObzect()))
								.toString());
					} else {
						exceptionMessageKey = ExceptionMessageKey.valueOf(fieldError.getCode());
						if (exceptionMessageKey != null) {
							apiError.setMessageKey(exceptionMessageKey
									.get(ArgUtil.nonEmpty(fieldError.getField(), fieldError.getObzect()))
									.toString());
						}
					}
				}
				if (ArgUtil.isEmpty(apiError.getMessage())) {
					apiError.setMessage(fieldError.getDescription());
				}
			}
		}
	}

}
