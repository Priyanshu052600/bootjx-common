package com.boot.jx.sso;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.boot.utils.ArgUtil;
import com.boot.utils.URLBuilder;
import com.boot.utils.Urly;
import com.javachinna.oauth2.user.SocialEnums.ChannelPartner;

@Component
public class SSOUrlUtility {

	@Value("${bootjx.linq.base.url:}")
	String linqBaseUrl;

	@Value("${bootjx.linq.base.callback.path:/linq/app/v1/connect/{provider}/callback}")
	String linqCallbackPath;

	URLBuilder linkBase;

	public URLBuilder linkBase() {
		if (!ArgUtil.is(linkBase)) {
			try {
				linkBase = Urly.parse(linqBaseUrl);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				linkBase = new URLBuilder();
			}
		}
		return linkBase;
	}

	public String getOrigin(String subdomain) {
		if (!ArgUtil.is(subdomain)) {
			subdomain = linkBase().getSubdomain();
		}
		if (ArgUtil.is(subdomain)) {
			return "https://" + subdomain + "." + linkBase().getBasedomain();
		}
		return "https://" + linkBase().getBasedomain();
	}

	private String getOrigin() {
		return this.getOrigin(null);
	}

	public String getUrl(String path) {
		return this.getOrigin() + path;
	}

	public String getUrl(String subdomain, String path) {
		if (ArgUtil.is(subdomain)) {
			return this.getOrigin(subdomain) + path;
		}
		return this.getOrigin() + path;
	}

	public String redirectPath(String provider, ChannelPartner partner) {
		return linqCallbackPath.replace("{provider}", provider);
	}

	public String getLinqBaseUrl() {
		return linqBaseUrl;
	}

	public String getUrlOrPath(String path) {
		if (ArgUtil.is(getLinqBaseUrl())) {
			return getUrl(path);
		}
		return path;
	}
}
