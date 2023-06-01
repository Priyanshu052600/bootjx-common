package com.boot.jx.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.boot.jx.api.AmxResponseSchemes.ApiResultsMetaCompactResponse;
import com.boot.jx.api.AmxResponseSchemes.ApiResultsMetaResponse;
import com.boot.jx.exception.IExceptionEnum;
import com.boot.utils.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T, M> extends AResponse<M> implements ApiResultsMetaResponse<T, M>,
		ApiResultsMetaCompactResponse<T, M>, Serializable {

	private static final long serialVersionUID = 2026047322050489651L;

	protected List<T> results = null;

	protected Object query = null;

	public ApiResponse() {
		super();
		this.results = new ArrayList<T>();
	}

	/**
	 * Instantiates a new amx api response.
	 *
	 * @param resultList the result list
	 */
	public ApiResponse(List<T> resultList) {
		super();
		this.results = resultList;
	}

	/**
	 * Instantiates a new amx api response.
	 *
	 * @param resultList the result list
	 * @param meta       the meta
	 */
	public ApiResponse(List<T> resultList, M meta) {
		super();
		this.results = resultList;
		this.meta = meta;
	}


	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	@JsonIgnore
	public T getResult() {
		if (results != null && !results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}

	@JsonIgnore
	public void setResult(T result) {
		this.results = new ArrayList<T>();
		this.results.add(result);
	}

	public void addResult(T result) {
		this.results.add(result);
	}

	public ApiResponse<T, M> addResults(List<T> results) {
		for (T result : results) {
			this.addResult(result);
		}
		return this;
	}

	public ApiResponse<T, M> result(T result) {
		this.addResult(result);
		return this;
	}

	public ApiResponse<T, M> results(List<T> resultList) {
		this.setResults(resultList);
		return this;
	}

	public ApiResponse<T, M> results(T[] resultList) {
		this.setResults(CollectionUtil.asList(resultList));
		return this;
	}

	public static <TS, MS> ApiResponse<TS, MS> build() {
		return new ApiResponse<TS, MS>();
	}

	public static <TS, MS> ApiResponse<TS, MS> instance() {
		return new ApiResponse<TS, MS>();
	}

	public static <TS> ApiResponse<TS, Object> instance(Class<TS> clazz) {
		return new ApiResponse<TS, Object>();
	}

	public static <TS, MS> ApiResponse<TS, MS> instance(Class<TS> clazz, Class<MS> metaClass) {
		return new ApiResponse<TS, MS>();
	}

	
	public static <TS, MS> ApiResponse<TS, MS> build(TS result, MS meta) {
		return buildResult(result, meta);
	}

	/**
	 * Builds the list.
	 * 
	 * @param <TS>       the generic type
	 * @param resultList the result list
	 * @return the amx api response
	 */
	public static <TS> ApiResponse<List<TS>, HashMap<String, Object>> build(List<TS> resultList) {
		return buildResult(resultList, new HashMap<String, Object>());
	}

	public static <MS> ApiResponse<Object, MS> buildMeta(MS meta) {
		ApiResponse<Object, MS> resp = new ApiResponse<Object, MS>();
		resp.setMeta(meta);
		return resp;
	}

	/**
	 * Builds the list.
	 *
	 * @param <TS>       the generic type
	 * @param resultList the result list
	 * @return the amx api response
	 */
	public static <TS> ApiResponse<TS, Object> buildList(List<TS> resultList) {
		return buildList(resultList, new HashMap<String, Object>());
	}

	/**
	 * Builds the list.
	 *
	 * @param <TS>       the generic type
	 * @param <MS>       the generic type
	 * @param resultList the result list
	 * @param meta       the meta
	 * @return the amx api response
	 */
	public static <TS, MS> ApiResponse<TS, MS> buildList(List<TS> resultList, MS meta) {
		return buildResults(resultList, meta);
	}

	public static <TS, MS> ApiResponse<TS, MS> buildList(TS[] resultArray, MS meta) {
		return buildResults(CollectionUtil.getList(resultArray), meta);
	}

	public static <TS> ApiResponse<TS, Object> buildResults(TS[] resultArray) {
		return buildResults(CollectionUtil.getList(resultArray), new HashMap<String, Object>());
	}

	public static <TS> ApiResponse<TS, Object> buildResults(TS resultList) {
		return buildResult(resultList, new HashMap<String, Object>());
	}

	public static <TS, MS> ApiResponse<TS, MS> buildResults(TS resultList, MS meta) {
		return buildResult(resultList, meta);
	}

	public static <TS> ApiResponse<TS, Object> buildResults(List<TS> resultList) {
		return buildResults(resultList, new HashMap<String, Object>());
	}

	public static <TS, MS> ApiResponse<TS, MS> buildResults(List<TS> resultList, MS meta) {
		ApiResponse<TS, MS> resp = new ApiResponse<TS, MS>();
		// ArrayList<TS> listOfStrings = new ArrayList<TS>(resultList.size());
		// listOfStrings.addAll(resultList);
		resp.setResults(resultList);
		resp.setMeta(meta);
		return resp;
	}

	public static <TS, MS> ApiResponse<TS, MS> buildResult(TS result, MS meta) {
		ApiResponse<TS, MS> resp = new ApiResponse<TS, MS>();
		resp.addResult(result);
		resp.setMeta(meta);
		return resp;
	}

	public static <TS, MS> ApiResponse<TS, MS> buildResult(TS result) {
		ApiResponse<TS, MS> resp = new ApiResponse<TS, MS>();
		resp.addResult(result);
		return resp;
	}

	@JsonIgnore
	public ApiResponse<T, M> redirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
		return this;
	}

	public ApiResponse<T, M> statusKey(String status) {
		this.setStatus(status);
		return this;
	}

	public ApiResponse<T, M> statusEnum(IExceptionEnum statusEnum) {
		this.setStatusEnum(statusEnum);
		return this;
	}

	public ApiResponse<T, M> message(String message) {
		this.message = message;
		return this;
	}


	public ApiResponse<T, M> details(List<M> details) {
		this.details = details;
		return this;
	}

	public ApiResponse<T, M> query(Object query) {
		this.query = query;
		return this;
	}

	public Object getQuery() {
		return query;
	}

	public void setQuery(Object query) {
		this.query = query;
	}

}
