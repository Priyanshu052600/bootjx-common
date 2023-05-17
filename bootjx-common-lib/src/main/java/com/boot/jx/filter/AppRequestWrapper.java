package com.boot.jx.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.util.StreamUtils;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class AppRequestWrapper extends HttpServletRequestWrapper {

	private byte[] cachedBody;

	public AppRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		InputStream requestInputStream = request.getInputStream();
		this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new CachedBodyServletInputStream(this.cachedBody);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		// Create a reader from cachedContent
		// and return it
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
		return new BufferedReader(new InputStreamReader(byteArrayInputStream));
	}

}
