package com.boot.jx.def;

import java.util.HashMap;
import java.util.Map;

public interface IndicatorListner {
	public static class GaugeIndicator {
		public static final double UP = 1.0;
		public static final double DOWN = 0.0;

		Map<String, Double> map;

		public GaugeIndicator() {
			super();
			this.map = new HashMap<String, Double>();
		}

		public void set(String key, Double value) {
			map.put(key, value);
		}

		public Map<String, Double> toMap() {
			return map;
		}

	}

	public Map<String, Object> getIndicators(GaugeIndicator gauge);
}
