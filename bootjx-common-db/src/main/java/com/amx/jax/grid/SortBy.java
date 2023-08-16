package com.amx.jax.grid;

import java.util.LinkedHashMap;
import java.util.Map;

public class SortBy {

	/** The map of sorts. */
	private Map<String, SortOrder> mapOfSorts;

	/**
	 * Instantiates a new sort by.
	 */
	public SortBy() {
		if (null == mapOfSorts) {
			mapOfSorts = new LinkedHashMap<String, SortOrder>();
		}
	}

	/**
	 * Gets the sort bys.
	 *
	 * @return the sortBys
	 */
	public Map<String, SortOrder> getSortBys() {
		return mapOfSorts;
	}

	/**
	 * Adds the sort.
	 *
	 * @param sortBy
	 *            the sort by
	 */
	public void addSort(String sortBy) {
		mapOfSorts.put(sortBy, SortOrder.ASC);
	}

	/**
	 * Adds the sort.
	 *
	 * @param sortBy
	 *            the sort by
	 * @param sortOrder
	 *            the sort order
	 */
	public void addSort(String sortBy, SortOrder sortOrder) {
		if (!mapOfSorts.containsKey(sortBy)) {
			mapOfSorts.put(sortBy, sortOrder);
		}
	}

}
