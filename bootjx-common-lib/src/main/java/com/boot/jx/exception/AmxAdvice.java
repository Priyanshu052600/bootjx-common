package com.boot.jx.exception;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.boot.json.MapModelDeserializer.MapModelEditor;
import com.boot.json.NamedEntityDeserializer.NamedEntityEditor;
import com.boot.json.NamedEntityDeserializer.NamedMapModel;
import com.boot.jx.AppConfig;
import com.boot.jx.AppConstants;
import com.boot.jx.AppContextUtil;
import com.boot.jx.api.AResponse;
import com.boot.jx.api.ApiFieldError;
import com.boot.jx.api.ApiResponse;
import com.boot.jx.api.ApiResponseUtil;
import com.boot.jx.exception.ApiHttpExceptions.ApiHttpArgException;
import com.boot.jx.exception.ApiHttpExceptions.ApiHttpException;
import com.boot.jx.exception.ApiHttpExceptions.ApiStatusCodes;
import com.boot.jx.http.ApiRequest.ResponeError;
import com.boot.jx.http.CommonHttpRequest.ApiRequestDetail;
import com.boot.jx.logger.AuditService;
import com.boot.jx.logger.LoggerService;
import com.boot.jx.logger.events.ApiAuditEvent;
import com.boot.model.MapModel;
import com.boot.utils.ArgUtil;
import com.boot.utils.HttpUtils;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public abstract class AmxAdvice implements ResponseBodyAdvice<ApiResponse<?, ?>> {

	private Logger logger = LoggerService.getLogger(AmxAdvice.class);

	@Autowired
	private AuditService auditService;

	@Autowired
	private AppConfig appConfig;

	@InitBinder
	public void registerCustomEditors(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(MapModel.class, new MapModelEditor());
		binder.registerCustomEditor(NamedMapModel.class, new NamedEntityEditor());
	}

	@ExceptionHandler(AmxApiException.class)
	@ResponseBody
	public ResponseEntity<AmxApiError> handle(AmxApiException ex, HttpServletRequest request,
			HttpServletResponse response) {
		AmxApiError apiError = ex.createAmxApiError();
		apiError.setException(ex.getClass().getName());
		apiError.setStatusEnum(ex.getError());
		apiError.setMeta(ex.getMeta());
		apiError.setMessage(ArgUtil.nonEmpty(apiError.getMessage(), ex.getMessage(), ex.getErrorMessage()));
		apiError.setPath(request.getRequestURI());
		apiError.setRedirectUrl(ex.getRedirectUrl());
		ExceptionMessageKey.resolveLocalMessage(apiError);
		ApiAuditEvent apiAuditEvent = new ApiAuditEvent(ex);
		alert(ex, apiAuditEvent);

		response.setHeader(AppConstants.EXCEPTION_HEADER_KEY, apiError.getException());
		response.setHeader(AppConstants.EXCEPTION_HEADER_CODE_KEY, apiAuditEvent.getErrorCode());

		if (!ArgUtil.is(apiError.getPagination()) && ArgUtil.is(ApiResponseUtil.pagination())) {
			apiError.setPagination(ApiResponseUtil.pagination());
		}

		for (ApiFieldError warning : ApiResponseUtil.getWarnings()) {
			apiError.addWarning(warning);
		}

		apiError.setErrors(ApiResponseUtil.getErrors());

		for (String log : ApiResponseUtil.getLogs()) {
			apiError.addLog(log);
		}

		for (Object log : ApiResponseUtil.getTraces()) {
			apiError.addTrace(log);
		}

		apiError.setServerVersion(appConfig.getAppAppBuildStamp());
		return new ResponseEntity<AmxApiError>(apiError, getHttpStatus(ex));
	}

	public HttpStatus getHttpStatus(AmxApiException exp) {
		ApiRequestDetail apiRequestDetail = AppContextUtil.getApiRequestDetail();

		if (apiRequestDetail.getResponeError() == ResponeError.PROPAGATE) {
			return exp.getHttpStatus();
		}

		if (appConfig.isAppResponseOK()) {
			return HttpStatus.OK;
		}
		return exp.getHttpStatus();
	}

	private void alert(AmxApiException ex, ApiAuditEvent apiAuditEvent) {
		// Raise Alert for Specific Event
		auditService.log(apiAuditEvent, ex);
	}

	public void alert(Exception ex) {
		logger.error("Exception occured in controller {} error message: {}", ex.getClass().getName(), ex.getMessage(),
				ex);
	}

	protected ResponseEntity<AmxApiError> badRequest(Exception ex, List<ApiFieldError> errors,
			HttpServletRequest request, HttpServletResponse response, IExceptionEnum iExceptionEnum) {
		AmxApiError apiError = new AmxApiError();
		apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
		// apiError.setMessage(ex.getMessage());
		apiError.setStatusKey(iExceptionEnum.toString());
		apiError.setErrors(errors);
		apiError.setException(ApiHttpArgException.class.getName());
		ExceptionMessageKey.resolveLocalMessage(apiError);
		response.setHeader(AppConstants.EXCEPTION_HEADER_KEY, apiError.getException());

		if (!ArgUtil.is(apiError.getPagination()) && ArgUtil.is(ApiResponseUtil.pagination())) {
			apiError.setPagination(ApiResponseUtil.pagination());
		}

		for (ApiFieldError warning : ApiResponseUtil.getWarnings()) {
			apiError.addWarning(warning);
		}

		for (String log : ApiResponseUtil.getLogs()) {
			apiError.addLog(log);
		}

		for (Object log : ApiResponseUtil.getTraces()) {
			apiError.addTrace(log);
		}

		apiError.setServerVersion(appConfig.getAppAppBuildStamp());
		return new ResponseEntity<AmxApiError>(apiError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<AmxApiError> handle(MethodArgumentNotValidException ex, HttpServletRequest request,
			HttpServletResponse response) {

		List<ApiFieldError> errors = new ArrayList<ApiFieldError>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			ApiFieldError newError = new ApiFieldError();
			newError.setObzect(error.getObjectName());
			newError.setField(error.getField());
			newError.setDescription(HttpUtils.sanitze(error.getDefaultMessage()));
			newError.setCode(error.getCode());
			errors.add(newError);
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			ApiFieldError newError = new ApiFieldError();
			newError.setObzect(error.getObjectName());
			newError.setDescription(HttpUtils.sanitze(error.getDefaultMessage()));
			errors.add(newError);
		}
		return badRequest(ex, errors, request, response, ApiStatusCodes.PARAM_INVALID);
	}

	@ExceptionHandler(ApiHttpArgException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<AmxApiError> handle(ApiHttpArgException ex, HttpServletRequest request,
			HttpServletResponse response) {
		List<ApiFieldError> errors = ApiResponseUtil.getErrors();
		ResponseEntity<AmxApiError> respEntity = badRequest(ex, errors, request, response, ex.getError());

		if (ArgUtil.is(ex.getMessage()))
			respEntity.getBody().setMessage(ex.getMessage());
		return respEntity;
	}

	/**
	 * Handle.
	 *
	 * @param ex       the ex
	 * @param request  the request
	 * @param response the response
	 * @return the response entity
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<AmxApiError> handle(HttpMessageNotReadableException ex, HttpServletRequest request,
			HttpServletResponse response) {
		List<ApiFieldError> errors = new ArrayList<ApiFieldError>();

		// newError.setField(ex.getName());
		Throwable x = ex.getRootCause();
		if (x instanceof InvalidFormatException) {
			InvalidFormatException x1 = (InvalidFormatException) x;
			ApiFieldError newError = new ApiFieldError();

			StringBuilder sb = new StringBuilder();
			Iterator<Reference> stit = x1.getPath().iterator();
			while (stit.hasNext()) {
				Reference ref = stit.next();
				sb.append(ref.getFieldName());
				if (stit.hasNext()) {
					sb.append(".");
				}
			}

			newError.setField(sb.toString());
			newError.setObzect(x1.getPathReference());
			newError.setDescription(HttpUtils.sanitze(x1.getOriginalMessage()));
			errors.add(newError);
		} else {
			ApiFieldError newError = new ApiFieldError();
			newError.setDescription(HttpUtils.sanitze(ex.getMessage()));
			errors.add(newError);
		}

		return badRequest(ex, errors, request, response, ApiStatusCodes.PARAM_INVALID);
	}

	/**
	 * Handle.
	 *
	 * @param ex       the ex
	 * @param request  the request
	 * @param response the response
	 * @return the response entity
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<AmxApiError> handle(MethodArgumentTypeMismatchException ex, HttpServletRequest request,
			HttpServletResponse response) {
		List<ApiFieldError> errors = new ArrayList<ApiFieldError>();
		ApiFieldError newError = new ApiFieldError();
		newError.setField(ex.getName());
		newError.setDescription(HttpUtils.sanitze(ex.getMessage()));
		errors.add(newError);
		return badRequest(ex, errors, request, response, ApiStatusCodes.PARAM_TYPE_MISMATCH);
	}

	/**
	 * Handle.
	 *
	 * @param exception the exception
	 * @return the response entity
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<AmxApiError> handle(ConstraintViolationException exception, HttpServletRequest request,
			HttpServletResponse response) {
		List<ApiFieldError> errors = new ArrayList<ApiFieldError>();
		for (ConstraintViolation<?> responseError : exception.getConstraintViolations()) {
			ApiFieldError newError = new ApiFieldError();
			newError.setField(responseError.getPropertyPath().toString());
			newError.setDescription(HttpUtils.sanitze(responseError.getMessage()));
			errors.add(newError);
		}
		return badRequest(exception, errors, request, response, ApiStatusCodes.PARAM_ILLEGAL);
	}

	@ExceptionHandler(ApiHttpException.class)
	@ResponseBody
	protected ResponseEntity<AmxApiError> handle(ApiHttpException ex, HttpServletRequest request,
			HttpServletResponse response) {
		List<ApiFieldError> errors = ApiResponseUtil.getErrors();

		AmxApiError apiError = new AmxApiError();
		apiError.setHttpStatus(ex.getHttpStatus());
		apiError.setMessage(ex.getMessage());
		apiError.setStatusKey(ex.getStatusKey());
		apiError.setErrors(errors);
		apiError.setException(ApiHttpArgException.class.getName());
		ExceptionMessageKey.resolveLocalMessage(apiError);
		response.setHeader(AppConstants.EXCEPTION_HEADER_KEY, apiError.getException());

		if (!ArgUtil.is(apiError.getPagination()) && ArgUtil.is(ApiResponseUtil.pagination())) {
			apiError.setPagination(ApiResponseUtil.pagination());
		}

		for (ApiFieldError warning : ApiResponseUtil.getWarnings()) {
			apiError.addWarning(warning);
		}
		for (String log : ApiResponseUtil.getLogs()) {
			apiError.addLog(log);
		}
		for (Object log : ApiResponseUtil.getTraces()) {
			apiError.addTrace(log);
		}

		apiError.setServerVersion(appConfig.getAppAppBuildStamp());
		return new ResponseEntity<AmxApiError>(apiError, ex.getHttpStatus());
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return AResponse.class.isAssignableFrom(returnType.getParameterType());
	}

	@Override
	public ApiResponse<?, ?> beforeBodyWrite(ApiResponse<?, ?> body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {

		if (!ArgUtil.is(body.getPagination()) && ArgUtil.is(ApiResponseUtil.pagination())) {
			body.setPagination(ApiResponseUtil.pagination());
		}

		for (ApiFieldError warning : ApiResponseUtil.getWarnings()) {
			body.addWarning(warning);
		}
		for (String log : ApiResponseUtil.getLogs()) {
			body.addLog(log);
		}
		for (Object log : ApiResponseUtil.getTraces()) {
			body.addTrace(log);
		}
		body.setServerVersion(appConfig.getAppAppBuildStamp());
		return body;
	}

	public ResponseEntity<?> handle(org.springframework.web.multipart.MultipartException exception) {
		logger.error("handle->MultipartException" + exception.getMessage(), exception);
		// general exception
		if (exception.getCause() instanceof IOException
				&& exception.getCause().getMessage().startsWith("The temporary upload location")) {
			String pathToRecreate = exception.getMessage().substring(exception.getMessage().indexOf("[") + 1,
					exception.getMessage().indexOf("]"));
			Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
			// add permission as rw-r--r-- 644
			perms.add(PosixFilePermission.OWNER_WRITE);
			perms.add(PosixFilePermission.OWNER_READ);
			perms.add(PosixFilePermission.OWNER_EXECUTE);
			perms.add(PosixFilePermission.GROUP_READ);
			perms.add(PosixFilePermission.GROUP_WRITE);
			perms.add(PosixFilePermission.GROUP_EXECUTE);
			FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(perms);
			try {
				Files.createDirectories(FileSystems.getDefault().getPath(pathToRecreate), fileAttributes);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				return new ResponseEntity<String>(
						"Unable to recreate deleted temp directories. Please check  " + pathToRecreate,
						HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<String>(
					"Recovered from temporary error by recreating temporary directory. Please try to upload logo again.",
					HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("Unable to process this request.", HttpStatus.BAD_REQUEST);
	}

}
