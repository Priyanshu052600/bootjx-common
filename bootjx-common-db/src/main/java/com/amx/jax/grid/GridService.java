package com.amx.jax.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.amx.jax.grid.GridConstants.FilterDataType;
import com.amx.jax.grid.GridConstants.FilterOperater;
import com.amx.jax.grid.GridConstants.GridColumnOrder;
import com.amx.jax.grid.repository.DynamicFieldDescriptorRepository;
import com.boot.jx.api.ApiResponse;
import com.boot.jx.dict.GridView;
import com.boot.jx.exception.AmxException;
import com.boot.jx.logger.LoggerService;
import com.boot.utils.ArgUtil;
import com.boot.utils.EntityDtoUtil;
import com.boot.utils.IoUtils;
import com.boot.utils.StringUtils.StringMatcher;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Component
public class GridService {

	public static Logger LOGGER = LoggerService.getLogger(GridService.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired(required = false)
	private DynamicFieldDescriptorRepository dynamicFieldDescriptorRepository;

	public <T> GridViewBuilder<T> view(String gridViewName, GridInfo<T> gridInfo, GridQuery gridQuery) {

		String baseQuery = gridInfo.getQuery();
		Class<T> gridViewRecordClass = gridInfo.getResultClass();

		boolean isGridViewRecordClass = false;
		List<GridFieldDescriptor> fields = new ArrayList<GridFieldDescriptor>();
		if (ArgUtil.is(gridViewRecordClass)) {
			isGridViewRecordClass = GridViewRecord.class.isAssignableFrom(gridViewRecordClass);
			Map<String, String> map = gridInfo.getMap();
			if (ArgUtil.is(gridQuery.getColumns()) && ArgUtil.is(map)) {
				for (GridColumn column : gridQuery.getColumns()) {
					String data = column.getKey();
					if (map.containsKey(data)) {
						column.setKey(map.get(data));
					}
				}
			}
		} else {

			if (ArgUtil.isEmpty(dynamicFieldDescriptorRepository)) {
				throw new AmxException("DynamicFieldDescriptorRepository is Not Defined");
			}

			List<DynamicFieldDescriptorEntity> fieldEntities = dynamicFieldDescriptorRepository
					.findActiveColumnByDBviewName(gridInfo.getTable(), Sort.by(Sort.Direction.ASC, "displayOrder"));

			Map<String, GridFieldDescriptor> map = new HashMap<String, GridFieldDescriptor>();
			for (DynamicFieldDescriptorEntity fieldEntity : fieldEntities) {
				GridFieldDescriptorDTO x = EntityDtoUtil.entityToDto(fieldEntity, new GridFieldDescriptorDTO());
				map.put(fieldEntity.getDbColumnName(), x);
				fields.add(fieldEntity);

				if (ArgUtil.is(gridQuery.getSortColumn())) {
					if (gridQuery.getSortColumn().equals(fieldEntity.getDbColumnName())) {
						// gridQuery.setSortBy(gridQuery.getColumns().size());
						GridColumnOrder sortOrder = new GridColumn();
						sortOrder.setKey(fieldEntity.getDbColumnName());
						sortOrder.setSortDir(
								ArgUtil.parseAsEnumT(gridQuery.getSortOrder(), SortOrder.ASC, SortOrder.class));
						gridQuery.setOrder(sortOrder);
					}
				}

			}

			GridFieldDescriptor des = new GridFieldDescriptorDTO();
			des.setDbColumnName("SYSDATE");
			des.setIsActive("N");
			des.setFieldDataType("DATE");
			des.setFieldLabel("System Date");
			fields.add(des);

			if (ArgUtil.is(gridQuery.getColumns()) && ArgUtil.is(map)) {
				List<GridColumn> columns = new ArrayList<GridColumn>();
				for (GridColumn column : gridQuery.getColumns()) {
					String data = column.getKey();
					if (map.containsKey(data)) {
						columns.add(column);
					}
				}
				gridQuery.setColumns(columns);
			}

			if (ArgUtil.isEmpty(gridQuery.getColumns())) {
				gridQuery.setColumns(new ArrayList<GridColumn>());
			}

			if (ArgUtil.is(gridQuery.getFilter())) {
				for (Entry<String, Object> filter : gridQuery.getFilter().entrySet()) {

					String keyStr = filter.getKey();
					StringMatcher matcher = new StringMatcher(keyStr);
					FilterOperater op = FilterOperater.EQ;
					String keyFunc  = null;

					if (matcher.isMatch(GridConstants.OPERATOR_FILTER_DOUBLE)
							|| matcher.isMatch(GridConstants.OPERATOR_FILTER_SINGLE)) {
						op = FilterOperater.fromSign(matcher.group(2));
						keyStr = matcher.group(1);
						keyFunc =  matcher.group(3);
						
					}

					GridFieldDescriptor ds = map.get(keyStr);
					if (ArgUtil.is(ds)) {
						FilterDataType filterDataType = ArgUtil.parseAsEnumT(ds.getFieldDataType(), null,
								FilterDataType.class);
						if (ArgUtil.is(filterDataType)) {
							GridColumn filterCol = new GridColumn();
							filterCol.setOperator(op);
							filterCol.setValueFunc(keyFunc);
							filterCol.setValue(ArgUtil.parseAsString(filter.getValue()));
							filterCol.setDataType(filterDataType);
							filterCol.setKey(keyStr);

							if (ArgUtil.is(gridQuery.getSortColumn())) {
								if (gridQuery.getSortColumn().equals(keyStr)) {
									// gridQuery.setSortBy(gridQuery.getColumns().size());
									filterCol.setSortDir(gridQuery.getSortOrder());
								}
							} else {
								filterCol.setSortDir(SortOrder.ASC);
							}
							gridQuery.getColumns().add(filterCol);
						}
					}
				}
			}
			baseQuery = createBaseQuery(gridInfo.getTable(), fields);
		}

		DataTableRequest dataTableInRQ = new DataTableRequest(gridQuery);
		PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();
		String paginatedQuery = GridUtil.buildPaginatedQueryForOracle(baseQuery, pagination,
				gridQuery.isPaginated() && isGridViewRecordClass, gridInfo.isCustomeQuery(), gridInfo);
		try {
			LOGGER.debug("Grid Query {} "+paginatedQuery);
			Query query = null;
			if (ArgUtil.is(gridViewRecordClass)) {
				query = entityManager.createNativeQuery(paginatedQuery, gridViewRecordClass);
			} else {
				query = entityManager.createNativeQuery(paginatedQuery);
			}

			return new GridViewBuilder<T>(query, dataTableInRQ).queryStr(paginatedQuery).gridView(gridViewName)
					.fieldDescriptors(fields);
		} catch (Exception e) {
			LOGGER.error("GridViewError V:{} Q {}", gridViewName, paginatedQuery);
			throw e;
		}
	}

	public <T> GridViewBuilder<T> view(GridView gridView, GridQuery gridQuery) {
		@SuppressWarnings("unchecked")
		GridInfo<T> gridInfo = (GridInfo<T>) GridViewFactory.get(gridView);
		return this.view(gridView.name(), gridInfo, gridQuery);
	}

	public GridViewBuilder<Map<String, Object>> view(String gridViewName, GridQuery gridQuery) {
		GridInfo<Map<String, Object>> gridInfo = new GridInfo<Map<String, Object>>(gridViewName).build();
		return this.view(gridViewName, gridInfo, gridQuery);
	}

	public <T> GridViewBuilder<T> view(GridView gridView, String sqlQuery) {
		@SuppressWarnings("unchecked")
		GridInfo<T> gridInfo = (GridInfo<T>) GridViewFactory.get(gridView);
		Class<T> gridViewRecordClass = gridInfo.getResultClass();
		try {
			LOGGER.debug(sqlQuery);
			Query query = entityManager.createNativeQuery(sqlQuery, gridViewRecordClass);
			return new GridViewBuilder<T>(query, null).queryStr(sqlQuery).gridView(gridView.name());
		} catch (Exception e) {
			LOGGER.error("GridViewError V:{} Q {}", gridView, sqlQuery);
			throw e;
		}
	}

	public static class GridViewBuilder<T> {
		Query query;
		DataTableRequest dataTableRequest;
		String queryStr;
		String gridView;
		List<GridFieldDescriptor> fieldDescriptors;

		GridViewBuilder(Query query, DataTableRequest dataTableRequest) {
			this.query = query;
			this.dataTableRequest = dataTableRequest;
		}

		public GridViewBuilder<T> queryStr(String queryStr) {
			this.queryStr = queryStr;
			return this;
		}

		public GridViewBuilder<T> gridView(String gridViewName) {
			this.gridView = gridViewName;
			return this;
		}

		public GridViewBuilder<T> fieldDescriptors(List<GridFieldDescriptor> fieldDescriptors) {
			this.fieldDescriptors = fieldDescriptors;
			return this;
		}

		public ApiResponse<T, GridMeta> meta() {
			try {
				List<T> returnList = new ArrayList<T>();
				GridMeta meta = new GridMeta();
				meta.setDescriptors(fieldDescriptors);
				meta.setQueryStr(queryStr);
				return ApiResponse.buildList(returnList, meta);
			} catch (Exception e) {
				LOGGER.error("GridViewError V:{} Q {}", gridView, queryStr);
				throw e;
			}
		}

		@SuppressWarnings("unchecked")
		public ApiResponse<T, GridMeta> get() {
			try {
				List<T> returnList = new ArrayList<T>();

				List<T> userList = query.getResultList();

				GridMeta meta = new GridMeta();
				if (ArgUtil.is(userList)) {
					T firstElement = userList.get(0);
					int totalRecords = userList.size();
					if (firstElement instanceof GridViewRecord) {
						totalRecords = ((GridViewRecord) firstElement).getTotalRecords();
					}

					meta.setRecordsTotal(ArgUtil.parseAsString(totalRecords));
					if (dataTableRequest != null && dataTableRequest.getPaginationRequest().isFilterByEmpty()) {
						meta.setRecordsFiltered(ArgUtil.parseAsString(totalRecords));
					} else {
						meta.setRecordsFiltered(ArgUtil.parseAsString(userList.size()));
					}

					meta.setDescriptors(fieldDescriptors);
					if (ArgUtil.is(firstElement) && firstElement.getClass().isArray() && ArgUtil.is(fieldDescriptors)) {
						for (T record : userList) {
							List<Object> recordList = ArgUtil.parseAsListOfT(record, null, null, false);
							Map<String, Object> recordMap = new HashMap<String, Object>();
							int j = 0;
							for (int i = 0; i < fieldDescriptors.size(); i++) {
								GridFieldDescriptor fieldDescriptor = fieldDescriptors.get(i);
								if (isDBColumn(fieldDescriptor)) {
									Object value = recordList.get(j);
									if (ArgUtil.is(value) && (value instanceof java.sql.Clob)) {
										value = IoUtils.clobStringConversion((java.sql.Clob) value);
									}
									recordMap.put(fieldDescriptor.getDbColumnName(), value);
									j++;
								} else {
									recordMap.put(fieldDescriptor.getDbColumnName(), null);
								}

							}
							returnList.add((T) recordMap);
						}
					} else {
						returnList = userList;
					}
				}

				meta.setQueryStr(queryStr);
				return ApiResponse.buildList(returnList, meta);
			} catch (Exception e) {
				LOGGER.error("GridViewError V:{} Q {}", gridView, queryStr);
				throw e;
			}

		}

	}

	private String createBaseQuery(String tableName, List<GridFieldDescriptor> fields) {
		StringJoiner sj = new StringJoiner(",");
		for (GridFieldDescriptor dynamicFieldDescriptor : fields) {
			// if (AmxDBConstants.Yes.equals(dynamicFieldDescriptor.getIsActive()))
			if (isDBColumn(dynamicFieldDescriptor)) {
				sj.add(dynamicFieldDescriptor.getDbColumnName());
			}
		}
		return String.format("SELECT %s from %s", sj.toString(), tableName);
	}

	private static boolean isDBColumn(GridFieldDescriptor gridFieldDescriptor) {
		return ArgUtil.is(gridFieldDescriptor.getFieldDataType());
	}

}