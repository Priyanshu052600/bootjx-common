package com.boot.jx.http;

import com.boot.jx.logger.client.AuditServiceClient;

public enum RequestType {

	DEFAULT(true, true), POLL(false, false), PING(true, false), PUBG(true, false),

	NO_TRACK_PING(false, false), STOMP(false, false);

	boolean track = false;
	boolean auth = true;

	RequestType(boolean track, boolean auth) {
		this.track = track;
		this.auth = auth;
	}

	public boolean isTrack() {
		return track || AuditServiceClient.isDebugEnabled();
	}

	public boolean isDebugOnly() {
		return !track;
	}

	public boolean isAuth() {
		return auth;
	}

}