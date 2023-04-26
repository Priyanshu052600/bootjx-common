package com.boot.jx.scope.tnt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.jackson.JsonComponent;

import com.boot.utils.ArgUtil;
import com.boot.utils.Constants;
import com.boot.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Tenants {

	@JsonDeserialize(as = TenentGeneric.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public interface Tenant {

		public Integer getId();

		public String getName();

		public String getCode();

		default public boolean isTenant() {
			return true;
		}
	}

	public static class TenentGeneric implements Tenant {

		private int id;
		private String code;
		private String name;

		public TenentGeneric(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}

		@Override
		public Integer getId() {
			return this.id;
		}

		@Override
		public String getCode() {
			return code;
		}

		@Override
		public String getName() {
			return name;
		};
	}

	public static abstract class TenantResolver {

		public String resolve(String tnt) {
			return StringUtils.toLowerCase(tnt);
		}

		public String getDBName(String tnt) {
			return StringUtils.toLowerCase(tnt);
		}

		public boolean isValid() {
			return true;
		}

	}

	public static Map<String, Tenant> strMapping = new HashMap<String, Tenant>();
	public static final Map<Integer, Tenant> idMapping = new HashMap<Integer, Tenant>();
	public static final List<Tenant> list = new ArrayList<Tenant>();
	public static final Pattern pattern = Pattern.compile("^(.+?)-(.+?)$");

	public static final String NONE_STR = "NONE";
	public static final Tenant NONE = new TenentGeneric(NONE_STR);

	public static String DEFAULT_STR = "DEFAULT";
	public static Tenant DEFAULT = new TenentGeneric(DEFAULT_STR);

	public static void setDefault(Object tenant) {
		if (ArgUtil.is(tenant)) {
			DEFAULT = from(tenant, DEFAULT);
			DEFAULT_STR = tenant.toString();
		}
	}

	public static String getDefault() {
		return DEFAULT_STR;
	}

	public static boolean isDefault(Object tenant) {
		return ArgUtil.areEqual(tenant, DEFAULT_STR);
	}

	public static Tenant from(Object siteId, Tenant defaultValue) {
		return fromString(ArgUtil.parseAsString(siteId), defaultValue);
	}

	public static String fromAsString(Object siteId, Tenant defaultValue) {
		return ArgUtil.parseAsString(fromString(ArgUtil.parseAsString(siteId), defaultValue));
	}

	public static Tenant fromString(String siteId, Tenant defaultValue) {
		if (siteId != null) {
			String siteIdStr = siteId.toLowerCase();
			Matcher matcher = pattern.matcher(siteIdStr);
			if (matcher.find()) {
				siteIdStr = matcher.group(2);
			}

			if (strMapping.containsKey(siteIdStr)) {
				return strMapping.get(siteIdStr);
			}
			for (TenantByCountry site : TenantByCountry.values()) {
				if (site.toString().equalsIgnoreCase(siteIdStr)) {
					return site;
				}
			}
			return new TenentGeneric(siteIdStr);
		}
		return defaultValue;
	}

	public static Tenant fromString(String siteId, Tenant defaultValue, boolean onlyTenant) {
		Tenant tnt = fromString(siteId, defaultValue);
		if (onlyTenant && (tnt != null && !tnt.isTenant())) {
			return defaultValue;
		}
		return tnt;
	}

	public static List<String> tenantStrings() {
		List<String> values = new ArrayList<>();
		for (Tenant site : list) {
			if (site.isTenant()) {
				values.add(site.toString());
			}
		}
		return values;
	}

	public static void register(Tenant tenant) {
		strMapping.put(tenant.toString().toLowerCase(), tenant);
		strMapping.put(ArgUtil.parseAsString(tenant.getId(), Constants.BLANK).toLowerCase(), tenant);
		strMapping.put("app-" + tenant.toString().toLowerCase(), tenant);
		idMapping.put(tenant.getId(), tenant);
		list.add(tenant);
	}

	public static List<Tenant> values() {
		return list;
	}

	@JsonComponent
	public class TenantDeSerializer extends JsonDeserializer<Tenant> {

		@Override
		public Tenant deserialize(JsonParser jp, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			return Tenants.from(jp.getValueAsString(), Tenants.DEFAULT);
		}
	}
}
