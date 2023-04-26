package com.boot.utils;

import java.io.Serializable;

/**
 * The Class BitFlags. Utility class for managing bit binary flags.
 * 
 */
public class BitFlags implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The Class Field.
	 */
	public class Field implements Serializable {

		private static final long serialVersionUID = 1L;

		/** The offset. */
		private int offset;

		/** The size. */
		private int size;

		public Field() {
			super();
		}

		/**
		 * Instantiates a new field.
		 * 
		 * @param offset
		 *            the offset
		 * @param size
		 *            the size
		 */
		public Field(int offset, int size) {
			this.offset = offset;
			this.size = size;
		}

		/**
		 * Gets the.
		 * 
		 * @return true, if successful
		 */
		public boolean get() {
			return getLong() == 1L;
		}

		/**
		 * Sets the.
		 * 
		 * @param value
		 *            the value
		 */
		public void set(boolean value) {
			setLong(value ? 1L : 0L);
		}

		/**
		 * Gets the long.
		 * 
		 * @return the long
		 */
		public long getLong() {
			long mask = (1 << this.size) - 1;
			return ((getValue() >> this.offset) & mask);
		}

		/**
		 * Sets the long.
		 * 
		 * @param value
		 *            the new long
		 */
		public void setLong(long value) {
			long mask = (1 << this.size) - 1;
			setValue((getValue() & (~(mask << this.offset))) | ((value & mask) << this.offset));
		}
	}

	/** The value. */
	protected long value;

	public BitFlags() {
		// Sets value to default
		this.value = 0l;
	}

	/**
	 * Instantiates a new bit flags.
	 * 
	 * @param value
	 *            the value
	 */
	public BitFlags(long value) {
		this.value = value;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public long getValue() {
		return this.value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(long value) {
		this.value = value;
	}
}
