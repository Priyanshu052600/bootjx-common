package com.boot.jx.filter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.boot.jx.AppContextUtil;
import com.boot.jx.api.ApiResponseUtil;
import com.boot.jx.exception.AmxApiError;
import com.boot.jx.exception.ApiHttpExceptions.ApiStatusCodes;
import com.boot.jx.exception.ExceptionMessageKey;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.http.CommonHttpRequest.ApiRequestDetail;
import com.boot.jx.rest.AppRequestInterfaces.AppAuthFilter;
import com.boot.jx.rest.RestService;
import com.boot.utils.ArgUtil;
import com.boot.utils.JsonUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AppRequestInterceptor implements HandlerInterceptor {

	final String sameSiteAttribute = "; SameSite=None";
	final String secureAttribute = "; Secure";

	@Autowired
	RestService restService;

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Autowired(required = false)
	List<AppAuthFilter> appAuthFilters;

	private boolean isValidRequest() {
		ApiRequestDetail apiRequest = AppContextUtil.getApiRequestDetail();
		if (ArgUtil.is(apiRequest) && ArgUtil.is(apiRequest.getRules())) {
			if (appAuthFilters != null) {
				for (AppAuthFilter appAuthFilter : appAuthFilters) {
					if (!appAuthFilter.filterAppRequest(apiRequest, commonHttpRequest, AppContextUtil.getTraceId())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		restService.importMetaFromStatic(request);

		if (!isValidRequest()) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			AmxApiError apiError = new AmxApiError();
			apiError.setHttpStatus(HttpStatus.FORBIDDEN);
			apiError.setStatusKey(ApiStatusCodes.ACCESS_DENIED.toString());
			apiError.setErrors(ApiResponseUtil.getErrors());
			ExceptionMessageKey.resolveLocalMessage(apiError);
			JsonUtil.getMapper().writeValue(response.getWriter(), apiError);
			return false;
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		// addEtagHeader(request, response);

		Collection<String> setCookieHeaders = response.getHeaders(HttpHeaders.SET_COOKIE);

		if (setCookieHeaders == null || setCookieHeaders.isEmpty())
			return;

		List<String> headers = setCookieHeaders.stream().filter(StringUtils::isNotBlank).map(header -> {
			if (header.toLowerCase().contains("samesite")) {
				return header;
			} else {
				return header.concat(sameSiteAttribute);
			}
		}).map(header -> {
			if (header.toLowerCase().contains("secure")) {
				return header;
			} else {
				return header.concat(secureAttribute);
			}
		}).collect(Collectors.toList());

		boolean isFirst = true;
		for (String finalHeader : headers) {
			if (isFirst) {
				response.setHeader(HttpHeaders.SET_COOKIE, finalHeader);
				isFirst = false;
			} else
				response.addHeader(HttpHeaders.SET_COOKIE, finalHeader);
		}

	}
}
