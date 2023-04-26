package com.boot.jx.dict;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.boot.utils.ArgUtil;

public enum Countries {

	/** Dev Environments **/
	KWT2("KW", 91, "Kuwait Alt", true), BRNDEV("BH", 104, "Bahrain dev", true), OMNDEV("OM", 82, "oman dev", true),

	AND("AD", 91, "Andorra"), ARE("AE", 91, "United Arab Emirates"), AFG("AF", 91, "Afghanistan"),

	ATG("AG", 91, "Antigua and Barbuda"), AIA("AI", 91, "Anguilla"), ALB("AL", 91, "Albania"),

	ARM("AM", 91, "Armenia"), ANT("AN", 91, "Netherlands Antilles"), AGO("AO", 91, "Angola"),

	ATA("AQ", 91, "Antarctica"), ARG("AR", 91, "Argentina"), ASM("AS", 91, "American Samoa"),

	AUT("AT", 91, "Austria"), AUS("AU", 91, "Australia"), ABW("AW", 91, "Aruba"),

	ALA("AX", 91, "Åland Islands"), AZE("AZ", 91, "Azerbaijan"),

	BIH("BA", 91, "Bosnia and Herzegovina"), BRB("BB", 91, "Barbados"), BGD("BD", 91, "Bangladesh"),

	BEL("BE", 91, "Belgium"), BFA("BF", 91, "Burkina Faso"), BGR("BG", 91, "Bulgaria"), BHR("BH", 104, "Bahrain", true),

	BDI("BI", 91, "Burundi"), BEN("BJ", 91, "Benin"), BLM("BL", 91, "Saint Barthélemy"), BMU("BM", 91, "Bermuda"),

	BRN("BN", 91, "Brunei"), BOL("BO", 91, "Bolivia"), BES("BQ", 91, "Bonaire, Sint Eustatius and Saba"),

	BRA("BR", 91, "Brazil"), BHS("BS", 91, "Bahamas"), BTN("BT", 91, "Bhutan"),

	BVT("BV", 91, "Bouvet Island"), BWA("BW", 91, "Botswana"), BLR("BY", 91, "Belarus"),

	BLZ("BZ", 91, "Belize"),

	CAN("CA", 91, "Canada"), CCK("CC", 91, "Cocos Islands"), COD("CD", 91, "The Democratic Republic Of Congo"),

	CAF("CF", 91, "Central African Republic"), COG("CG", 91, "Congo"), CHE("CH", 91, "Switzerland"),

	CIV("CI", 91, "Côte d'Ivoire"), COK("CK", 91, "Cook Islands"), CHL("CL", 91, "Chile"),

	CMR("CM", 91, "Cameroon"), CHN("CN", 91, "China"), COL("CO", 91, "Colombia"), CRI("CR", 91, "Costa Rica"),

	CUB("CU", 91, "Cuba"), CPV("CV", 91, "Cape Verde"), CUW("CW", 91, "Curaçao"),

	CXR("CX", 91, "Christmas Island"), CYP("CY", 91, "Cyprus"), CZE("CZ", 91, "Czech Republic"),

	DEU("DE", 91, "Germany"), DJI("DJ", 91, "Djibouti"), DNK("DK", 91, "Denmark"), DMA("DM", 91, "Dominica"),

	DOM("DO", 91, "Dominican Republic"), DZA("DZ", 91, "Algeria"),

	ECU("EC", 91, "Ecuador"), EST("EE", 91, "Estonia"), EGY("EG", 91, "Egypt"),

	ESH("EH", 91, "Western Sahara"), ERI("ER", 91, "Eritrea"), ESP("ES", 91, "Spain"), ETH("ET", 91, "Ethiopia"),

	FIN("FI", 91, "Finland"), FJI("FJ", 91, "Fiji"), FLK("FK", 91, "Falkland Islands"),

	FSM("FM", 91, "Micronesia"), FRO("FO", 91, "Faroe Islands"), FRA("FR", 91, "France"),

	GAB("GA", 91, "Gabon"), GBR("GB", 91, "United Kingdom"), GRD("GD", 91, "Grenada"), GEO("GE", 91, "Georgia"),

	GUF("GF", 91, "French Guiana"), GGY("GG", 91, "Guernsey"), GHA("GH", 91, "Ghana"), GIB("GI", 91, "Gibraltar"),

	GRL("GL", 91, "Greenland"), GMB("GM", 91, "Gambia"), GIN("GN", 91, "Guinea"), GLP("GP", 91, "Guadeloupe"),

	GNQ("GQ", 91, "Equatorial Guinea"), GRC("GR", 91, "Greece"),

	SGS("GS", 91, "South Georgia And The South Sandwich Islands"), GTM("GT", 91, "Guatemala"),

	GUM("GU", 91, "Guam"), GNB("GW", 91, "Guinea-Bissau"), GUY("GY", 91, "Guyana"),

	HKG("HK", 91, "Hong Kong"), HMD("HM", 91, "Heard Island And McDonald Islands"), HND("HN", 91, "Honduras"),

	HRV("HR", 91, "Croatia"), HTI("HT", 91, "Haiti"), HUN("HU", 91, "Hungary"),

	IDN("ID", 91, "Indonesia"), IRL("IE", 91, "Ireland"), ISR("IL", 91, "Israel"), IMN("IM", 91, "Isle Of Man"),

	IND("IN", 91, "India"), IOT("IO", 91, "British Indian Ocean Territory"), IRQ("IQ", 91, "Iraq"),

	IRN("IR", 91, "Iran"), ISL("IS", 91, "Iceland"), ITA("IT", 91, "Italy"),

	JEY("JE", 91, "Jersey"), JAM("JM", 91, "Jamaica"), JOR("JO", 91, "Jordan"), JPN("JP", 91, "Japan"),

	KEN("KE", 91, "Kenya"), KGZ("KG", 91, "Kyrgyzstan"), KHM("KH", 91, "Cambodia"), KIR("KI", 91, "Kiribati"),

	COM("KM", 91, "Comoros"), KNA("KN", 91, "Saint Kitts And Nevis"), PRK("KP", 91, "North Korea"),

	KOR("KR", 91, "South Korea"), KWT("KW", 91, "Kuwait", true), CYM("KY", 91, "Cayman Islands"), KAZ("KZ", 91,
			"Kazakhstan"),

	LAO("LA", 91, "Laos"), LBN("LB", 91, "Lebanon"), LCA("LC", 91, "Saint Lucia"), LIE("LI", 91, "Liechtenstein"),

	LKA("LK", 91, "Sri Lanka"), LBR("LR", 91, "Liberia"), LSO("LS", 91, "Lesotho"),

	LTU("LT", 91, "Lithuania"), LUX("LU", 91, "Luxembourg"), LVA("LV", 91, "Latvia"), LBY("LY", 91, "Libya"),

	MAR("MA", 91, "Morocco"), MCO("MC", 91, "Monaco"), MDA("MD", 91, "Moldova"), MNE("ME", 91, "Montenegro"),

	MAF("MF", 91, "Saint Martin"), MDG("MG", 91, "Madagascar"), MHL("MH", 91, "Marshall Islands"),

	MKD("MK", 91, "Macedonia"), MLI("ML", 91, "Mali"), MMR("MM", 91, "Myanmar"), MNG("MN", 91, "Mongolia"),

	MAC("MO", 91, "Macao"), MNP("MP", 91, "Northern Mariana Islands"), MTQ("MQ", 91, "Martinique"),

	MRT("MR", 91, "Mauritania"), MSR("MS", 91, "Montserrat"), MLT("MT", 91, "Malta"), MUS("MU", 91, "Mauritius"),

	MDV("MV", 91, "Maldives"), MWI("MW", 91, "Malawi"), MEX("MX", 91, "Mexico"), MYS("MY", 91, "Malaysia"),

	MOZ("MZ", 91, "Mozambique"),

	NAM("NA", 91, "Namibia"), NCL("NC", 91, "New Caledonia"), NER("NE", 91, "Niger"),

	NFK("NF", 91, "Norfolk Island"), NGA("NG", 91, "Nigeria"), NIC("NI", 91, "Nicaragua"),

	NLD("NL", 91, "Netherlands"), NOR("NO", 91, "Norway"), NPL("NP", 91, "Nepal"), NRU("NR", 91, "Nauru"),

	NIU("NU", 91, "Niue"), NZL("NZ", 91, "New Zealand"),

	OMN("OM", 82, "Oman", true),

	PAN("PA", 91, "Panama"), PER("PE", 91, "Peru"), PYF("PF", 91, "French Polynesia"),

	PNG("PG", 91, "Papua New Guinea"), PHL("PH", 91, "Philippines"), PAK("PK", 91, "Pakistan"),

	POL("PL", 91, "Poland"), SPM("PM", 91, "Saint Pierre And Miquelon"), PCN("PN", 91, "Pitcairn"),

	PRI("PR", 91, "Puerto Rico"), PSE("PS", 91, "Palestine"), PRT("PT", 91, "Portugal"), PLW("PW", 91,
			"Palau"),
	PRY("PY", 91, "Paraguay"),

	QAT("QA", 91, "Qatar"),

	REU("RE", 91, "Reunion"), ROU("RO", 91, "Romania"), SRB("RS", 91, "Serbia"), RUS("RU", 91, "Russia"),

	RWA("RW", 91, "Rwanda"),

	SAU("SA", 91, "Saudi Arabia"), SLB("SB", 91, "Solomon Islands"), SYC("SC", 91, "Seychelles"),

	SDN("SD", 91, "Sudan"), SWE("SE", 91, "Sweden"), SGP("SG", 91, "Singapore"), SHN("SH", 91, "Saint Helena"),

	SVN("SI", 91, "Slovenia"), SJM("SJ", 91, "Svalbard And Jan Mayen"), SVK("SK", 91, "Slovakia"),

	SLE("SL", 91, "Sierra Leone"), SMR("SM", 91, "San Marino"), SEN("SN", 91, "Senegal"), SOM("SO", 91, "Somalia"),

	SUR("SR", 91, "Suriname"), SSD("SS", 91, "South Sudan"), STP("ST", 91, "Sao Tome And Principe"),

	SLV("SV", 91, "El Salvador"), SXM("SX", 91, "Sint Maarten (Dutch part)"), SYR("SY", 91, "Syria"),

	SWZ("SZ", 91, "Swaziland"),

	TCA("TC", 91, "Turks And Caicos Islands"), TCD("TD", 91, "Chad"), ATF("TF", 91, "French Southern Territories"),

	TGO("TG", 91, "Togo"), THA("TH", 91, "Thailand"), TJK("TJ", 91, "Tajikistan"), TKL("TK", 91, "Tokelau"),

	TLS("TL", 91, "Timor-Leste"), TKM("TM", 91, "Turkmenistan"), TUN("TN", 91, "Tunisia"), TON("TO", 91, "Tonga"),

	TUR("TR", 91, "Turkey"), TTO("TT", 91, "Trinidad and Tobago"), TUV("TV", 91, "Tuvalu"),

	TWN("TW", 91, "Taiwan"), TZA("TZ", 91, "Tanzania"),

	UKR("UA", 91, "Ukraine"), UGA("UG", 91, "Uganda"), UMI("UM", 91, "United States Minor Outlying Islands"),

	USA("US", 91, "United States"), URY("UY", 91, "Uruguay"), UZB("UZ", 91, "Uzbekistan"),

	VAT("VA", 91, "Vatican"), VCT("VC", 91, "Saint Vincent And The Grenadines"), VEN("VE", 91, "Venezuela"),

	VGB("VG", 91, "British Virgin Islands"), VIR("VI", 91, "U.S. Virgin Islands"), VNM("VN", 91, "Vietnam"),

	VUT("VU", 91, "Vanuatu"),

	WLF("WF", 91, "Wallis And Futuna"), WSM("WS", 91, "Samoa"),

	YEM("YE", 91, "Yemen"), MYT("YT", 91, "Mayotte"),

	ZAF("ZA", 91, "South Africa"), ZMB("ZM", 91, "Zambia"), ZWE("ZW", 91, "Zimbabwe"),

	NONE("none", 0, null);

	public static Countries DEFAULT = KWT;

	public static Map<String, Countries> mapping = new HashMap<String, Countries>();
	public static final Pattern pattern = Pattern.compile("^(.+?)-(.+?)$");

	static {
		// Additional Mappings
		// mapping.put("app-devq", KWT2);
		for (Countries site : Countries.values()) {
			mapping.put(site.toString().toLowerCase(), site);
			mapping.put(site.getId().toLowerCase(), site);
			mapping.put("app-" + site.toString().toLowerCase(), site);
		}

	}

	private String id;
	private String code;

	Countries(String id, int code, String name) {
		this.id = id;
		this.code = ArgUtil.parseAsString(code);
	}

	Countries(String id, int code, String name, boolean isTenantApp) {
		this.id = id;
		this.code = ArgUtil.parseAsString(code);
	}

	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public BigDecimal getBDCode() {
		return new BigDecimal(code);
	}

	public static Countries fromString(String siteId, Countries defaultValue) {
		if (siteId != null) {
			String siteIdStr = siteId.toLowerCase();
			Matcher matcher = pattern.matcher(siteIdStr);
			if (matcher.find()) {
				siteIdStr = matcher.group(2);
			}

			if (mapping.containsKey(siteIdStr)) {
				return mapping.get(siteIdStr);
			}
			for (Countries site : Countries.values()) {
				if (site.toString().equalsIgnoreCase(siteIdStr)) {
					return site;
				}
			}
		}
		return defaultValue;
	}

}