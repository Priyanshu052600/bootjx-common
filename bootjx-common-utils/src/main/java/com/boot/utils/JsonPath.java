package com.boot.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.boot.utils.ArgExceptions.ParameterException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//TODO: Auto-generated Javadoc
/**
 * JsonPath is to Json, what XPath is to XML. The main use of this class is to
 * navigate Json data structure to load/save/delete(map only) entries. Best
 * explained by taking an example to illustrate. <br>
 * ;
 * 
 * <pre>
* Consider the Json: {"key1":{"key2":["val1",{"key3":"val2"}]}}
* 
* JsonPath of "key1" =&gt; {"key2":["val1",{"key3":"val2"}]}
* JsonPath of "key1/key2/[0]" =&gt; val1
* JsonPath of "key1/key2/[1]/key3" =&gt; val2
 * </pre>
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@JsonSerialize(using = JsonPathSerializer.class)
public class JsonPath {

	/** The Constant VALID_TYPE. */
	private static final String VALID_TYPE = "valid_type";

	/** The Constant KEY. */
	private static final String KEY = "key";

	/**
	 * The Interface PathElement.
	 */
	interface PathElement {

		/**
		 * Load.
		 *
		 * @param from the from
		 * @return the object
		 */
		Object load(Object from);

		/**
		 * Save.
		 *
		 * @param from  the from
		 * @param value the value
		 * @return the object
		 */
		Object save(Object from, Object value);
	}

	/**
	 * The Class KeyAccess.
	 */
	private static class KeyAccess implements PathElement {

		/** The key. */
		private final String key;

		/**
		 * Instantiates a new key access.
		 *
		 * @param key the key
		 */
		public KeyAccess(String key) {
			this.key = key;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.spamjs.utils.JsonPath.PathElement#load(java.lang.Object)
		 */
		@Override
		public Object load(Object from) {
			if (from instanceof Map) {
				return ((Map) from).get(this.key);
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.spamjs.utils.JsonPath.PathElement#save(java.lang.Object,
		 * java.lang.Object)
		 */
		@Override
		public Object save(Object from, Object value) {
			if (from == null) {
				from = new LinkedHashMap<String, Object>();
			}
			if (from instanceof Map) {
				((Map) from).put(this.key, value);
				return from;
			}
			return null;
		}

		/**
		 * Removes the.
		 *
		 * @param from the from
		 */
		public void remove(Object from) {
			if (from instanceof Map) {
				((Map) from).remove(this.key);
			}
		}
	}

	/**
	 * The Class ArrayAccess.
	 */
	private static class ArrayAccess implements PathElement {

		/** The index. */
		private final int index;

		/**
		 * Instantiates a new array access.
		 *
		 * @param key the key
		 */
		public ArrayAccess(String key) {
			this.index = Integer.parseInt(key.substring(1, key.length() - 1));
		}

		/**
		 * Exists.
		 *
		 * @param key the key
		 * @return true, if successful
		 */
		public static boolean exists(String key) {
			return key.startsWith("[") && key.endsWith("]");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.spamjs.utils.JsonPath.PathElement#load(java.lang.Object)
		 */
		@Override
		public Object load(Object from) {
			if (from instanceof Object[]) {
				return ((Object[]) from)[this.index];
			}
			return CollectionUtil.getArray(((List<Object>) from), this.index);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.spamjs.utils.JsonPath.PathElement#save(java.lang.Object,
		 * java.lang.Object)
		 */
		@Override
		public Object save(Object from, Object value) {
			if (from == null) {
				from = new ArrayList<Object>();
			}
			if (from instanceof List) {
				CollectionUtil.putArray(((List) from), this.index, value);
				return from;
			}
			return null;
		}
	}

	/** The reference. */
	private final String reference;

	/** The paths. */
	private final ArrayList<PathElement> paths;

	/**
	 * Instantiates a new json path.
	 *
	 * @param reference the reference
	 */
	public JsonPath(String reference) {

		reference = reference.replaceAll("\\.", "/");
		this.reference = StringUtils.trim(reference, '/');
		this.paths = new ArrayList<PathElement>();
		StringTokenizer tokenizer = new StringTokenizer(reference, "/");
		while (tokenizer.hasMoreTokens()) {
			String tokenStr = tokenizer.nextToken();
			if (tokenStr == null || "".equals(tokenStr.trim())) {
				continue;
			}
			if (ArrayAccess.exists(tokenStr)) {
				this.paths.add(new ArrayAccess(tokenStr));
			} else {
				this.paths.add(new KeyAccess(tokenStr));
			}
		}
	}

	/**
	 * Exists.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	public static boolean exists(String value) {
		return value != null && value.startsWith("${") && value.endsWith("}");
	}

	/**
	 * Creates the.
	 *
	 * @param reference the reference
	 * @return the json path
	 */
	public static JsonPath create(String reference) {
		return new JsonPath(reference.substring(2, reference.length() - 1));
	}

	/**
	 * Gets the reference.
	 *
	 * @return the reference
	 */
	public String getReference() {
		return this.reference;
	}

	// load and loadRequired method of T
	/**
	 * Load.
	 *
	 * @param <T>          the generic type
	 * @param properties   the properties
	 * @param defaultValue the default value
	 * @return the t
	 */
	public <T> T load(Map<String, Object> properties, T defaultValue) {
		return loadInternal(properties, null, defaultValue, false);
	}

	/**
	 * Returns null if value is absent.
	 *
	 * @param <T>          the generic type
	 * @param properties   the properties
	 * @param defaultValue the default value
	 * @return the t
	 */
	public <T> T loadNullable(Map<String, Object> properties, T defaultValue) {
		Object value = internalLoad(properties, null);
		if (value == null) {
			return null;
		}

		return ArgUtil.parseAsT(value, defaultValue, false);

	}

	/**
	 * Load required.
	 *
	 * @param <T>          the generic type
	 * @param properties   the properties
	 * @param defaultValue the default value
	 * @return the t
	 */
	public <T> T loadRequired(Map<String, Object> properties, T defaultValue) {
		return loadInternal(properties, null, defaultValue, true);
	}

	/**
	 * Load.
	 *
	 * @param <T>           the generic type
	 * @param properties    the properties
	 * @param refProperties the ref properties
	 * @param defaultValue  the default value
	 * @return the t
	 */
	public <T> T load(Map<String, Object> properties, Map<String, Object> refProperties, T defaultValue) {
		return loadInternal(properties, refProperties, defaultValue, false);
	}

	/**
	 * Load nullable.
	 *
	 * @param <T>           the generic type
	 * @param properties    the properties
	 * @param refProperties the ref properties
	 * @param defaultValue  the default value
	 * @return the t
	 */
	public <T> T loadNullable(Map<String, Object> properties, Map<String, Object> refProperties, T defaultValue) {
		Object value = internalLoad(properties, refProperties);
		if (value == null) {
			return null;
		}

		return ArgUtil.parseAsT(value, defaultValue, false);
	}

	/**
	 * Load required.
	 *
	 * @param <T>           the generic type
	 * @param properties    the properties
	 * @param refProperties the ref properties
	 * @param defaultValue  the default value
	 * @return the t
	 */
	public <T> T loadRequired(Map<String, Object> properties, Map<String, Object> refProperties, T defaultValue) {
		return loadInternal(properties, refProperties, defaultValue, true);
	}

	/**
	 * Load internal.
	 *
	 * @param <T>           the generic type
	 * @param properties    the properties
	 * @param refProperties the ref properties
	 * @param defaultValue  the default value
	 * @param required      the required
	 * @return the t
	 */
	public <T> T loadInternal(Map<String, Object> properties, Map<String, Object> refProperties, T defaultValue,
			boolean required) {
		try {
			return ArgUtil.parseAsT(internalLoad(properties, refProperties), defaultValue, required);
		} catch (ParameterException e) {
			e.getData().put(KEY, this.reference);
			throw e;
		}
	}

	// load and loadRequired method of List<T>
	/**
	 * Load list.
	 *
	 * @param <T>          the generic type
	 * @param properties   the properties
	 * @param defaultValue the default value
	 * @return the list
	 */
	public <T> List<T> loadList(Map<String, Object> properties, T defaultValue) {
		return loadInternalList(properties, null, defaultValue, ((List<T>) Constants.EMPTY_LIST), false);
	}

	/**
	 * Load list.
	 *
	 * @param <T>              the generic type
	 * @param properties       the properties
	 * @param defaultValue     the default value
	 * @param defaultListValue the default list value
	 * @return the list
	 */
	public <T> List<T> loadList(Map<String, Object> properties, T defaultValue, List<T> defaultListValue) {
		return loadInternalList(properties, null, defaultValue, defaultListValue, false);
	}

	/**
	 * Load list nullable.
	 *
	 * @param <T>          the generic type
	 * @param properties   the properties
	 * @param defaultValue the default value
	 * @return the list
	 */
	public <T> List<T> loadListNullable(Map<String, Object> properties, T defaultValue) {
		List<T> value = loadInternalList(properties, null, defaultValue, ((List<T>) Constants.EMPTY_LIST), false);
		if (value == Constants.EMPTY_LIST) {
			return null;
		}
		return value;
	}

	/**
	 * Load list nullable.
	 *
	 * @param <T>              the generic type
	 * @param properties       the properties
	 * @param defaultValue     the default value
	 * @param defaultListValue the default list value
	 * @return the list
	 */
	public <T> List<T> loadListNullable(Map<String, Object> properties, T defaultValue, List<T> defaultListValue) {
		List<T> value = loadInternalList(properties, null, defaultValue, defaultListValue, false);
		if (value == defaultListValue) {
			return null;
		}
		return value;
	}

	/**
	 * Load required list.
	 *
	 * @param <T>          the generic type
	 * @param properties   the properties
	 * @param defaultValue the default value
	 * @return the list
	 */
	public <T> List<T> loadRequiredList(Map<String, Object> properties, T defaultValue) {
		return loadInternalList(properties, null, defaultValue, ((List<T>) Constants.EMPTY_LIST), true);
	}

	/**
	 * Load required list.
	 *
	 * @param <T>              the generic type
	 * @param properties       the properties
	 * @param defaultValue     the default value
	 * @param defaultListValue the default list value
	 * @return the list
	 */
	public <T> List<T> loadRequiredList(Map<String, Object> properties, T defaultValue, List<T> defaultListValue) {
		return loadInternalList(properties, null, defaultValue, defaultListValue, true);
	}

	/**
	 * Load list.
	 *
	 * @param <T>           the generic type
	 * @param properties    the properties
	 * @param refProperties the ref properties
	 * @param defaultValue  the default value
	 * @return the list
	 */
	public <T> List<T> loadList(Map<String, Object> properties, Map<String, Object> refProperties, T defaultValue) {
		return loadInternalList(properties, refProperties, defaultValue, ((List<T>) Constants.EMPTY_LIST), false);
	}

	/**
	 * Load list.
	 *
	 * @param <T>              the generic type
	 * @param properties       the properties
	 * @param refProperties    the ref properties
	 * @param defaultValue     the default value
	 * @param defaultListValue the default list value
	 * @return the list
	 */
	public <T> List<T> loadList(Map<String, Object> properties, Map<String, Object> refProperties, T defaultValue,
			List<T> defaultListValue) {
		return loadInternalList(properties, refProperties, defaultValue, defaultListValue, false);
	}

	/**
	 * Load list nullable.
	 *
	 * @param <T>           the generic type
	 * @param properties    the properties
	 * @param refProperties the ref properties
	 * @param defaultValue  the default value
	 * @return the list
	 */
	public <T> List<T> loadListNullable(Map<String, Object> properties, Map<String, Object> refProperties,
			T defaultValue) {
		List<T> value = loadInternalList(properties, refProperties, defaultValue, ((List<T>) Constants.EMPTY_LIST),
				false);
		if (value == Constants.EMPTY_LIST) {
			return null;
		}
		return value;
	}

	/**
	 * Load list nullable.
	 *
	 * @param <T>              the generic type
	 * @param properties       the properties
	 * @param refProperties    the ref properties
	 * @param defaultValue     the default value
	 * @param defaultListValue the default list value
	 * @return the list
	 */
	public <T> List<T> loadListNullable(Map<String, Object> properties, Map<String, Object> refProperties,
			T defaultValue, List<T> defaultListValue) {
		List<T> value = loadInternalList(properties, refProperties, defaultValue, defaultListValue, false);
		if (value == defaultListValue) {
			return null;
		}
		return value;
	}

	/**
	 * Load required list.
	 *
	 * @param <T>           the generic type
	 * @param properties    the properties
	 * @param refProperties the ref properties
	 * @param defaultValue  the default value
	 * @return the list
	 */
	public <T> List<T> loadRequiredList(Map<String, Object> properties, Map<String, Object> refProperties,
			T defaultValue) {
		return loadInternalList(properties, refProperties, defaultValue, ((List<T>) Constants.EMPTY_LIST), true);
	}

	/**
	 * Load required list.
	 *
	 * @param <T>              the generic type
	 * @param properties       the properties
	 * @param refProperties    the ref properties
	 * @param defaultValue     the default value
	 * @param defaultListValue the default list value
	 * @return the list
	 */
	public <T> List<T> loadRequiredList(Map<String, Object> properties, Map<String, Object> refProperties,
			T defaultValue, List<T> defaultListValue) {
		return loadInternalList(properties, refProperties, defaultValue, defaultListValue, true);
	}

	/**
	 * Load internal list.
	 *
	 * @param <T>              the generic type
	 * @param properties       the properties
	 * @param refProperties    the ref properties
	 * @param defaultValue     the default value
	 * @param defaultListValue the default list value
	 * @param required         the required
	 * @return the list
	 */
	public <T> List<T> loadInternalList(Map<String, Object> properties, Map<String, Object> refProperties,
			T defaultValue, List<T> defaultListValue, boolean required) {
		try {
			return ArgUtil.parseAsListOfT(internalLoad(properties, refProperties), defaultValue, defaultListValue,
					required);
		} catch (ParameterException e) {
			e.getData().put(KEY, this.reference);
			e.getData().put(VALID_TYPE, "array[" + e.getData().get(VALID_TYPE) + "]");
			throw e;
		}
	}

	// load and loadRequired method of List<List<T>>
	/**
	 * Load list of list.
	 *
	 * @param <T>          the generic type
	 * @param properties   the properties
	 * @param defaultValue the default value
	 * @return the list
	 */
	public <T> List<List<T>> loadListOfList(Map<String, Object> properties, T defaultValue) {
		return loadInternalListOfList(properties, null, defaultValue, (List<T>) Constants.EMPTY_LIST,
				(List<List<T>>) ((Object) Constants.EMPTY_LISTOF_LIST), false);
	}

	/**
	 * Load list of list.
	 *
	 * @param <T>                    the generic type
	 * @param properties             the properties
	 * @param defaultValue           the default value
	 * @param defaultListValue       the default list value
	 * @param defaultListOfListValue the default list of list value
	 * @return the list
	 */
	public <T> List<List<T>> loadListOfList(Map<String, Object> properties, T defaultValue, List<T> defaultListValue,
			List<List<T>> defaultListOfListValue) {
		return loadInternalListOfList(properties, null, defaultValue, defaultListValue, defaultListOfListValue, false);
	}

	/**
	 * Load required list of list.
	 *
	 * @param <T>          the generic type
	 * @param properties   the properties
	 * @param defaultValue the default value
	 * @return the list
	 */
	public <T> List<List<T>> loadRequiredListOfList(Map<String, Object> properties, T defaultValue) {
		return loadInternalListOfList(properties, null, defaultValue, (List<T>) Constants.EMPTY_LIST,
				(List<List<T>>) ((Object) Constants.EMPTY_LISTOF_LIST), true);
	}

	/**
	 * Load required list of list.
	 *
	 * @param <T>                    the generic type
	 * @param properties             the properties
	 * @param defaultValue           the default value
	 * @param defaultListValue       the default list value
	 * @param defaultListOfListValue the default list of list value
	 * @return the list
	 */
	public <T> List<List<T>> loadRequiredListOfList(Map<String, Object> properties, T defaultValue,
			List<T> defaultListValue, List<List<T>> defaultListOfListValue) {
		return loadInternalListOfList(properties, null, defaultValue, defaultListValue, defaultListOfListValue, true);
	}

	/**
	 * Load list of list.
	 *
	 * @param <T>           the generic type
	 * @param properties    the properties
	 * @param refProperties the ref properties
	 * @param defaultValue  the default value
	 * @return the list
	 */
	public <T> List<List<T>> loadListOfList(Map<String, Object> properties, Map<String, Object> refProperties,
			T defaultValue) {
		return loadInternalListOfList(properties, refProperties, defaultValue, (List<T>) Constants.EMPTY_LIST,
				(List<List<T>>) ((Object) Constants.EMPTY_LISTOF_LIST), false);
	}

	/**
	 * Load list of list.
	 *
	 * @param <T>                    the generic type
	 * @param properties             the properties
	 * @param refProperties          the ref properties
	 * @param defaultValue           the default value
	 * @param defaultListValue       the default list value
	 * @param defaultListOfListValue the default list of list value
	 * @return the list
	 */
	public <T> List<List<T>> loadListOfList(Map<String, Object> properties, Map<String, Object> refProperties,
			T defaultValue, List<T> defaultListValue, List<List<T>> defaultListOfListValue) {
		return loadInternalListOfList(properties, refProperties, defaultValue, defaultListValue, defaultListOfListValue,
				false);
	}

	/**
	 * Load required list of list.
	 *
	 * @param <T>           the generic type
	 * @param properties    the properties
	 * @param refProperties the ref properties
	 * @param defaultValue  the default value
	 * @return the list
	 */
	public <T> List<List<T>> loadRequiredListOfList(Map<String, Object> properties, Map<String, Object> refProperties,
			T defaultValue) {
		return loadInternalListOfList(properties, refProperties, defaultValue, (List<T>) Constants.EMPTY_LIST,
				(List<List<T>>) ((Object) Constants.EMPTY_LISTOF_LIST), true);
	}

	/**
	 * Load required list of list.
	 *
	 * @param <T>                    the generic type
	 * @param properties             the properties
	 * @param refProperties          the ref properties
	 * @param defaultValue           the default value
	 * @param defaultListValue       the default list value
	 * @param defaultListOfListValue the default list of list value
	 * @return the list
	 */
	public <T> List<List<T>> loadRequiredListOfList(Map<String, Object> properties, Map<String, Object> refProperties,
			T defaultValue, List<T> defaultListValue, List<List<T>> defaultListOfListValue) {
		return loadInternalListOfList(properties, refProperties, defaultValue, defaultListValue, defaultListOfListValue,
				true);
	}

	/**
	 * Load internal list of list.
	 *
	 * @param <T>                    the generic type
	 * @param properties             the properties
	 * @param refProperties          the ref properties
	 * @param defaultValue           the default value
	 * @param defaultListValue       the default list value
	 * @param defaultListOfListValue the default list of list value
	 * @param required               the required
	 * @return the list
	 */
	public <T> List<List<T>> loadInternalListOfList(Map<String, Object> properties, Map<String, Object> refProperties,
			T defaultValue, List<T> defaultListValue, List<List<T>> defaultListOfListValue, boolean required) {
		try {
			return ArgUtil.parseAsListListOfT(internalLoad(properties, refProperties), defaultValue, defaultListValue,
					defaultListOfListValue, required);
		} catch (ParameterException e) {
			e.getData().put(KEY, this.reference);
			e.getData().put(VALID_TYPE, "array[array[" + e.getData().get(VALID_TYPE) + "]]");
			throw e;
		}
	}

	/**
	 * actually follow the path in JsonPath and extract the value.
	 *
	 * @param properties    the properties
	 * @param refProperties the ref properties
	 * @return Object
	 */
	private Object internalLoad(Map<String, Object> properties, Map<String, Object> refProperties) {
		Object current = properties;
		for (PathElement path : this.paths) {
			if (current == null) {
				break;
			}
			Object element = path.load(current);
			if (refProperties != null && element instanceof JsonPath) {
				element = ((JsonPath) element).load(refProperties, null);
			}
			current = element;
		}
		return current;
	}

	/**
	 * actually follow the path in JsonPath and save the value.
	 *
	 * @param properties the properties
	 * @param value      the value
	 */
	public void save(Map<String, Object> properties, Object value) {
		Object current = properties;
		int i = 0;
		for (; i < this.paths.size() - 1; ++i) {
			Object element = this.paths.get(i).load(current);
			if (element == null) {
				break;
			}
			current = element;
		}
		// we could only find path upto index i-1
		// we need now to create the path elements now in reverse
		Object insert = value;
		int j = this.paths.size() - 1;
		for (; j > i; --j) {
			insert = this.paths.get(j).save(null, insert);
		}
		this.paths.get(j).save(current, insert);
	}

	/**
	 * actually follow the path in JsonPath and remove (map only) the value.
	 *
	 * @param properties the properties
	 */
	public void remove(Map<String, Object> properties) {
		Object current = properties;
		for (int i = 0; i < this.paths.size() - 1; ++i) {
			PathElement path = this.paths.get(i);
			if (current == null) {
				break;
			}
			current = path.load(current);
		}
		PathElement path = this.paths.get(this.paths.size() - 1);
		if (current != null && path instanceof KeyAccess) {
			((KeyAccess) path).remove(current);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.reference == null) ? 0 : this.reference.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		JsonPath other = (JsonPath) obj;
		if (this.reference == null) {
			if (other.reference != null) {
				return false;
			}
		} else if (!this.reference.equals(other.reference)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder().append("${").append(this.reference).append("}").toString();
	}

	public static JsonPath at(String string) {
		return new JsonPath(string);
	}
}

class JsonPathSerializer extends JsonSerializer<JsonPath> {
	@Override
	public void serialize(JsonPath ref, JsonGenerator jgen, SerializerProvider sp) throws IOException {
		jgen.writeString(ref.toString());
	}
}
