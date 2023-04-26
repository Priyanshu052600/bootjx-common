package com.boot.model;

import java.io.IOException;
import java.io.Serializable;

import com.boot.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TimeModels {

	@JsonDeserialize(as = ITimeStampIndexImpl.class, keyUsing = TimeStampIndexKeyDeserializer.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public interface ITimeStampIndex extends Serializable {

		public long getStamp();

		void setStamp(long stamp);

		long getHour();

		void setHour(long hour);

		long getDay();

		void setDay(long day);

		long getWeek();

		void setWeek(long week);

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ITimeStampIndexImpl implements ITimeStampIndex {

		private static final long serialVersionUID = -923904958433975647L;
		private long stamp;
		private long hour;
		private long day;
		private long week;

		public long getStamp() {
			return stamp;
		}

		public void setStamp(long stamp) {
			this.stamp = stamp;
		}

		public long getHour() {
			return hour;
		}

		public void setHour(long hour) {
			this.hour = hour;
		}

		public long getDay() {
			return day;
		}

		public void setDay(long day) {
			this.day = day;
		}

		public long getWeek() {
			return week;
		}

		public void setWeek(long week) {
			this.week = week;
		}
	}

	public static class TimeStampIndexKeyDeserializer extends KeyDeserializer {
		@Override
		public Object deserializeKey(String key, DeserializationContext deserializationContext)
				throws IOException, JsonProcessingException {
			return JsonUtil.getMapper().readValue(key, ITimeStampIndexImpl.class);
		}
	}
}
