package com.boot.utils;

import java.io.OutputStream;
import java.io.PrintStream;

public class SysUtils {

	public static PrintStream printStreamOriginal = null;

	public static void disableSysOut() {
		if (printStreamOriginal == null) {
			printStreamOriginal = System.out;
		}
		System.setOut(new PrintStream(new OutputStream() {
			public void close() {
			}

			public void flush() {
			}

			public void write(byte[] b) {
			}

			public void write(byte[] b, int off, int len) {
			}

			public void write(int b) {
			}
		}));
	}

	public static void enableSysOut() {
		System.setOut(printStreamOriginal);
	}
}
