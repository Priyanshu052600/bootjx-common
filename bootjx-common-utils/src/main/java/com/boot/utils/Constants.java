package com.boot.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class Constants.
 */
public class Constants {

	/**
	 * Instantiates a new constants.
	 */
	protected Constants() {
		// not allowed
	}

	/** The Constant BLANK. */
	public static final String BLANK = "";
	public static final String UNDERSCORE = "_";

	/** The Constant defaultBoolean. */
	public static final Boolean DEFAULT_BOOLEAN = Boolean.FALSE;

	/** The Constant defaultInteger. */
	public static final Integer DEFAULT_INTEGER = Integer.valueOf(0);

	/** The Constant defaultLong. */
	public static final Long DEFAULT_LONG = Long.valueOf(0L);

	/** The Constant defaultDouble. */
	public static final Double DEFAULT_DOUBLE = Double.valueOf(0D);

	/** The Constant defaultString. */
	public static final String DEFAULT_STRING = "";
	public static final String NULL_STRING = "NULL";

	/** The Constant defaultObject. */
	public static final Object DEFAULT_OBJECT = new Object();

	/** The Constant emptyMap. */
	public static final Map<String, Object> EMPTY_MAP = Collections.unmodifiableMap(new HashMap<String, Object>());

	/** The Constant emptyList. */
	public static final List<Object> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<Object>());

	/** The Constant emptyListOfList. */
	public static final List<List<Object>> EMPTY_LISTOF_LIST = Collections
			.unmodifiableList(new ArrayList<List<Object>>());

	/** The Constant emptyListOfListOfList. */
	public static final List<List<List<Object>>> EMPTY_LISTOF_LISTOF_LIST = Collections
			.unmodifiableList(new ArrayList<List<List<Object>>>());

	/** The Constant emptyStringList. */
	public static final List<String> EMPTY_STRING_LIST = Collections.unmodifiableList(new ArrayList<String>());

	/** The Constant CANCELLED_REQUEST. */
	public static final String CANCELLED_REQUEST = "04.03";

	/** The Constant CUSTOMERTYPE_INDU. */
	public static final String CUSTOMERTYPE_INDU = "Individual";

	/** The Constant CUST_ACTIVE_INDICATOR. */
	public static final String CUST_ACTIVE_INDICATOR = "Y";

	/**
	 * @deprecated use AmxDBConstants.Compliance The Constant
	 *             CUST_COMPLIANCE_CHECK_INDICATOR.
	 */
	@Deprecated
	public static final String CUST_COMPLIANCE_CHECK_INDICATOR = "C";

	/** The Constant IDENTITY_TYPE_ID. */
	public static final String IDENTITY_TYPE_CIVIL_ID_STR = "198";

	public static final String IDENTITY_TYPE_CIVIL_ID_STRING = "2000";

	public static final Long IDENTITY_TYPE_CIVIL_ID = Long.parseLong(IDENTITY_TYPE_CIVIL_ID_STR);

	public static final String COMPNY_TYPE = "Corporate";

	public static final String COMMON_NATIONALITY = "ALL";

	public static final String COMPONENT_NAME = "Identity Type";

	public static final String NO = "N";

	public static final String YES = "Y";

	public static final String DELETED_SOFT = "D";

	/** The Constant CUST_DB_SCAN. */
	public static final String CUST_DB_SCAN = "D";

	public static final String NULL_VALUE_STRING = "null";
	
	public static final BigDecimal COMPALINT_DOC_ID= new BigDecimal(35);
	
	public static final BigDecimal COMPLAINT_LOC_COD = new BigDecimal(1);

	// EKYC IMAGE Constants
	public static final String INVALID = "Invalid";
	public static final String FAKE_IMAGE_INDICATOR_TRUE = "true";
	public static final String EKYC_UPLOAD_SUCCESS = "EKYC_UPLOAD_SUCCESS";
	public static final String EKYC_UPLOAD_COMPLIANCE = "EKYC_UPLOAD_COMPLIANCE";
	public static final String EKYC_UPLOAD_PENDING = "EKYC_UPLOAD_PENDING";
	public static final String EKYC_UPLOAD_FAILED = "EKYC_UPLOAD_FAILED";
	public static final String EKYC_PENDING = "PENDING";
	public static final String CUST_PENDING_INDICATOR = "P";
	public static final String CUST_EKYC_COMPLAINCE = "C";

	public static class Common {
		public static final String SUCCESS = "SUCCESS";
		public static final String FAILED = "FAILED";
		public static final String UNKNOWN = "UNKNOWN";
		public static final String YES = "YES";
		public static final String NO = "NO";
		public static final String FALSE = "FALSE";
		public static final String TRUE = "TRUE";
	}

	// Image size 1MB in bits constant field for annual income

	public static final Integer IMAGE_SIZE = 1048576;

	public static class TimeInterval {
		public static final int SEC = 1000;
		public static final int MIN = 60 * SEC;
		public static final int MIN_30 = 30 * MIN;
		public static final int MIN_10 = 10 * MIN;
		public static final long HRS = 60 * MIN;
		public static final long HRS_12 = 12 * HRS;
		public static final long DAY = 24 * HRS;
	}

	public static final Long ANNUALINCOME_VERIFIED_LIMIT = 25000L;

	public static final String MALE = "MALE";

	public static final String GENDER_MALE = "M";

	public static final String FEMALE = "FEMALE";

	public static final String GENDER_FEMALE = "F";

	public static final String SENDER_ZIP_CODE = "13077";

	public static class IDTYPE {
		public static final String C = "CIVIL ID";
		public static final String BD = "BEDOUIN";
		public static final String CN = "CIVIL ID NEW";
		public static final String P = "PASSPORT";
		public static final String GN = "GCC NATIONAL ID";
	}
	public static enum WU_ID_TYPE {
		A,B,D
	}
	public static final String PAY_INQUIRY="PAY_INQUIRY";
	public static final String NONE = "NONE";
	public static final String BRANCH = "BRANCH";

	public static final String COLLECTION_NAME = "MESSAGE_";
	

}
