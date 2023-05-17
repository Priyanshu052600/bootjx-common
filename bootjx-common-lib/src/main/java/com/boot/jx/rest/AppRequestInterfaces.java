package com.boot.jx.rest;

import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.http.CommonHttpRequest.ApiRequestDetail;
import com.boot.utils.JsonUtil;

import jakarta.servlet.http.HttpServletRequest;

public final class AppRequestInterfaces {

	public interface AppAuthUser {
		public boolean hasAccess(ApiRequestDetail apiRequest, CommonHttpRequest req);

		public String getAuthUser();
	}

	public interface AppAuthFilter {
		public boolean filterAppRequest(ApiRequestDetail apiRequest, CommonHttpRequest req, String traceId);
	}

	public interface IMetaRequestInFilter<T extends ARequestMetaInfo> {

		public Class<T> getMetaClass();

		default T export(String metaString) {
			return JsonUtil.fromJson(metaString, getMetaClass());
		}

		/**
		 * Meta Data Info you want to extract from incoming request
		 * 
		 * @param req
		 * @throws Exception
		 */
		public void importMeta(T meta, HttpServletRequest req);

		public void inFilter(T requestMeta);

	}

	public interface IMetaRequestOutFilter<T extends ARequestMetaInfo> {

		/**
		 * Meta Data Info you want to send with outgoing request
		 * 
		 * @param meta
		 */
		public void outFilter(T requestMeta);

	}

}
