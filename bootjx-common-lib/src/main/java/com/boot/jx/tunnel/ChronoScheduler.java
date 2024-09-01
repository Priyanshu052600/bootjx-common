package com.boot.jx.tunnel;

import java.util.HashMap;
import java.util.Map;

import com.boot.jx.tunnel.ITunnelDefs.ITunnelEvent;
import com.boot.model.MapModel;
import com.boot.model.UtilityModels.JsonIgnoreNull;
import com.boot.model.UtilityModels.JsonIgnoreUnknown;
import com.boot.utils.JsonUtil;

public class ChronoScheduler implements ITunnelEvent, JsonIgnoreNull, JsonIgnoreUnknown {
	private static final long serialVersionUID = 1L;

	protected String topic;
	protected String startAt;
	protected boolean repeat;
	protected String interval;
	protected long repeatCount;
	protected String endAt;

	protected Map<String, Object> data;

	protected String taskId;

	public MapModel data() {
		if (this.data == null) {
			this.data = new HashMap<String, Object>();
		}
		return new MapModel(data);
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getStartAt() {
		return startAt;
	}

	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public long getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(long repeatCount) {
		this.repeatCount = repeatCount;
	}

	public String getEndAt() {
		return endAt;
	}

	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public static class ChronoTaskChained<T> extends ChronoScheduler {
		private static final long serialVersionUID = 5621731217111619405L;

		@SuppressWarnings("unchecked")
		public T data(String key, Object value) {
			this.data().put(key, value);
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		public T data(Object anyPojo) {
			this.data = JsonUtil.toJsonMap(anyPojo);
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		public T topic(String topic) {
			this.topic = topic;
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		public T startAt(String startAt) {
			this.startAt = startAt;
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		public T repeat(boolean repeat) {
			this.repeat = repeat;
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		public T interval(String interval) {
			this.interval = interval;
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		public T repeatCount(long repeatCount) {
			this.repeatCount = repeatCount;
			return (T) this;
		}

		@SuppressWarnings("unchecked")
		public T endAt(String endAt) {
			this.endAt = endAt;
			return (T) this;
		}
	}

	public static class ChronoTaskEvent extends ChronoTaskChained<ChronoTaskEvent> {
		private static final long serialVersionUID = 5621731217111619405L;

	}

	public static ChronoTaskEvent task(String topic) {
		return new ChronoTaskEvent().topic(topic);
	}

	public static ChronoTaskEvent task() {
		return new ChronoTaskEvent();
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}