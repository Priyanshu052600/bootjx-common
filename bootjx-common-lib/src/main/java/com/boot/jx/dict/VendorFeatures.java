package com.boot.jx.dict;

import com.boot.jx.types.Pnum;
import com.boot.utils.EnumType;

public class VendorFeatures extends Pnum implements EnumType {

	public VendorFeatures(String name, int ordinal) {
		super(name, ordinal);
	}

	/**
	 * Explicit definition of values() is needed here to trigger static initializer.
	 * 
	 * @return
	 */
	public static VendorFeatures[] values() {
		return values(VendorFeatures.class);
	}

	public static VendorFeatures valueOf(String name) {
		return fromString(VendorFeatures.class, name);
	}

	static {
		init(VendorFeatures.class);
	}

}
