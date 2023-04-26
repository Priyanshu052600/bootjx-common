package com.boot.jx.dict;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.boot.utils.ArgUtil;
import com.boot.utils.EnumType;

public enum Language implements EnumType {

	AB("abk", 100, "Abkhazian"),
	AE("ave", 100, "Avestan"),
	AF("afr", 100, "Afrikaans"),
	AK("aka", 100, "Akan"),
	AM("amh", 100, "Amharic"),
	AN("arg", 100, "Aragonese"),
	AR("ara", 2, "Arabic"),
	AS("asm", 100, "Assamese"),
	AV("ava", 100, "Avaric"),
	AY("aym", 100, "Aymara"),
	AZ("aze", 100, "Azerbaijani"),

	BA("bak", 100, "Bashkir"),
	BE("bel", 100, "Belarusian"),
	BG("bul", 100, "Bulgarian"),
	BH("bih", 100, "Bihari"),
	BI("bis", 100, "Bislama"),

	BM("bam", 100, "Bambara"),

	BN("ben", 10, "Bengali"),
	BO("bod", 100, "Tibetan"),
	BR("bre", 100, "Breton"),
	BS("bos", 100, "Bosnian"),

	CA("cat", 100, "Catalan"),
	CE("che", 100, "Chechen"),
	CH("cha", 100, "Chamorro"),
	CO("cos", 100, "Corsican"),

	CR("cre", 100, "Cree"),
	CS("ces", 100, "Czech"),
	CU("chu", 100, "Church Slavic"),
	CV("chv", 100, "Chuvash"),
	CY("cym", 100, "Welsh"),

	DA("dan", 100, "Danish"),
	DE("deu", 100, "German"),
	DV("div", 100, "Divehi"),
	DZ("dzo", 100, "Dzongkha"),

	EE("ewe", 100, "Ewe"),
	EL("ell", 100, "Greek"),
	EN("eng", 1, "English"),
	EO("epo", 100, "Esperanto"),
	ES("spa", 100, "Spanish"),

	ET("est", 100, "Estonian"),
	EU("eus", 100, "Basque"),

	FA("fas", 100, "Persian"),
	FF("ful", 100, "Fulah"),
	FI("fin", 100, "Finnish"),
	FJ("fij", 100, "Fijian"),
	FO("fao", 100, "Faroese"),

	FR("fra", 100, "French"),
	FY("fry", 100, "Frisian"),

	GA("gle", 100, "Irish"),
	GD("gla", 100, "Scottish Gaelic"),
	GL("glg", 100, "Gallegan"),
	GN("grn", 100, "Guarani"),
	GU("guj", 6, "Gujarati"),

	GV("glv", 100, "Manx"),

	HA("hau", 100, "Hausa"),
	HI("hin", 3, "Hindi"),
	HO("hmo", 100, "Hiri Motu"),
	HR("hrv", 100, "Croatian"),
	HT("hat", 100, "Haitian"),

	HU("hun", 100, "Hungarian"),
	HY("hye", 100, "Armenian"),
	HZ("her", 100, "Herero"),

	IA("ina", 100, "Interlingua"),
	IN("ind", 100, "Indonesian"),
	IE("ile", 100, "Interlingue"),
	IG("ibo", 100, "Igbo"),

	II("iii", 100, "Sichuan Yi"),
	IK("ipk", 100, "Inupiaq"),
	IO("ido", 100, "Ido"),

	IS("isl", 100, "Icelandic"),
	IT("ita", 100, "Italian"),
	IU("iku", 100, "Inuktitut"),
	IW("heb", 100, "Hebrew"),

	JA("jpn", 100, "Japanese"),
	JI("yid", 100, "Yiddish"),
	JV("jav", 100, "Javanese"),

	KA("kat", 100, "Georgian"),
	KG("kon", 100, "Kongo"),
	KI("kik", 100, "Kikuyu"),
	KJ("kua", 100, "Kwanyama"),

	KK("kaz", 100, "Kazakh"),
	KL("kal", 100, "Greenlandic"),
	KM("khm", 100, "Khmer"),
	KN("kan", 100, "Kannada"),

	KO("kor", 100, "Korean"),
	KR("kau", 100, "Kanuri"),
	KS("kas", 100, "Kashmiri"),
	KU("kur", 100, "Kurdish"),

	KV("kom", 100, "Komi"),
	KW("cor", 100, "Cornish"),
	KY("kir", 100, "Kirghiz"),

	LA("lat", 100, "Latin"),
	LB("ltz", 100, "Luxembourgish"),
	LG("lug", 100, "Ganda"),
	LI("lim", 100, "Limburgish"),

	LN("lin", 100, "Lingala"),
	LO("lao", 100, "Lao"),
	LT("lit", 100, "Lithuanian"),
	LU("lub", 100, "Luba-Katanga"),

	LV("lav", 100, "Latvian"),

	MG("mlg", 100, "Malagasy"),
	MH("mah", 100, "Marshallese"),
	MI("mri", 100, "Maori"),
	MK("mkd", 100, "Macedonian"),

	ML("mal", 5, "Malayalam"),
	MN("mon", 100, "Mongolian"),
	MO("mol", 100, "Moldavian"),
	MR("mar", 4, "Marathi"),

	MS("msa", 100, "Malay"),
	MT("mlt", 100, "Maltese"),
	MY("mya", 100, "Burmese"),

	NA("nau", 100, "Nauru"),
	NB("nob", 100, "Norwegian Bokmål"),
	ND("nde", 100, "North Ndebele"),
	NE("nep", 100, "Nepali"),

	NG("ndo", 100, "Ndonga"),
	NL("nld", 100, "Dutch"),
	NN("nno", 100, "Norwegian Nynorsk"),
	NO("nor", 100, "Norwegian"),

	NR("nbl", 100, "South Ndebele"),
	NV("nav", 100, "Navajo"),
	NY("nya", 100, "Nyanja"),

	OC("oci", 100, "Occitan"),
	OJ("oji", 100, "Ojibwa"),
	OM("orm", 100, "Oromo"),
	OR("ori", 100, "Oriya"),
	OS("oss", 100, "Ossetian"),

	PA("pan", 100, "Panjabi"),
	PI("pli", 100, "Pali"),
	PL("pol", 100, "Polish"),
	PS("pus", 100, "Pushto"),
	PT("por", 100, "Portuguese"),

	QU("que", 100, "Quechua"),
	RM("roh", 100, "Raeto-Romance"),
	RN("run", 100, "Rundi"),
	RO("ron", 100, "Romanian"),
	RU("rus", 100, "Russian"),

	RW("kin", 100, "Kinyarwanda"),

	SA("san", 100, "Sanskrit"),
	SC("srd", 100, "Sardinian"),
	SD("snd", 100, "Sindhi"),
	SE("sme", 100, "Northern Sami"),

	SG("sag", 100, "Sango"),
	SI("sin", 9, "Sinhalese"),
	SK("slk", 100, "Slovak"),
	SL("slv", 100, "Slovenian"),

	SM("smo", 100, "Samoan"),
	SN("sna", 100, "Shona"),
	SO("som", 100, "Somali"),
	SQ("sqi", 100, "Albanian"),
	SR("srp", 100, "Serbian"),

	SS("ssw", 100, "Swati"),
	ST("sot", 100, "Southern Sotho"),

	SU("sun", 100, "Sundanese"),
	SV("swe", 100, "Swedish"),
	SW("swa", 100, "Swahili"),

	TA("tam", 7, "Tamil"),
	TE("tel", 11, "Telugu"),
	TG("tgk", 100, "Tajik"),
	TH("tha", 100, "Thai"),
	TI("tir", 100, "Tigrinya"),

	TK("tuk", 100, "Turkmen"),
	TL("tgl", 8, "Tagalog"), PH(TL),
	TN("tsn", 100, "Tswana"),

	TO("ton", 100, "Tonga"),
	TR("tur", 100, "Turkish"),
	TS("tso", 100, "Tsonga"),
	TT("tat", 100, "Tatar"),
	TW("twi", 100, "Twi"),

	TY("tah", 100, "Tahitian"),

	UG("uig", 100, "Uighur"),
	UK("ukr", 100, "Ukrainian"),
	UR("urd", 100, "Urdu"),
	UZ("uzb", 100, "Uzbek"),

	VE("ven", 100, "Venda"),
	VI("vie", 100, "Vietnamese"),
	VO("vol", 100, "Volapük"),

	WA("wln", 100, "Walloon"),
	WO("wol", 100, "Wolof"),

	XH("xho", 100, "Xhosa"),
	YO("yor", 100, "Yoruba"),
	ZA("zha", 100, "Zhuang"),
	ZH("zho", 100, "Chinese"),
	ZU("zul", 100, "Zulu");

	public static final Map<Integer, Language> MAP = new HashMap<Integer, Language>();

	public static final Language DEFAULT = EN;

	private String iso3code;
	private int id;
	private Language lang;
	private String label;

	Language(String iso3code, int id, String label) {
		this.id = id;
		this.iso3code = iso3code;
		this.lang = this;
		this.label = label;
	}

	Language(Language lang) {
		this.lang = lang;
		if (lang != null) {
			this.id = lang.id;
			this.iso3code = lang.iso3code;
		}
	}

	public String getISO3Code() {
		return iso3code;
	}

	public int getId() {
		return id;
	}

	public String getCode() {
		return this.toString();
	}

	public BigDecimal getBDCode() {
		return new BigDecimal(id);
	}

	public Language getLang() {
		return lang;
	}

	public String stringValue() {
		return this.iso3code;
	}

	public static Language fromId(BigDecimal id) {
		if (id == null) {
			return null;
		}
		return MAP.get(id.intValue());
	}

	public static Language fromId(Integer id) {
		if (id == null) {
			return null;
		}
		return MAP.get(id);
	}

	public static String toString(Language lang, Language deflang) {
		if (lang != null) {
			return lang.toString();
		} else if (deflang != null) {
			return deflang.toString();
		}
		return null;
	}

	public static String toString(Language lang, String defString) {
		if (lang != null) {
			return lang.toString();
		}
		return defString;
	}

	public static Language fromString(String lang, Language defLang) {
		Language x = (Language) ArgUtil.parseAsEnum(lang, defLang, Language.class);
		if (x == null) {
			return null;
		}
		return x.getLang();
	}

	public static Language fromString(String lang) {
		return fromString(lang, Language.EN);
	}

	static {
		for (Language site : Language.values()) {
			MAP.put(site.getId(), site.getLang());
		}
	}

	public String getLabel() {
		return label;
	}

}
