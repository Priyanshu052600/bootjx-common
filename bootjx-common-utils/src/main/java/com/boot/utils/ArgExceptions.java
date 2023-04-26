package com.boot.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A factory for creating ArgException objects.
 */
public final class ArgExceptions {
	// generic types
	/** The Constant PARAMETER_MISSING. */
	public static final String PARAMETER_MISSING = "01.001";

	/** The Constant PARAMETER_INVALID. */
	public static final String PARAMETER_INVALID = "01.002";

	/**
	 * Instantiates a new arg exception factory.
	 */
	private ArgExceptions() {
		throw new IllegalStateException("This is a utility class with static methods and should not be instantiated");
	}

	/**
	 * The Class ArgException.
	 */
	@SuppressWarnings("serial")
	public static class ArgException extends RuntimeException {

		/** The Constant CODE. */
		public static final String CODE = "code";

		/** The Constant DATA. */
		public static final String DATA_KEY = "data";

		/** The warn local. */
		private static ThreadLocal<Set<String>> warnLocal = new ThreadLocal<Set<String>>() {
			@Override
			protected Set<String> initialValue() {
				return new HashSet<String>();
			}
		};

		/** The error code. */
		private String errorCode;

		/** The data. */
		private Map<String, Object> data = new LinkedHashMap<String, Object>();

		/**
		 * Instantiates a new arg exception.
		 */
		public ArgException() {
		}

		/**
		 * Instantiates a new arg exception.
		 *
		 * @param message the message
		 */
		public ArgException(String message) {
			super(message);
		}

		/**
		 * Instantiates a new arg exception.
		 *
		 * @param exp the exp
		 */
		public ArgException(Throwable exp) {
			super(exp);
		}

		/**
		 * Gets the error code.
		 *
		 * @return the error code
		 */
		public String getErrorCode() {
			return this.errorCode;
		}

		/**
		 * Sets the error code.
		 *
		 * @param errorCode the new error code
		 */
		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		/**
		 * Gets the data.
		 *
		 * @return the data
		 */
		public Map<String, Object> getData() {
			return this.data;
		}

		/**
		 * Errors.
		 *
		 * @return the map
		 */
		public Map<String, Object> errors() {
			Map<String, Object> innerMap = new LinkedHashMap<String, Object>();
			innerMap.put(CODE, this.errorCode);
			innerMap.put(DATA_KEY, this.data);
			return innerMap;
		}

		/**
		 * Warning.
		 *
		 * @param message the message
		 */
		public static void warning(String message) {
			warnLocal.get().add(message);
		}

		/**
		 * Warnings.
		 *
		 * @return the list
		 */
		public static List<String> warnings() {
			List<String> app = new ArrayList<String>(warnLocal.get());
			warnLocal.get().clear();
			return app;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#getMessage()
		 */
		@Override
		public String getMessage() {
			return JsonUtil.toJson(errors());
		}
	}

	/**
	 * The Class ParameterException.
	 */
	@SuppressWarnings("serial")
	public static class ParameterException extends ArgException {

		/** The is missing. */
		protected boolean isMissing;

		/**
		 * Instantiates a new parameter exception.
		 *
		 * @param isMissing the is missing
		 */
		public ParameterException(boolean isMissing) {
			this.isMissing = isMissing;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.spamjs.utils.ArgUtil.ArgException#getMessage()
		 */
		@Override
		public String getMessage() {
			StringBuilder sb = new StringBuilder();
			if (getData().get("key") != null) {
				sb.append(getData().get("key")).append(":");
			}
			sb.append(getMessageOverride());
			return sb.toString();
		}

		/**
		 * Gets the message override.
		 *
		 * @return the message override
		 */
		public String getMessageOverride() {
			return this.isMissing ? "parameter missing"
					: "parameter invalid - requires type " + getData().get("valid_type");
		}
	}

	/**
	 * The Class EnumParameterException.
	 */
	@SuppressWarnings({ "serial", "rawtypes" })
	public static class EnumParameterException extends ParameterException {

		/** The enum value. */
		private Enum enumValue;

		/** The message override. */
		private String messageOverride;

		/**
		 * Instantiates a new enum parameter exception.
		 *
		 * @param isMissing the is missing
		 * @param enumValue the enum value
		 */
		public EnumParameterException(boolean isMissing, Enum enumValue) {
			super(isMissing);
			this.enumValue = enumValue;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.spamjs.utils.ArgUtil.ParameterException#getMessageOverride()
		 */
		@Override
		public String getMessageOverride() {
			if (this.messageOverride == null) {
				StringBuilder sb = new StringBuilder("[");
				for (String element : ArgUtil.getTypeEnum(this.enumValue)) {
					if (sb.length() > 1) {
						sb.append(",");
					}
					sb.append(element);
				}
				sb.append("]");

				this.messageOverride = this.isMissing ? "parameter missing"
						: "parameter invalid - requires one of " + sb.toString();

			}
			return this.messageOverride;
		}
	}

	// return Exception for missing or invalid parameter
	/**
	 * Param missing or invalid.
	 *
	 * @param key          the key
	 * @param value        the value
	 * @param defaultValue the default value
	 * @return the arg exception
	 */
	@SuppressWarnings("rawtypes")
	public static ArgException paramMissingOrInvalid(Object key, Object value, Object defaultValue) {
		ParameterException argException;
		if (defaultValue instanceof Enum) {
			argException = new EnumParameterException(value == null, (Enum) defaultValue);
			argException.getData().put("valid_val", ArgUtil.getTypeEnum((Enum) defaultValue));
		} else {
			argException = new ParameterException(value == null);
			argException.getData().put("valid_type", ArgUtil.getType(defaultValue));
		}
		if (value != null) {
			argException.getData().put("val", value);
			argException.setErrorCode(PARAMETER_INVALID);
		} else {
			argException.setErrorCode(PARAMETER_MISSING);
		}
		if (key != null) {
			argException.getData().put("key", key);
		}
		return argException;
	}

}