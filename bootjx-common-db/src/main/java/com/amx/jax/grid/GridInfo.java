package com.amx.jax.grid;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.boot.jx.logger.LoggerService;
import com.boot.utils.ArgUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

public class GridInfo<T> {

	public static final Logger LOGGER = LoggerService.getLogger(GridInfo.class);

	public static <T> Map<String, String> map(Class<?> clazz, Map<String, String> fieldMap) {
		Class<?> tmpClass = clazz;
		do {
			Field[] f = tmpClass.getDeclaredFields();
			for (Field field : f) {
				Column y = field.getAnnotation(Column.class);
				if (!ArgUtil.isEmpty(y) && !ArgUtil.isEmpty(y.name()) && !ArgUtil.isEmpty(field.getName())) {
					LOGGER.debug("{} : {} {}", clazz.getName(), field.getName(), y.name());
					fieldMap.put(field.getName(), y.name());
				}
			}
			tmpClass = tmpClass.getSuperclass();

		} while (tmpClass != null);

		return fieldMap;
	}

	public static <T> String query(GridInfo<?> info, String table, Class<?> clazz, String query) {
		if (ArgUtil.isEmpty(query)) {
			Class<?> tmpClass = clazz;
			String columnString = "";
			String groupString = "";
			boolean hasGroupCondition = false;
			do {
				Field[] f = tmpClass.getDeclaredFields();

				for (Field field : f) {
					Column c = field.getAnnotation(Column.class);
					GridGroup g = field.getAnnotation(GridGroup.class);
					if (!ArgUtil.isEmpty(g) && !ArgUtil.isEmpty(g.value())) {
						hasGroupCondition = true;
						columnString = columnString
								+ ((ArgUtil.isEmpty(columnString) ? "" : ",") + g.value() + " as " + c.name());
					} else if (!ArgUtil.isEmpty(c) && !ArgUtil.isEmpty(c.name())) {
						columnString = columnString + (ArgUtil.isEmpty(columnString) ? c.name() : ("," + c.name()));
						groupString = groupString + (ArgUtil.isEmpty(groupString) ? c.name() : ("," + c.name()));
					}
				}
				tmpClass = tmpClass.getSuperclass();

			} while (tmpClass != null);

			if (ArgUtil.isEmpty(columnString)) {
				columnString = "*";
			}

			if (hasGroupCondition && !ArgUtil.isEmpty(groupString)) {
				info.setGroupBy(groupString);
			}

			if (ArgUtil.isEmpty(table)) {
				Table tableAnnot = clazz.getAnnotation(Table.class);
				if (!ArgUtil.isEmpty(tableAnnot) && !ArgUtil.isEmpty(tableAnnot.name())) {
					table = tableAnnot.name();
				}
			}

			if (ArgUtil.is(table)) {
				return String.format("SELECT %s  FROM %s", columnString, table);
			}
		}
		return query;
	}

	Map<String, String> map;
	String table;
	String query;
	String groupBy;
	Class<T> resultClass;
	boolean customeQuery;

	GridInfo(String table, Class<T> resultClass, String query) {
		this.query = query;
		this.table = table;
		this.resultClass = resultClass;
	}

	GridInfo(Class<T> resultClass, String query) {
		this(null, resultClass, query);
	}

	GridInfo(String table, Class<T> resultClass) {
		this(table, resultClass, null);
	}

	GridInfo(Class<T> resultClass) {
		this(null, resultClass, null);
	}

	GridInfo(String table) {
		this(table, null, null);
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Class<T> getResultClass() {
		return resultClass;
	}

	public void setResultClass(Class<T> resultClass) {
		this.resultClass = resultClass;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public boolean isCustomeQuery() {
		return customeQuery;
	}

	public void setCustomeQuery(boolean customeQuery) {
		this.customeQuery = customeQuery;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public GridInfo<T> groupBy(String groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public GridInfo<T> build() {
		if (!ArgUtil.isEmpty(this.query)) {
			this.customeQuery = true;
		}

		if (ArgUtil.is(this.resultClass)) {
			this.query = query(this, this.table, this.resultClass, this.query);
			this.map = map(this.resultClass, new HashMap<String, String>());
		}

		return this;
	}

}
