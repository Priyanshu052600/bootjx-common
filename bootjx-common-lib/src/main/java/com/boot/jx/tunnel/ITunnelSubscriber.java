package com.boot.jx.tunnel;

import com.boot.model.MapModel;
import com.boot.utils.JsonUtil;

public interface ITunnelSubscriber<M> {

	default void onListen(String channel, M message) {
	}

	default void onMessage(String channel, M message) {
		this.onListen(channel, message);
	}

	default String getTopic() {
		return null;
	};

	default void setSampleEvent(M message) {
		// This is only for help
	}

	default public void onSend(M message) {
		System.out.println("send:Nothing to " + JsonUtil.toJson(message));
	}

	default public void onReceive(M message) {
		this.onSend(message);
	}

	default public void onPublish(M message) {
		System.out.println("publish:Nothing to " + JsonUtil.toJson(message));
	}

	default public void onPublishRaw(MapModel data) {
		Class<M> clazz = toClass();
		if (clazz != null) {
			M msgdata = data.as(clazz);
			if (msgdata != null) {
				this.onPublish(msgdata);
			}
		}
	}

	default public Class<M> toClass() {
		return null;
	}

}
