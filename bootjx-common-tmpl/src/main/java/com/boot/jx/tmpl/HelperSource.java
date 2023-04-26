package com.boot.jx.tmpl;

import java.util.Date;

import com.boot.utils.ArgUtil;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public class HelperSource {

	public static String now() {
		return new Date().toString();
	}

	public String any(String param0, String param1, Options options) {
		return ArgUtil.nonEmpty(param0, param1);
	}

	public static class FallbackHelper implements Helper<String> {
		public CharSequence apply(String list, Options options) {
			return list;
		}
	}

}
