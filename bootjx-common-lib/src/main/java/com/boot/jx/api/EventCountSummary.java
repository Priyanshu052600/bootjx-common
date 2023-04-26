package com.boot.jx.api;

import java.util.HashMap;
import java.util.Map;

public class EventCountSummary {
	String tenant;
	String month;
	
	Map<Object, Map<Object, Long>> langWiseCountMap=new HashMap<>();
	
	Map<Object, Map<Object, Long>> eventWiseCountMap=new HashMap<>();

	public Map<Object, Map<Object, Long>> getLangWiseCountMap() {
		return langWiseCountMap;
	}

	public void setLangWiseCountMap(Map<Object, Map<Object, Long>> langWiseCountMap) {
		this.langWiseCountMap = langWiseCountMap;
	}

	public Map<Object, Map<Object, Long>> getEventWiseCountMap() {
		return eventWiseCountMap;
	}

	public void setEventWiseCountMap(Map<Object, Map<Object, Long>> eventWiseCountMap) {
		this.eventWiseCountMap = eventWiseCountMap;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	
	
}
