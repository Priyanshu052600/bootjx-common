package com.boot.jx.demo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.boot.jx.swagger.ApiMockParam;
import com.boot.jx.swagger.ApiMockParams;
import com.boot.jx.swagger.MockParamBuilder.MockParamType;
import com.boot.utils.Constants;

public class DemoConstants extends Constants {
	public static enum SafeRelation {
		CONTACT, NOCONTACT, REGULAR, HOUSEMATE, NEARBY
	}

	public static final String DEVICE_ID_XKEY = "x-device-id";
	public static final String DEVICE_TOKEN_XKEY = "x-device-token";

	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@ApiMockParams({
			@ApiMockParam(name = DEVICE_ID_XKEY, value = "Device Id", defaultValue = "ab2b3bxo7844",
					paramType = MockParamType.HEADER),
			@ApiMockParam(name = DEVICE_TOKEN_XKEY, value = "Device Token", defaultValue = "xx*ssqwqwqxxxx",
					paramType = MockParamType.HEADER) })
	public @interface ApiPhoneDeviceHeaders {

	}
}
