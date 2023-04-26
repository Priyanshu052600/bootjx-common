package com.boot.jx.api;

public class FileSubmitRequestModel implements IRespModel {

	private static final long serialVersionUID = 2020776300828288965L;

	private String name;

	private String data;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
