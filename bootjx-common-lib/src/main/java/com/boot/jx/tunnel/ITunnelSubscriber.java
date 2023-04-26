package com.boot.jx.tunnel;

public interface ITunnelSubscriber<M> {

	void onMessage(String channel, M message);

	default String getTopic() {
		return null;
	};

}
