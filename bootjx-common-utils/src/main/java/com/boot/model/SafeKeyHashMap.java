package com.boot.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.boot.json.JsonSerializerType;
import com.boot.model.MapModel.NodeEntry;
import com.boot.utils.ArgExceptions;
import com.boot.utils.ArgUtil;
import com.boot.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SafeKeyHashMap<V> implements JsonSerializerType<Object>, Serializable {
	private static final long serialVersionUID = 5570692867938930622L;

	protected Map<String, V> map;

	public static String sanitizeKey(Object key) {
		if (key == null) {
			throw ArgExceptions.paramMissingOrInvalid(null, null, key);
		}
		if (key instanceof String) {
			return (ArgUtil.parseAsString(key, Constants.BLANK).replaceAll("[\\.@$]", "_"));
		}
		return (String) key;
	}

	public SafeKeyHashMap() {
		super();
		this.map = new HashMap<String, V>();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SafeKeyHashMap(Map<String, V> newmap) {
		super();
		if (newmap != null) {
			if (newmap instanceof SafeKeyHashMap) {
				map = ((SafeKeyHashMap) newmap).map();
			} else {
				this.map = newmap;
			}
		}
	}

	private Map<String, V> map() {
		if (this.map == null)
			this.map = new HashMap<String, V>();
		return this.map;
	}

	public V put(String key, V value) {
		if (ArgUtil.is(key)) {
			return this.map().put(sanitizeKey(key), value);
		}
		return null;
	}

	public V get(Object key) {
		return this.map().get(sanitizeKey(key));
	}

	public NodeEntry<V> keyEntry(Object key) {
		return new NodeEntry<V>(this.map().get(sanitizeKey(key)));
	}

	public V remove(Object key) {
		return this.map().remove(sanitizeKey(key));
	}

	public V getOrDefault(Object key, V defaultValue) {
		return this.map().getOrDefault(sanitizeKey(key), defaultValue);
	}

	@Override
	public Object toObject() {
		return this.map();
	}

	public int size() {
		return 0;
	}

	public boolean isEmpty() {
		return this.map().isEmpty();
	}

	public boolean containsKey(Object key) {
		return this.map().containsKey(sanitizeKey(key));
	}

	public boolean containsValue(Object value) {
		return this.map().containsValue(value);
	}

	public void putAll(Map<? extends String, ? extends V> m) {
		this.map().putAll(m);
	}

	public void clear() {
		this.map().clear();
	}

	public Set<String> keySet() {
		return this.map().keySet();
	}

	public Collection<V> values() {
		return this.map().values();
	}

	public Set<Entry<String, V>> entrySet() {
		return this.map().entrySet();
	}
}