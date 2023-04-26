package com.boot.jx.exception;

import org.springframework.http.HttpStatus;

public class AmxException extends RuntimeException {

    private static final long serialVersionUID = 7427401687837732495L;

    protected HttpStatus httpStatus;

    public AmxException(Exception e) {
	super(e);
    }

    public AmxException(String msg) {
	super(msg);
    }

    public AmxException(HttpStatus httpStatus, String msg) {
	this(msg);
	this.httpStatus = httpStatus;
    }

    public AmxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpStatus getHttpStatus() {
	return httpStatus == null ? HttpStatus.BAD_REQUEST : httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
	this.httpStatus = httpStatus;
    }

    public String getStatusKey() {
	return getHttpStatus().series().toString();
    }

}
