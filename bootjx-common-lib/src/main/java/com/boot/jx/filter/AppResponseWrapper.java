package com.boot.jx.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;

import com.boot.jx.AppConstants;
import com.boot.jx.AppContextUtil;
import com.boot.jx.logger.LoggerService;
import com.boot.utils.ArgUtil;

public class AppResponseWrapper extends HttpServletResponseWrapper {

    Logger LOGGER = LoggerService.getLogger(getClass());

    public AppResponseWrapper(HttpServletResponse response) {
	super(response);
    }

    @Override
    public void setStatus(int sc) {
	super.setStatus(sc);
	handleStatus(sc);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setStatus(int sc, String sm) {
	super.setStatus(sc, sm);
	handleStatus(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
	super.sendError(sc, msg);
	handleStatus(sc);
    }

    @Override
    public void sendError(int sc) throws IOException {
	super.sendError(sc);
	handleStatus(sc);
    }

    @Override
    public void setHeader(String name, String value) {
	super.setHeader(name, value);
	handleStatus(this.getStatus());
    }

    @Override
    public void addHeader(String name, String value) {
	super.addHeader(name, value);
	handleStatus(this.getStatus());
    }

    private void handleStatus(int code) {
	String tranxId = AppContextUtil.getTranxId();
	String traceId = AppContextUtil.getTraceId();
	if (ArgUtil.isEmptyString(super.getHeader(AppConstants.TRANX_ID_XKEY)) && !ArgUtil.isEmptyString(tranxId)) {
	    super.addHeader(AppConstants.TRANX_ID_XKEY, tranxId);
	}
	if (ArgUtil.isEmptyString(super.getHeader(AppConstants.TRACE_ID_XKEY)) && !ArgUtil.isEmptyString(traceId)) {
	    super.addHeader(AppConstants.TRACE_ID_XKEY, traceId);
	}
    }

}
