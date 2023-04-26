package com.boot.jx.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.boot.jx.AppConfig;
import com.boot.jx.AppConstants;
import com.boot.jx.AppContextUtil;
import com.boot.jx.api.ApiResponseUtil;
import com.boot.jx.dict.Language;
import com.boot.jx.dict.UserClient.UserDeviceClient;
import com.boot.jx.exception.AmxApiError;
import com.boot.jx.exception.ApiHttpExceptions.ApiStatusCodes;
import com.boot.jx.exception.ExceptionMessageKey;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.http.CommonHttpRequest.ApiRequestDetail;
import com.boot.jx.http.RequestType;
import com.boot.jx.logger.client.AuditServiceClient;
import com.boot.jx.logger.events.RequestTrackEvent;
import com.boot.jx.logger.events.RequestTrackEvent.Type;
import com.boot.jx.rest.AppRequestContextInFilter;
import com.boot.jx.rest.AppRequestInterfaces.AppAuthFilter;
import com.boot.jx.scope.tnt.TenantAuthContext;
import com.boot.jx.scope.tnt.TenantAuthContext.TenantAuthFilter;
import com.boot.jx.scope.tnt.TenantContextHolder;
import com.boot.jx.scope.tnt.Tenants.TenantResolver;
import com.boot.jx.scope.vendor.VendorAuthContext;
import com.boot.jx.scope.vendor.VendorAuthContext.VendorAuthFilter;
import com.boot.jx.scope.vendor.VendorAuthService;
import com.boot.jx.scope.vendor.VendorContext;
import com.boot.jx.session.SessionContextService;
import com.boot.model.MapModel;
import com.boot.utils.ArgUtil;
import com.boot.utils.Constants;
import com.boot.utils.CryptoUtil;
import com.boot.utils.HttpUtils;
import com.boot.utils.JsonUtil;
import com.boot.utils.StringUtils;
import com.boot.utils.UniqueID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppRequestFilter implements Filter {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.info("Filter Intialzed");
	}

	/**
	 * DO NOT REMOVE THIS ONE AS IT WILL DO VERY IMPORTANT STUFF LIKE TENANT BEAN
	 * INIT
	 */
	@Autowired
	AppConfig appConfig;

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Autowired(required = false)
	AppRequestContextInFilter appContextInFilter;

	@Autowired(required = false)
	SessionContextService sessionContextService;

	@Autowired(required = false)
	VendorAuthService appVendorConfig;

	@Autowired(required = false)
	VendorAuthContext vendorAuthContext;

	@Autowired(required = false)
	TenantAuthContext tenantAuthContext;

	// @Autowired(required = false)
	// VendorAuthFilter vendorAuthFilter;

	@Autowired(required = false)
	List<AppAuthFilter> appAuthFilters;

	@Autowired(required = false)
	TenantResolver tenantResolver;

	private boolean doesTokenMatch(CommonHttpRequest localCommonHttpRequest, HttpServletRequest req,
			HttpServletResponse resp, String traceId, boolean checkHMAC) {
		String authToken = localCommonHttpRequest.get(AppConstants.AUTH_TOKEN_XKEY);
		if (checkHMAC) {
			if (StringUtils.isEmpty(authToken)
					|| (CryptoUtil.validateHMAC(appConfig.getAppAuthKey(), traceId, authToken) == false)) {
				return false;
			}
			return true;
		} else {
			if (StringUtils.isEmpty(authToken) || !authToken.equalsIgnoreCase(appConfig.getAppAuthToken())) {
				return false;
			}
			return true;
		}
	}

	/**
	 * {@link AppRequestInterceptor}
	 * 
	 * @param localCommonHttpRequest
	 * @param apiRequest
	 * @param req
	 * @param resp
	 * @param traceId
	 * @return
	 */
	private boolean isRequestValid(CommonHttpRequest localCommonHttpRequest, ApiRequestDetail apiRequest,
			HttpServletRequest req, HttpServletResponse resp, String traceId) {
		String authVendor = localCommonHttpRequest.get(AppConstants.AUTH_ID_XKEY);

		if (ArgUtil.is(authVendor)) {
			VendorContext.setVendor(authVendor);
			String authToken = localCommonHttpRequest.get(AppConstants.AUTH_TOKEN_XKEY);
			if (ArgUtil.is(authToken)) {
				VendorAuthFilter vendorAuthFilter = vendorAuthContext.get();
				return vendorAuthFilter.filterVendorRequest(apiRequest, localCommonHttpRequest, traceId, authToken);
			}
			return false;
		}

		if (apiRequest.isAuthenticateTenant()) {
			TenantAuthFilter tenantAuthFilter = tenantAuthContext.get();
			boolean conitnue = tenantAuthFilter.filterTenantRequest(apiRequest, localCommonHttpRequest, traceId);
			if (!conitnue) {
				return conitnue;
			}
		}

		/**
		 * This code has been moved to {@link AppRequestInterceptor}
		 */
//		if (ArgUtil.is(apiRequest.getRules())) {
//			if (appAuthFilters != null) {
//				for (AppAuthFilter appAuthFilter : appAuthFilters) {
//					if (!appAuthFilter.filterAppRequest(apiRequest, localCommonHttpRequest, traceId)) {
//						return false;
//					}
//				}
//			}
//		}

		if (apiRequest.isUseAuthKey() && appConfig.isAppAuthEnabled()
				&& !doesTokenMatch(localCommonHttpRequest, req, resp, traceId, true)) {
			return false;
		} else if (!appConfig.isAppAuthEnabled() && apiRequest.isUseAuthToken()
				&& !doesTokenMatch(localCommonHttpRequest, req, resp, traceId, false)) {
			return false;
		} else {
			return true;
		}
	}

	public void setFlow(HttpServletRequest req, ApiRequestDetail apiRequest) {
		String url = ArgUtil.nonEmpty(apiRequest.getFlow(), req.getRequestURI());
		AppContextUtil.setFlow(url);
//		AppContextUtil.setFlowfix(url.toLowerCase().replace("pub", "b").replace("api", "p").replace("user", "")
//				.replace("get", "").replace("post", "").replace("save", "").replaceAll("[AaEeIiOoUuYyWwHh]", ""));
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		long startTime = System.currentTimeMillis();
		HttpServletRequest req = ((HttpServletRequest) request);
		HttpServletResponse resp = ((HttpServletResponse) response);
		try {
			ApiRequestDetail apiRequest = commonHttpRequest.getApiRequest(req);
			CommonHttpRequest localCommonHttpRequest = commonHttpRequest.instance(req, resp, appConfig);
			RequestType reqType = apiRequest.getType();

			AppContextUtil.setRequestType(reqType);
			AppContextUtil.setApiRequestDetail(apiRequest);

			// Tenant Tracking
			String siteId = req.getHeader(TenantContextHolder.TENANT);
			if (StringUtils.isEmpty(siteId)) {
				siteId = ArgUtil.parseAsString(localCommonHttpRequest.getRequestParam(TenantContextHolder.TENANT));
				if (siteId == null) {
					siteId = HttpUtils.getSubDomain(req);
				}
			}

			if (ArgUtil.is(tenantResolver)) {
				siteId = tenantResolver.resolve(siteId);
			}

			if (!StringUtils.isEmpty(siteId)) {
				TenantContextHolder.setCurrent(siteId, null);
			}

			String tnt = TenantContextHolder.currentSite();

			AppContextUtil.importAppContextFromRequest(req);

			// ***** SESSION ID Tracking ********
			String sessionId = ArgUtil.parseAsString(req.getParameter(AppConstants.SESSION_ID_XKEY));
			if (StringUtils.isEmpty(sessionId)) {
				sessionId = req.getHeader(AppConstants.SESSION_ID_XKEY);
			}
			if (!StringUtils.isEmpty(sessionId)) {
				AppContextUtil.setSessionId(sessionId);
			}

			// Tranx Id Tracking
			String tranxId = ArgUtil.parseAsString(req.getParameter(AppConstants.TRANX_ID_XKEY));
			if (StringUtils.isEmpty(tranxId)) {
				tranxId = req.getHeader(AppConstants.TRANX_ID_XKEY);
			}

			if (!StringUtils.isEmpty(tranxId)) {
				AppContextUtil.setTranxId(tranxId);
			}

			// User Id Tracking
			String actorId = ArgUtil.parseAsString(req.getParameter(AppConstants.ACTOR_ID_XKEY));
			if (StringUtils.isEmpty(actorId)) {
				actorId = req.getHeader(AppConstants.ACTOR_ID_XKEY);
			}

			if (!StringUtils.isEmpty(actorId)) {
				AppContextUtil.setActorId(actorId);
			}

			// User Language Tracking
			Language lang = localCommonHttpRequest.getLanguage();

			if (ArgUtil.is(lang)) {
				AppContextUtil.setLang(lang);
			}

			String fp = Constants.BLANK;
			// UserClient Tracking
			String userClientJson = req.getHeader(AppConstants.USER_CLIENT_XKEY);
			if (!StringUtils.isEmpty(userClientJson)) {
				UserDeviceClient x = JsonUtil.fromJson(userClientJson, UserDeviceClient.class);
				AppContextUtil.setUserClient(x);
				fp = x.getFingerprint();
			} else {
				UserDeviceClient userDevice = localCommonHttpRequest.getUserDevice().toUserDeviceClient();
				UserDeviceClient userClient = AppContextUtil.getUserClient();
				userClient.importFrom(userDevice);
				fp = userClient.getFingerprint();
				AppContextUtil.setUserClient(userClient);
			}

			// Session Actor Tracking
			if (!ArgUtil.isEmpty(sessionId) && !ArgUtil.isEmpty(sessionContextService)) {
				String actorInfoJson = req.getHeader(AppConstants.ACTOR_INFO_XKEY);
				if (!StringUtils.isEmpty(actorInfoJson)) {
					sessionContextService.setContext(new MapModel(actorInfoJson));
				}
			}

			String requestdParamsJson = ArgUtil.nonEmpty(req.getParameter(AppConstants.REQUESTD_PARAMS_XKEY),
					req.getHeader(AppConstants.REQUESTD_PARAMS_XKEY));
			if (!ArgUtil.isEmpty(requestdParamsJson)) {
				AppContextUtil.setAuthParams(null, requestdParamsJson);
			} else {
				AppContextUtil.setAuthParams(ArgUtil.nonEmpty(req.getParameter(AppConstants.REQUEST_PARAMS_XKEY),
						req.getHeader(AppConstants.REQUEST_PARAMS_XKEY)), requestdParamsJson);
			}

			if (appContextInFilter != null) {
				appContextInFilter.appRequestContextInFilter(localCommonHttpRequest);
			}

			// Trace Id Tracking
			String traceId = req.getHeader(AppConstants.TRACE_ID_XKEY);
			if (StringUtils.isEmpty(traceId)) {
				traceId = ArgUtil.parseAsString(req.getParameter(AppConstants.TRACE_ID_XKEY));
			}
			if (StringUtils.isEmpty(traceId) && ArgUtil.is(apiRequest.getTraceFilter())) {
				Pattern pattern = Pattern.compile("^" + appConfig.getAppPrefix() + apiRequest.getTraceFilter());
				Matcher matcher = pattern.matcher(req.getRequestURI());
				if (matcher.find()) {
					traceId = matcher.group("traceid");
				}
			}

			if (StringUtils.isEmpty(traceId)) {
				setFlow(req, apiRequest);
				String flowFix = AppContextUtil.getFlowfix();

				HttpSession session = req.getSession(apiRequest.isSession() || appConfig.isAppSessionEnabled());
				if (ArgUtil.isEmpty(sessionId)) {
					if (ArgUtil.isEmpty(fp)) {
						fp = localCommonHttpRequest.getRequestParam(AppConstants.DEVICE_XID_KEY);
						if (ArgUtil.isEmpty(fp)) {
							fp = UniqueID.generateString62();
							localCommonHttpRequest.setCookie(AppConstants.DEVICE_XID_KEY, fp);
						}
					}
					AppContextUtil.setSessionPrefix(fp);
					AppContextUtil.setRequestUser(localCommonHttpRequest.getTraceUserIdentifier());

					if (session == null) {
						sessionId = AppContextUtil.getSessionId(true);
					} else {
						sessionId = AppContextUtil.getSessionId(
								ArgUtil.parseAsString(session.getAttribute(AppConstants.SESSION_ID_XKEY)));
						flowFix = ArgUtil.parseAsString(session.getAttribute(AppConstants.FLOW_ID_XKEY));
					}
				}

				if (apiRequest.isInitFlow()) {
					long flowId = StringUtils.alpha62(flowFix);
					flowFix = StringUtils.pad(StringUtils.alpha62(++flowId), "000", 1);
				}

				AppContextUtil.setSessionId(sessionId);
				AppContextUtil.setFlowfix(flowFix);
				flowFix = AppContextUtil.getFlowfix();
				traceId = AppContextUtil.getTraceId();
				AppContextUtil.init();

				if (session != null) {
					req.getSession().setAttribute(AppConstants.FLOW_ID_XKEY, flowFix);
					req.getSession().setAttribute(AppConstants.SESSION_ID_XKEY, sessionId);
					req.getSession().setAttribute(TenantContextHolder.TENANT, tnt);
					// localCommonHttpRequest.setCookie(AppConstants.SESSION_ID_XKEY, sessionId);
				}
			} else {
				AppContextUtil.loadTraceId(traceId);
				AppContextUtil.init();
			}

			if (ArgUtil.is(req.getSession(false))) {
				AppContextUtil.setJSessionId(req.getSession().getId());
			}

			// Actual Request Handling
			AppContextUtil.setTraceTime(startTime);
			if (reqType.isTrack() || AuditServiceClient.isDebugEnabled()) {
				AuditServiceClient.trackStatic(new RequestTrackEvent(req).debug(reqType.isDebugOnly()));
				req = AppRequestUtil.printIfDebug(req);
			}
			try {
				if (isRequestValid(localCommonHttpRequest, apiRequest, req, resp, traceId)) {
					if (ArgUtil.is(apiRequest.getDeprecated())) {
						ApiResponseUtil.addWarning(apiRequest.getDeprecated());
					}
					AppResponseWrapper wresp = new AppResponseWrapper(resp);
					addSameSiteCookieAttribute(wresp);
					chain.doFilter(req, wresp);

				} else {
					resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
					resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
					AmxApiError apiError = new AmxApiError();
					apiError.setHttpStatus(HttpStatus.FORBIDDEN);
					apiError.setStatusKey(ApiStatusCodes.ACCESS_DENIED.toString());
					apiError.setErrors(ApiResponseUtil.getErrors());
					ExceptionMessageKey.resolveLocalMessage(apiError);
					JsonUtil.getMapper().writeValue(resp.getWriter(), apiError);
				}
			} finally {
				if (reqType.isTrack() || AuditServiceClient.isDebugEnabled()) {
					AuditServiceClient.trackStatic(new RequestTrackEvent(Type.RESP_OUT).inbound(resp, req)
							.responseTime(System.currentTimeMillis() - startTime).debug(reqType.isDebugOnly()));
					AppRequestUtil.printIfDebug(resp);
				}
			}

		} finally {
			// Tear down MDC data:
			// ( Important! Cleans up the ThreadLocal data again )
			AppContextUtil.clear();
		}
	}

	private void addSameSiteCookieAttribute(HttpServletResponse response) {
		Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
		boolean firstHeader = true;
		for (String header : headers) { // there can be multiple Set-Cookie attributes
			if (firstHeader) {
				response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=None"));
				firstHeader = false;
				continue;
			}
			response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=None"));
		}
	}

	@Override
	public void destroy() {
		LOGGER.info("Filter Destroyed");
	}

}
