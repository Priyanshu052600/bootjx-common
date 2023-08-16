package com.amx.jax.grid;

import java.io.Serializable;

public interface GridViewRecord extends Serializable {
	public Integer getTotalRecords();

	public void setTotalRecords(Integer totalRecords);

	public Integer getRn();

	public void setRn(Integer rn);
}
