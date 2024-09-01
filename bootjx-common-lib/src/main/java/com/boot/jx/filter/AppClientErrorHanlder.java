package com.boot.jx.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.boot.jx.AppConstants;
import com.boot.jx.api.ApiResponseUtil;
import com.boot.jx.exception.AmxApiError;
import com.boot.jx.exception.AmxApiException;
import com.boot.jx.exception.ApiHttpExceptions.ApiErrorException;
import com.boot.jx.exception.ApiHttpExceptions.ApiHttpClientException;
import com.boot.jx.exception.ApiHttpExceptions.ApiHttpNotFoundException;
import com.boot.jx.exception.ApiHttpExceptions.ApiHttpServerException;
import com.boot.jx.exception.ApiHttpExceptions.ApiHttpStatus;
import com.boot.jx.exception.ApiHttpExceptions.ApiStatusCodes;
import com.boot.jx.exception.ExceptionFactory;
import com.boot.jx.exception.IExceptionEnum;
import com.boot.utils.ArgUtil;
import com.boot.utils.IoUtils;
import com.boot.utils.JsonUtil;

@Component
public class AppClientErrorHanlder implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {

		ApiHttpStatus status = ApiHttpStatus.from(response);

		if (!status.is(HttpStatus.OK)) {
			return true;
		}
		String apiErrorJson = (String) response.getHeaders().getFirst(AppConstants.ERROR_HEADER_KEY);
		if (!ArgUtil.isEmpty(apiErrorJson)) {
			return true;
		}
		Object hasExceptionHeader = response.getHeaders().getFirst(AppConstants.EXCEPTION_HEADER_KEY);
		if (!ArgUtil.isEmpty(hasExceptionHeader)) {
			return true;
		}
		return false;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		ApiHttpStatus status = ApiHttpStatus.from(response);

		// HttpStatus statusCode = response.getStatusCode();
		String statusText = status.getStatusText();
		String apiErrorJson = ArgUtil.parseAsString(response.getHeaders().getFirst(AppConstants.ERROR_HEADER_KEY));
		AmxApiError apiError = throwError(apiErrorJson, ApiStatusCodes.UNKNOWN, response);

//	if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
//	    throw new ApiHttpNotFoundException(statusCode);
//	}
		boolean hasExceptionHeader = !ArgUtil
				.isEmpty(response.getHeaders().getFirst(AppConstants.EXCEPTION_HEADER_KEY));

		if (status.series() == HttpStatus.Series.SERVER_ERROR) {
			if (status.is(HttpStatus.BAD_GATEWAY)) {
				apiError = throwError(null, ApiStatusCodes.HTTP_SERVER_ERROR, response, status.getStatus());

				ApiResponseUtil.addTrace(apiError);
				throw new ApiHttpServerException(status.getStatus(), apiError);
			} else {
				String body = IoUtils.inputstream_to_string(response.getBody());
				apiError = throwError(body, ApiStatusCodes.HTTP_SERVER_ERROR, response);
			}
			ApiResponseUtil.addTrace(apiError);
			throw new ApiHttpServerException(status.getStatus(), apiError);
		} else if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
			String body2 = IoUtils.inputstream_to_string(response.getBody());
			apiError = throwError(body2, ApiStatusCodes.UNKNOWN_CLIENT_ERROR, response);

			ApiResponseUtil.addTrace(apiError);
			if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new ApiHttpNotFoundException(status.getStatus(), apiError);
			} else {
				throw new ApiHttpClientException(status.getStatus(), apiError);
			}
		} else if (hasExceptionHeader) {
			String body = IoUtils.inputstream_to_string(response.getBody());
			apiError = throwError(body, ApiStatusCodes.UNKNOWN, response);

			ApiResponseUtil.addTrace(apiError);
			throw new ApiErrorException(apiError);
		}

	}

	private AmxApiError throwError(String apiErrorJson, IExceptionEnum errorEnum, ClientHttpResponse response)
			throws IOException {
		AmxApiError e = throwError(apiErrorJson, errorEnum, response, null);
		e.setRawStatusCode(response.getRawStatusCode());
		return e;
	}

	private AmxApiError throwError(String apiErrorJson, IExceptionEnum errorEnum, ClientHttpResponse response,
			HttpStatus httpStatus) {
		return throwError(apiErrorJson, errorEnum, httpStatus);
	}

	private AmxApiError throwError(String apiErrorJson, IExceptionEnum errorEnum, HttpStatus httpStatus) {
		if (!ArgUtil.is(apiErrorJson) && ArgUtil.is(httpStatus)) {
			AmxApiError defaulError = new AmxApiError();
			defaulError.setHttpStatus(httpStatus);
			defaulError.setMessage(httpStatus.getReasonPhrase());
			return defaulError;
		}

		AmxApiError apiError = JsonUtil.fromJson(apiErrorJson, AmxApiError.class, true);
		if (!ArgUtil.isEmpty(apiError)) {
			apiError.setBody(apiErrorJson);
			AmxApiException defExcp = ExceptionFactory.get(apiError.getException());
			if (defExcp == null) {
				defExcp = ExceptionFactory.get(apiError.getErrorKey());
			}
			if (defExcp != null) {
				throw defExcp.getInstance(apiError);
			}
			return apiError;
		}
		AmxApiError defaulError = new AmxApiError(errorEnum);
		defaulError.setMessage("Error cannot be parsed to JSON see body for full response");
		defaulError.setBody(apiErrorJson);
		return defaulError;
	}

	static {
		ExceptionFactory.readExceptions();
	}
}
