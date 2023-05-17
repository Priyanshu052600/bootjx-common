package com.boot.jx.dict;

public enum ContactType {
	SMS("sms", "Mobile No.") {
		@Override
		public boolean isPhoneIndex() {
			return true;
		}
	},
	EMAIL("email", "Email Id") {
		@Override
		public boolean isEmailIndex() {
			return true;
		}
	},

	WHATSAPP("wa", "WhatsApp No.") {
		@Override
		public boolean isPhoneIndex() {
			return true;
		}
	},

	PUSH("push"), FACEBOOK("fb", "Facebook Id"),

	TELEGRAM("tg", "Telegram No.") {
		@Override
		public boolean isPhoneIndex() {
			return true;
		}
	},

	WEBSITE("web"), TWITTER("tw", "Twitter"),
	INSTAGRAM("ig","Instagram"),
	
	// Default Null Value
	DUMMY("dummy"), EMPTY(""),
	FBPUSH("push");

	ContactType contactType;
	String shortCode;
	private String label;

	ContactType(String shortCode) {
		this.contactType = this;
		this.shortCode = shortCode;
		this.label = null;
	}

	ContactType(String shortCode, String label) {
		this.contactType = this;
		this.shortCode = shortCode;
		this.label = label;
	}

	ContactType(ContactType contactType) {
		this.contactType = contactType;
		this.shortCode = contactType.getShortCode();
	}

	@Override
	public String toString() {
		return this.contactType.name();
	}

	public ContactType contactType() {
		return this.contactType;
	}

	public String getShortCode() {
		return shortCode;
	}

	public String getLabel() {
		return label;
	}

	public boolean isPhoneIndex() {
		return false;
	}

	public boolean isEmailIndex() {
		return false;
	}
}
