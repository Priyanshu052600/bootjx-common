package com.boot.test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boot.jx.AppConstants;
import com.boot.jx.AppContext;
import com.boot.jx.AppContextUtil;
import com.boot.jx.dict.FileFormat;
import com.boot.jx.dict.UserClient.AppType;
import com.boot.jx.dict.UserClient.DeviceType;
import com.boot.jx.dict.UserClient.UserDeviceClient;
import com.boot.jx.tunnel.TunnelMessage;
import com.boot.utils.ArgUtil;
import com.boot.utils.ContextUtil;
import com.boot.utils.JsonUtil;
import com.boot.utils.TimeUtils;
import com.fasterxml.jackson.core.type.TypeReference;

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

	public static void main(String[] args) {
		System.out.println(FileFormat.from("audio/webm;codecs=opus"));
		System.out.println(FileFormat.from("audio/webm; codecs=opus"));
		System.out.println(FileFormat.from("audio/webm"));
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
