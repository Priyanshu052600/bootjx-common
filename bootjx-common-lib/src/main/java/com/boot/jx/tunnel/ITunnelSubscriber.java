package com.boot.jx.tunnel;

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

	default public void onPush(M message) {
		System.out.println("poll:Nothing to " + JsonUtil.toJson(message));
	}

}
