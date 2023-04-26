package com.boot.jx.dict;

import java.io.Serializable;

import com.boot.utils.ArgUtil;
import com.boot.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserClient {

	/**
	 * Business Channel
	 * 
	 * @author lalittanwar
	 *
	 */
	public enum Channel {
		ONLINE, KIOSK, MOBILE, BRANCH, TPC, SYSTEM, UNKNOWN, OFFSITE, PAYMENT_LINK, EKYC_CUSTOMER, WU_PAYMENT_LINK;
	}

	/**
	 * Physical Device type
	 * 
	 * @author lalittanwar
	 *
	 */
	public enum DeviceType {
		COMPUTER, MOBILE, TABLET(MOBILE), IPAD(MOBILE), UNKNOWN(COMPUTER);

		DeviceType parent;

		DeviceType() {
			this.parent = this;
		}

		DeviceType(DeviceType parent) {
			this.parent = parent.getParent();
		}

		public DeviceType getParent() {
			return parent;
		}

		public boolean isParentOf(DeviceType check) {
			return (this == check || check.getParent() == this);
		}

		public boolean hasParent(DeviceType check) {
			return check.isParentOf(this);
		}

		public boolean isRelated(DeviceType check) {
			return check.isParentOf(this.parent);
		}

		public boolean isMobile() {
			return this.hasParent(MOBILE);
		}
	}

	/**
	 * More of generic OS
	 * 
	 * @author lalittanwar
	 *
	 */
	public enum DevicePlatform {
		IOS, ANDROID, WINDOWS, MAC, LINUX, UNKNOWN;
	}

	/**
	 * Client Type, tech or language
	 * 
	 * @author lalittanwar
	 *
	 */
	public enum AppType {
		WEB, IOS(Channel.MOBILE), ANDROID(Channel.MOBILE), UNKNOWN;

		Channel channel;

		AppType(Channel channel) {
			this.channel = channel;
		}

		AppType() {
			this(Channel.UNKNOWN);
		}

		public Channel getChannel() {
			return channel;
		}

		public boolean isMobile() {
			return this.channel == Channel.MOBILE;
		}
	}

	public enum MapOption {
		TERMINAL, USER;

		public static MapOption byDeviceType(DeviceType deviceType) {
			if (DeviceType.COMPUTER.isParentOf(deviceType)) {
				return TERMINAL;
			} else if (DeviceType.MOBILE.isParentOf(deviceType)) {
				return USER;
			}
			return null;
		}
	}

	public enum AuthSystem {
		TERMINAL, DEVICE;

		public static AuthSystem byDeviceType(DeviceType deviceType) {
			if (DeviceType.COMPUTER.isParentOf(deviceType)) {
				return TERMINAL;
			} else if (DeviceType.MOBILE.isParentOf(deviceType)) {
				return DEVICE;
			}
			return null;
		}
	}

	public enum ClientOption {
		MULTIREGISTER, MULTIACTIVE, FIRSTREGISTER, AUTOACTIVATE
	}

	public enum ClientType {
		// Auth Apps
		NOTP_APP(DeviceType.MOBILE, Channel.MOBILE),

		// branch cleints
		BRANCH_WEB_OLD(DeviceType.COMPUTER, Channel.BRANCH), BRANCH_WEB(DeviceType.COMPUTER, Channel.BRANCH),
		SIGNATURE_PAD(DeviceType.TABLET, Channel.BRANCH),
		BRANCH_ADAPTER(DeviceType.COMPUTER, Channel.BRANCH, MapOption.TERMINAL, ClientOption.FIRSTREGISTER),

		// Other Channels
		OFFSITE_PAD(DeviceType.TABLET, Channel.BRANCH),
		KIOSK_ADAPTER(DeviceType.COMPUTER, Channel.KIOSK, AuthSystem.TERMINAL, MapOption.TERMINAL,
				ClientOption.FIRSTREGISTER),
		DELIVERY_APP(DeviceType.MOBILE, Channel.BRANCH),
		OFFSITE_WEB(DeviceType.COMPUTER, Channel.OFFSITE, AuthSystem.DEVICE),
		KIOSK_WEB(DeviceType.COMPUTER, Channel.KIOSK, AppType.WEB),

		// Customer Facing interfaces
		ONLINE_WEB(DeviceType.COMPUTER, Channel.ONLINE, AppType.WEB),
		ONLINE_AND(DeviceType.MOBILE, Channel.MOBILE, AppType.ANDROID),
		ONLINE_IOS(DeviceType.MOBILE, Channel.MOBILE, AppType.IOS),

		// Unknown
		SYSTEM, UNKNOWN;

		DeviceType deviceType;

		Channel channel = Channel.ONLINE;
		AppType appType;
		AuthSystem authSystem = AuthSystem.TERMINAL;
		MapOption mapOption = null;
		boolean multiRegister = true;
		boolean multiActive = false;
		boolean firstRegister = false;
		boolean autoActivate = false;

		private ClientType(Object... options) {

			this.multiRegister = false;
			this.multiActive = false;
			this.firstRegister = false;
			this.autoActivate = false;

			for (Object option : options) {
				if (option instanceof Channel) {
					this.channel = (Channel) option;
				} else if (option instanceof AuthSystem) {
					this.authSystem = (AuthSystem) option;
				} else if (option instanceof AppType) {
					this.appType = (AppType) option;
				} else if (option instanceof DeviceType) {
					this.deviceType = (DeviceType) option;
				} else if (option instanceof MapOption) {
					this.mapOption = (MapOption) option;
				} else if (option instanceof ClientOption) {
					ClientOption clientOption = (ClientOption) option;
					if (clientOption == ClientOption.MULTIREGISTER) {
						multiRegister = true;
					} else if (clientOption == ClientOption.MULTIACTIVE) {
						multiActive = true;
					} else if (clientOption == ClientOption.FIRSTREGISTER) {
						firstRegister = true;
					} else if (clientOption == ClientOption.AUTOACTIVATE) {
						autoActivate = true;
					}
				}
			}

			if (ArgUtil.isEmpty(this.authSystem)) {
				this.authSystem = AuthSystem.byDeviceType(this.deviceType);
			}
		}

		private ClientType(DeviceType deviceType, Channel channel, AuthSystem authSystem, AppType appType) {
			this.deviceType = deviceType;
			this.channel = channel;
			this.authSystem = authSystem;
			this.appType = appType;
			this.multiRegister = true;
			this.multiActive = false;
			this.firstRegister = false;
			this.autoActivate = false;
			if (ArgUtil.isEmpty(this.authSystem)) {
				this.authSystem = AuthSystem.byDeviceType(this.deviceType);
			}
		}

		ClientType(DeviceType deviceType, Channel channel, AuthSystem authSystem) {
			this(deviceType, channel, authSystem, AppType.WEB);
		}

		ClientType(DeviceType deviceType, Channel channel, AppType appType) {
			this(deviceType, channel, null, appType);
		}

		ClientType(DeviceType deviceType, Channel channel) {
			this(deviceType, channel, null, AppType.WEB);
		}

		ClientType(DeviceType deviceType) {
			this(deviceType, null, null, AppType.WEB);
		}

		ClientType() {
			this(DeviceType.COMPUTER, null, null, AppType.WEB);
		}

		public AuthSystem getAuthSystem() {
			return authSystem;
		}

		public boolean isAutoActivate() {
			return autoActivate;
		}

		public void setAutoActivate(boolean autoActivate) {
			this.autoActivate = autoActivate;
		}

		/**
		 * Allowed Device for This client
		 * 
		 * @return
		 */
		public DeviceType getDeviceType() {
			return deviceType;
		}

		public void setDeviceType(DeviceType deviceType) {
			this.deviceType = deviceType;
		}

		public Channel getChannel() {
			return channel;
		}

		public void setChannel(Channel channel) {
			this.channel = channel;
		}

		public AppType getAppType() {
			return appType;
		}

		public boolean isMultiRegister() {
			return multiRegister;
		}

		public void setMultiRegister(boolean multiRegister) {
			this.multiRegister = multiRegister;
		}

		public boolean isMultiActive() {
			return multiActive;
		}

		public void setMultiActive(boolean multiActive) {
			this.multiActive = multiActive;
		}

		public boolean isFirstActivate() {
			return firstRegister;
		}

		@Deprecated
		public boolean isFirstRegister() {
			return firstRegister;
		}

		public void setFirstRegister(boolean firstRegister) {
			this.firstRegister = firstRegister;
		}

		static {
			OFFSITE_WEB.setMultiActive(true);
			OFFSITE_WEB.setFirstRegister(true);
			BRANCH_ADAPTER.setFirstRegister(true);
		}

		public MapOption getMapOption() {
			return mapOption;
		}

	}

	@JsonInclude(Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class UserDeviceClient implements Serializable {

		private static final long serialVersionUID = -3209167233695023422L;

		private String ip;

		@JsonProperty("fp")
		private String fingerprint;

		@JsonProperty("cn")
		private Channel channel;

		@JsonProperty("dt")
		private DeviceType deviceType;

		@JsonProperty("at")
		private AppType appType;

		@JsonProperty("ct")
		private ClientType clientType;

		@JsonProperty("cv")
		private String clientVersion;

		@JsonProperty("lang")
		private Language lang;

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public String getFingerprint() {
			return fingerprint;
		}

		public void setFingerprint(String fingerprint) {
			this.fingerprint = fingerprint;
		}

		public Channel getChannel() {
			return channel;
		}

		public void setChannel(Channel channel) {
			this.channel = channel;
		}

		public DeviceType getDeviceType() {
			return deviceType;
		}

		public void setDeviceType(DeviceType deviceType) {
			this.deviceType = deviceType;
		}

		public AppType getAppType() {
			if (appType == null && this.clientType != null) {
				appType = clientType.getAppType();
			}
			return appType;
		}

		public void setAppType(AppType appType) {
			this.appType = appType;
		}

		public ClientType getClientType() {
			return clientType;
		}

		public void setClientType(ClientType clientType) {
			this.clientType = clientType;
		}

		public UserDeviceClient importFrom(UserDeviceClient userDevice) {
			this.setDeviceType(userDevice.getDeviceType());
			this.setAppType(userDevice.getAppType());
			this.setIp(userDevice.getIp());
			this.setFingerprint(userDevice.getFingerprint());
			this.setClientType(userDevice.getClientType());
			this.setClientVersion(userDevice.getClientVersion());
			this.setLang(userDevice.getLang());
			return this;
		}

		public String getClientVersion() {
			return clientVersion;
		}

		public void setClientVersion(String clientVersion) {
			this.clientVersion = clientVersion;
		}

		public Language getLang() {
			return lang;
		}

		public void setLang(Language lang) {
			this.lang = lang;
		}

		public String toString() {
			return String.format("%s=%s/%s=%s/%s=%s", this.deviceType, this.ip, this.appType,
					ArgUtil.parseAsString(this.fingerprint, Constants.BLANK), this.clientType,
					ArgUtil.parseAsString(this.clientVersion, Constants.BLANK));
		}

	}

	public static boolean isAuthSystem(ClientType clientType, AuthSystem authSystem) {
		if (ArgUtil.isEmpty(clientType) || ArgUtil.isEmpty(authSystem)) {
			return false;
		}
		if (ArgUtil.is(clientType.getAuthSystem())) {
			return clientType.getAuthSystem().equals(authSystem);
		} else {
			return authSystem.equals(AuthSystem.byDeviceType(clientType.getDeviceType()));
		}
	}

}
