package com.boot.loaderjs;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boot.jx.AppConstants;
import com.boot.jx.AppContext;
import com.boot.jx.AppContextUtil;
import com.boot.jx.dict.FileFormat;
import com.boot.jx.dict.UserClient.AppType;
import com.boot.jx.dict.UserClient.DeviceType;
import com.boot.jx.dict.UserClient.UserDeviceClient;
import com.boot.jx.scope.tnt.TenantContextHolder;
import com.boot.jx.tunnel.TunnelMessage;
import com.boot.utils.ArgUtil;
import com.boot.utils.ContextUtil;
import com.boot.utils.JsonUtil;
import com.boot.utils.OTPUtils;
import com.boot.utils.OTPUtils.OTPDetails;
import com.boot.utils.StringUtils.StringMatcher;
import com.boot.utils.TimeUtils;
import com.boot.utils.UniqueID;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.gianlucanitti.javaexpreval.Expression;
import com.github.gianlucanitti.javaexpreval.ExpressionContext;
import com.github.gianlucanitti.javaexpreval.ExpressionException;

public class App { // Noncompliant

	/**
	 * [id^='someId'] will match all ids starting with someId.
	 * 
	 * [id$='someId'] will match all ids ending with someId.
	 * 
	 * [id*='someId'] will match all ids containing someId.
	 * 
	 */
	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");
	public static Pattern OPERATOR_FILTER_DOUBLE = Pattern.compile("^(.*)(>=|\\*=|<=|!=)$");
	public static Pattern OPERATOR_FILTER_SINGLE = Pattern.compile("^(.*)(>|=|<|~)$");

	private static Logger LOGGER = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws ExpressionException {
		System.out.println(FileFormat.from("audio/webm;codecs=opus"));
		System.out.println(FileFormat.from("audio/webm; codecs=opus"));
		System.out.println(FileFormat.from("audio/webm"));
	}

	public static void mainx(String[] args) throws ExpressionException {
		MDC.put(TenantContextHolder.TENANT, "heohooh");
		LOGGER.info("Hello");
	}

	public static void main8(String[] args) throws ExpressionException {

		StringMatcher matcher = new StringMatcher("DOCUMENT_DATE<");

		if (matcher.isMatch(OPERATOR_FILTER_DOUBLE) || matcher.isMatch(OPERATOR_FILTER_SINGLE)) {
			System.out.println(matcher.group(1));
			System.out.println(matcher.group(2));
		}

	}

	public static void main9(String[] args) throws ExpressionException {
		OTPDetails details = OTPUtils.genrateBasicOTP("512", "REMIT");
		System.out.println(JsonUtil.toJson(details));
		System.out.println(OTPUtils.validateBasicOTP(details.getId(), "512", "REMIT", details.getOtp()));
		System.out.println(OTPUtils.validateBasicOTP(details.getId(), "512", "REMIT", "121218"));

		OTPDetails detailsYinYang = OTPUtils.genrateBasicOTP("512", "REMIT");
		System.out.println(JsonUtil.toJson(detailsYinYang));
		System.out.println(new OTPDetails().yin(detailsYinYang.getYin()).yang(detailsYinYang.getYang())
				.genrate("512", "REMIT").isValid(detailsYinYang.getOtp()));
		System.out.println(new OTPDetails().yin(detailsYinYang.getYin()).yang(detailsYinYang.getYang())
				.genrate("512", "REMIT").isValid("121218"));
	}

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws ExpressionException
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */

	public static void main6(String[] args) throws ExpressionException {
		AppContextUtil.getSessionId(true);
		String traceId = AppContextUtil.getTraceId();

		System.out.println("traceId  " + traceId);
		Matcher matcher = UniqueID.SYSTEM_STRING_PATTERN.matcher(traceId);
		if (matcher.find()) {
			System.out.println("=" + matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3));
		}
		Matcher matcher2 = UniqueID.SYSTEM_STRING_PATTERN_V2.matcher(traceId);
		if (matcher2.find()) {
			System.out.println("==" + matcher2.group(1) + "-" + matcher2.group(2) + "-" + matcher2.group(3));
		}
	}

	public static void main5(String[] args) throws ExpressionException {
		ExpressionContext c = new ExpressionContext();
		c.setVariable("x", ArgUtil.parseAsDouble("2.04", Double.valueOf(0)));
		c.setVariable("y", ArgUtil.parseAsDouble("3.0", Double.valueOf(0)));
		Expression expr = Expression.parse("x*y");
		double result = expr.eval(c);
		System.out.println("Z=" + result);
	}

	public static void main4(String[] args) {
		Long traceTime = ArgUtil.parseAsLong(ContextUtil.map().get(AppConstants.TRACE_TIME_XKEY), 0L);
		if (traceTime != null && traceTime != 0L) {
			System.out.println(TimeUtils.timeSince(AppContextUtil.getTraceTime()));
		}
	}

	public static void main3(String[] args) {
		String url = "/api/user/tranx/history";
		System.out.println(
				url.toLowerCase().replace("pub", "b").replace("api", "p").replace("user", "").replace("get", "")
						.replace("post", "").replace("save", "").replace("/", "").replaceAll("[AaEeIiOoUuYyWwHh]", ""));
	}

	public static void main2(String[] args) throws MalformedURLException, URISyntaxException {

		AppContext context = AppContextUtil.getContext();

		UserDeviceClient client = new UserDeviceClient();

		client.setIp("0:0:0:0:0:0:0:1");
		client.setFingerprint("38b3dd46de1d7df8303132bba73ca1e6");
		client.setDeviceType(DeviceType.COMPUTER);
		client.setAppType(AppType.WEB);
		context.setClient(client);
		context.setTraceId("TST-1d59nub55kbgg-1d59nub5827sx");
		context.setTranxId("TST-1d59nub55kbgg-1d59nub5827sx");

		TunnelMessage<Map<String, String>> message = new TunnelMessage<Map<String, String>>(
				new HashMap<String, String>(), context);
		message.setTopic("DATAUPD_CUSTOMER");

		String messageJson = JsonUtil.toJson(message);
		LOGGER.info("====== {}", messageJson);
		TunnelMessage<Map<String, String>> message2 = JsonUtil.fromJson(messageJson,
				new TypeReference<TunnelMessage<Map<String, String>>>() {
				});
		LOGGER.info("====== {}", JsonUtil.toJson(message2));

	}
}
