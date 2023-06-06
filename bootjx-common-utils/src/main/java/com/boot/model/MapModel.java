package com.boot.model;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.boot.json.JsonSerializerType;
import com.boot.json.JsonSerializerTypeSerializer;
import com.boot.json.MapModelDeserializer;
import com.boot.utils.ArgUtil;
import com.boot.utils.Constants;
import com.boot.utils.CryptoUtil;
import com.boot.utils.CryptoUtil.Encoder;
import com.boot.utils.JsonPath;
import com.boot.utils.JsonUtil;
import com.boot.utils.TimeUtils;
import com.boot.utils.TimeUtils.TimePeriod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = JsonSerializerTypeSerializer.class)
@JsonDeserialize(using = MapModelDeserializer.class)
public class MapModel implements JsonSerializerType<Object> {

	public static interface EntryMeta {
		public String getKey();

		public String getUkey();
	}

	public static class NodeEntry<T> {
		protected T value;

		public NodeEntry() {
			this.value = null;
		}

		public NodeEntry(T value) {
			this.value = value;
		}

		public String asString() {
			return ArgUtil.parseAsString(value);
		}

		/**
		 * Save as {@link #defaultValue(Object)} but type is always {@link String}
		 * 
		 * @param defaultvalue
		 * @return
		 */
		public String asString(String defaultvalue) {
			return ArgUtil.parseAsString(value, defaultvalue);
		}

		public Long asLong() {
			return ArgUtil.parseAsLong(value);
		}

		/**
		 * Same as {@link #defaultValue(Object)} but type is always {@link Long}
		 * 
		 * @param defaultvalue
		 * @return
		 */
		public Long asLong(Long defaultvalue) {
			return ArgUtil.parseAsLong(value, defaultvalue);
		}

		public Integer asInteger() {
			return ArgUtil.parseAsInteger(value);
		}

		public Integer asInteger(Integer defaultvalue) {
			return ArgUtil.parseAsInteger(value, defaultvalue);
		}

		public BigDecimal asBigDecimal() {
			return ArgUtil.parseAsBigDecimal(value);
		}

		/**
		 * Same as {@link #defaultValue(Object)} but type is always {@link BigDecimal}
		 * 
		 * @param defaultvalue
		 * @return
		 */
		public BigDecimal asBigDecimal(BigDecimal defaultvalue) {
			return ArgUtil.parseAsBigDecimal(value, defaultvalue);
		}

		public Boolean asBoolean() {
			return ArgUtil.parseAsBoolean(value, false);
		}

		public Long asMillis() {
			return TimeUtils.toMillis(ArgUtil.parseAsString(value, Constants.BLANK));
		}

		public TimePeriod asTimePeriod() {
			return TimePeriod.from(ArgUtil.parseAsString(value));
		}

		/**
		 * Save as {@link #defaultValue(Object)} but type is always {@link Boolean}
		 * 
		 * @param defaultvalue
		 * @return
		 */
		public Boolean asBoolean(boolean defaultvalue) {
			return ArgUtil.parseAsBoolean(value, defaultvalue);
		}

		public <T extends Enum<T>> T asEnum(Class<T> clazz) {
			return ArgUtil.parseAsEnumT(value, clazz);
		}

		public <T extends Enum<T>> T asEnum(Class<T> clazz, T defaultvalue) {
			return ArgUtil.parseAsEnumT(value, clazz, defaultvalue);
		}

		public <T extends Enum<T>> T asEnum(T defaultvalue, Class<T> clazz) {
			return ArgUtil.parseAsEnumT(value, clazz, defaultvalue);
		}

		public Enum<?> asEnum(Object parseAsEnum, Type type) {
			return ArgUtil.parseAsEnum(value, type);
		}

		public <T> T defaultValue(T defaultValue) {
			return ArgUtil.parseAsT(value, defaultValue, false);
		}

		public <T> T as(Class<T> clazz) {
			return JsonUtil.getMapper().convertValue(value, clazz);
		}

		public <T> T as(TypeReference<T> toValueTypeRef) {
			return JsonUtil.getMapper().convertValue(value, toValueTypeRef);
		}

		public <T> List<T> asList(Class<T> clazz) {
			List<Object> list = this.asList();
			List<T> newList = new ArrayList<T>();
			for (Object object : list) {
				newList.add(JsonUtil.parse(object, clazz));
			}
			return newList;
		}

		public List<Object> asList() {
			return ArgUtil.parseAsListOfT(value, new Object(), Constants.EMPTY_LIST, false);
		}

		public List<List<Map<String, Object>>> asListListOfMap() {
			return ArgUtil.parseAsListListOfT(value, new HashMap<String, Object>(),
					new ArrayList<Map<String, Object>>(), new ArrayList<List<Map<String, Object>>>(), false);
		}

		public List<Map<String, Object>> asListOfMap() {
			return ArgUtil.parseAsListOfT(value, new HashMap<String, Object>(), new ArrayList<Map<String, Object>>(),
					false);
		}

		public <O> List<Map<String, O>> asListOfMapT() {
			return ArgUtil.parseAsListOfT(value, new HashMap<String, O>(), new ArrayList<Map<String, O>>(), false);
		}

		public Map<String, Object> asMap() {
			return JsonUtil.toMap(this.value);
		}

		public MapModel asMapModel() {
			return MapModel.from(this.asMap());
		}

		public boolean exists() {
			return ArgUtil.is(value);
		}

		public boolean is(Object compare) {
			return ArgUtil.areEqual(this.value, compare);
		}

		public boolean not(Object compare) {
			return !ArgUtil.areEqual(this.value, compare);
		}

		public boolean in(Object... compare) {
			return ArgUtil.isEqual(this.value, compare);
		}

		public T getValue() {
			return value;
		}

		public T value() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}

		public NodeEntry<T> value(T value) {
			this.value = value;
			return this;
		}

	}

	public static class MapEntry extends NodeEntry<Object> {

		public MapEntry(Object value) {
			super(value);
		}

	}

	public static class MapPathEntry extends MapEntry {

		JsonPath jsonPath;
		String key;
		protected Map<String, Object> map;

		public MapPathEntry() {
			super(null);
		}

		public MapPathEntry map(Map<String, Object> map) {
			this.map = map;
			return this;
		}

		public MapPathEntry path(JsonPath jsonPath) {
			this.jsonPath = jsonPath;
			return this;
		}

		public MapPathEntry key(String key) {
			this.key = key;
			return this;
		}

		public MapPathEntry load(Object defaultValue) {
			if (ArgUtil.is(key)) {
				this.value(this.map.getOrDefault(key, defaultValue));
			} else if (ArgUtil.is(jsonPath)) {
				this.value(jsonPath.load(this.map, defaultValue));
			}
			return this;
		}

		public MapPathEntry save(Object value) {
			if (ArgUtil.is(key)) {
				this.value(this.map.put(key, value));
			} else if (ArgUtil.is(jsonPath)) {
				jsonPath.save(map, value);
			}
			return this;
		}

		public MapPathEntry keyEntry(String key) {
			return new MapPathEntry().map(this.asMap()).key(key).load(null);
		}

		public MapPathEntry pathEntry(JsonPath path) {
			return new MapPathEntry().map(this.asMap()).path(path).load(null);
		}

		public MapPathEntry pathEntry(String path) {
			return this.pathEntry(new JsonPath(path));
		}

		public MapPathEntry pathEntrySafe(String path) {
			if (!this.exists() && ArgUtil.is(path)) {
				return this.pathEntry(new JsonPath(path));
			}
			return this;
		}

		public MapPathEntry orPathEntry(String path) {
			if (!this.exists() && ArgUtil.is(path)) {
				return new MapPathEntry().map(this.map).path(new JsonPath(path)).load(null);
			}
			return this;
		}

		public MapPathEntry orKeyEntry(String key) {
			if (!this.exists() && ArgUtil.is(key)) {
				return new MapPathEntry().map(this.map).key(key).load(null);
			}
			return this;
		}

	}

	protected Map<String, Object> map;
	protected List<Object> list;
	protected Map<String, Object> elem;

	public MapModel() {
		this.map = new HashMap<String, Object>();
	}

	public MapModel(Map<String, Object> map) {
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	public MapModel(String json) {
		if (json.indexOf("[") == 0) {
			try {
				this.list = JsonUtil.getObjectListFromJsonString(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			this.map = JsonUtil.fromJson(json, Map.class);
		}
	}

	public MapModel(List<Object> list) {
		this.list = list;
	}

	public MapPathEntry entry(String key) {
		return new MapPathEntry().map(this.map()).key(key).load(null);
	}

	public MapPathEntry entry(JsonPath jsonPath) {
		return new MapPathEntry().map(this.map()).path(jsonPath).load(null);
	}

	public MapPathEntry keyEntry(String key) {
		return this.entry(key);
	}

	public MapPathEntry keyEntry(EntryMeta metaKey) {
		return this.entry(metaKey.getKey());
	}

	public MapPathEntry pathEntry(String path) {
		return this.entry(new JsonPath(path));
	}

	public MapPathEntry path(JsonPath jsonPath) {
		return this.entry(jsonPath);
	}

	public MapEntry first() {
		return new MapEntry(this.getFirst());
	}

	public Object get(String key) {
		return this.map().get(key);
	}

	public Object get(String key, Object defaultValue) {
		return this.map().getOrDefault(key, defaultValue);
	}

	public Object getFirst() {
		if (this.list != null) {
			return list.get(0);
		}

		if (this.map != null) {
			for (Entry<String, Object> iterable_element : map().entrySet()) {
				return iterable_element.getValue();
			}
		}
		return null;
	}

	public String getString(String key) {
		return ArgUtil.parseAsString(this.get(key));
	}

	public String getString(String key, String defaultvalue) {
		return ArgUtil.parseAsString(this.get(key), defaultvalue);
	}

	public Long getLong(String key) {
		return ArgUtil.parseAsLong(this.get(key));
	}

	public Long getLong(String key, Long defaultvalue) {
		return ArgUtil.parseAsLong(this.get(key), defaultvalue);
	}

	public Integer getInteger(String key) {
		return ArgUtil.parseAsInteger(this.get(key));
	}

	public Integer getInteger(String key, Integer defaultvalue) {
		return ArgUtil.parseAsInteger(this.get(key, defaultvalue));
	}

	public BigDecimal getBigDecimal(String key) {
		return ArgUtil.parseAsBigDecimal(this.get(key));
	}

	public BigDecimal getBigDecimal(String key, BigDecimal defaultvalue) {
		return ArgUtil.parseAsBigDecimal(this.get(key), defaultvalue);
	}

	public <T> T getAs(String key, Class<T> clazz) {
		return JsonUtil.getMapper().convertValue(this.get(key), clazz);
	}

	public <T extends Enum<T>> T getAsEnum(String key, Class<T> clazz) {
		return ArgUtil.parseAsEnumT(this.get(key), clazz);
	}

	@SuppressWarnings("unchecked")
	public MapModel getMap(String key) {
		return new MapModel((Map<String, Object>) this.get(key));
	}

	@Override
	public Object toObject() {
		if (this.list != null) {
			return list;
		}

		if (this.map != null) {
			return this.map;
		}
		return this.map();
	}

	public MapModel fromMap(Map<String, Object> map) {
		this.map = map;
		return this;
	}

	public Map<String, Object> map() {
		if (this.map == null) {
			this.map = new HashMap<String, Object>();
		}
		return this.map;
	}

	public List<Object> list() {
		if (this.list == null) {
			this.list = new ArrayList<Object>();
		}
		return this.list;
	}

	public Map<String, Object> toMap() {
		return this.map();
	}

	public String toJson() {
		return JsonUtil.toJson(this.toObject());
	}

	public String toJsonPretty() {
		return JsonUtil.toJsonPrettyPrint(this.toObject());
	}

	public <T> T as(Class<T> clazz) {
		return JsonUtil.getMapper().convertValue(this.map(), clazz);
	}

	public static MapModel from(Map<String, Object> map) {
		if (map == null) {
			return createInstance();
		}
		return new MapModel(map);
	}

	public static MapModel from(List<Object> list) {
		return new MapModel(list);
	}

	public static MapModel from(String json) {
		return new MapModel(json);
	}

	public static MapModel createInstance() {
		return new MapModel(new HashMap<String, Object>());
	}
	
	public MapModel putAll(Map<? extends String, ? extends Object> source) {
		if (source != null)
			this.map().putAll(source);
		return this;
	}

	public MapModel putAll(MapModel source) {
		if (source != null)
			this.map().putAll(source.toMap());
		return this;
	}

	public MapModel put(String key, Object value) {
		if (value != null) {
			this.map().put(key, value);
		}
		return this;
	}

	public MapModel put(String key) {
		this.map().put(key, true);
		return this;
	}

	public MapModel add(Object value) {
		this.list().add(value);
		return this;
	}

	public MapModel put(JsonPath jsonPath, Object value) {
		jsonPath.save(this.map(), value);
		return this;
	}

	public MapModel remove(String key) {
		this.map().remove(key);
		return this;
	}

	public boolean containsKey(String key) {
		if (this.map == null) {
			return false;
		}
		return this.map.containsKey(key);
	}

	public int size() {
		if (this.list != null) {
			return this.list.size();
		} else if (this.map != null) {
			return this.map.size();
		}
		return 0;
	}

	public MapModel map2list() {
		this.list().add(this.map());
		this.map = null;
		return this;
	}

	public MapModel list2map(String key) {
		this.map().put(key, this.list());
		this.list = null;
		return this;
	}

	public MapModel map2map(String key) {
		Map<String, Object> child = this.map();
		this.map = null;
		this.map().put(key, child);
		return this;
	}

	public Encoder encoder() {
		return CryptoUtil.getEncoder().obzect(this.toObject());
	}

	public static Encoder decoder(String state) {
		return CryptoUtil.getEncoder().message(state);
	}

}
