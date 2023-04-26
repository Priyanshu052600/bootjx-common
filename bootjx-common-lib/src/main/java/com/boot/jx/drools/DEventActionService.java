package com.boot.jx.drools;

public interface DEventActionService {

	public static class Path {
		public static String BEFORE_EVENT_PATH = "/event/BEFORE/{eventName}";
		public static String ON_EVENT_PATH = "/event/ON/{eventName}";
		public static String AFTER_EVENT_PATH = "/event/AFTER/{eventName}";
	}

	public static class Param {
		public static String EVENT_NAME = "eventName";
	}

	public <T extends DEvent> void beforeEvent(String eventName, T event);

	public <T extends DEvent> void onEvent(String eventName, T event);

	public <T extends DEvent> void afterEvent(String eventName, T event);
}
