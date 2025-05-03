package com.javachinna.oauth2.user;

import com.boot.utils.ArgUtil;
import com.javachinna.oauth2.user.SocialEnums.ChannelProvider;

public class SocialEnums {

	public static enum ChannelPartner {
		ANY, FIREBASE, OTPLESS, TRUECALLER, WABA, TQ;

		public boolean is(ChannelPartner partner) {
			return this.equals(partner);
		}

		public boolean is(String partner) {
			return this.name().equalsIgnoreCase(partner);
		}
	}

	public static enum ChannelProvider {

		LINKEDIN("linkedin"), FACEBOOK("facebook"), TWITTER("twitter"), GOOGLE("google"), GITHUB("github"),
		EMAIL("email"), MOBILE("mobile"), WHATSAPP("whatsapp", ChannelPartner.WABA), TELEGRAM("telegram"),
		OUTLOOK("outlook"), APPLE("apple"), TRUELINQ("truelinq", ChannelPartner.TQ), DIRECT("direct");

		private String type;
		private ChannelPartner defaultPartner = ChannelPartner.ANY;

		public String getType() {
			return type;
		}

		public ChannelPartner getDefaultPartner() {
			return defaultPartner;
		}

		public ChannelPartner getPartner(ChannelPartner partner) {
			if (!ArgUtil.is(partner)) {
				partner = this.getDefaultPartner();
			}
			return partner;
		}

		public boolean is(String providerTypeTemp) {
			return this.type.equalsIgnoreCase(providerTypeTemp);
		}

		ChannelProvider(final String providerType) {
			this.type = providerType;
		}

		ChannelProvider(final String providerType, ChannelPartner defaultPartner) {
			this.type = providerType;
			this.defaultPartner = defaultPartner;
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
