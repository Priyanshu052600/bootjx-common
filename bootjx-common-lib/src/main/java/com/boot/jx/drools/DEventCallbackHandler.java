package com.boot.jx.drools;

public interface DEventCallbackHandler<T extends DEvent> {

	public void beforeEventCallback(T event);

	public void onEventCallback(T event);

	public void afterEventCallback(T event);

}
