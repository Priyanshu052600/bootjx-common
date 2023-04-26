package com.boot.jx.scope.tnt;

import com.boot.jx.scope.tnt.Tenants.Tenant;
import com.boot.utils.ArgUtil;
import com.boot.utils.ContextUtil;

public class TenantContextHolder {

	public static final String TENANT = "tnt";

	public static void setCurrent(Tenant site) {
		ContextUtil.map().put(TENANT, site);
	}

	public static void setCurrent(String siteId) {
		ContextUtil.map().put(TENANT, fromString(siteId, Tenants.DEFAULT));
	}

	public static void setCurrent(String siteId, Tenant defaultTnt) {
		ContextUtil.map().put(TENANT, fromString(siteId, defaultTnt));
	}

	public static void setDefault() {
		ContextUtil.map().put(TENANT, Tenants.DEFAULT.toString());
	}

	private static String currentSite(boolean returnDefault, Tenant defaultTnt) {
		Object site = ContextUtil.map().get(TENANT);
		if (site == null) {
			if (returnDefault) {
				return ArgUtil.parseAsString(defaultTnt);
			} else {
				return null;
			}
		}
		return (String) site;
	}

	/**
	 * Returns Current Tenant/Site OR {@link Tenant#DEFAULT} if no mathicng found in
	 * case returnDefault is TRUE
	 * 
	 * @param returnDefault - to return Tenant#DEFAULT in case no current is set
	 * @return
	 */
	public static String currentSite(boolean returnDefault) {
		return currentSite(returnDefault, Tenants.DEFAULT);
	}

	/**
	 * Returns Current Tenant/Site OR defaultTnt if null OR {@link Tenant#DEFAULT}
	 * if no mathicng found
	 * 
	 * @param defaultTnt
	 * @return
	 */
	public static String currentSite(Tenant defaultTnt) {
		return currentSite(true, Tenants.DEFAULT);
	}

	/**
	 * Returns Current Tenant/Site OR defaultTnt if null OR {@link Tenant#DEFAULT}
	 * if no mathicng found
	 * 
	 * @param defaultTnt
	 * @return
	 */
	public static String currentSite(String defaultTnt) {
		return currentSite(fromString(defaultTnt, Tenants.DEFAULT));
	}

	/**
	 * Returns Current Tenant/Site OR {@link Tenant#DEFAULT} if no mathicng found
	 * 
	 * @return
	 */
	public static String currentSite() {
		return currentSite(true);
	}

	public static String fromString(String siteId, Tenant defaultTnt) {
		return ArgUtil.parseAsString(Tenants.fromString(siteId, defaultTnt));
	}

}
