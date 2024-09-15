package com.boot.jx.sso;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.boot.jx.swagger.ApiMockParam;
import com.boot.jx.swagger.ApiMockParams;
import com.boot.jx.swagger.MockParamBuilder.MockParamType;
import com.boot.utils.Constants;

public class SsoConstants extends Constants {
	public static enum SafeRelation {
		CONTACT, NOCONTACT, REGULAR, HOUSEMATE, NEARBY
	}

	public static enum MEMBERSHIP_TYPE {
		OWNER, ADMIN, MODERATOR, MEMBER, PENDING, NONE, REJECTED, BLOCKED;

		public boolean isStrongerThan(MEMBERSHIP_TYPE membershipType) {
			if (membershipType == null) {
				return true;
			}
			return this.ordinal() < membershipType.ordinal();
		}

		public boolean isWeakerThan(MEMBERSHIP_TYPE membershipType) {
			if (membershipType == null) {
				return false;
			}
			return this.ordinal() > membershipType.ordinal();
		}

		public static boolean isNone(MEMBERSHIP_TYPE membershipType) {
			if (membershipType == null) {
				return true;
			}
			return membershipType.equals(NONE);
		}

		public static boolean isActive(MEMBERSHIP_TYPE membershipType) {
			if (membershipType == null) {
				return false;
			}
			return MEMBER.ordinal() >= membershipType.ordinal();
		}

		public static boolean canJoin(MEMBERSHIP_TYPE membershipType) {
			if (membershipType == null) {
				return true;
			}
			return membershipType.equals(NONE) || membershipType.equals(REJECTED);
		}

		public static boolean isRemovable(MEMBERSHIP_TYPE membershipType) {
			return !(BLOCKED.equals(membershipType) || OWNER.equals(membershipType));
		}
	}

	public static enum ASSOCIATION_TYPE {
		MEMBER, EMPLOYEE, CONSULTANT, WORKER, CUSTOMER, CLIENT, VENDOR, PATIENT, STAFF, DOCTOR
	}

	public static final String DEVICE_ID_XKEY = "x-device-id";
	public static final String DEVICE_TOKEN_XKEY = "x-device-token";

	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@ApiMockParams({
			@ApiMockParam(name = DEVICE_ID_XKEY, value = "Device Id", defaultValue = "ab2b3bxo7844", paramType = MockParamType.HEADER),
			@ApiMockParam(name = DEVICE_TOKEN_XKEY, value = "Device Token", defaultValue = "xx*ssqwqwqxxxx", paramType = MockParamType.HEADER) })
	public @interface ApiPhoneDeviceHeaders {

	}
}
