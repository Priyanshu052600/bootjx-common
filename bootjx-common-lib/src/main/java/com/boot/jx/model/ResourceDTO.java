package com.boot.jx.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.boot.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Use this class to return response,
 * 
 * Entity should be create by implmenting IResourceEntity and use
 * {@link ResourceDTO.create(IResourceEntity))} to create its instance
 * 
 * @see {@link com.boot.jx.model.IResourceEntity}
 * @author lalittanwar
 * @author Prashant
 *
 */
public class ResourceDTO implements IResourceEntity, Serializable {

	private static final long serialVersionUID = -5802360864645036772L;

	/**
	 * db identifier of resource
	 */
	@JsonProperty("_id")
	protected BigDecimal resourceId;

	/**
	 * name of resource
	 */
	@JsonProperty("_name")
	protected String resourceName;

	@JsonProperty("_local_name")
	protected String resourceLocalName;

	/**
	 * A short name for resource, eg:- ISO3 codes for Countries
	 */
	@JsonProperty("_code")
	protected String resourceCode;

	@JsonProperty("_value")
	protected Object resourceValue;

	public ResourceDTO() {

	}

	public ResourceDTO(IResourceEntity resource) {
		resource.resources();
		this.resourceId = resource.resourceId();
		this.resourceName = resource.resourceName();
		this.resourceCode = resource.resourceCode();
		this.resourceLocalName = resource.resourceLocalName();
		this.resourceValue = resource.resourceValue();
	}

	public ResourceDTO(BigDecimal resourceId, String resourceName, String resourceCode) {
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.resourceCode = resourceCode;
	}

	public ResourceDTO(BigDecimal resourceId, String resourceName) {
		this.resourceId = resourceId;
		this.resourceName = resourceName;
	}

	@Override
	public BigDecimal resourceId() {
		return resourceId;
	}

	public void setResourceId(BigDecimal resourceId) {
		this.resourceId = resourceId;
	}

	public BigDecimal getResourceId() {
		return resourceId;
	}

	@Override
	public String resourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(Object resourceName) {
		this.resourceName = ArgUtil.parseAsString(resourceName);
	}

	@Override
	public String resourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public void setResourceCode(Object resourceCode) {
		this.resourceCode = ArgUtil.parseAsString(resourceCode);
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public Object getResourceValue() {
		return resourceValue;
	}

	public Object resourceValue() {
		return resourceValue;
	}

	public void setResourceValue(Object resourceValue) {
		this.resourceValue = resourceValue;
	}

	public void importFrom(IResourceEntity entity) {
		entity.resources();
		this.resourceId = entity.resourceId();
		this.resourceCode = entity.resourceCode();
		this.resourceName = entity.resourceName();
		this.resourceLocalName = entity.resourceLocalName();
	}

	public static ResourceDTO create(IResourceEntity entity) {
		ResourceDTO dto = new ResourceDTO();
		dto.importFrom(entity);
		return dto;
	}

	public static List<ResourceDTO> create(List<? extends IResourceEntity> entityList) {
		List<ResourceDTO> list = new ArrayList<ResourceDTO>();
		for (IResourceEntity entity : entityList) {
			ResourceDTO dto = create(entity);
			list.add(dto);
		}
		return list;
	}

	public static List<ResourceDTO> createList(List<? extends IResourceEntity> entityList) {
		List<ResourceDTO> list = new ArrayList<ResourceDTO>();
		for (IResourceEntity entity : entityList) {
			ResourceDTO dto = create(entity);
			list.add(dto);
		}
		return list;
	}

	public String getResourceLocalName() {
		return resourceLocalName;
	}

	public void setResourceLocalName(String resourceLocalName) {
		this.resourceLocalName = resourceLocalName;
	}

	@Override
	public String resourceLocalName() {
		return resourceLocalName;
	}

	@Override
	public IResourceEntity resources() {
		this.resourceId = ArgUtil.nonEmpty(this.resourceId, this.resourceId());
		this.resourceName = ArgUtil.nonEmpty(this.resourceName, this.resourceName());
		this.resourceCode = ArgUtil.nonEmpty(this.resourceCode, this.resourceCode());
		this.resourceLocalName = ArgUtil.nonEmpty(this.resourceLocalName, this.resourceLocalName());
		this.resourceValue = ArgUtil.nonEmpty(this.resourceValue, this.resourceValue());
		return this;
	}

}
