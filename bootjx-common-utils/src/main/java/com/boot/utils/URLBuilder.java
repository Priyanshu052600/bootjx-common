package com.boot.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class URLBuilder.
 */
public class URLBuilder {

	public static String URL_VALIDATOR = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	public static boolean isValid(String s) {
		try {
			Pattern patt = Pattern.compile(URL_VALIDATOR);
			Matcher matcher = patt.matcher(s);
			return matcher.matches();
		} catch (RuntimeException e) {
			return false;
		}
	}

	/** The params. */
	private StringBuilder folders, params;

	/** The host. */
	private String connType, host;

	/**
	 * Sets the connection type.
	 *
	 * @param conn the new connection type
	 */
	public void setConnectionType(String conn) {
		connType = conn;
	}

	public URLBuilder protocol(String conn) {
		connType = conn;
		return this;
	}

	/**
	 * Instantiates a new URL builder.
	 */
	public URLBuilder() {
		folders = new StringBuilder();
		params = new StringBuilder();
	}

	/**
	 * Instantiates a new URL builder.
	 *
	 * @param host the host
	 */
	public URLBuilder(String host) {
		this();
		this.host = host;
	}

	/**
	 * Sets the path.
	 *
	 * @param folder the folder
	 * @return the URL builder
	 */
	public URLBuilder path(String folder) {
		folders.append("/");
		folders.append(folder);
		return this;
	}

	/**
	 * 
	 * @deprecated use {@link URLBuilder#path(String)}
	 */
	@Deprecated
	public URLBuilder setPath(String folder) {
		return this.path(folder);
	}

	/**
	 * Adds the parameter.
	 *
	 * @param query the query
	 * @return the URL builder
	 */
	public URLBuilder query(String query) {
		if (!ArgUtil.isEmptyString(query)) {
			if (params.toString().length() > 0) {
				params.append("&");
			}
			params.append(query);
		}
		return this;
	}

	/**
	 * 
	 * @deprecated use {@link URLBuilder#query(String)}
	 * 
	 */
	@Deprecated
	public URLBuilder addParameter(String query) {
		return this.query(query);
	}

	public String query() {
		String query = params.toString().trim();
		if (ArgUtil.isEmpty(query))
			return null;
		return query;
	}

	/**
	 * Adds the query parameter.
	 *
	 * @param parameter the parameter
	 * @param value     the value
	 * @return the URL builder
	 */
	public URLBuilder queryParam(String parameter, Object value) {
		String valueStr = ArgUtil.parseAsString(value, "");
		if (params.toString().length() > 0) {
			params.append("&");
		}
		params.append(parameter);
		params.append("=");
		params.append(valueStr);
		return this;
	}

	/**
	 * 
	 * @deprecated use {@link URLBuilder#queryParam(String, Object)}
	 */
	@Deprecated
	public URLBuilder addParameter(String parameter, Object value) {
		return this.queryParam(parameter, value);
	}

	public URLBuilder pathParam(String parameter, Object value) {
		String valueStr = ArgUtil.parseAsString(value, "");
		String path = folders.toString();
		path = path.replace("{" + parameter + "}", valueStr);
		folders = new StringBuilder(path);
		return this;
	}

	/**
	 * @deprecated use {@link URLBuilder#pathParam(String, Object)}
	 */
	@Deprecated
	public URLBuilder addPathVariable(String parameter, Object value) {
		return this.pathParam(parameter, value);
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 * @throws URISyntaxException    the URI syntax exception
	 * @throws MalformedURLException the malformed URL exception
	 */
	public String getURL() throws URISyntaxException, MalformedURLException {
		URI uri;
		String query = query();
		if (host == null) {
			// uri = new URI(null, null, folders.toString(), params.toString(), null);
			return folders.toString().replaceAll("/+", "/") + (query == null ? query : ("?" + query));
		} else if (connType == null) {
			// uri = new URI(null, null, folders.toString(), params.toString(), null);
			return host + folders.toString().replaceAll("/+", "/") + (query == null ? query : ("?" + query));
		} else {
			uri = new URI(connType, host, folders.toString().replaceAll("/+", "/"), query, null);
			return uri.toURL().toString();
		}
	}

	/**
	 * Gets the relative URL.
	 *
	 * @return the relative URL
	 * @throws URISyntaxException    the URI syntax exception
	 * @throws MalformedURLException the malformed URL exception
	 */
	public String getRelativeURL() throws URISyntaxException, MalformedURLException {
		URI uri = new URI(null, null, folders.toString().replaceAll("/+", "/"), query(), null);
		return uri.toString();
	}

	public String getPath() throws URISyntaxException, MalformedURLException {
		URI uri = new URI(null, null, "/" + StringUtils.trim(folders.toString().replaceAll("/+", "/"), '/'), null,
				null);
		return uri.toString();
	}

	/**
	 * Parses the.
	 *
	 * @param urlString the url string
	 * @return the URL builder
	 * @throws MalformedURLException the malformed URL exception
	 */
	public static URLBuilder parse(String urlString) throws MalformedURLException {
		URL url;
		URLBuilder builder;
		if (urlString.startsWith("/")) {
			url = new URL("https://localhost/" + urlString);
			builder = new URLBuilder();
		} else {
			if (!isValid(urlString)) {
				urlString = "https://" + urlString;
			}
			url = new URL(urlString);
			builder = new URLBuilder(url.getAuthority());
			builder.setConnectionType(url.getProtocol());
		}

		builder.setConnectionType(url.getProtocol());
		builder.setHost(url.getHost());

		builder.setPath(url.getPath());
		builder.addParameter(url.getQuery());
		return builder;
	}

	public File toFile() {
		return new File(folders.toString());
	}

	public String getConnectionType() {
		return connType;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
