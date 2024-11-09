package com.boot.jx.common.impl;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boot.model.SafeKeyHashMap;
import com.boot.utils.ArgUtil;

public class ConfigMeta implements Serializable {

	public static enum INPUT_TYPE {
		TEXT, OPTIONS, RANGE, NUMBER, COLOR, COLOR_PALLETE, NONE, MESSAGE, TEXTAREA, JSON;
	}

	public static enum DATA_TYPE {
		TIMESPAN, SWITCH, NONE;
	}

	public static enum CONVERT_TYPE {
		TIME_MILLIS, BOOLEAN, NONE
	}

	public static enum MESSAGE_TYPE {
		PRIMARY, SECONDARY, SUCCESS, DANGER, WARNING, INFO, DARK
	}

	public static class ConfigOption {

		public static ConfigOption ON = new ConfigOption(Boolean.TRUE).label("ON");
		public static ConfigOption OFF = new ConfigOption(Boolean.FALSE).label("OFF");

		private String label;
		private Object value;

		public String getLabel() {
			return label;
		}

		public Object getValue() {
			return value;
		}

		public ConfigOption(Object value) {
			this.value = value;
		}

		public ConfigOption label(String label) {
			this.label = label;
			return this;
		}
	}

	public static class ColorPalette {
		String primary;
		String secondary;
		String accent;

		public ColorPalette() {
			this.primary = "#FFFFFF";
			this.secondary = "#FDFDFD";
			this.accent = "#1DC4E9";
		}

		public String getPrimary() {
			return primary;
		}

		public void setPrimary(String primary) {
			this.primary = primary;
		}

		public String getSecondary() {
			return secondary;
		}

		public void setSecondary(String secondary) {
			this.secondary = secondary;
		}

		public String getAccent() {
			return accent;
		}

		public void setAccent(String accent) {
			this.accent = accent;
		}

	}

	private static final long serialVersionUID = -8418291522478302778L;

	private String title;
	private String key;
	private String ukey;
	private String superKey;
	private String desc;
	private String group;
	private String path;
	private String pathRaw;
	private Object defaultValue;
	private Object example;
	private boolean optional;
	private boolean readonly;
	private boolean createonly;
	private boolean writeonly;
	private boolean hidden;
	private boolean deprecated;
	private boolean searchable;
	private boolean clearable;
	private boolean filterable;
	private Integer order;
	private Integer max;
	private Integer min;

	private INPUT_TYPE inputType;
	private DATA_TYPE dataType;
	private CONVERT_TYPE converterType;
	private MESSAGE_TYPE messageType;

	private List<ConfigOption> options;
	private Map<String, Object> filter;
	private String optionsKey;
	private String optionsLabel;
	private String optionsSource;

	@Target({ ElementType.FIELD, ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ConfigMetaProperty {
		String title() default "";

		String desc() default "";

		String context() default "";

		String path() default "";

		String pathRaw() default "";

		boolean hidden() default false;

		boolean readonly() default false;

		boolean createonly() default false;

		boolean writeonly() default false;

		boolean deprecated() default false;

		boolean searchable() default false;

		boolean clearable() default false;

		boolean filterable() default false;

		boolean optional() default false;

		INPUT_TYPE inputType() default INPUT_TYPE.NONE;

		DATA_TYPE dataType() default DATA_TYPE.NONE;

		CONVERT_TYPE converterType() default CONVERT_TYPE.NONE;

		String optionsSource() default "";

		String optionsLabel() default "";

		String optionsKey() default "";

		String[] optionValues() default {};

		String defaultValue() default "";

	}

	public ConfigMeta() {
		this.messageType = MESSAGE_TYPE.PRIMARY;
		this.searchable = true;
		this.clearable = true;
		this.filterable = true;
	}

	public ConfigMeta(String title, String key) {
		super();
		this.key = key;
		this.ukey = SafeKeyHashMap.sanitizeKey(key);
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<ConfigOption> getOptions() {
		return options;
	}

	public void setOptions(List<ConfigOption> options) {
		this.options = options;
	}

	public List<ConfigOption> options() {
		if (this.inputType == null) {
			this.inputType = INPUT_TYPE.OPTIONS;
		}
		if (this.options == null) {
			this.options = new ArrayList<ConfigOption>();
		}
		return this.options;
	}

	public ConfigMeta options(ConfigOption... options) {
		this.options = this.options();
		for (ConfigOption configOption : options) {
			this.options.add(configOption);
		}
		return this;
	}

	public ConfigMeta optionValues(Object... optionValues) {
		this.options = this.options();
		for (Object optionValue : optionValues) {
			this.options.add(new ConfigOption(optionValue));
		}
		return this;
	}

	public ConfigMeta optionsSource(String src) {
		this.options = this.options();
		this.optionsSource = src;
		return this;
	}

	public String getOptionsSource() {
		return optionsSource;
	}

	public void setOptionsSource(String src) {
		this.optionsSource = src;
	}

	public ConfigMeta optionsOnOff() {
		return this.options(ConfigOption.ON, ConfigOption.OFF);
	}

	public INPUT_TYPE getInputType() {
		return inputType;
	}

	public void setInputType(INPUT_TYPE inputType) {
		this.inputType = inputType;
	}

	public ConfigMeta inputType(INPUT_TYPE inputType) {
		this.inputType = inputType;
		return this;
	}

	public ConfigMeta inputType(INPUT_TYPE inputType, MESSAGE_TYPE messageType) {
		this.inputType = inputType;
		this.messageType = messageType;
		return this;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public ConfigMeta defaultValue(Object defaultValue) {
		if (ArgUtil.is(defaultValue) && defaultValue instanceof ConfigOption) {
			this.defaultValue = ((ConfigOption) defaultValue).getValue();
		} else {
			this.defaultValue = defaultValue;
		}
		return this;
	}

	public ConfigMeta defaultFalse() {
		this.defaultValue(Boolean.FALSE);
		return this;
	}

	public ConfigMeta defaultTrue() {
		this.defaultValue(Boolean.TRUE);
		return this;
	}

	public ConfigMeta title(String title) {
		this.title = title;
		return this;
	}

	public ConfigMeta key(String key) {
		this.key = key;
		this.ukey = SafeKeyHashMap.sanitizeKey(key);
		return this;
	}

	public static List<ConfigMeta> createList() {
		return new ArrayList<ConfigMeta>();
	}

	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public ConfigMeta optional() {
		this.optional = true;
		return this;
	}

	public ConfigMeta optional(boolean optional) {
		this.optional = optional;
		return this;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public ConfigMeta readonly() {
		this.readonly = true;
		return this;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public ConfigMeta hidden() {
		this.hidden = true;
		return this;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ConfigMeta path(String path) {
		this.path = path;
		return this;
	}

	public DATA_TYPE getDataType() {
		return dataType;
	}

	public void setDataType(DATA_TYPE dataType) {
		this.dataType = dataType;
	}

	public CONVERT_TYPE getConverterType() {
		return converterType;
	}

	public void setConverterType(CONVERT_TYPE converterType) {
		this.converterType = converterType;
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	public ConfigMeta deprecated() {
		this.deprecated = true;
		return this;
	}

	public ConfigMeta createonly() {
		this.createonly = true;
		return this;
	}

	public ConfigMeta createonly(boolean createonly) {
		this.createonly = createonly;
		return this;
	}

	public boolean isCreateonly() {
		return createonly;
	}

	public void setCreateonly(boolean createonly) {
		this.createonly = createonly;
	}

	public boolean isWriteonly() {
		return writeonly;
	}

	public void setWriteonly(boolean writeonly) {
		this.writeonly = writeonly;
	}

	public ConfigMeta writeonly() {
		this.writeonly = true;
		return this;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public ConfigMeta desc(String desc) {
		this.desc = desc;
		return this;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public ConfigMeta group(String group) {
		this.group = group;
		return this;
	}

	public String getOptionsKey() {
		return optionsKey;
	}

	public void setOptionsKey(String optionKey) {
		this.optionsKey = optionKey;
	}

	public ConfigMeta optionsKey(String optionKey) {
		this.optionsKey = optionKey;
		return this;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public ConfigMeta order(Integer order) {
		this.order = order;
		return this;
	}

	public String getOptionsLabel() {
		return optionsLabel;
	}

	public void setOptionsLabel(String optionsLabel) {
		this.optionsLabel = optionsLabel;
	}

	public ConfigMeta optionsLabel(String optionsLabel) {
		this.optionsLabel = optionsLabel;
		return this;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public ConfigMeta max(Integer max) {
		this.max = max;
		return this;
	}

	public ConfigMeta min(Integer min) {
		this.min = min;
		return this;
	}

	public ConfigMeta writeonly(boolean writeonly) {
		this.writeonly = writeonly;
		return this;
	}

	public Object getExample() {
		return example;
	}

	public void setExample(Object example) {
		this.example = example;
	}

	public ConfigMeta example(String example) {
		this.example = example;
		return this;
	}

	public MESSAGE_TYPE getMessageType() {
		return messageType;
	}

	public void setMessageType(MESSAGE_TYPE messageType) {
		this.messageType = messageType;
	}

	public ConfigMeta messageType(MESSAGE_TYPE messageType) {
		this.messageType = messageType;
		return this;
	}

	public Map<String, Object> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}

	public Map<String, Object> filter() {
		if (this.filter == null) {
			this.filter = new HashMap<String, Object>();
		}
		return this.filter;
	}

	public ConfigMeta filter(String key, Object value) {
		filter().put(key, value);
		return this;
	}

	public String getSuperKey() {
		return superKey;
	}

	public void setSuperKey(String superKey) {
		this.superKey = superKey;
	}

	public ConfigMeta superKey(String superKey) {
		this.superKey = superKey;
		return this;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public boolean isClearable() {
		return clearable;
	}

	public void setClearable(boolean clearable) {
		this.clearable = clearable;
	}

	public boolean isFilterable() {
		return filterable;
	}

	public void setFilterable(boolean filterable) {
		this.filterable = filterable;
	}

	public ConfigMeta searchable(boolean searchable) {
		this.searchable = searchable;
		return this;
	}

	public ConfigMeta filterable(boolean filterable) {
		this.filterable = filterable;
		return this;
	}

	public ConfigMeta clearable(boolean clearable) {
		this.clearable = true;
		return this;
	}

	public String getPathRaw() {
		return pathRaw;
	}

	public void setPathRaw(String pathRaw) {
		this.pathRaw = pathRaw;
	}

	public ConfigMeta pathRaw(String pathRaw) {
		this.pathRaw = pathRaw;
		return this;
	}

	public String getUkey() {
		return ukey;
	}

	public void setUkey(String ukey) {
		this.ukey = ukey;
	}
}
