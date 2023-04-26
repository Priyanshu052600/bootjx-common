package com.boot.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class FileUtil.
 */
public final class FileUtil {

	/** The Constant RESOURCE. */
	private static final String RESOURCE = "resource";

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

	/** The Constant FILE_PREFIX. */
	public static final String FILE_PREFIX = "file://";

	/** The Constant FILE_PREFIX2. */
	public static final String FILE_PREFIX2 = "file:/";
	public static final String FILE_TRAVER_BACK = "..";
	public static final String FILE_TRAVER_HOME = "./";

	/** The Constant CLASSPATH_PREFIX. */
	public static final String CLASSPATH_PREFIX = "classpath:";

	/**
	 * Instantiates a new file util.
	 */
	private FileUtil() {
		throw new IllegalStateException("This is a class with static methods and should not be instantiated");
	}

	public static String normalize(String path) {
		String resolvedPath = null;
		try {
			String forwrdPath = path.replace("\\", "/");
			// URI uri = new File(path).toURI();
			URI uri = new URI(forwrdPath.replace(" ", "%20"));

			resolvedPath = uri.normalize().toString();
			if (resolvedPath.contains(FILE_TRAVER_BACK) || resolvedPath.contains(FILE_TRAVER_HOME)) {
				throw new Exception();
			}
		} catch (Exception e) {
			LOG.error("Path normalize {}  to {} ", path, resolvedPath, e);
			throw new RuntimeException("FILE_TRAVERSAL FOUND");
		}
		return path;
	}

	public static String deprefix(String propertiesPath) {
		propertiesPath = propertiesPath.startsWith(FILE_PREFIX2) ? propertiesPath.substring(FILE_PREFIX2.length())
				: propertiesPath;
		propertiesPath = propertiesPath.startsWith(FILE_PREFIX) ? propertiesPath.substring(FILE_PREFIX.length())
				: propertiesPath;
		return propertiesPath;
	}

	/**
	 * Read file.
	 *
	 * @param filename the filename
	 * @return the string
	 */
	@SuppressWarnings(RESOURCE)
	public static String readFile(String filename) {
		boolean isFilesystem = filename.startsWith(FILE_PREFIX);
		filename = isFilesystem ? filename.substring(FILE_PREFIX.length()) : filename;
		StringBuilder sb = new StringBuilder();
		InputStream in = null;
		BufferedReader reader = null;
		try {
			if (isFilesystem) {
				in = new FileInputStream(new File(normalize(filename)));
			} else {
				in = FileUtil.class.getResourceAsStream(normalize(filename));
			}
			if (in == null) {
				return null;
			}
			reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}

		} catch (IOException e) {
			LOG.error("cannot load " + (isFilesystem ? "filesystem" : "classpath") + " file " + filename, e);
		} finally {
			CloseUtil.close(reader);
			CloseUtil.close(in);
		}
		return sb.toString();
	}

	/**
	 * Write the data into file.
	 *
	 * @param fileLocation Path of file
	 * @param content      Content
	 * @throws IOException        Signals that an I/O exception has occurred.
	 * @throws URISyntaxException
	 */
	public static void saveToFile(String fileLocation, String content) throws IOException, URISyntaxException {
		Writer output = null;
		fileLocation = normalize(fileLocation);
		File file = new File(fileLocation);

		try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(content);

		} catch (IOException io) {
			throw io;
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	/**
	 * Save to file.
	 *
	 * @param fileLocation the file location
	 * @param content      the content
	 * @throws IOException        Signals that an I/O exception has occurred.
	 * @throws URISyntaxException
	 */
	public static void saveToFile(String fileLocation, byte[] content) throws IOException, URISyntaxException {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		fileLocation = normalize(fileLocation);

		try {
			fos = new FileOutputStream(new File(fileLocation));
			// create an object of BufferedOutputStream
			bos = new BufferedOutputStream(fos);
			bos.write(content);

		} catch (IOException io) {
			throw io;
		} finally {
			if (bos != null) {
				bos.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}

	public static String read(URL url) {
		LOG.debug("read:{}", url);
		StringBuilder sb = new StringBuilder();
		InputStream in = null;
		BufferedReader reader = null;

		try {
			in = url.openStream();
			if (in == null) {
				return null;
			}
			reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}

		} catch (IOException e) {
			LOG.error("cannot load  file " + url.toString(), e);
		} finally {
			CloseUtil.close(reader);
			CloseUtil.close(in);
		}
		return sb.toString();
	}

	/**
	 * Can be used to load file inside classpath ie: src/resources.
	 *
	 * @param filePath the file path
	 * @param clazz    the clazz
	 * @return the resource
	 */
	public static URL getResource(String filePath, Class<?> clazz) {
		LOG.debug("getResource:{}", filePath);
		boolean isClassPath = filePath.startsWith(CLASSPATH_PREFIX);
		if (isClassPath) {
			return getResource(filePath.substring(CLASSPATH_PREFIX.length()), clazz);
		}
		if (clazz == null) {
			return getResource(filePath);
		}
		URL u = clazz.getClassLoader().getResource(CLASSPATH_PREFIX + filePath);
		if (u != null) {
			return u;
		}

		u = clazz.getClassLoader().getResource(filePath);
		if (u != null) {
			return u;
		}
		return u;
	}

	/**
	 * Gets the resource.
	 *
	 * @param filePath the file path
	 * @return the resource
	 */
	public static URL getResource(String filePath) {
		return getResource(filePath, FileUtil.class);
	}

	/**
	 * Gets the external file.
	 *
	 * @param filePath the file path
	 * @return the external file
	 */
	public static File getExternalFile(String filePath) {
		return getExternalFile(filePath, FileUtil.class);
	}

	/**
	 * Gets the external resource.
	 *
	 * @param filePath the file path
	 * @return the external resource
	 */
	public static URL getExternalResource(String filePath) {
		return getExternalResource(filePath, FileUtil.class);
	}

	/**
	 * Gets the external resource.
	 *
	 * @param filePath the file path
	 * @param clazz    the clazz
	 * @return the external resource
	 */
	public static URL getExternalResource(String filePath, Class<?> clazz) {
		LOG.debug("getExternalResource:{}", filePath);
		if (clazz == null) {
			return getExternalResource(filePath);
		}
		// Search in jar folder
		File jarPath = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath().split("!")[0]);
		String propertiesPath = jarPath.getParent();

		LOG.debug("getExternalResource:jarPath:{} {}", jarPath, jarPath.getParent());

		URL u = clazz.getClassLoader().getResource(propertiesPath + "/" + filePath);
		if (u != null) {
			return u;
		}

		// Search working folder
		propertiesPath = System.getProperty("user.dir");
		try {
			u = clazz.getClassLoader().getResource(propertiesPath + "/" + filePath);
		} catch (Exception e) {
			LOG.error("clazz.getClassLoader().getResource({}) : {}", propertiesPath + "/" + filePath, e.getMessage());
		}

		if (u != null) {
			return u;
		}

		if (filePath.startsWith("ext-resources") && jarPath.getParent().endsWith("/target")) {
			String modulePath = jarPath.getParentFile().getParent();
			String targetFilePath = modulePath + "/" + filePath;
			u = clazz.getClassLoader().getResource(targetFilePath);
			LOG.warn("SLOW Module getExternalResource {}", targetFilePath);
			if (u != null) {
				return u;
			}
		}

		if (SysConfigUtil.FILE_SEARCH_TARGET) {
			// Search in target folder
			String targetFilePath = propertiesPath + "/target/" + filePath;
			u = clazz.getClassLoader().getResource(targetFilePath);
			LOG.warn("SLOW Target getExternalResource {}", targetFilePath);
			if (u != null) {
				return u;
			}

			targetFilePath = FILE_PREFIX2 + propertiesPath + "/target/" + filePath;
			u = clazz.getClassLoader().getResource(targetFilePath);
			LOG.warn("SLOW Target getExternalResource {}", targetFilePath);
			if (u != null) {
				return u;
			}
		}

		// Return default
		return null;
	}

	/**
	 * Is used to load file relative to project or jar.
	 *
	 * @param filePath the file path
	 * @param clazz    the clazz
	 * @return the external file
	 * @throws URISyntaxException
	 */
	public static File getExternalFile(String filePath, Class<?> clazz) {
		LOG.debug("getExternalFile:{}", filePath);
		if (clazz == null) {
			return getExternalFile(filePath);
		}

		// Search in jar folder
		CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();

		String propertiesPath = null;

		String jarPathParent = null;
		if (ArgUtil.is(codeSource)) {
			String jarPathString = codeSource.getLocation().getPath().split("!")[0];
			File jarPath = new File(normalize(jarPathString));
			jarPathParent = jarPath.getParent();
			propertiesPath = jarPath.getParentFile().getPath();
			propertiesPath =  "/" + deprefix(propertiesPath);
			File file = new File(normalize(propertiesPath + "/" + filePath));
			if (file.exists()) {
				return file;
			}
		}

		// Search working folder
		propertiesPath = System.getProperty("user.dir");
		File file2 = new File(normalize(propertiesPath + "/" + filePath));
		if (file2.exists()) {
			return file2;
		}

		if (filePath.startsWith("ext-resources") && ArgUtil.is(jarPathParent) && jarPathParent.endsWith("/target")) {
			String modulePath = jarPathParent.split("/target")[0];
			modulePath =  "/" + deprefix(modulePath);
			file2 = new File(normalize(modulePath + "/" + filePath));
			if (file2.exists()) {
				return file2;
			}
		}

		if (SysConfigUtil.FILE_SEARCH_TARGET) {
			// Search in target folder
			file2 = new File(normalize(propertiesPath + "/target/" + filePath));
			if (file2.exists()) {
				return file2;
			}
		}

		// Return default
		return new File(normalize(filePath));
	}

	/**
	 * Gets the external resource as stream.
	 *
	 * @param filePath the file path
	 * @return the external resource as stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static InputStream getExternalResourceAsStream(String filePath) throws IOException {
		return getExternalResourceAsStream(filePath, FileUtil.class);
	}

	/**
	 * Gets the external resource as stream.
	 *
	 * @param filePath the file path
	 * @param clazz    the clazz
	 * @return the external resource as stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static InputStream getExternalResourceAsStream(String filePath, Class<?> clazz) throws IOException {
		LOG.debug("getExternalResourceAsStream:{}", filePath);
		if (clazz == null) {
			return getExternalResourceAsStream(filePath);
		}
		InputStream in = null;
		URL url = getExternalResource(filePath, clazz);
		if (url != null) {
			return url.openStream();
		}
		File file = getExternalFile(filePath, clazz);
		if (file.isFile()) {
			return new FileInputStream(file);
		}
		return in;
	}

	public static InputStream getInternalOrExternalResourceAsStream(String filePath) {
		return getInternalOrExternalResourceAsStream(filePath, FileUtil.class);
	}

	public static InputStream getInternalOrExternalResourceAsStream(String filePath, Class<?> clazz) {
		String propertyFile = filePath;
		InputStream inSideInputStream = null;
		InputStream outSideInputStream = null;
		try {
			URL ufile = FileUtil.getResource(propertyFile, clazz);
			if (ufile != null) {
				inSideInputStream = ufile.openStream();
				// tenantProperties.load(inSideInputStream);
				if (inSideInputStream != null) {
					LOG.info("Loaded from classpath: {}", ufile.getPath());
					return inSideInputStream;
				} else {
					LOG.info("Stream is EMPTY from classpath: {}", ufile.getPath());
				}
			} else {
				LOG.info("URL is EMPTY from classpath: {}", propertyFile);
			}
		} catch (IllegalArgumentException | IOException e) {
			LOG.error("Fail:inSideInputStream:getResource", e);
		}

		try {
			outSideInputStream = FileUtil.getExternalResourceAsStream(propertyFile, clazz);
			if (outSideInputStream != null) {
				LOG.info("Loaded from jarpath: {}", propertyFile);
				return outSideInputStream;
			} else {
				LOG.info("Stream is EMPTY from jarpath: {}", propertyFile);
			}

		} catch (IllegalArgumentException | IOException e) {
			LOG.error("Fail:outSideInputStream:getExternalResourceAsStream", e);
		}
		return null;
	}

	public static InputStream getExternalOrInternalResourceAsStream(String filePath) {
		return getExternalOrInternalResourceAsStream(filePath, FileUtil.class);
	}

	public static InputStream getExternalOrInternalResourceAsStream(String filePath, Class<?> clazz) {
		String propertyFile = filePath;
		InputStream inSideInputStream = null;
		InputStream outSideInputStream = null;

		try {
			outSideInputStream = FileUtil.getExternalResourceAsStream(propertyFile, clazz);
			if (outSideInputStream != null) {
				LOG.info("Loaded from jarpath: {}", propertyFile);
				return outSideInputStream;
			} else {
				LOG.info("Stream is EMPTY from jarpath: {}", propertyFile);
			}

		} catch (IllegalArgumentException | IOException e) {
			LOG.error("Fail:outSideInputStream:getExternalResourceAsStream", e);
		}

		try {
			URL ufile = FileUtil.getResource(propertyFile, clazz);
			if (ufile != null) {
				inSideInputStream = ufile.openStream();
				// tenantProperties.load(inSideInputStream);
				if (inSideInputStream != null) {
					LOG.info("Loaded from classpath: {}", ufile.getPath());
					return inSideInputStream;
				} else {
					LOG.info("Stream is EMPTY from classpath: {}", ufile.getPath());
				}
			} else {
				LOG.info("URL is EMPTY from classpath: {}", propertyFile);
			}
		} catch (IllegalArgumentException | IOException e) {
			LOG.error("Fail:inSideInputStream:getResource", e);
		}

		return null;
	}

	public static File getExternalOrInternalFile(String filePath) {
		return getExternalOrInternalFile(filePath, FileUtil.class);
	}

	public static File getExternalOrInternalFile(String filePath, Class<?> clazz) {
		File file = getExternalFile(filePath, clazz);
		if (ArgUtil.is(file) && file.isFile()) {
			return file;
		}
		// Handle internal File

		return file;
	}
}