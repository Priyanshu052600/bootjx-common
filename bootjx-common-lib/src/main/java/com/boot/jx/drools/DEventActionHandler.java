package com.boot.jx.drools;

public interface DEventActionHandler<T extends DEvent> {
	public void handle(T event, DAction action);
}
