package com.boot.test;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.boot.jx.auth.AuthStateManager.AuthState;
import com.boot.utils.JsonUtil;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthStateTests {

	public static void main(String[] arg) {
		// new JsonConversionTests().curl();
		// new JsonConversionTests().chatSessionDoc2DTO();
	}

	public static String stateStr = "eyJtZXNzYWdlIjoiR3paWmgzSzZ1bFNXS2lUOGtlL3dWN0FCczN3ZXYzYXFTRmJLYmllSzFHMmNRNG1jeVY2TVB2YTdDSDV4VVdPdmc0c2t2N3BVVkpXSGFxM05CWHIxeXoxWGdTL281Qm1zMXNGSi9vY0FQOHo2Vkw0aEVKWnFIcmlYVko2WU5HSGRocU1VSXFBRXZoL08vVW1EM1N5dnFwcDk0NXFra1l1NmZaZzdMUFZDdDdJZVZNNUl6WGVHZm1tby8rNmhQaFBFYVN2dVBiSEdWNkk9IiwiZXhwaXJlQXQiOjE3Mjg5ODIxMDA4MjMsImV4cGlyZWQiOmZhbHNlfQ==";

	@Test
	public void decodeTest() {
		AuthState state = AuthState.fromString(stateStr);
		System.out.println(JsonUtil.toJson(state));
	}

}
