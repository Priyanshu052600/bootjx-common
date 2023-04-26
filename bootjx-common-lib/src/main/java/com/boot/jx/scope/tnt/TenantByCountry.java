package com.boot.jx.scope.tnt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.boot.jx.dict.Currency;
import com.boot.jx.dict.Language;
import com.boot.jx.scope.tnt.Tenants.Tenant;
import com.boot.utils.ArgUtil;

public enum TenantByCountry implements Tenant {

	/** Dev Environments **/
	KWT2("KW", 91, "Kuwait Alt", Currency.KWD, Language.AR),
	BRNDEV("BH", 104, "Bahrain dev", Currency.BHD, Language.AR),
	OMNDEV("OM", 82, "oman dev", Currency.OMR, Language.AR),

	KWT("KW", 91, "Kuwait", Currency.KWD, Language.AR), BHR("BH", 104, "Bahrain", Currency.BHD, Language.AR),
	OMN("OM", 82, "Oman", Currency.OMR, Language.AR),

	KWTV2("KW", 91, "Kuwait", Currency.KWD, Language.AR),

	NONE("none", 0, null, null, Language.EN);

	public static TenantByCountry DEFAULT = KWT;

	static {
		for (TenantByCountry site : TenantByCountry.values()) {
			Tenants.register(site);
		}
	}

	private Integer id;
	private String code;
	private boolean tenant;
	private Currency currency;
	public final Integer countryId;
	public Language defaultLang;

	public boolean isTenant() {
		return tenant;
	}

	public Language defaultLang() {
		return defaultLang;
	}

	public void setTenant(boolean tenant) {
		this.tenant = tenant;
	}

	TenantByCountry(String id, int code, String name, boolean isTenantApp, Currency currency, Language defaultLang) {
		this.id = code;
		this.countryId = code;
		this.code = ArgUtil.parseAsString(id);
		this.tenant = isTenantApp;
		this.currency = currency;
		this.defaultLang = defaultLang;
	}

	TenantByCountry(String id, int code, String name) {
		this(id, code, name, true, null, Language.EN);
	}

	TenantByCountry(String id, int code, String name, boolean isTenantApp) {
		this(id, code, name, isTenantApp, null, Language.EN);
	}

	TenantByCountry(String id, int code, String name, Currency currency, Language defaultLang) {
		this(id, code, name, true, currency, defaultLang);
	}

	TenantByCountry() {
		this(null, 0, null);
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getISO2Code() {
		return code;
	}

	public String getCode() {
		return code;
	}

	public BigDecimal getBDCode() {
		return new BigDecimal(code);
	}

	public static List<String> tenantStrings() {
		List<String> values = new ArrayList<>();
		for (TenantByCountry site : TenantByCountry.values()) {
			if (site.isTenant()) {
				values.add(site.toString());
			}
		}
		return values;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public Currency getCurrency() {
		return currency;
	}

	public String toString() {
		return this.name();
	}

	@Override
	public String getName() {
		return this.name();
	}

}
