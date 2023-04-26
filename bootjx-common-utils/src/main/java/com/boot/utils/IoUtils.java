package com.boot.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialException;

import org.springframework.util.StreamUtils;

/**
 * The Class IoUtils.
 */
public class IoUtils {

	/** The Constant BUFFER_SIZE. */
	private static final int BUFFER_SIZE = 1024;

	private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

	/**
	 * To byte array.
	 *
	 * @param is the is
	 * @return the byte[]
	 */
	public static byte[] toByteArray(InputStream is) {
		BufferedInputStream bis = new BufferedInputStream(is);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[BUFFER_SIZE];
		int readBytes;
		try {
			while ((readBytes = bis.read(buffer)) > 0) {
				baos.write(buffer, 0, readBytes);
			}
			return baos.toByteArray();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Inputstream to string.
	 *
	 * @param in the in
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String inputstream_to_string(InputStream in) throws IOException {
		Reader reader = new InputStreamReader(in);
		StringWriter writer = new StringWriter();
		char[] buf = new char[1000];
		while (true) {
			int n = reader.read(buf, 0, 1000);
			if (n == -1) {
				break;
			}
			writer.write(buf, 0, n);
		}
		return writer.toString();
	}

	public static class StreamWrapper {
		InputStream in;
		private byte[] body;

		public StreamWrapper(InputStream in) {
			this.in = in;
		}

		public InputStream toStream() throws IOException {
			if (this.body == null) {
				this.body = StreamUtils.copyToByteArray(in);
			}
			return new ByteArrayInputStream(this.body);
		}
	}

	/**
	 * The Class DrainableOutputStream.
	 */
	public class DrainableOutputStream extends FilterOutputStream {

		/** The buffer. */
		private final ByteArrayOutputStream buffer;

		/**
		 * Instantiates a new drainable output stream.
		 *
		 * @param out the out
		 */
		public DrainableOutputStream(OutputStream out) {
			super(out);
			this.buffer = new ByteArrayOutputStream();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.FilterOutputStream#write(byte[])
		 */
		@Override
		public void write(byte b[]) throws IOException {
			this.buffer.write(b);
			super.write(b);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.FilterOutputStream#write(byte[], int, int)
		 */
		@Override
		public void write(byte b[], int off, int len) throws IOException {
			this.buffer.write(b, off, len);
			super.write(b, off, len);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.FilterOutputStream#write(int)
		 */
		@Override
		public void write(int b) throws IOException {
			this.buffer.write(b);
			super.write(b);
		}

		/**
		 * To byte array.
		 *
		 * @return the byte[]
		 */
		public byte[] toByteArray() {
			return this.buffer.toByteArray();
		}
	}

	public static java.sql.Clob stringToClob(String source) throws SerialException, SQLException {
		return new javax.sql.rowset.serial.SerialClob(source.toCharArray());
	}

	/**
	 * converts inputstream to string
	 * 
	 * @param inputStream
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream inputStream, Charset charset) throws IOException {

		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))) {
			return br.lines().collect(Collectors.joining(System.lineSeparator()));
		}
	}
	
	// Clob to string
	public static String clobStringConversion(Clob clb)
	{
		StringBuffer str = new StringBuffer();
		String strng;

		BufferedReader bufferRead;
		try {
			bufferRead = new BufferedReader(clb.getCharacterStream());
			while ((strng=bufferRead.readLine())!=null){
				str.append(strng);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return str.toString();
	}

}
