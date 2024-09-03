package com.boot.model;

import java.io.IOException;
import java.io.Serializable;

import com.boot.utils.JsonUtil;
import com.boot.utils.TimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class TimeModels {

	@JsonDeserialize(as = ITimeStampIndexImpl.class, keyUsing = TimeStampIndexKeyDeserializer.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public interface ITimeStampIndex<T extends ITimeStampIndex<T>> extends Serializable {
		public long getStamp();

		void setStamp(long stamp);

		long getHour();

		void setHour(long hour);

		long getDay();

		void setDay(long day);

		long getWeek();

		void setWeek(long week);

		String getByUser();

		void setByUser(String byUser);

		@SuppressWarnings("unchecked")
		public default T fromStamp(long stamp) {
			this.setStamp(stamp);
			this.setHour(stamp / TimeUtils.Constants.MILLIS_IN_HOUR);
			this.setDay(stamp / TimeUtils.Constants.MILLIS_IN_DAY);
			this.setWeek(stamp / TimeUtils.Constants.MILLIS_IN_WEEK);
			return (T) this;
		}

		public default T fromNow() {
			return this.fromStamp(System.currentTimeMillis());
		}

		@SuppressWarnings("unchecked")
		public default T by(String byUser) {
			this.setByUser(byUser);
			return (T) this;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static abstract class ITimeStampIndexAbstract<T extends ITimeStampIndex<T>> implements ITimeStampIndex<T> {
		private static final long serialVersionUID = -923904958433975647L;
		private long stamp;
		private long hour;
		private long day;
		private long week;
		private String byUser;

		public String getByUser() {
			return byUser;
		}

		public void setByUser(String byUser) {
			this.byUser = byUser;
		}

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

	public interface TimeStampUpdatedSupport {
		public ITimeStampIndex<?> getUpdated();

		public void setUpdated(ITimeStampIndex<?> updated);
	}

	public interface TimeStampCreatedSupport {
		public ITimeStampIndex<?> getCreated();

		public void setCreated(ITimeStampIndex<?> created);
	}

	public interface TimeStampSupport extends TimeStampUpdatedSupport, TimeStampCreatedSupport {
	}

	public static class TimeStampSupportedModel implements TimeStampSupport {
		private ITimeStampIndex<?> updated;

		public ITimeStampIndex<?> getUpdated() {
			return updated;
		}

		public void setUpdated(ITimeStampIndex<?> updated) {
			this.updated = updated;
		}

		private ITimeStampIndex<?> created;

		public ITimeStampIndex<?> getCreated() {
			return created;
		}

		public void setCreated(ITimeStampIndex<?> created) {
			this.created = created;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ITimeStampIndexImpl extends ITimeStampIndexAbstract<ITimeStampIndexImpl> {
		private static final long serialVersionUID = 4863858945194917227L;

		public static ITimeStampIndexImpl from(long stamp) {
			return new ITimeStampIndexImpl().fromStamp(stamp);
		}

		public static ITimeStampIndexImpl now() {
			return new ITimeStampIndexImpl().fromNow();
		}
	}

	public static class TimeStampIndexKeyDeserializer extends KeyDeserializer {
		@Override
		public Object deserializeKey(String key, DeserializationContext deserializationContext)
				throws IOException, JsonProcessingException {
			return JsonUtil.getMapper().readValue(key, ITimeStampIndexImpl.class);
		}
	}

	static {
		ObjectMapper objectMapper = JsonUtil.getMapper();
		SimpleModule module = new SimpleModule();
		module.addKeyDeserializer(ITimeStampIndexImpl.class, new TimeStampIndexKeyDeserializer());
		objectMapper.registerModule(module);
	}
}
