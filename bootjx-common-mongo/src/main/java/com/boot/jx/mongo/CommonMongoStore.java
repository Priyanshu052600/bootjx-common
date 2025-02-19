package com.boot.jx.mongo;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpMethod;

import com.boot.jx.api.ApiPagination;
import com.boot.jx.api.ApiResponse;
import com.boot.jx.api.ApiResponseUtil;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.mongo.CommonDocInterfaces.IDocument;
import com.boot.jx.mongo.CommonMongoQB.FieldCriteriaMap;
import com.boot.jx.mongo.CommonMongoQB.MQB;
import com.boot.jx.mongo.CommonMongoQB.MongoQueryBuilder;
import com.boot.model.MapModel;
import com.boot.model.MapModel.MapEntry;
import com.boot.utils.ArgUtil;
import com.boot.utils.Constants;
import com.boot.utils.StringUtils;
import com.boot.utils.StringUtils.StringMatcher;
import com.mongodb.BasicDBObject;

public class CommonMongoStore<TStore extends CommonMongoStore<TStore>> extends CommonMongoTemplateAbstract<TStore> {

	static {
		CommonDocInterfaces.initObjectMapping();
	}

	@Autowired
	private CommonHttpRequest commonHttpRequest;

	public static class PaginatedQuery<T> {

		public String collectionName;
		public Class<T> docClass;
		public int pageNo;
		public int pageSize;
		public String sortBy;
		public String sortDir;
		public boolean count;
		public MapModel extraParams;
		private List<T> results;
		private ApiPagination pagination;
		private boolean skipDBRef;

		public PaginatedQuery(Class<T> docClass, String collectionName) {
			this.docClass = docClass;
			this.collectionName = collectionName;
		}

		public PaginatedQuery<T> pageNo(int pageNo) {
			this.pageNo = pageNo;
			return this;
		}

		public PaginatedQuery<T> pageSize(int pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		public PaginatedQuery<T> sortBy(String sortBy) {
			this.sortBy = sortBy;
			return this;
		}

		public PaginatedQuery<T> sortDir(String sortDir) {
			this.sortDir = sortDir;
			return this;
		}

		public PaginatedQuery<T> count() {
			this.count = true;
			return this;
		}

		public PaginatedQuery<T> skipDBRef() {
			this.skipDBRef = true;
			return this;
		}

		/**
		 * 
		 * These params will override any query params
		 * 
		 * @param extraParams
		 * @return
		 */
		public PaginatedQuery<T> extraParams(MapModel extraParams) {
			this.extraParams = extraParams;
			return this;
		}

		public MapModel extraParams() {
			if (this.extraParams == null) {
				this.extraParams = MapModel.createInstance();
			}
			return this.extraParams;
		}

		/**
		 * These param will override any query param with same name
		 * 
		 * @param key
		 * @param value
		 * @return
		 */
		public PaginatedQuery<T> extraParam(String key, Object value) {
			this.extraParams().put(key, value);
			return this;
		}

		public static <T2> PaginatedQuery<T2> select(Class<T2> docClass2, String collectionName2) {
			return new PaginatedQuery<T2>(docClass2, collectionName2);
		}

		public static PaginatedQuery<BasicDBObject> select(String collectionName2) {
			return new PaginatedQuery<BasicDBObject>(BasicDBObject.class, collectionName2);
		}

		public PaginatedQuery<T> where(String field, String value) {
			this.extraParams().put(field, value);
			return this;
		}

		public List<T> getResults() {
			return results;
		}

		public void setResults(List<T> results) {
			this.results = results;
		}

		public ApiPagination getPagination() {
			return pagination;
		}

		public void setPagination(ApiPagination pagination) {
			this.pagination = pagination;
		}

		public PaginatedQuery<T> sort(Direction sortDir, String sortBy) {
			this.sortDir = sortDir.toString();
			this.sortBy = sortBy;
			return this;
		}
	}

	public interface QueryParams {
		public static String RANGE_FORMAT = "<=?\\d*,\\d*=?\\>";
		public static String ANY_OF_FORMAT = "\\(([^|]+\\|?)+\\)";
		public static String ALL_OF_FORMAT = "\\(([^,]+,?)+\\)";
		public static final Pattern COMPARE_PATTERN = Pattern.compile("(.+?)(<=|>=|<|>)$");
		public static final Pattern RANGE_PATTERN = Pattern
				.compile("\\s*(<=|=<|>=|=>|<|>)?\\s*(\\d*)\\s*,\\s*(\\d*)\\s*(<=|=<|>=|=>|<|>)?\\s*");

		public Enumeration<String> getParameterNames();

		public String getParameter(String param);

	}

	public static class HttpQueryParams implements QueryParams {
		CommonHttpRequest commonHttpRequest;

		public HttpQueryParams(CommonHttpRequest commonHttpRequest) {
			this.commonHttpRequest = commonHttpRequest;
		}

		public String getParameter(String param) {
			return commonHttpRequest.getRequest().getParameter(param);
		}

		public Enumeration<String> getParameterNames() {
			return commonHttpRequest.getRequest().getParameterNames();
		}
	}

	public static class ModelQueryParams implements QueryParams {
		MapModel model;

		public ModelQueryParams(MapModel model) {
			this.model = model;
		}

		public String getParameter(String param) {
			return model.getString(param);
		}

		public Enumeration<String> getParameterNames() {
			return Collections.enumeration(model.map().keySet());
		}
	}

	public static <T> MQB<T> getPages(QueryParams queryParams, PaginatedQuery<T> query) {
		final MQB<T> q = MongoQueryBuilder.select(query.docClass, query.collectionName);

		query.extraParams = (query.extraParams == null) ? MapModel.createInstance() : query.extraParams;

		Enumeration<String> params = queryParams.getParameterNames();

		while (params.hasMoreElements()) {
			String param = (String) params.nextElement();
			String paramLowCase = param.toLowerCase();
			switch (paramLowCase) {
			case "pagesize":
			case "page_size":
				query.pageSize = new MapEntry(queryParams.getParameter(param)).asInteger(query.pageSize);
				break;
			case "page_no":
			case "pageno":
				query.pageNo = new MapEntry(queryParams.getParameter(param)).asInteger(query.pageNo);
				break;
			case "sort_by":
			case "sortby":
				query.sortBy = new MapEntry(queryParams.getParameter(param)).asString(query.sortBy);
				break;
			case "sort_dir":
			case "sortdir":
				query.sortDir = new MapEntry(queryParams.getParameter(param)).asString(query.sortDir);
				break;
			case "id":
				String idValue = queryParams.getParameter("id");
				q.whereId(idValue);
				break;
			case "_":
			case "*":
				// Ignore these as can be used for api versioning
				break;
			default:
				String paramValue = queryParams.getParameter(param);
				if (ArgUtil.is(paramValue)) {
					if (!query.extraParams.entry(param).exists()) {
						query.extraParams.put(param, paramValue);
					}
				}
			}
		}

		// Map to hold conditions for each field
		FieldCriteriaMap fieldCriteriaMap = new FieldCriteriaMap();

		for (Entry<String, Object> entry : query.extraParams.map().entrySet()) {
			String value = ArgUtil.parseAsString(entry.getValue(), Constants.BLANK);
			String key = entry.getKey();
			if (value.startsWith("*") && value.endsWith("*")) {
				q.search(entry.getKey(), StringUtils.trim(value, '*'));
			} else {
				StringMatcher keyMatcher = new StringMatcher(entry.getKey());
				if (keyMatcher.isMatch(QueryParams.COMPARE_PATTERN)) {
					String fieldName = keyMatcher.group(1);;
					String fieldOperator = keyMatcher.group(2);
					fieldCriteriaMap.where(fieldName, fieldOperator, value);
				} else {
					StringMatcher valueMatcher = new StringMatcher(value);
					if (valueMatcher.isMatch(QueryParams.RANGE_PATTERN)) {
						String fromOperator = valueMatcher.group(1); // < or <=
						String fromValue = valueMatcher.group(2); // The first number
						String toValue = valueMatcher.group(3); // The second number
						String toOperator = valueMatcher.group(4); // > or >=
						fieldCriteriaMap.range(key, fromOperator, fromValue, toValue, toOperator);
					} else if (value.matches(QueryParams.ANY_OF_FORMAT)) {
						String inner = value.substring(1, value.length() - 1); // Remove the parentheses
						fieldCriteriaMap.where(key).in(Arrays.asList(inner.split("\\|")));
					} else if (value.matches(QueryParams.ALL_OF_FORMAT)) {
						String inner = value.substring(1, value.length() - 1); // Remove the parentheses
						fieldCriteriaMap.where(key).all(Arrays.asList(inner.split(",")));
					} else {
						q.where(entry.getKey()).is(value);
					}
				}
			}

		}

		q.and(fieldCriteriaMap);

		q.page(query.pageNo, query.pageSize);

		if (ArgUtil.is(query.sortBy)) {
			q.sortBy(query.sortBy, Direction.fromString(query.sortDir));
		}

		if (query.skipDBRef) {
			q.skipDBRef();
		}

		ApiResponseUtil.addLog(q.build().getQuery().toString());
		// System.out.println(q.build().getQuery().toString());
		return q;
	}

	public <T> PaginatedQuery<T> getPages(PaginatedQuery<T> query) {

		MQB<T> q = getPages(new HttpQueryParams(commonHttpRequest), query);

		ApiPagination pagination = new ApiPagination();
		pagination.setPageNo(query.pageNo);
		pagination.setPageSize(query.pageSize);
		pagination.setSortBy(query.sortBy);
		pagination.setSortDir(query.sortDir);
		if (query.count) {
			pagination.setTotal(this.count(q));
		}
		query.setResults(this.find(q));
		query.setPagination(pagination);
		ApiResponseUtil.pagination(pagination);
		return query;
	}

	public <T extends IDocument> ApiResponse<T, Object> submit(HttpMethod method, String id, T quickGalleryItem,
			Class<T> entityClass, String docName) throws InstantiationException, IllegalAccessException {
		switch (method) {
		case GET:
			if (ArgUtil.is(id)) {
				return ApiResponse.buildResults(mongoTemplate.findById(id, entityClass));
			}
			return ApiResponse.buildResults(mongoTemplate.findAll(entityClass));
		case DELETE:
			T qr = removeAndAudit(id, entityClass);
			return ApiResponse.buildResults(mongoTemplate.findAll(entityClass)).data(qr).message(docName + " deleted");
		case POST:
			saveOnSubmit(quickGalleryItem, entityClass);
			return ApiResponse.buildResults(quickGalleryItem).message(docName + " Saved");
		default:
			break;
		}
		return null;
	}

	public <T extends IDocument> T saveOnSubmit(T quickGalleryItem, Class<T> entityClass) {
		save(quickGalleryItem);
		return quickGalleryItem;
	}

	public <T extends IDocument> ApiResponse<T, Object> submit(HttpMethod method, String id, Class<T> entityClass,
			String docName) throws InstantiationException, IllegalAccessException {
		return submit(method, id, null, entityClass, docName);
	}

	public <T extends IDocument> ApiResponse<T, Object> submit(HttpMethod method, T quickGalleryItem,
			Class<T> entityClass, String docName) throws InstantiationException, IllegalAccessException {
		return submit(method, null, quickGalleryItem, entityClass, docName);
	}

}
