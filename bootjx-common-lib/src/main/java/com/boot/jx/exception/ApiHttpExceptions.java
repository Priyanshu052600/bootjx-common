package com.boot.jx.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.client.ClientHttpResponse;

import com.boot.utils.ArgUtil;

public class ApiHttpExceptions {

	public static enum ApiStatusCodes implements IExceptionEnum {

		SUCCESS(200), NO_CONTENT(204), NO_STATUS(200),

		HTTP_CLIENT_ERROR, HTTP_SERVER_ERROR(500), API_ERROR(500), FAIL(500), SERVICE_UNAVAILABLE(503),

		PARAM_INVALID(400), PARAM_MISSING(400), PARAM_ILLEGAL(400), PARAM_TYPE_MISMATCH(400), UNKNOWN_CLIENT_ERROR(400),
		ENTITY_CONVERSION_EXCEPTION(400), PARAM_DUPLICATE(400),

		UNAUTHORIZED(401), ACCESS_DENIED(403), HTTP_NOT_FOUND(404),

		OTP_REQUIRED(461), MOTP_REQUIRED(461), EOTP_REQUIRED(463), DOTP_REQUIRED(464), USER_NOT_FOUND(465),
		HANDSHAKE_REQUIRED(466),

		UNKNOWN(520);

		int statusCode;

		ApiStatusCodes(int statusCode) {
			this.statusCode = statusCode;
		}

		ApiStatusCodes() {
			this(400);
		}

		@Override
		public String getStatusKey() {
			return this.toString();
		}

		@Override
		public int getStatusCode() {
			return this.statusCode;
		}

		public static HttpStatus getHttpStatus(ClientHttpResponse response) throws IOException {
			int code = response.getRawStatusCode();
			for (HttpStatus status : HttpStatus.values()) {
				if (status.value() == code) {
					return status;
				}
			}
			code = (code / 100) * 100;
			return HttpStatus.valueOf(code);
		}

	}

	public static class ApiHttpStatus {
		int statusCode;
		HttpStatus status;
		String statusText;

		public HttpStatus getStatus() {
			return status;
		}

		public void setStatus(HttpStatus status) {
			this.status = status;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}

		public Series series() {
			return status == null ? null : status.series();
		}

		public ApiHttpStatus update(ClientHttpResponse response) throws IOException {
			statusCode = response.getRawStatusCode();
			for (HttpStatus ostatus : HttpStatus.values()) {
				if (ostatus.value() == statusCode) {
					this.status = ostatus;
					break;
				}
			}
			if (this.status == null) {
				this.status = HttpStatus.resolve((statusCode / 100) * 100);
			}

			this.statusText = response.getStatusText();
			return this;
		}

		public boolean is(HttpStatus status) {
			return this.status == status;
		}

		public static ApiHttpStatus from(ClientHttpResponse response) throws IOException {
			return new ApiHttpStatus().update(response);
		}

		public String getStatusText() {
			return statusText;
		}

		public void setStatusText(String statusText) {
			this.statusText = statusText;
		}
	}

	public static class ApiHttpException extends AmxException {

		private static final long serialVersionUID = -1220231169516141164L;

		protected AmxApiError response;

		public ApiHttpException(HttpStatus statusCode, String message) {
			super(statusCode, message);
		}

		public ApiHttpException(HttpStatus statusCode, AmxApiError apiError) {
			super(statusCode, apiError.getMessage());
			this.response = apiError;
		}

		public AmxApiError getResponse() {
			return response;
		}

		public void setResponse(AmxApiError response) {
			this.response = response;
		}

	}

	public static class ApiHttpClientException extends ApiHttpException {

		private static final long serialVersionUID = 7427401687837732495L;

		public ApiHttpClientException(HttpStatus statusCode, AmxApiError apiError) {
			super(statusCode, apiError);
		}

	}

	public static class ApiHttpNotFoundException extends ApiHttpException {
		private static final long serialVersionUID = -2333301958163665424L;

		public ApiHttpNotFoundException(HttpStatus statusCode) {
			super(statusCode, String.format("[%s]", statusCode.value(), statusCode.getReasonPhrase()));
		}

		public ApiHttpNotFoundException(HttpStatus statusCode, AmxApiError apiError) {
			super(statusCode, apiError);
		}
	}

	public static class ApiHttpServerException extends ApiHttpException {
		private static final long serialVersionUID = 3282368584279205762L;

		public ApiHttpServerException(HttpStatus statusCode, AmxApiError apiError) {
			super(statusCode, apiError);

		}
	}

	public static class ApiErrorException extends AmxApiException {

		private static final long serialVersionUID = -7306148895312312163L;

		public ApiErrorException(AmxApiError apiError) {
			super(apiError);
		}

		public ApiErrorException(Exception e) {
			super(e);
		}

		public ApiErrorException(String errorMessage) {
			super(errorMessage);
		}

		public ApiErrorException(IExceptionEnum errorCode, String message) {
			super(errorCode, message);
		}

		public ApiErrorException(IExceptionEnum errorCode, Exception ex) {
			super(errorCode, ex);
		}

		public ApiErrorException() {
			super();
		}

		@Override
		public IExceptionEnum getErrorIdEnum(String errorId) {
			return ArgUtil.parseAsEnumT(errorId, ApiStatusCodes.API_ERROR, ApiStatusCodes.class);
		}

		@Override
		public boolean isReportable() {
			return false;
		}

	}

	public static class ApiHttpArgException extends AmxApiException {

		private static final long serialVersionUID = 1L;

		public ApiHttpArgException(AmxApiError apiError) {
			super(apiError);
		}

		public ApiHttpArgException() {
			super("Http Argument Exception");
			this.setError(ApiStatusCodes.PARAM_INVALID);
		}

		public ApiHttpArgException(ApiStatusCodes statusCode) {
			super(statusCode);
		}

		public ApiHttpArgException(ApiStatusCodes statusCode, String message) {
			super(statusCode, message);
		}

		public ApiHttpArgException(Exception e) {
			super(e);
			this.setError(ApiStatusCodes.PARAM_INVALID);
		}

		@Override
		public AmxApiException getInstance(AmxApiError apiError) {
			return new ApiHttpArgException(apiError);
		}

		@Override
		public IExceptionEnum getErrorIdEnum(String errorId) {
			return ApiStatusCodes.PARAM_INVALID;
		}

		public static <T> T evaluate(Exception e) {
			if (e instanceof ApiHttpArgException) {
				throw (ApiHttpArgException) e;
			} else {
				throw new ApiHttpArgException(e);
			}
		}

		@Override
		public boolean isReportable() {
			return false;
		}

	}
}
