package com.boot.jx.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.boot.jx.AppConfig;
import com.boot.jx.AppConstants;
import com.boot.jx.AppContextUtil;
import com.boot.jx.logger.client.AuditServiceClient;
import com.boot.jx.logger.events.RequestTrackEvent;
import com.boot.jx.rest.AppRequestContextOutFilter;
import com.boot.utils.CryptoUtil;

@Component
public class AppClientInterceptor implements ClientHttpRequestInterceptor {

	@Autowired
	AppConfig appConfig;

	@Autowired(required = false)
	AppRequestContextOutFilter appContextOutFilter;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		if (appContextOutFilter != null) {
			appContextOutFilter.appRequestContextOutFilter(request);
		}

		AppContextUtil.exportAppContextTo(request.getHeaders());
		// restMetaService.exportMetaTo(request.getHeaders());

		request.getHeaders().add(AppConstants.AUTH_TOKEN_XKEY,
				CryptoUtil.generateHMAC(appConfig.getAppAuthKey(), AppContextUtil.getTraceId()));
		// RequestTrackEvent requestTrackEvent = new RequestTrackEvent(request);
		// AuditServiceClient.trackStatic(requestTrackEvent);

		AppRequestUtil.printIfDebug(request, body);
		long startTime = System.currentTimeMillis();
		ClientHttpResponse response = execution.execute(request, body);
		AppContextUtil.importAppContextFromResponseHEader(response.getHeaders());
		RequestTrackEvent e = new RequestTrackEvent(RequestTrackEvent.Type.HTTP_OUT)
				.responseTime(System.currentTimeMillis() - startTime)
				.outbound(response, request);
		AuditServiceClient.trackStatic(e);

		return AppRequestUtil.printIfDebug(response);
	}

}
