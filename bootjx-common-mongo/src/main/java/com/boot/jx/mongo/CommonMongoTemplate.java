package com.boot.jx.mongo;

import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.boot.jx.api.ApiPagination;
import com.boot.jx.api.ApiResponseUtil;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.mongo.CommonMongoQB.MQB;
import com.boot.jx.mongo.CommonMongoQB.MongoQueryBuilder;
import com.boot.jx.scope.tnt.TenantDefinations.TenantDefaultQualifier;
import com.boot.model.MapModel;
import com.boot.model.MapModel.MapEntry;
import com.boot.utils.ArgUtil;
import com.boot.utils.Constants;
import com.boot.utils.StringUtils;

@Primary
@Component
public class CommonMongoTemplate extends CommonMongoTemplateAbstract<CommonMongoTemplate> {

	public static class TenantDefaultMongoTemplateImpl extends CommonMongoTemplate {

		@Autowired
		@TenantDefaultQualifier
		protected MongoTemplate mongoTemplateTenantDefault;

		@Override
		protected MongoTemplate getCommonMongoTemplate() {
			return mongoTemplateTenantDefault;
		}

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

		public static <T2> PaginatedQuery<T2> select(Class<T2> docClass2, String collectionName2) {
			return new PaginatedQuery<T2>(docClass2, collectionName2);
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

	public <T> PaginatedQuery<T> getPages(PaginatedQuery<T> query) {

		MQB<T> q = MongoQueryBuilder.select(query.docClass, query.collectionName).page(query.pageNo, query.pageSize);

		query.extraParams = (query.extraParams == null) ? MapModel.createInstance() : query.extraParams;

		Enumeration<String> params = commonHttpRequest.getRequest().getParameterNames();

		while (params.hasMoreElements()) {
			String param = (String) params.nextElement();
			switch (param) {
			case "pageSize":
				query.pageSize = new MapEntry(commonHttpRequest.getRequest().getParameter("pageSize"))
						.asInteger(query.pageSize);
				break;
			case "pageNo":
				query.pageNo = new MapEntry(commonHttpRequest.getRequest().getParameter("pageNo"))
						.asInteger(query.pageNo);
				break;
			case "sortBy":
				query.sortBy = new MapEntry(commonHttpRequest.getRequest().getParameter("sortBy"))
						.asString(query.sortBy);
				break;
			case "sortDir":
				query.sortDir = new MapEntry(commonHttpRequest.getRequest().getParameter("sortDir"))
						.asString(query.sortDir);
				break;
			case "id":
				String idValue = commonHttpRequest.getRequest().getParameter("id");
				q.whereId(idValue);
				break;
			default:
				String paramValue = commonHttpRequest.getRequest().getParameter(param);
				if (ArgUtil.is(paramValue)) {
					if (!query.extraParams.entry(param).exists()) {
						query.extraParams.put(param, paramValue);
					}
				}
			}
		}

		for (Entry<String, Object> entry : query.extraParams.map().entrySet()) {
			String paramValue = ArgUtil.parseAsString(entry.getValue(), Constants.BLANK);
			if (paramValue.startsWith("*") && paramValue.endsWith("*")) {
				q.search(entry.getKey(), StringUtils.trim(paramValue, '*'));
			} else {
				q.where(entry.getKey()).is(paramValue);
			}
		}

		if (ArgUtil.is(query.sortBy)) {
			q = q.sortBy(query.sortBy, Direction.fromString(query.sortDir));
		}
		ApiResponseUtil.addLog(q.build().getQuery().toString());
		// System.out.println(q.build().getQuery().toString());
		ApiPagination pagination = new ApiPagination();
		pagination.setPageNo(query.pageNo);
		pagination.setPageSize(query.pageSize);
		pagination.setSortBy(query.sortBy);
		pagination.setSortDir(query.sortDir);
		if (query.count) {
			pagination.setTotal(this.count(null, query.docClass));
		}
		query.setResults(this.find(q));
		query.setPagination(pagination);
		ApiResponseUtil.pagination(pagination);
		return query;
	}

}
