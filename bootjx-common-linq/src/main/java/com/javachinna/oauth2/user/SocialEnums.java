package com.javachinna.oauth2.user;

import com.boot.utils.ArgUtil;

public class SocialEnums {
	public static enum ChannelProvider {

		LINKEDIN("linkedin"), FACEBOOK("facebook"), TWITTER("twitter"), GOOGLE("google"), GITHUB("github"),
		EMAIL("email"), MOBILE("mobile"), WHATSAPP("whatsapp"), TELEGRAM("telegram"), OUTLOOK("outlook"),APPLE("apple");

		private String type;

		public String getType() {
			return type;
		}

		public boolean is(String providerTypeTemp) {
			return this.type.equalsIgnoreCase(providerTypeTemp);
		}

		ChannelProvider(final String providerType) {
			this.type = providerType;
		}

	}

	public static enum ChannelPartner {
		ANY, FIREBASE, OTPLESS, TRUECALLER;

		public boolean is(ChannelPartner partner) {
			return this.equals(partner);
		}

		public boolean is(String partner) {
			return this.name().equalsIgnoreCase(partner);
		}
	}

	public static String contactId(BasicOAuth2UserInfo info) {
		ChannelProvider provider = ArgUtil.parseAsEnumT(info.getProvider(), ChannelProvider.class);
		switch (provider) {
		case EMAIL:
		case GOOGLE:
		case OUTLOOK:
			return "mailto:" + info.getEmail();
		case MOBILE:
			return "tel:" + info.getPhone();
		default:
			return String.format("%s:%s", info.getProvider(), info.getProfileId()).toLowerCase();
		}
	}

}
