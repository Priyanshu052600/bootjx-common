package com.boot.jx.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.boot.jx.swagger.ApiMockModelProperty;
import com.boot.model.UtilityModels.JsonIgnoreUnknown;
import com.boot.utils.ArgUtil;

public class CommonTemplateMeta implements Serializable, JsonIgnoreUnknown {

	private static final long serialVersionUID = -77493889473952472L;

	@ApiMockModelProperty(example = "60d236c6142e53561cb7716c", required = false, value = "Unique Template Id",
			notes = "Explicit template.id to be used for message")
	private String id;

	@ApiMockModelProperty(example = "FEEDBACK", required = false, value = "Code of Template",
			notes = "Template Code will be searched in the repository and match will be served."
					+ "\n code will be ignored in case template.id is provided")
	private String code;

	@ApiMockModelProperty(example = "en_US", required = false, value = "Language of Template to pick",
			notes = "Language is an optional param which fallback to en, in all the scenarios of missing params/template"
					+ "\n lang will ignored when template.id is provided")
	private String lang;

	@ApiMockModelProperty(example = "{ \"amount\" : 10, \"currency\" : \"INR\" }", required = false,
			value = "Data will be used to resolve placeholders in template, in case of missing value blank will be attempted, "
					+ "\n Kindly note Template may be rejected in case it does not match the approved format")
	private Map<String, Object> data;

	private String linked;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String toString() {
		return String.format("%s_%s(%s)", code, lang, id);
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public Map<String, Object> data() {
		if (!ArgUtil.is(data)) {
			this.data = new HashMap<String, Object>();
		}
		return data;
	}

	public CommonTemplateMeta id(String id) {
		this.id = id;
		return this;
	}

	public CommonTemplateMeta code(String code) {
		this.code = code;
		return this;
	}

	public CommonTemplateMeta lang(String lang) {
		this.lang = lang;
		return this;
	}

	public CommonTemplateMeta data(Map<String, Object> data) {
		this.data = data;
		return this;
	}

	public String getLinked() {
		return linked;
	}

	public void setLinked(String linked) {
		this.linked = linked;
	}

	public CommonTemplateMeta linked(String linked) {
		this.linked = linked;
		return this;
	}

}