package com.boot.loaderjs;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boot.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

public class JsonTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");

	private static Logger LOGGER = LoggerFactory.getLogger(JsonTest.class);

	public static class JsonSample {
		long someLong;
		BigDecimal terminalId;

		public BigDecimal getTerminalId() {
			return terminalId;
		}

		public void setTerminalId(BigDecimal terminalId) {
			this.terminalId = terminalId;
		}

		public long getSomeLong() {
			return someLong;
		}

		public void setSomeLong(long someLong) {
			this.someLong = someLong;
		}
	}

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws MalformedURLException, URISyntaxException {

		JsonSample client = new JsonSample();
		client.setSomeLong(1234);
		client.setTerminalId(new BigDecimal(123));

		String messageJson = JsonUtil.toJson(client);
		LOGGER.info("====== {}", messageJson);
		JsonSample message2 = JsonUtil.fromJson(messageJson,
				new TypeReference<JsonSample>() {
				});
		LOGGER.info("====== {}", JsonUtil.toJson(message2));

	}
}
