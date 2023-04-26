package com.boot.jx.api;

public class BoolRespModel extends ARespModel {

	private static final long serialVersionUID = -2291893147463745029L;
	protected boolean success;

	public BoolRespModel() {
		super();
	}

	public BoolRespModel(boolean success) {
		super();
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
