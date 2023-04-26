package com.boot.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * The Class FlatMap.
 */
public class FlatMap {

	/** The Constant jsonpaths. */
	private static final Map<String, JsonPath> jsonpaths = new ConcurrentHashMap<String, JsonPath>();

	/** The map. */
	private Map<String, Object> map = null;

	/**
	 * Instantiates a new flat map.
	 *
	 * @param tempmap
	 *            the tempmap
	 */
	public FlatMap(Map<String, Object> tempmap) {
		this.map = tempmap;
	}

	/**
	 * Gets the json path.
	 *
	 * @param key
	 *            the key
	 * @return the json path
	 */
	public JsonPath getJsonPath(String key) {
		JsonPath jsonPath = jsonpaths.getOrDefault(key, null);
		if (jsonPath == null) {
			jsonPath = new JsonPath(key);
			jsonpaths.put(key, jsonPath);
		}
		return jsonPath;
	}

	/**
	 * Gets the.
	 *
	 * @param key
	 *            the key
	 * @return the string
	 */
	public String get(String key) {
		JsonPath jsonPath = getJsonPath(key);
		return jsonPath.load(this.map, Constants.BLANK);
	}

	public String get(String key, String defaultValue) {
		JsonPath jsonPath = getJsonPath(key);
		return jsonPath.load(this.map, defaultValue);
	}

	public String getNumber(String key, String format) {
		JsonPath jsonPath = getJsonPath(key);
		return String.format (format, jsonPath.load(this.map,0.0));
	}
	
	public String getNumber(String key) {
		return this.getNumber(key, "%.2f");
	}

	public String getValue(String key, String defaultValue) {
		JsonPath jsonPath = getJsonPath(key);
		String s = jsonPath.load(this.map, Constants.BLANK);
		return s.equals("") || s.equals("false") || s.equals("null") ? "" : defaultValue;
	}
	/**
	 * Gets the integer.
	 *
	 * @param key
	 *            the key
	 * @return the integer
	 */
	public Integer getInteger(String key) {
		JsonPath jsonPath = getJsonPath(key);
		return jsonPath.load(this.map, 0);
	}

	/**
	 * Gets the flat map.
	 *
	 * @param key the key
	 * @return the flat map
	 */
	public FlatMap getFlatMap(String key) {
		JsonPath jsonPath = getJsonPath(key);
		return new FlatMap(jsonPath.load(this.map, null));
	}

	/**
	 * Gets the list.
	 *
	 * @param key
	 *            the key
	 * @return the list
	 */
	public List<FlatMap> getList(String key) {
		JsonPath jsonPath = getJsonPath(key);
		List<Map<String, Object>> list = jsonPath.loadList(this.map, null);
		List<FlatMap> list2 = new ArrayList<FlatMap>();
		for (Map<String, Object> item : list) {
			list2.add(new FlatMap(item));
		}
		return list2;
	}

	/**
	 * Gets the map.
	 *
	 * @return the map
	 */
	public Map<String, Object> getMap() {
		return map;
	}

	/**
	 * Sets the map.
	 *
	 * @param map the map
	 */
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
}
