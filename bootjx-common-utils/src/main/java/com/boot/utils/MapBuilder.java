package com.boot.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class MapBuilder.
 */
public class MapBuilder {

	/**
	 * Instantiates a new map builder.
	 */
	private MapBuilder() {
		// private constructor to hide the implicit public one.
	}

	/**
	 * The Class MapItem.
	 */
	public static class MapItem {

		/** The key. */
		String key;

		/** The value. */
		Object value;

		/**
		 * Instantiates a new map item.
		 *
		 * @param key   the key
		 * @param value the value
		 */
		MapItem(String key, Object value) {
			this.key = key;
			this.value = value;
		}

		/**
		 * Gets the key.
		 *
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public Object getValue() {
			return value;
		}
	}

	/**
	 * The Class BuilderMap.
	 */
	public static class BuilderMap {

		/** The map. */
		private Map<String, Object> map = new HashMap<String, Object>();

		/**
		 * Instantiates a new builder map.
		 *
		 * @param items the items
		 */
		public BuilderMap(MapItem... items) {
			for (MapItem item : items) {
				this.put(item.getKey(), item.getValue());
			}
		}

		/**
		 * Put.
		 *
		 * @param key   the key
		 * @param value the value
		 * @return the builder map
		 */
		public BuilderMap put(String key, Object value) {
			map.put(key, value);
			return this;
		}

		public BuilderMap putIfNotNull(String key, Object value) {
			if (value != null) {
				map.put(key, value);
			}
			return this;
		}

		public BuilderMap putIfNotEmpty(String key, Object value) {
			if (ArgUtil.is(value)) {
				map.put(key, value);
			}
			return this;
		}

		/**
		 * data will be stored against data key
		 * 
		 * @param value
		 * @return
		 */
		public BuilderMap data(Object value) {
			map.put("data", value);
			return this;
		}

		/**
		 * Put.
		 *
		 * @param jsonPath the json path
		 * @param value    the value
		 * @return the builder map
		 */
		public BuilderMap put(JsonPath jsonPath, Object value) {
			jsonPath.save(map, value);
			return this;
		}

		/**
		 * To map.
		 *
		 * @return the map
		 */
		public Map<String, Object> toMap() {
			return map;
		}

		public Map<String, Object> build() {
			return map;
		}

		/**
		 * To json node.
		 *
		 * @return the json node
		 */
		public JsonNode toJsonNode() {
			return JsonUtil.getMapper().valueToTree(map);
		}

	}

	/**
	 * Map.
	 *
	 * @return the builder map
	 */
	public static BuilderMap map() {
		return new BuilderMap();
	}

	public static <K, V> MultiValueMap<K, V> multiValueMap(Map<K, List<V>> map) {
		return new MultiValueMapAdapter<K, V>(map);
	}

	@SuppressWarnings("unused")
	private static class MultiValueMapAdapter<K, V> implements MultiValueMap<K, V>, Serializable {

		private static final long serialVersionUID = 1479947542891619565L;
		private final Map<K, List<V>> map;

		public MultiValueMapAdapter(Map<K, List<V>> map) {
			Assert.notNull(map, "'map' must not be null");
			this.map = map;
		}

		@Override
		public void add(K key, V value) {
			List<V> values = this.map.get(key);
			if (values == null) {
				values = new LinkedList<V>();
				this.map.put(key, values);
			}
			values.add(value);
		}

		@Override
		public V getFirst(K key) {
			List<V> values = this.map.get(key);
			return (values != null ? values.get(0) : null);
		}

		@Override
		public void set(K key, V value) {
			List<V> values = new LinkedList<V>();
			values.add(value);
			this.map.put(key, values);
		}

		@Override
		public void setAll(Map<K, V> values) {
			for (Entry<K, V> entry : values.entrySet()) {
				set(entry.getKey(), entry.getValue());
			}
		}

		@Override
		public Map<K, V> toSingleValueMap() {
			LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<K, V>(this.map.size());
			for (Entry<K, List<V>> entry : map.entrySet()) {
				singleValueMap.put(entry.getKey(), entry.getValue().get(0));
			}
			return singleValueMap;
		}

		@Override
		public int size() {
			return this.map.size();
		}

		@Override
		public boolean isEmpty() {
			return this.map.isEmpty();
		}

		@Override
		public boolean containsKey(Object key) {
			return this.map.containsKey(key);
		}

		@Override
		public boolean containsValue(Object value) {
			return this.map.containsValue(value);
		}

		@Override
		public List<V> get(Object key) {
			return this.map.get(key);
		}

		@Override
		public List<V> put(K key, List<V> value) {
			return this.map.put(key, value);
		}

		@Override
		public List<V> remove(Object key) {
			return this.map.remove(key);
		}

		@Override
		public void putAll(Map<? extends K, ? extends List<V>> map) {
			this.map.putAll(map);
		}

		@Override
		public void clear() {
			this.map.clear();
		}

		@Override
		public Set<K> keySet() {
			return this.map.keySet();
		}

		@Override
		public Collection<List<V>> values() {
			return this.map.values();
		}

		@Override
		public Set<Entry<K, List<V>>> entrySet() {
			return this.map.entrySet();
		}

		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}
			return map.equals(other);
		}

		@Override
		public int hashCode() {
			return this.map.hashCode();
		}

		@Override
		public String toString() {
			return this.map.toString();
		}

		@Override
		public void addAll(K key, List<? extends V> values) {
			for (V v : values) {
				this.add(key, v);
			}
		}

		@Override
		public void addAll(MultiValueMap<K, V> values) {
			for (Entry<K, List<V>> entry : values.entrySet()) {
				addAll(entry.getKey(), entry.getValue());
			}
		}
	}

}
