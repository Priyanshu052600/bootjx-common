package com.boot.jx.filter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.jx.api.ApiResponse;
import com.boot.utils.TimeZoneUtil;
import com.boot.utils.TimeZoneUtil.TimeZoneDto;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

@RestController
public class AppOptionsController {

	public static final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();
	
	@RequestMapping(value = "/pub/meta/options/timezone", method = { RequestMethod.GET })
	public ApiResponse<TimeZoneDto, Object> getTimeZones() {
		return ApiResponse.buildResults(TimeZoneUtil.getTimeZoneDtoLst());
	}

	@RequestMapping(value = "/pub/meta/options/timezonekey", method = { RequestMethod.GET })
	public ApiResponse<String, Object> getTimeZoneKeys() {
		return ApiResponse.buildResults(TimeZoneUtil.getTimeZoneLst());
	}
	
	@RequestMapping(value = "/pub/meta/options/isdcode", method = { RequestMethod.GET })
	public ApiResponse<Object, Object> getISDCodes() {
		return ApiResponse.buildResults(PHONE_NUMBER_UTIL.getSupportedRegions().toArray());
	}


}
