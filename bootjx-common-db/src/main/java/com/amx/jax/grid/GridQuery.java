package com.amx.jax.grid;

import java.util.List;
import java.util.Map;

import com.amx.jax.grid.GridConstants.GridColumnOrder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GridQuery {

	public GridQuery() {
		super();
		this.params = new GridParams();
	}

	@Schema(example = "0", description = "page")
	private Integer pageNo;

	@Schema(example = "5", description = "Length of page")
	private Integer pageSize;

	@Schema(example = "", description = "Global Search String")
	private String search;

	@Schema(example = "false", description = "false,if pagination is not required, faster")
	private boolean paginated;

	List<GridColumn> columns;
	int sortBy;
	SortOrder sortOrder;
	String sortColumn;
	GridColumnOrder order;
	GridParams params;
	Map<String, Object> filter;

	public List<GridColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<GridColumn> columns) {
		this.columns = columns;
	}

	public int getSortBy() {
		return sortBy;
	}

	public void setSortBy(int sortBy) {
		this.sortBy = sortBy;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer start) {
		this.pageNo = start;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer length) {
		this.pageSize = length;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public boolean isPaginated() {
		return paginated;
	}

	public void setPaginated(boolean paginated) {
		this.paginated = paginated;
	}

	public GridParams getParams() {
		return params;
	}

	public void setParams(GridParams params) {
		this.params = params;
	}

	public Map<String, Object> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public void setOrder(GridColumnOrder order) {
		this.order = order;
	}

	public GridColumnOrder getOrder() {
		return order;
	}

}
