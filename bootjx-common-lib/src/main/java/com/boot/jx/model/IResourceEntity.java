package com.boot.jx.model;

import java.math.BigDecimal;

public interface IResourceEntity {

	public BigDecimal resourceId();

	public String resourceName();

	public String resourceCode();

	public String resourceLocalName();

	public default Object resourceValue() {
		return null;
	}

	public default IResourceEntity resources() {
		return this;
	}
}
