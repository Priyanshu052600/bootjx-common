package com.amx.jax.grid;

import java.util.HashMap;
import java.util.Map;

import com.amx.jax.grid.GridConstants.ColumnFunction;
import com.amx.jax.grid.GridConstants.FilterDataType;
import com.amx.jax.grid.GridConstants.FilterOperater;
import com.boot.utils.ArgUtil;

/**
 * The Class SortBy.
 * 
 * @author pavan.solapure
 */
public class FilterBy {

	public static class Condition {
		private String column;
		private String value;
		private ColumnFunction func;
		private FilterOperater opertor;
		private String valueFunc;
		private FilterDataType type;

		public Condition(String column, FilterOperater opertor, String value, FilterDataType type) {
			super();
			this.column = column;
			this.opertor = opertor;
			this.value = value;
			this.type = type;
			this.func = null;
		}

		public Condition(String column, FilterOperater opertor, String value, FilterDataType type,
				ColumnFunction func) {
			super();
			this.column = column;
			this.opertor = opertor;
			this.value = value;
			this.type = type;
			this.func = func;
		}

		public ColumnFunction getFunc() {
			return func;
		}

		public void setFunc(ColumnFunction func) {
			this.func = func;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public FilterOperater getOpertor() {
			return opertor;
		}

		public void setOpertor(FilterOperater opertor) {
			this.opertor = opertor;
		}

		public FilterDataType getType() {
			return type;
		}

		public void setType(FilterDataType type) {
			this.type = type;
		}

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}

		public String toColumn() {
			if (!ArgUtil.isEmpty(this.func)) {
				return func.toString() + "(" + this.column + ")";
			}
			return this.column;
		}

		public String getValueFunc() {
			return valueFunc;
		}

		public void setValueFunc(String valueFunc) {
			this.valueFunc = valueFunc;
		}

		public Condition valueFunc(String valueFunc) {
			this.valueFunc = valueFunc;
			return this;
		}

	}

	/** The map of sorts. */
	private Map<String, String> mapOfLikeFilters;
	private Map<String, Condition> mapOfWhereFilters;

	/** The global search. */
	private boolean globalSearch;

	/**
	 * Instantiates a new sort by.
	 */
	public FilterBy() {
		if (null == mapOfLikeFilters) {
			mapOfLikeFilters = new HashMap<String, String>();
		}
		if (null == mapOfWhereFilters) {
			mapOfWhereFilters = new HashMap<String, Condition>();
		}
	}

	/**
	 * Gets the map of filters.
	 *
	 * @return the mapOfFilters
	 */
	public Map<String, String> getMapOfLikeFilters() {
		return mapOfLikeFilters;
	}

	/**
	 * Sets the map of filters.
	 *
	 * @param mapOfFilters the mapOfFilters to set
	 */
	public void setMapOfFilters(Map<String, String> mapOfFilters) {
		this.mapOfLikeFilters = mapOfFilters;
	}

	/**
	 * Adds the sort.
	 *
	 * @param filterColumn the filter column
	 * @param filterValue  the filter value
	 */
	public void addSearchFilter(String filterColumn, String filterValue) {
		mapOfLikeFilters.put("lower(" + filterColumn + ")", filterValue.toLowerCase());
	}

	public void addWhereFilter(GridColumn colSpec) {
		FilterOperater filterOperater = (FilterOperater) ArgUtil.parseAsEnum(colSpec.getOperator(), FilterOperater.EQ);
		mapOfWhereFilters.put(colSpec.getKey() + colSpec.getIndex(), new Condition(colSpec.getKey(), filterOperater,
				colSpec.getValue(), colSpec.getDataType(), colSpec.getFunc()).valueFunc(colSpec.getValueFunc()));
	}

	/**
	 * Checks if is global search.
	 *
	 * @return the globalSearch
	 */
	public boolean isGlobalSearch() {
		return globalSearch;
	}

	/**
	 * Sets the global search.
	 *
	 * @param globalSearch the globalSearch to set
	 */
	public void setGlobalSearch(boolean globalSearch) {
		this.globalSearch = globalSearch;
	}

	public Map<String, Condition> getMapOfWhereFilters() {
		return mapOfWhereFilters;
	}

	public void setMapOfWhereFilters(Map<String, Condition> mapOfWhereFilters) {
		this.mapOfWhereFilters = mapOfWhereFilters;
	}

}
