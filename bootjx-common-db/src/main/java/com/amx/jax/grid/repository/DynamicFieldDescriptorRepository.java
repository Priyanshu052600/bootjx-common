package com.amx.jax.grid.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.grid.DynamicFieldDescriptorEntity;

public interface DynamicFieldDescriptorRepository extends CrudRepository<DynamicFieldDescriptorEntity, BigDecimal> {

	@Query("select a from DynamicFieldDescriptorEntity a where a.dbViewName =?1")
	public List<DynamicFieldDescriptorEntity> findActiveColumnByDBviewName(String viewName, Sort sort);

}