package com.boot.jx.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StreamUtils;

import com.boot.jx.logger.LoggerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AppRequestUtil {

	private static final Logger LOGGER = LoggerService.getLogger(AppRequestUtil.class);
	private static final int MAX_BODY_SIZE = 7000;
	private static final int MAX_BODY_SIZE_PRINT = 200;

	private static boolean LOCAL_LOGGER = false;

	public static void isLocalEnable() {
		LOCAL_LOGGER = !LOCAL_LOGGER;
	}

	public static boolean isLocal() {
		return LOCAL_LOGGER || false;
	}

	public static LinkedMultiValueMap<String, String> getHeader(HttpServletRequest req) {
		LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<String, String>();
		Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			Enumeration<String> headerValues = req.getHeaders(headerName);
			while (headerValues.hasMoreElements()) {
				String headerValue = headerValues.nextElement();
				headerMap.add(headerName, headerValue);
			}
		}
		return headerMap;
	}

	public static HttpServletRequest printIfDebug(HttpServletRequest req) throws IOException {
		if (LOGGER.isDebugEnabled() || isLocal()) {
			LinkedMultiValueMap<String, String> headerMap = getHeader(req);
			log(">>>>> RQT-IN-HEDR =====: {}", headerMap.toString());

			StringBuilder sb = new StringBuilder();
			Enumeration params = req.getParameterNames();
			while (params.hasMoreElements()) {
				String paramName = (String) params.nextElement();
				sb.append(paramName + " = " + req.getParameter(paramName) + ";");
			}
			log(">>>>> RQT-IN-PRMS =====: {}", sb.toString());
			req = new AppRequestWrapper(req);
			InputStream inputStream = req.getInputStream();
			byte[] body = StreamUtils.copyToByteArray(inputStream);
			log(">>>>> RQT-IN-BODY =====: {}", new String(body));
			return req;
		}
		return req;
	}

	private static LinkedMultiValueMap<String, String> getHeader(HttpServletResponse resp) {
		LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<String, String>();
		Collection<String> headerNames = resp.getHeaderNames();
		for (String headerName : headerNames) {
			List<String> values = (List<String>) resp.getHeaders(headerName);
			headerMap.put(headerName, values);
		}
		return headerMap;
	}

	public static HttpServletResponse printIfDebug(HttpServletResponse resp) {
		if (LOGGER.isDebugEnabled() || isLocal()) {
			LinkedMultiValueMap<String, String> headerMap = getHeader(resp);
			LOGGER.debug("<<<<< RSP-OUT-HEDR =====: {}", headerMap.toString());
		}
		return resp;
	}

	public static ClientHttpResponse printIfDebug(ClientHttpResponse response) throws IOException {
		if (LOGGER.isDebugEnabled() || isLocal()) {
			final ClientHttpResponse responseWrapper = new BufferingClientHttpResponseWrapper(response);
			StringBuilder inputStringBuilder = new StringBuilder();
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(responseWrapper.getBody(), "UTF-8"));
				String line = bufferedReader.readLine();
				while (line != null) {
					inputStringBuilder.append(line);
					inputStringBuilder.append('\n');
					line = bufferedReader.readLine();
				}
			} catch (Exception e) {
				LOGGER.error("traceResponse", e);
			} finally {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			}

			// this.header = response.getHeaders();
			LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<String, String>();
			Collection<Entry<String, List<String>>> headers = response.getHeaders().entrySet();
			for (Entry<String, List<String>> header : headers) {
				headerMap.put(header.getKey(), header.getValue());
			}
			String toString = inputStringBuilder.toString();
			log("===== RSP-IN-HEDR <<<<<{}:", headerMap.toString());
			if (toString.length() < MAX_BODY_SIZE) {
				log("===== RSP-IN-BODY <<<<<: {}", toString);
			} else {
				log("===== RSP-IN-BODY <<<<<: Too Big Content-len={} body {} ...", toString.length(),
						toString.substring(0, MAX_BODY_SIZE_PRINT - 1));
			}
			return responseWrapper;
		}
		return response;
	}

	public static void printIfDebug(HttpRequest request, byte[] body) throws UnsupportedEncodingException {
		if (LOGGER.isDebugEnabled() || isLocal()) {
			LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<String, String>();
			Collection<Entry<String, List<String>>> headers = request.getHeaders().entrySet();
			for (Entry<String, List<String>> header : headers) {
				headerMap.put(header.getKey(), header.getValue());
			}
			log("===== RQT-OUT-HEDR >>>>>: {}", headerMap.toString());
			String toString = new String(body, "UTF-8");
			if (toString.length() < MAX_BODY_SIZE) {
				log("===== RQT-OUT-BODY >>>>>: {}", toString);
			} else {
				log("===== RQT-OUT-BODY >>>>>: Too Big Content-len={} body {}  ...", toString.length(),
						toString.substring(0, MAX_BODY_SIZE_PRINT - 1));
			}
		}
	}

	public static void log(String format, Object... arg) {
		if (isLocal()) {
			LOGGER.info(format, arg);
		} else {
			LOGGER.debug(format, arg);
		}
	}

}
