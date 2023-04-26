package com.boot.jx.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListRequestModel<T> implements IRespModel {
	private static final long serialVersionUID = 546575527587061399L;
	public List<T> values;
	public List<T> add;
	public List<T> remove;

	public List<T> getValues() {
		return this.values;
	}

	public void setValues(List<T> values) {
		this.values = values;
	}

	public List<T> getAdd() {
		return add;
	}

	public void setAdd(List<T> add) {
		this.add = add;
	}

	public List<T> getRemove() {
		return remove;
	}

	public void setRemove(List<T> remove) {
		this.remove = remove;
	}
}
