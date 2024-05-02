package com.boot.jx.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.boot.jx.AppConfig;
import com.boot.jx.AppContextUtil;
import com.boot.jx.logger.LoggerService;
import com.boot.utils.ArgUtil;
import com.boot.utils.StringUtils;
import com.boot.utils.URLBuilder;
import com.boot.utils.Urly;

@Service
public class ProxyService {

	@Autowired
	CommonHttpRequest httpRequest;

	@Autowired
	AppConfig appConfig;

	@Value("${app.proxy.token}")
	private String appProxyToken;

	public static Logger LOGGER = LoggerService.getLogger(ProxyService.class);

	public static void nomain(String[] args) throws ParseException, IOException, URISyntaxException {
		URLBuilder parsedDomain = Urly.parse("https://app.mehery.xyz/nexus");

		URI uri = new URI(parsedDomain.getConnectionType(), null, parsedDomain.getHost(), -1,
				StringUtils.trim(parsedDomain.getPath()), null, null);

		// replacing context path form urI to match actual gateway URI
		uri = UriComponentsBuilder.fromUri(uri).path("/a/b").build(true).toUri();

		System.out.println(uri.toURL().toString());

	}

	@Retryable(exclude = { HttpStatusCodeException.class }, include = Exception.class,
			backoff = @Backoff(delay = 5000, multiplier = 4.0), maxAttempts = 4)
	public ResponseEntity<String> processProxyRequest(String domain, String path, String body,
			Map<String, String> addheaders, HttpMethod method, HttpServletRequest request, HttpServletResponse response)
			throws URISyntaxException, MalformedURLException {
		// LOGGER.info(method.name()": " + domain + "/" + path);

		String traceId = AppContextUtil.getTraceId();
		ThreadContext.put("traceId", traceId);
		// log if required in this line

		URLBuilder parsedDomain = Urly.parse(domain);

		URI uri = new URI(parsedDomain.getConnectionType(), null, parsedDomain.getHost(), -1, parsedDomain.getPath(),
				null, null);

		// System.out.println("---domain:" + domain);
		// System.out.println("---path:" + path);
		// System.out.println("---uri:" + uri.toString());

		// replacing context path form urI to match actual gateway URI
		uri = UriComponentsBuilder.fromUri(uri).path("/" + path).query(request.getQueryString()).build(true).toUri();

		System.out.println("---" + uri.toString());

		if (ArgUtil.is(uri)) {
			return null;
		}

		HttpHeaders headers = new HttpHeaders();
		Enumeration<String> headerNames = request.getHeaderNames();

		String replacerPrefix = null;
		String replacerValue = null;
		String replacerString = httpRequest.get("x-api-replacer");
		String headerprint = httpRequest.get("x-print-log");
		if (ArgUtil.is(replacerString)) {
			String[] replacer = replacerString.toLowerCase().split(":");
			replacerPrefix = replacer[0];
			replacerValue = replacer[1];
		}

		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			headers.set(headerName, headerValue);
			if (ArgUtil.is(replacerPrefix) && headerName.toLowerCase().indexOf(replacerPrefix) == 0) {
				headers.set(headerName.replaceFirst(replacerPrefix, replacerValue), headerValue);
				if (ArgUtil.is(headerprint)) {
					LOGGER.info("Header - " + headerName + " : " + headerValue);
				}
			}
			if (ArgUtil.is(headerprint)) {
				httpRequest.addHeader("Y-found", headerName);
				httpRequest.addHeader("YY-" + headerName, headerValue);
			}
		}

		headers.set("app-proxy-token", appProxyToken);

		if (ArgUtil.is(addheaders)) {
			for (Entry<String, String> addHeader : addheaders.entrySet()) {
				headers.set(addHeader.getKey(), addHeader.getValue());
			}
		}

		headers.set("TRACE", traceId);
		headers.remove(HttpHeaders.ACCEPT_ENCODING);
		headers.remove(HttpHeaders.ORIGIN);
		headers.remove(HttpHeaders.REFERER);

		HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		RestTemplate restTemplate = new RestTemplate(factory);
		try {

			ResponseEntity<String> serverResponse = restTemplate.exchange(uri, method, httpEntity, String.class);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.put(HttpHeaders.CONTENT_TYPE, serverResponse.getHeaders().get(HttpHeaders.CONTENT_TYPE));
			// LOGGER.info(serverResponse);
			return serverResponse;
		} catch (HttpStatusCodeException e) {
			// LOGGER.error(e.getMessage());
			return ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
					.body(e.getResponseBodyAsString());
		}

	}

	@Retryable(exclude = { HttpStatusCodeException.class }, include = Exception.class,
			backoff = @Backoff(delay = 5000, multiplier = 4.0), maxAttempts = 4)
	public ResponseEntity<String> processProxyRequest(String domain, String path, String body, HttpMethod method,
			HttpServletRequest request, HttpServletResponse response) throws URISyntaxException, MalformedURLException {
		return this.processProxyRequest(domain, path, body, null, method, request, response);
	}

	@Retryable(exclude = { HttpStatusCodeException.class }, include = Exception.class,
			backoff = @Backoff(delay = 5000, multiplier = 4.0), maxAttempts = 4)
	public ResponseEntity<String> forwardRequest(String sourcePrefix, String targetUrl, String body,
			Map<String, String> addheaders, HttpServletRequest request, HttpServletResponse response)
			throws URISyntaxException, MalformedURLException {
		String path = request.getRequestURI().replaceFirst(appConfig.getAppPrefix() + sourcePrefix, "");
		return this.processProxyRequest(targetUrl, path, body, addheaders, HttpMethod.valueOf(request.getMethod()),
				request, response);
	}

	@Recover
	public ResponseEntity<String> recoverFromRestClientErrors(Exception e, String body, HttpMethod method,
			HttpServletRequest request, HttpServletResponse response, String traceId) {
		// LOGGER.error("retry method for the following url " + request.getRequestURI()
		// + " has failed" + e.getMessage());
		// LOGGER.error("ERROR", e);
		throw new RuntimeException("There was an error trying to process you request. Please try again later");
	}

}