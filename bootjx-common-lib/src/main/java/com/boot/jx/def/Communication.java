package com.boot.jx.def;

import com.boot.utils.EnumType;

public class Communication {
	public static interface CommunicationEvent extends EnumType {

	}

	public static class CommunicationEventModel implements CommunicationEvent {

		private String name;

		public CommunicationEventModel(String name) {
			this.name = name;
		}

		@Override
		public String name() {
			return this.name;
		}

	}
}
