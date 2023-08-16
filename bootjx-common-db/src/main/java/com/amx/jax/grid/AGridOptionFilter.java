package com.amx.jax.grid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.boot.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AGridOptionFilter implements Serializable {

	private static final long serialVersionUID = 8729995525943277702L;

	@Schema(example = "STATE", description = "Generic Type for Entity")
	private String type;

	@Schema(example = "PAYOUT_STATE", description = "Application Specific Paramter")
	private String fieldName;

	public AGridOptionFilter() {
		super();
	}

	public AGridOptionFilter(String type) {
		super();
		this.type = type;
		this.fieldName = ArgUtil.parseAsString(type);
	}

	public AGridOptionFilter(GridOptionType type) {
		super();
		this.type = ArgUtil.parseAsString(type);
		this.fieldName = ArgUtil.parseAsString(type);
	}

	public AGridOptionFilter(GridOptionType type, String fieldName) {
		this(type);
		this.fieldName = fieldName;
	}

	public AGridOptionFilter(String type, String fieldName) {
		this(type);
		this.fieldName = fieldName;
	}

	public String getType() {
		return type;
	}

	@JsonIgnore
	public void setGridOptionType(GridOptionType type) {
		this.type = ArgUtil.parseAsString(type);
	}

	public void setType(String type) {
		this.type = type;
	}

	public static class GridOption extends AGridOptionFilter {
		private static final long serialVersionUID = -7143799348092923192L;
		List<GridOptionFilter> filter;
		String permission;
		String action;

		public GridOption() {
			super();
			this.filter = new ArrayList<GridOptionFilter>();
		}

		public GridOption(GridOptionType type) {
			super(type);
			this.filter = new ArrayList<GridOptionFilter>();
		}

		public GridOption(String type) {
			super(type);
			this.filter = new ArrayList<GridOptionFilter>();
		}

		public GridOption(GridOptionType type, String fieldName) {
			super(type, fieldName);
			this.filter = new ArrayList<GridOptionFilter>();
		}

		public GridOption(String type, String fieldName) {
			super(type, fieldName);
			this.filter = new ArrayList<GridOptionFilter>();
		}

		public List<GridOptionFilter> getFilter() {
			return filter;
		}

		public void setFilter(List<GridOptionFilter> filter) {
			this.filter = filter;
		}

		public GridOptionFilter queryBy(AGridOptionFilter gridOption) {
			for (GridOptionFilter gridOptionFilter : filter) {
				if ((ArgUtil.isEmpty(gridOption.getType()) || gridOption.getType().equals(gridOptionFilter.getType()))
						&& (ArgUtil.isEmpty(gridOption.getFieldName())
								|| gridOption.getFieldName().equals(gridOptionFilter.getFieldName()))) {
					return gridOptionFilter;
				}
			}
			return null;
		}

		public GridOptionFilter queryByType(String type) {
			for (GridOptionFilter gridOptionFilter : filter) {
				if (type.equals(gridOptionFilter.getType())) {
					return gridOptionFilter;
				}
			}
			return null;
		}

		public GridOptionFilter queryByType(GridOptionType type) {
			return this.queryByType(ArgUtil.parseAsString(type));
		}

		public GridOptionFilter queryByFieldName(Object entity) {
			String entityString = ArgUtil.parseAsString(entity);
			for (GridOptionFilter gridOptionFilter : filter) {
				if (entityString.equals(gridOptionFilter.getFieldName())) {
					return gridOptionFilter;
				}
			}
			return null;
		}

		@Deprecated
		public GridOptionFilter queryByEntity(Object entity) {
			return this.queryByFieldName(entity);
		}

		public GridOption filter(GridOptionFilter... filters) {
			for (GridOptionFilter gridOptionFilter : filters) {
				this.filter.add(gridOptionFilter);
			}
			return this;
		}

		public GridOption filter(GridOption... filters) {
			for (GridOption gridOptionFilter : filters) {
				GridOptionFilter filter = new GridOptionFilter();
				filter.setType(gridOptionFilter.getType());
				filter.setFieldName(gridOptionFilter.getFieldName());
				this.filter.add(filter);
			}
			return this;
		}

		public String getPermission() {
			return permission;
		}

		public void setPermission(String permission) {
			this.permission = permission;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}
	}

	public static class GridOptionFilter extends AGridOptionFilter {
		private static final long serialVersionUID = 5569196192010034826L;

		public GridOptionFilter() {
			super();
		}

		public GridOptionFilter(GridOptionType type) {
			super(type);
		}

		public GridOptionFilter(GridOptionType type, String fieldName) {
			super(type, fieldName);
		}

		@Schema(example = "", description = "Value to be used")
		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
