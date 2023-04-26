package com.boot.utils;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * The Class CloseUtil.
 */
public final class CloseUtil {

	/**
	 * Instantiates a new close util.
	 */
	private CloseUtil() {
		throw new IllegalStateException("This is a utility class with static methods and should not be instantiated");
	}

	/**
	 * Close.
	 *
	 * @param closeable the closeable
	 */
	public static void close(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {
			/* ignore */
		}
	}

	/**
	 * Close.
	 *
	 * @param hasCloseMethod the has close method
	 */
	public static void close(Object hasCloseMethod) {
		try {
			if (hasCloseMethod == null) {
				return;
			}
			Method method = hasCloseMethod.getClass().getMethod("close", new Class<?>[0]);
			method.invoke(hasCloseMethod, new Object[0]);
		} catch (Exception e) {
			/* ignore */
		}
	}

	public static void close(CallableStatement cs, Connection connection) {
		if (cs != null) {
			try {
				cs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}