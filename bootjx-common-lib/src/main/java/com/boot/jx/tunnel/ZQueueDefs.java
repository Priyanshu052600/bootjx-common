package com.boot.jx.tunnel;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

public class ZQueueDefs {
	public interface ZQueueElement {

		String getQueueType();

		String getQueueId();

		String getItemId();

		long getItemOrder();

		void setQueueType(String queueType);

		void setQueueId(String queueId);

		void setItemId(String itemId);

		void setItemOrder(long itemOrder);

		void setItem(Map<String, Object> item);

		Map<String, Object> getItem();

		default ZQueueElement queueType(String queueType) {
			this.setQueueType(queueType);
			return this;
		}

		default ZQueueElement queueId(String queueId) {
			this.setQueueId(queueId);
			return this;
		}

		default ZQueueElement itemId(String itemId) {
			this.setItemId(itemId);
			return this;
		}

		default ZQueueElement itemOrder(long itemOrder) {
			this.setItemOrder(itemOrder);
			return this;
		}

		default ZQueueElement item(Map<String, Object> item) {
			this.setItem(item);
			return this;
		}

	}

	public interface ZQueue {
		public void push(ZQueueElement element);
	}

	public interface ZQueueStore {
		public void enqueue(ZQueueElement element);

		public ZQueueElement dequeue(String queueType, String queueId);

		public List<? extends ZQueueElement> dequeueAll(String queueType, String queueId);
	}

	public static interface Zqueuelizer {
		default public void zqueuelized(ZQueueElement element) {
		}
	}

	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Controller
	public @interface Zqueuelized {
		/**
		 * Type of Queue. This is for the categorization, executions will happen per
		 * queueId,
		 * 
		 * @return
		 */
		String value();

		/**
		 * Minimum delay or gap between two consecutive executions. (in milli-seconds)
		 * 
		 * @return
		 */
		long delay() default 500L;
	}

	public static class ZQMethodWrapper implements Serializable {
		private static final long serialVersionUID = 7112166704689180115L;
		private String controller;
		private Method method;
		private long delay;

		public String getController() {
			return controller;
		}

		public void setController(String controller) {
			this.controller = controller;
		}

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}

		public long getDelay() {
			return delay;
		}

		public void setDelay(long delay) {
			this.delay = delay;
		}

	}
}
