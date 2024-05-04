package com.boot.jx.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.boot.jx.AppConfig;
import com.boot.jx.AppConstants;
import com.boot.jx.api.ApiResponse;
import com.boot.jx.filter.AppClientInterceptor;
import com.boot.jx.rest.AppRequestInterfaces.IMetaRequestInFilter;
import com.boot.jx.rest.AppRequestInterfaces.IMetaRequestOutFilter;
import com.boot.model.MapModel;
import com.boot.utils.ArgUtil;
import com.boot.utils.ClazzUtil;
import com.boot.utils.JsonUtil;

@Component
public class RestService implements AjaxRestService {

	public static final Pattern PATTERN_OUT = ClazzUtil.getGenericTypePattern(IMetaRequestOutFilter.class);

	public static final Pattern PATTERN_IN = ClazzUtil.getGenericTypePattern(IMetaRequestInFilter.class);

	private static final Logger LOGGER = LoggerFactory.getLogger(RestService.class);
	private static boolean OUT_FILTER_MAP_DONE = false;
	private static boolean IN_FILTER_MAP_DONE = false;
	private static final Map<String, IMetaRequestOutFilter<ARequestMetaInfo>> OUT_FILTERS_MAP = new HashMap<>();
	private static final Map<String, IMetaRequestInFilter<ARequestMetaInfo>> IN_FILTERS_MAP = new HashMap<>();

	public static RestTemplate staticRestTemplate;

	@Autowired(required = false)
	RestTemplate restTemplate;

	@Autowired(required = false)
	RequestMetaInfo requestMetaInfoBean;

	@Autowired(required = false)
	RequestMetaFilter requestMetaFilter;

	@SuppressWarnings("rawtypes")
	@Autowired(required = false)
	List<IMetaRequestOutFilter> outFilters;

	@SuppressWarnings("rawtypes")
	@Autowired(required = false)
	List<IMetaRequestInFilter> inFilters;

	@SuppressWarnings("rawtypes")
	@Autowired(required = false)
	IMetaRequestInFilter inFilter;

	@Autowired
	AppClientInterceptor appClientInterceptor;

	@Autowired
	AppConfig appConfig;

	public void setErrorHandler(ResponseErrorHandler errorHandler) {
		Assert.notNull(errorHandler, "ResponseErrorHandler must not be null");
		this.restTemplate.setErrorHandler(errorHandler);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, IMetaRequestOutFilter<ARequestMetaInfo>> getOutFilters() {
		if (!OUT_FILTER_MAP_DONE) {
			if (outFilters == null) {
				LOGGER.warn("NO IMetaRequestOutFilter Filters FOUND and SCANNED");
			} else {
				for (IMetaRequestOutFilter filter : outFilters) {
					Matcher matcher = PATTERN_OUT.matcher(filter.getClass().getGenericInterfaces()[0].getTypeName());
					if (matcher.find()) {
						OUT_FILTERS_MAP.put(matcher.group(1), filter);
					}
				}
			}
			OUT_FILTER_MAP_DONE = true;
			LOGGER.info("RestMataFilters Filters scanned");
		}
		return OUT_FILTERS_MAP;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, IMetaRequestInFilter<ARequestMetaInfo>> getInFilters() {
		if (!IN_FILTER_MAP_DONE) {
			if (inFilters == null) {
				LOGGER.warn("NO IMetaRequestInFilter Filters FOUND and SCANNED");
			} else {
				for (IMetaRequestInFilter filter : inFilters) {
					Matcher matcher = PATTERN_IN.matcher(filter.getClass().getGenericInterfaces()[0].getTypeName());
					if (matcher.find()) {
						IN_FILTERS_MAP.put(matcher.group(1), filter);
					}
				}
			}
			IN_FILTER_MAP_DONE = true;
			LOGGER.info("RestMataFilters Filters scanned");
		}
		return IN_FILTERS_MAP;
	}

	public RestTemplate getLocalRestTemplate(RestTemplate restTemplateLocal) {
		restTemplateLocal.setInterceptors(Collections.singletonList(appClientInterceptor));
		return restTemplateLocal;
	}

	public RestTemplate getRestTemplate() {
		if (staticRestTemplate == null) {
			if (restTemplate != null) {
				restTemplate.setInterceptors(Collections.singletonList(appClientInterceptor));
				RestService.staticRestTemplate = restTemplate;
			} else {
				throw new RuntimeException("No RestTemplate bean found");
			}
		}
		return restTemplate;
	}

	public Ajax ajax(String url) {
		this.getOutFilters();
		return new Ajax(getRestTemplate(), url).header(AppConstants.APP_VERSION_XKEY, appConfig.getAppVersion())
				.requestMeta(requestMetaFilter, requestMetaInfoBean);
	}

	public Ajax ajax(URI uri) {
		this.getOutFilters();
		return new Ajax(getRestTemplate(), uri).header(AppConstants.APP_VERSION_XKEY, appConfig.getAppVersion())
				.requestMeta(requestMetaFilter, requestMetaInfoBean);
	}

	public Ajax ajax(RestTemplate restTemplate, String url) {
		this.getOutFilters();
		return new Ajax(restTemplate, url).requestMeta(requestMetaFilter, requestMetaInfoBean);
	}

	public static class Ajax {
		public static final Pattern pattern = Pattern.compile("^.*\\{(.*)\\}.*$");

		public static enum RestMethod {
			POST, FORM, GET
		}

		private UriComponentsBuilder builder;
		private HttpEntity<?> requestEntity;
		private HttpMethod method;
		private Map<String, String> uriParams;// = new HashMap<String, String>();
		private MultiValueMap<String, Object> parameters;// = new LinkedMultiValueMap<String, Object>();
		private RestTemplate restTemplate;
		private HttpHeaders headers;// new HttpHeaders();
		private Map<String, String> headersMeta;// = new HashMap<String, String>();
		private List<Cookie> cookies;// = new ArrayList<Cookie>();;
		boolean isForm = false;

		protected Ajax(RestTemplate restTemplate) {
			this.uriParams = new HashMap<String, String>();
			this.parameters = new LinkedMultiValueMap<String, Object>();
			this.cookies = new ArrayList<Cookie>();
			this.headers = new HttpHeaders();
			this.headersMeta = new HashMap<String, String>();
			this.restTemplate = restTemplate;
		}

		public Ajax(RestTemplate restTemplate, String url) {
			this(restTemplate);
			builder = UriComponentsBuilder.fromUriString(url);
		}

		public Ajax(RestTemplate restTemplate, URI uri) {
			this(restTemplate);
			builder = UriComponentsBuilder.fromUriString(uri.toString());
		}

		public <T extends ARequestMetaInfo> Ajax requestMeta(RequestMetaFilter requestMetaFilter,
				RequestMetaInfo<?> requestMetaInfo) {
			exportRequestMetaToStatic(requestMetaFilter, requestMetaInfo, this.header());
			return this;
		}

		public <T extends ARequestMetaInfo> Ajax meta(T requestMeta) {
			exportMetaToStatic(requestMeta, this.header());
			return this;
		}

		public Ajax path(String path) {
			builder.path(path);
			return this;
		}

		public Ajax pathParam(String paramKey, Object paramValue) {
			uriParams.put(paramKey, ArgUtil.parseAsString(paramValue));
			return this;
		}

		public Ajax params(Map<String, String> params) {
			uriParams.putAll(params);
			return this;
		}

		public Ajax query(String query) {
			builder.query(query);
			return this;
		}

		public Ajax queryParam(String paramKey, Object paramValue) {
			if (paramValue != null) {
				builder.queryParam(paramKey, paramValue);
			}
			return this;
		}

		public Ajax cookie(String cooKey, String cooValue) {
			cookies.add(new Cookie(cooKey, cooValue));
			return this;
		}

		public Ajax cookie(Cookie... cookieList) {
			if (cookieList != null) {
				for (Cookie cookie : cookieList) {
					cookies.add(cookie);
				}
			}
			return this;
		}

		public Ajax field(String paramKey, Object paramValue) {
			parameters.add(paramKey, ArgUtil.parseAsString(paramValue));
			return this;
		}

		public Ajax field(String paramKey, MultipartFile file) throws IOException {
			if (ArgUtil.is(file)) {
				headers.setContentType(MediaType.MULTIPART_FORM_DATA);
				parameters.add(paramKey, RestService.getByteArrayFile(file));
			}
			return this;
		}

		public Ajax resource(String paramKey, Object paramValue) {
			parameters.add(paramKey, paramValue);
			return this;
		}

		public Ajax field(RestQuery query, Map<String, String> params) throws UnsupportedEncodingException {
			Map<String, String> s = query.toMap();
			for (Entry<String, String> entry : s.entrySet()) {
				String value = entry.getValue();
				Matcher match = pattern.matcher(value);
				if (match.find() && params.containsKey(match.group(1))) {
					value = value.replace("{" + match.group(1) + "}", params.get(match.group(1)));
				}
				this.field(entry.getKey(), value);
			}
			return this;
		}

		public Ajax header(String paramKey, Object paramValue) {
			headers.add(paramKey, ArgUtil.parseAsString(paramValue));
			return this;
		}

		public Ajax priority(String priorityKey, int priorityOrder) {
			headers.add("x-priority", priorityKey + priorityOrder);
			return this;
		}

		public Ajax meta(String metaKey, String metaValue) {
			headers.add(AppConstants.META_XKEY, String.format("%s=%s", metaKey, metaValue));
			return this;
		}

		public Ajax contentTypeJson() {
			headers.add("content-type", "application/json");
			return this;
		}

		public Ajax acceptJson() {
			headers.add("accept", "application/json");
			return this;
		}

		public Ajax authBearer(String token) {
			headers.add("Authorization", "Bearer " + token);
			return this;
		}

		public Ajax header(HttpHeaders header) {
			if (!ArgUtil.isEmpty(header)) {
				this.headers = header;
			}
			return this;
		}

		public HttpHeaders header() {
			return this.headers;
		}

		private HttpHeaders processdHeaders() {
			if (this.cookies != null) {
				StringBuilder sb = new StringBuilder();
				for (Cookie cookie : this.cookies) {
					sb.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
				}
				String cookiesString = sb.toString();
				if (ArgUtil.is(cookiesString)) {
					this.headers.add("Cookie", cookiesString);
				}
			}
			return this.headers;
		}

		public Ajax post(HttpEntity<?> requestEntity) {
			this.method = HttpMethod.POST;
			this.requestEntity = requestEntity;
			return this;
		}

		public Ajax patch(HttpEntity<?> requestEntity) {
			this.method = HttpMethod.PATCH;
			this.requestEntity = requestEntity;
			return this;
		}

		public Ajax put(HttpEntity<?> requestEntity) {
			this.method = HttpMethod.PUT;
			this.requestEntity = requestEntity;
			return this;
		}

		public <T> Ajax post(T body) {
			return this.post(new HttpEntity<T>(body, processdHeaders()));
		}

		public Ajax post() {
			return this.post(new HttpEntity<Object>(null, processdHeaders()));
		}

		public <T> Ajax put(T body) {
			return this.put(new HttpEntity<T>(body, processdHeaders()));
		}

		public Ajax put() {
			return this.put(new HttpEntity<Object>(null, processdHeaders()));
		}

		public <T> Ajax patch(T body) {
			return this.patch(new HttpEntity<T>(body, processdHeaders()));
		}

		public Ajax patch() {
			return this.patch(new HttpEntity<Object>(null, processdHeaders()));
		}

		public Ajax submit() {
			this.isForm = true;
			return this.post(new HttpEntity<MultiValueMap<String, Object>>(parameters, processdHeaders()));
		}

		public Ajax postForm() {
			return this.submit();
		}

		public <T> Ajax postJson(T body) {
			return this.header("content-type", "application/json").post(body);
		}

		public <T> Ajax putJson(T body) {
			return this.header("content-type", "application/json").put(body);
		}

		public <T> Ajax patchJson(T body) {
			return this.header("content-type", "application/json").patch(body);
		}

		public Ajax get(HttpEntity<?> requestEntity) {
			this.method = HttpMethod.GET;
			this.requestEntity = requestEntity;
			return this;
		}

		private Ajax delete(HttpEntity<Object> requestEntity) {
			this.method = HttpMethod.DELETE;
			this.requestEntity = requestEntity;
			return this;
		}

		public Ajax get() {
			return this.get(new HttpEntity<Object>(null, processdHeaders()));
		}

		public Ajax delete() {
			return this.delete(new HttpEntity<Object>(null, processdHeaders()));
		}

		public Ajax call(RestMethod method) {
			if (method == RestMethod.FORM) {
				return this.postForm();
			} else if (method == RestMethod.POST) {
				return this.post();
			}
			return this.get();
		}

		public <T> T as(Class<T> responseType) {
			URI uri = builder.buildAndExpand(uriParams).toUri();
			return restTemplate.exchange(uri, method, requestEntity, responseType).getBody();
		}

		public <T> T as(ParameterizedTypeReference<T> responseType) {
			URI uri = builder.buildAndExpand(uriParams).toUri();
			return restTemplate.exchange(uri, method, requestEntity, responseType).getBody();
		}

		public byte[] asByteArray() {
			URI uri = builder.buildAndExpand(uriParams).toUri();
			return restTemplate.getForObject(uri, byte[].class);
		}

		public String asString() {
			return this.as(String.class);
		}

		public Object asObject() {
			return this.as(Object.class);
		}

		/**
		 * Ignore resposne
		 * 
		 * @return
		 */
		public void asNone() {
			this.as(Void.class);
		}

		public Map<String, Object> asMap() {
			return this.as(new ParameterizedTypeReference<Map<String, Object>>() {
			});
		}

		public List<Object> asList() {
			return this.as(new ParameterizedTypeReference<List<Object>>() {
			});
		}

		public <T> Map<String, T> asMap(Class<T> valueType) {
			return this.as(new ParameterizedTypeReference<Map<String, T>>() {
			});
		}

		public <T> List<T> asList(Class<T> valueType) {
			return this.as(new ParameterizedTypeReference<List<T>>() {
			});
		}

		public MapModel asMapModel() {
			return MapModel.from(this.as(new ParameterizedTypeReference<Map<String, Object>>() {
			}));
		}

		public MapModel asMapModelSafe() {
			return MapModel.fromSafe(this.as(String.class));
		}

		public MapModel asListModel() {
			return MapModel.from(this.as(new ParameterizedTypeReference<List<Object>>() {
			}));
		}

		public ApiResponse<Object, Object> asApiResponse() {
			return this.asApiResponse(Object.class);
		}

		public ApiResponse<Map<String, Object>, Object> asAmxApiResponseOfMap() {
			return this.as(new ParameterizedTypeReference<ApiResponse<Map<String, Object>, Object>>() {
			});
		}

		public ApiResponse<Object, Object> asAmxApiResponseOfObject() {
			return this.as(new ParameterizedTypeReference<ApiResponse<Object, Object>>() {
			});
		}

		/**
		 * @deprecated use {@link #as(ParameterizedTypeReference)} directly, to have
		 *             smooth casting of resultType
		 * 
		 * @param resultType
		 * @return
		 */
		@Deprecated
		public <T> ApiResponse<T, Object> asApiResponse(Class<T> resultType) {
			return this.asApiResponse(resultType, Object.class);
		}

		/**
		 * @deprecated use {@link #as(ParameterizedTypeReference)} directly, to have
		 *             smooth casting of resultType
		 * 
		 * @param resultType
		 * @return
		 */
		@Deprecated
		public <T, M> ApiResponse<T, M> asApiResponse(Class<T> resultType, Class<M> metaType) {
			return this.as(new ParameterizedTypeReference<ApiResponse<T, M>>() {
			});
		}

		public Object as() {
			return this.asObject();
		}

		public Ajax build(RestMethod reqType, String reqQuery, RestQuery reqFields, Map<String, String> paramValues)
				throws UnsupportedEncodingException {
			if (!ArgUtil.isEmpty(reqQuery)) {
				this.query(reqQuery);
			}
			if (!ArgUtil.isEmpty(paramValues)) {
				this.params(paramValues);
			}

			if (!ArgUtil.isEmpty(reqFields)) {
				this.params(reqFields.toMap());
			}

			if (!ArgUtil.isEmpty(reqFields)) {
				this.field(reqFields, paramValues);
			}
			return this.call(reqType);
		}

	}

	public static void exportRequestMetaToStatic(RequestMetaFilter requestMetaFilter,
			RequestMetaInfo<?> requestMetaInfoBean, HttpHeaders header) {
		RequestMetaInfo<?> requestMetaInfo = null;

		if (ArgUtil.is(requestMetaInfoBean)) {
			requestMetaInfo = requestMetaInfoBean.copy();
		}

		if (ArgUtil.is(requestMetaFilter)) {
			requestMetaFilter.requetMetaOutFilter(requestMetaInfo);
		}
		try {
			header.add(AppConstants.REQUEST_META_XKEY, JsonUtil.toJson(requestMetaInfo));
		} catch (Exception e) {
			LOGGER.error("Meta Header Conversion Error", e);
		}
	}

	public static <T extends ARequestMetaInfo> void exportMetaToStatic(T requestMeta, HttpHeaders header) {
		String metaClass = requestMeta.getClass().getName();
		if (OUT_FILTERS_MAP.containsKey(metaClass)) {
			IMetaRequestOutFilter<ARequestMetaInfo> filter = OUT_FILTERS_MAP.get(metaClass);
			try {
				if (filter != null) {
					filter.outFilter(requestMeta);
					header.add(metaClass, JsonUtil.toJson(requestMeta));
				}
			} catch (Exception e) {
				LOGGER.error("Exception while executing Filters", e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void importMetaFromStatic(HttpServletRequest req) throws InstantiationException, IllegalAccessException {
		this.getInFilters();
		if (inFilter != null) {
			String metaValueString = req.getHeader(AppConstants.META_XKEY);
			ARequestMetaInfo x = inFilter.export(metaValueString);
			if (x != null) {
				inFilter.importMeta(x, req);
			} else {
				inFilter.importMeta((ARequestMetaInfo) inFilter.getMetaClass().newInstance(), req);
			}
		}

		RequestMetaInfo<?> requestMetaInfo = null;
		String requestMetaInfoJson = req.getHeader(AppConstants.REQUEST_META_XKEY);
		if (ArgUtil.is(requestMetaInfoJson)) {
			if (ArgUtil.is(requestMetaInfoBean)) {
				requestMetaInfo = (RequestMetaInfo<?>) JsonUtil.fromJson(requestMetaInfoJson,
						requestMetaInfoBean.classT());
				if (ArgUtil.is(requestMetaInfo)) {
					requestMetaInfoBean.copyFrom(requestMetaInfo);
				} else {
					LOGGER.debug("No origin bean specified" + requestMetaInfoJson);
				}
			} else {
				requestMetaInfo = (RequestMetaInfo<?>) JsonUtil.fromJson(requestMetaInfoJson, RequestMetaInfo.class);
			}
		}

		if (ArgUtil.is(requestMetaFilter)) {
			requestMetaFilter.requetMetaInFilter(requestMetaInfo);
		}

		Set<Entry<String, IMetaRequestInFilter<ARequestMetaInfo>>> filters = IN_FILTERS_MAP.entrySet();
		for (Entry<String, IMetaRequestInFilter<ARequestMetaInfo>> entry : filters) {
			String metaClass = entry.getKey();
			if (IN_FILTERS_MAP.containsKey(metaClass)) {
				IMetaRequestInFilter<ARequestMetaInfo> filter = IN_FILTERS_MAP.get(metaClass);
				try {
					String metaStr = req.getHeader(metaClass);
					Class<ARequestMetaInfo> clzz = ClazzUtil.fromName(metaClass);
					if (filter != null && metaStr != null && clzz != null) {
						ARequestMetaInfo meta = JsonUtil.fromJson(metaStr, clzz);
						filter.inFilter(meta);
					}
				} catch (Exception e) {
					LOGGER.error("Exception while executing Filters", e);
				}
			}
		}
	}

	public static Resource getByteArrayFile(MultipartFile file) throws IOException {
		String fileName = file.getOriginalFilename();
		String ext = "";
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		Path testFile = Files.createTempFile(file.getName(), "." + ext.toLowerCase());
		Files.write(testFile, file.getBytes());
		return new FileSystemResource(testFile.toFile());
	}

}
