package com.boot.jx.tunnel;

import java.io.Serializable;
import java.util.Map;
import java.util.Queue;

import com.boot.model.MapModel;

public class ITunnelDefs {
	public interface TunnelQueue<T> extends Queue<T> {
	}

	public interface ITaskLimiter {
		Map<String, Object> getStats();

		String getName();

		void doTask(int pollQNum, int pushQNum, int pushQ10Num, int batchSize);

		public boolean doTask(TunnelTask task);

		void debounce(TunnelTask task);

		void throttle(TunnelTask task);
	}

	public static class TunnelTask implements ITunnelEvent {
		private static final long serialVersionUID = 7707873093764252510L;
		private String name;
		private String id;
		private long interval;
		private long matureStamp;

		private Map<String, Object> data;

		public Map<String, Object> getData() {
			return data;
		}

		public void setData(Map<String, Object> data) {
			this.data = data;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		/**
		 * 
		 * @return interval - milliseconds
		 */
		public long getInterval() {
			return interval;
		}

		/**
		 * 
		 * @param interval - milliseconds
		 */
		public void setInterval(long interval) {
			this.interval = interval;
		}

		/**
		 * 
		 * @param seconds
		 * @return
		 */
		public TunnelTask intervalSeconds(long seconds) {
			this.setInterval(seconds * 1000L);
			return this;
		}

		public TunnelTask intervalMinutes(long minutes) {
			this.setInterval(minutes * 60 * 1000L);
			return this;
		}

		public TunnelTask intervalMillis(long milliseconds) {
			this.setInterval(milliseconds);
			return this;
		}

		public TunnelTask id(String id) {
			this.setId(id);
			return this;
		}

		public TunnelTask name(String name) {
			this.setName(name);
			return this;
		}

		public TunnelTask data(MapModel data) {
			this.data = data.map();
			return this;
		}

		public MapModel data() {
			MapModel x = MapModel.from(this.data);
			if (this.data == null) {
				this.data = x.map();
			}
			return x;
		}

		public long getMatureStamp() {
			return matureStamp;
		}

		public void setMatureStamp(long matureStamp) {
			this.matureStamp = matureStamp;
		}

	}

	public static class TaskInfo implements Serializable {
		private static final long serialVersionUID = -8230113556466236531L;
		long timestamp;
		long matureStamp;
		long interval;
		String key;

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public long getInterval() {
			return interval;
		}

		public void setInterval(long interval) {
			this.interval = interval;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public long getMatureStamp() {
			return matureStamp;
		}

		public void setMatureStamp(long matureStamp) {
			this.matureStamp = matureStamp;
		}

	}

}
