package com.boot.jx.model;

import java.io.Serializable;

import com.boot.jx.dict.Language;
import com.boot.jx.dict.UserClient.AppType;
import com.boot.jx.dict.UserClient.Channel;
import com.boot.jx.dict.UserClient.ClientType;
import com.boot.jx.dict.UserClient.DevicePlatform;
import com.boot.jx.dict.UserClient.DeviceType;
import com.boot.jx.dict.UserClient.UserDeviceClient;
import com.boot.utils.ArgUtil;
import com.boot.utils.EntityDtoUtil;
import com.boot.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * 
 * @author lalittanwar
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDevice implements Serializable {

	private static final long serialVersionUID = 4015555971724271185L;

	protected String fingerprint = null;
	protected String ip = null;
	protected String id = null;
	protected String appVersion = null;
	protected AppType appType = null;
	protected Channel channel = null;
	protected ClientType clientType = null;

	protected DeviceType type = null;
	protected DevicePlatform platform = null;

	protected UserAgent userAgent = null;
	protected Language language;

	/**
	 * THis is unique id sent by device, in case of mobiles it is device id, in case
	 * of browser it is unique id generated to identify browser. Its value depends
	 * on values provided by client. can be compromised.
	 * 
	 * @return
	 */
	public String getFingerprint() {
		return fingerprint;
	}

	/**
	 * 
	 * unique id generated on server, it is less accurate than fingerprint, A new id
	 * is generated if device id is not identified, by us.
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Version of mobile app used by user.
	 * 
	 * @return
	 */
	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AppType getAppType() {
		return appType;
	}

	public void setAppType(AppType appType) {
		this.appType = appType;
	}

	/**
	 * 
	 * Enumeration for the type of device that has been resolved
	 * 
	 * NORMAL, MOBILE, TABLET.
	 * 
	 * @see org.springframework.mobile.device.DeviceType
	 * 
	 * @return
	 */
	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
		this.type = type;
	}

	/**
	 * Enumeration for the platform of device that has been resolved
	 * 
	 * IOS, ANDROID, UNKNOWN
	 * 
	 * @see org.springframework.mobile.device.DevicePlatform
	 * 
	 * @return
	 */
	public DevicePlatform getPlatform() {
		return platform;
	}

	public void setPlatform(DevicePlatform platform) {
		this.platform = platform;
	}

	/**
	 * Get IP Address of user deivce
	 * 
	 * @return
	 */
	public String getIp() {
		return ip;
	}

	public UserAgent getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(UserAgent userAgent) {
		this.userAgent = userAgent;
	}

	public UserDeviceClient toUserDeviceClient() {
		UserDeviceClient userClient = new UserDeviceClient();
		userClient.setDeviceType(this.getType());
		userClient.setAppType(this.getAppType());
		userClient.setIp(this.getIp());
		userClient.setFingerprint(this.getFingerprint());
		userClient.setClientType(this.getClientType());
		userClient.setChannel(this.getChannel());
		userClient.setLang(this.getLanguage());
		return userClient;

	}

	public UserDevice toSanitized() {
		UserDevice userDevice = EntityDtoUtil.entityToDto(this, new UserDevice());
		userDevice.setIp(StringUtils.mask(userDevice.getIp()));
		return userDevice;
	}

	public boolean isMobile() {
		if (!ArgUtil.isEmpty(this.getAppType()) && this.getAppType().isMobile()) {
			return true;
		}
		if (!ArgUtil.isEmpty(this.getType()) && this.getType().isMobile()) {
			return true;
		}
		return false;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

}
