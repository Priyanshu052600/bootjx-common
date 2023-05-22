package com.boot.jx.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.boot.model.UtilityModels.JsonIgnoreUnknown;
import com.boot.utils.ArgUtil;

public class CommonTemplateMeta implements Serializable, JsonIgnoreUnknown {

	private static final long serialVersionUID = -77493889473952472L;

	private String id;

	private String code;

	private String lang;

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