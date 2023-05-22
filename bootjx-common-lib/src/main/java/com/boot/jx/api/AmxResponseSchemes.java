package com.boot.jx.api;

import java.util.List;

import com.boot.jx.AppContextUtil;

public class AmxResponseSchemes {

	public interface ApiWrapperResponse {

		public void setTimestamp(Long timestamp);

//		@ApiMockModelProperty(example = "1541276788518")
		public Long getTimestamp();

		// @ApiMockModelProperty(example = "200")
		public String getStatus();

		public void setStatus(String status);

		// @ApiMockModelProperty(example = "SUCCESS")
		public String getStatusKey();

		public void setStatusKey(String statusKey);

		// @ApiMockModelProperty(example = "This is success message in plain english")
		public String getMessage();

		public void setMessage(String message);

		// @ApiMockModelProperty(example = "MESSAGE_SUCCESS:MOBILE:12")
		public String getMessageKey();

		public void setMessageKey(String messageKey);

		public default String getTraceid() {
			return AppContextUtil.getTraceId(false, false);
		};

	}

	public interface ApiMetaResponse<M> extends ApiWrapperResponse {
		public M getMeta();

		public void setMeta(M reta);
	}

	public interface ApiMetaDetailsResponse<M> extends ApiWrapperResponse, ApiMetaResponse<M> {

		public List<M> getDetails();

		public void setDetails(List<M> details);
	}

	public interface ApiResultsResponse<T> extends ApiWrapperResponse {
		public List<T> getResults();

		public void setResults(List<T> results);
	}

	public interface ApiDataResponse<T> extends ApiWrapperResponse {
		public T getData();

		public void setData(T data);
	}

	public interface ApiResultsMetaResponse<T, M> extends ApiResultsResponse<T>, ApiMetaDetailsResponse<M> {
	}

	public interface ApiDataMetaResponse<T, M> extends ApiDataResponse<T>, ApiMetaDetailsResponse<M> {
	}

	public interface ApiResultsMetaCompactResponse<T, M> extends ApiResultsResponse<T>, ApiMetaResponse<M> {
	}

}
