package com.boot.jx;

import com.boot.utils.Constants;

public class AppConstants extends Constants {

	protected AppConstants() {
		// Not Allowed
	}

	public static class Validator {
		public static final String IDENTITY = "^[0-9a-zA-Z]+$";
		public static final String OTP = "^[0-9]{6}$";
		public static final String PHONE = "^[0-9]{15}$";
		public static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
	}

	public static class Scopes {
		public static final String SESSION = "session";
		public static final String TENANT = "tenant";
		public static final String THREAD = "thread";
		public static final String VENDOR = "vendor";
	}

	public static final String ENV_XKEY = "x-env";
	public static final String AUTH_ID_XKEY = "x-app-auth-id";
	public static final String AUTH_KEY_XKEY = "x-app-auth-key";
	public static final String AUTH_TOKEN_XKEY = "x-app-auth-token";
	public static final String AUTH_CLIENT_KEY_XKEY = "x-client-auth-key";
	/**
	 * Not to be uses as clientAuth is being dropped
	 */
	@Deprecated
	public static final String AUTH_CLIENT_TOKEN_XKEY = "x-client-auth-token";
	public static final String SESSION_PREFIX_XKEY = "x-session-prefix";
	public static final String SESSION_SUFFIX_XKEY = "x-session-suffix";
	public static final String SESSION_ID_XKEY = "x-session-id";
	public static final String SESSION_JID_XKEY = "x-session-jid";
	public static final String SESSION_UID_XKEY = "x-session-uid";
	public static final String SESSION_TNT_XKEY = "x-session-tnt";
	public static final String FLOW_ID_XKEY = "x-flow-id";
	public static final String TRACE_ID_XKEY = "x-trace-id";
	public static final String LANG_PARAM_KEY = "lang";
	public static final String TRACE_TIME_XKEY = "x-time-id";
	public static final String REQUEST_TYPE_XKEY = "x-request-type";
	public static final String REQUEST_DETAILS_XKEY = "x-request-details";
	public static final String REQUEST_WARNING_XKEY = "x-request-warning";
	public static final String REQUEST_ERROR_XKEY = "x-request-error";
	public static final String REQUEST_LOGS_XKEY = "x-request-logs";
	public static final String TRANX_ID_XKEY = "x-tranx-id";
	public static final String CONTEXT_ID_XKEY = "x-cntxt-id";
	public static final String USER_CLIENT_XKEY = "x-user-client";
	public static final String USER_DEVICE_XKEY = "x-user-device";

	/**
	 * User Device Client Keys
	 */
	public static final String UDC_CLIENT_TYPE_XKEY = "x-client-type";
	public static final String UDC_CLIENT_TYPE_XKEY_CLEAN = UDC_CLIENT_TYPE_XKEY.replaceAll("[-]", "_");
	public static final String UDC_DEVICE_TYPE_XKEY = "x-device-type";
	public static final String APP_VERSION_XKEY = "x-app-version";

	public static final String REQUEST_PARAMS_XKEY = "x-app-auth-params";
	public static final String REQUESTD_PARAMS_XKEY = "x-app-authd-params";

	public static final String EXCEPTION_HEADER_KEY = "x-exception";
	public static final String EXCEPTION_HEADER_CODE_KEY = "x-exception-code";
	public static final String EXCEPTION_LOGS_XKEY = "x-exception-logs";
	public static final String ERROR_HEADER_KEY = "apiErrorJson";

	public static final String META_XKEY = "meta-info";
	public static final String REQUEST_META_XKEY = "request-meta-info";

	public static final String TRANX_ID_XKEY_CLEAN = TRANX_ID_XKEY.replaceAll("[-]", "_");
	public static final String ACTOR_ID_XKEY = "x-actor-id";
	public static final String ACTOR_INFO_XKEY = "x-actor-info";
	public static final String DEVICE_ID_KEY = "did";
	public static final String DEVICE_ID_XKEY = "x-did";
	public static final String CLIENT_TRACK_ID_KEY = "ctid";
	public static final String CLIENT_TRACK_ID_XKEY = "x-ctid";
	public static final String DEVICE_IP_XKEY = "x-ip";
	public static final String DEVICE_XID_KEY = "xid";
	public static final String DEVICE_IP_LOCAL_XKEY = "x-ip-local";
	public static final String APP_DETAILS = "app";
	public static final String BROWSER_ID_KEY = "bid";
	public static final String SESSIONID = "JSESSIONID";

	public static final String[] HEADERS = { ENV_XKEY };

}
