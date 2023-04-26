package com.boot.utils;

/**
 * The Interface EnumType.
 */
public interface EnumType {
	/**
	 * Name.
	 *
	 * @return the string
	 */
	String name();

	/**
	 * 
	 * @return
	 */
	public default String note() {
		return this.name();
	};

	public default String stringValue() {
		return this.name();
	};

	public default EnumType enumValue() {
		return this;
	};
}
