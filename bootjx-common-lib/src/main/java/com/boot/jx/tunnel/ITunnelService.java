package com.boot.jx.tunnel;

import com.boot.jx.AppContext;
import com.boot.jx.tunnel.ITunnelDefs.TunnelQueue;

public interface ITunnelService {
	public <T> long shout(String topic, T messagePayload);

	public <T> long audit(String topic, T messagePayload);

	public <T> long send(String topic, T messagePayload);

	public <E extends ITunnelEvent> long shout(E event);

	public <E extends ITunnelEvent> long task(E event);

	public <T> long task(String topic, T messagePayload);

	public <T> TunnelQueue<T> getQueue(String queueName);

	public <T> void taskEnd(String topic, T messagePayload, Thread parentThread) throws InterruptedException;

	public <T> long taskPublish(String topic, T messagePayload, AppContext context);

}
