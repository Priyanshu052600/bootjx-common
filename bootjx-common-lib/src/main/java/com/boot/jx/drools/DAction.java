package com.boot.jx.drools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DAction implements Serializable {

	private static final long serialVersionUID = 2732122665485798965L;

	private String action;

	private Map<String, Object> options;
	private List<Map<String, Object>> rules;

	public DAction() {
		super();
		this.options = new HashMap<String, Object>();
		this.rules = new ArrayList<Map<String, Object>>();
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<Map<String, Object>> getRules() {
		return rules;
	}

	public void setRules(List<Map<String, Object>> rules) {
		this.rules = rules;
	}

}
