package com.boot.jx.api;

import java.util.ArrayList;
import java.util.List;

import com.boot.jx.AppConstants;
import com.boot.jx.exception.AmxApiError;
import com.boot.jx.exception.AmxApiException;
import com.boot.jx.exception.ApiHttpExceptions.ApiErrorException;
import com.boot.jx.exception.ApiHttpExceptions.ApiHttpArgException;
import com.boot.jx.exception.ApiHttpExceptions.ApiHttpException;
import com.boot.jx.exception.ApiHttpExceptions.ApiStatusCodes;
import com.boot.jx.exception.IExceptionEnum;
import com.boot.utils.ArgUtil;
import com.boot.utils.ContextUtil;

public class ApiResponseUtil {

	@SuppressWarnings("unchecked")
	public static List<String> getLogs() {
		Object logsObject = ContextUtil.map().get(AppConstants.REQUEST_LOGS_XKEY);
		List<String> logs = null;
		if (logsObject == null) {
			logs = new ArrayList<String>();
			// logs.add(String.format(format, args));
			ContextUtil.map().put(AppConstants.REQUEST_LOGS_XKEY, logs);
		} else {
			logs = (List<String>) logsObject;
		}
		return logs;
	}

	public static void addLog(String format, Object... args) {
		if (ArgUtil.is(format)) {
			getLogs().add(String.format(format, args));
		}
	}

	public static void addLog(String message) {
		if (ArgUtil.is(message)) {
			getLogs().add(message);
		}
	}

	@SuppressWarnings("unchecked")
	public static List<ApiFieldError> getErrors() {
		Object warningsObject = ContextUtil.map().get(AppConstants.REQUEST_ERROR_XKEY);
		List<ApiFieldError> warnings = null;
		if (warningsObject == null) {
			warnings = new ArrayList<ApiFieldError>();
			ContextUtil.map().put(AppConstants.REQUEST_ERROR_XKEY, warnings);
		} else {
			warnings = (List<ApiFieldError>) warningsObject;
		}
		return warnings;
	}

	@SuppressWarnings("unchecked")
	public static List<ApiFieldError> getWarnings() {
		Object userDeviceClientObject = ContextUtil.map().get(AppConstants.REQUEST_WARNING_XKEY);
		List<ApiFieldError> warnings = null;
		if (userDeviceClientObject == null) {
			warnings = new ArrayList<ApiFieldError>();
			ContextUtil.map().put(AppConstants.REQUEST_WARNING_XKEY, warnings);
		} else {
			warnings = (List<ApiFieldError>) userDeviceClientObject;
		}
		return warnings;
	}

	public static void addWarning(ApiFieldError warning) {
		List<ApiFieldError> amxFieldWarnings = getWarnings();
		for (ApiFieldError amxFieldWarning : amxFieldWarnings) {
			// Find duplicate Warnings
			if (amxFieldWarning.toString().equals(warning.toString())) {
				return;
			}
		}
		amxFieldWarnings.add(warning);
	}

	public static void addWarning(List<ApiFieldError> warnings) {
		if (!ArgUtil.isEmpty(warnings)) {
			for (ApiFieldError warning : warnings) {
				addWarning(warning);
			}
		}
	}

	public static void addWarning(String warning) {
		ApiFieldError w = new ApiFieldError();
		w.setDescription(warning);
		addWarning(w);
	}

	public static void addWarning(AmxApiException warning) {
		ApiFieldError w = new ApiFieldError();
		if (ArgUtil.is(warning.getError())) {
			w.setCode(ArgUtil.parseAsString(warning.getError().getStatusCode()));
			w.setCodeKey(ArgUtil.parseAsString(warning.getError().getStatusKey()));
		}
		w.setDescription(warning.getErrorMessage());
		w.setDescriptionKey(warning.getErrorKey());
		addWarning(w);
	}

	// Errors
	public static void addError(ApiFieldError warning) {
		List<ApiFieldError> amxFieldErrors = getErrors();
		for (ApiFieldError amxFieldWarning : amxFieldErrors) {
			// Find duplicate Errors
			if (amxFieldWarning.toString().equals(warning.toString())) {
				return;
			}
		}
		amxFieldErrors.add(warning);
	}

	public static void addError(List<ApiFieldError> errors) {
		if (!ArgUtil.isEmpty(errors)) {
			for (ApiFieldError warning : errors) {
				addError(warning);
			}
		}
	}

	public static void addError(String error) {
		ApiFieldError w = new ApiFieldError();
		w.setDescription(error);
		addError(w);
	}

	public static void addError(AmxApiException error) {
		ApiFieldError w = new ApiFieldError();
		if (ArgUtil.is(error.getError())) {
			w.setCode(ArgUtil.parseAsString(error.getError().getStatusCode()));
			w.setCodeKey(ArgUtil.parseAsString(error.getError().getStatusKey()));
		}
		w.setDescription(error.getErrorMessage());
		w.setDescriptionKey(error.getErrorKey());
		addError(w);
	}

	public static void addError(ApiHttpException error) {
		ApiFieldError w = new ApiFieldError();
		if (ArgUtil.is(error.getResponse())) {
			w.setBody(error.getResponse().getBody());
			w.setCode(ArgUtil.parseAsString(error.getHttpStatus().value()));
			w.setCodeKey(error.getHttpStatus().name());
		}
		w.setDescription(error.getMessage());
		addError(w);
	}

	// Exception
	public static void throwException() {
		List<ApiFieldError> errors = getErrors();
		if (errors.size() > 0) {
			throwException(errors.get(0));
		}
	}

	public static void throwException(Exception exception) {
		AmxApiException.evaluate(exception);
	}

	public static void throwException(AmxApiError error) {
		throw new ApiErrorException(error);
	}

	public static void throwException(ApiFieldError fieldError) {
		AmxApiError error = new AmxApiError();
		error.setStatusKey(fieldError.getCodeKey());
		error.setErrorKey(fieldError.getDescriptionKey());
		error.setMessage(fieldError.getDescription());
		throwException(new ApiErrorException(error));
	}

	public static void throwException(String errorMessage) {
		throwException(new ApiErrorException(errorMessage));
	}

	public static void throwException(IExceptionEnum errorCode, String errorMessage) {
		throwException(new ApiErrorException(errorCode, errorMessage));
	}

	@SuppressWarnings("unchecked")
	public static List<String> getExceptionLogs() {
		Object exceptionLogObject = ContextUtil.map().get(AppConstants.EXCEPTION_LOGS_XKEY);
		List<String> exclogs = null;
		if (exceptionLogObject == null) {
			exclogs = new ArrayList<String>();
			ContextUtil.map().put(AppConstants.EXCEPTION_LOGS_XKEY, exclogs);
		} else {
			exclogs = (List<String>) exceptionLogObject;
		}
		return exclogs;
	}

	public static void addExceptionLog(String log) {
		getExceptionLogs().add(log);
	}

	// Additional Exceptions - InputExceptions
	public static void throwInputException(ApiStatusCodes code, String description) {
		throwException(new ApiHttpArgException(code, description));
	}

	public static void throwInputException(ApiStatusCodes code) {
		throwException(new ApiHttpArgException(code));
	}

	public static void throwInputException(ApiStatusCodes code, ApiFieldError error) {
		addError(error);
		throwInputException(code);
	}

	public static void throwInputException(ApiFieldError error) {
		addError(error);
		throwInputException(ApiStatusCodes.PARAM_INVALID);
	}

	public static void throwInputException(ApiStatusCodes code, String description, ApiFieldError error) {
		addError(error);
		throwInputException(code, description);
	}

	// Additional Exceptions - DuplicateInputException
	public static void throwDuplicateInputException(ApiFieldError error) {
		throwInputException(ApiStatusCodes.PARAM_DUPLICATE, error);
	}

	public static void throwDuplicateInputException(String description, ApiFieldError error) {
		throwInputException(ApiStatusCodes.PARAM_DUPLICATE, description, error);
	}

	public static void throwDuplicateInputException() {
		throwInputException(ApiStatusCodes.PARAM_DUPLICATE);
	}

	public static void throwMissinInputException() {
		throwInputException(ApiStatusCodes.PARAM_MISSING);
	}

	public static void throwMissinInputException(ApiFieldError error) {
		throwInputException(ApiStatusCodes.PARAM_MISSING, error);
	}

	public static void throwUnAuthorizedException(String description) {
		throwException(new ApiHttpArgException(ApiStatusCodes.UNAUTHORIZED, description));
	}

	public static void throwAccessDeniedException(String description) {
		throwException(new ApiHttpArgException(ApiStatusCodes.ACCESS_DENIED, description));
	}

}
