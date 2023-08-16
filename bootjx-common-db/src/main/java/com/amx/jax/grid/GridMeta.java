package com.amx.jax.grid;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GridMeta {
	private String recordsFiltered;
	private String queryStr;

	/** The records total. */
	private String recordsTotal;

	private List<GridFieldDescriptor> descriptors;

	public String getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(String recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public String getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(String recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public List<GridFieldDescriptor> getDescriptors() {
		return descriptors;
	}

	public void setDescriptors(List<GridFieldDescriptor> descriptors) {
		this.descriptors = descriptors;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
}
