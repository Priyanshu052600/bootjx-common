package com.boot.jx.rest;

public interface RequestMetaFilter {

	/**
	 * Meta Data Info you want to send with outgoing request
	 * 
	 * @param meta
	 */
	public void requetMetaOutFilter(ARequestMetaInfo requestMeta);

	/**
	 * Meta Data Info you want to fetch from incoming request
	 * 
	 * @param meta
	 */
	public void requetMetaInFilter(ARequestMetaInfo requestMeta);
}
