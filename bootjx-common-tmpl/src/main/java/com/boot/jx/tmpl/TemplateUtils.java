package com.boot.jx.tmpl;

import java.awt.Image;
import java.io.IOException;
import java.text.Bidi;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.boot.jx.dict.ContactType;
import com.boot.jx.logger.LoggerService;
import com.boot.jx.postman.PostManException;
import com.boot.jx.scope.tnt.TenantProperties;
import com.boot.utils.ArgUtil;
import com.boot.utils.Constants;
import com.boot.utils.ContextUtil;
import com.boot.utils.IoUtils;
import com.boot.utils.JsonUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;

/**
 * The Class TemplateUtils.
 */
@Component
public class TemplateUtils {

	private static final String CHECKED = "_NOT_FOUND_TEMPLATE_";

	/** The log. */
	private static Logger log = LoggerService.getLogger(TemplateUtils.class);

	/** The Constant base64. */
	private static final Map<String, String> base64 = new ConcurrentHashMap<String, String>();
	private static final Map<String, String> templateFiles = new ConcurrentHashMap<String, String>();

	private static final Cache<String, String> templateFilesExternal = CacheBuilder.newBuilder().maximumSize(10000)
			.expireAfterWrite(5, TimeUnit.MINUTES).build();

	private static boolean IS_TEMPLATE_SCANNED = false;

	/** The tenant properties. */
	@Autowired
	TenantProperties tenantProperties;

	/** The env. */
	@Autowired
	private Environment env;

	/** The application context. */
	@Autowired
	private ApplicationContext applicationContext;

	@Value("classpath*:*templates/html/**/*.html")
	private Resource[] htmlFiles;

	@Value("classpath*:*/templates/html/**/*.html")
	private Resource[] htmlFiles2;

	@Value("classpath*:*templates/json/*.json")
	private Resource[] jsonFiles;

	@Value("classpath*:*/templates/json/*.json")
	private Resource[] jsonFiles2;

	@Value("classpath*:*templates/jasper/*.jrxml")
	private Resource[] jasperFiles;

	@Value("classpath*:*/templates/jasper/*.jrxml")
	private Resource[] jasperFiles2;

	@Value("${jax.static.url}")
	String jaxStaticUrl;

	@Value("${jax.static.context}")
	String jaxStaticContext;

	@Value("${jax.static.path}")
	String jaxStaticPath;

	@Value("${spring.thymeleaf.cache}")
	boolean isTemplateCacheEnabled;

	public String getTemplateFile(String file, String tnt, Locale locale, ContactType contactType) {
		if (!isTemplateCacheEnabled) {
			IS_TEMPLATE_SCANNED = false;
			templateFiles.clear();
		}
		if (!IS_TEMPLATE_SCANNED) {
			try {
				for (Resource resource : htmlFiles) {
					String absPath = resource.getURI().toString().split("\\/templates\\/html\\/")[1];
					String[] fileName = absPath.split("\\.(?=[^\\.]+$)");
					String filePath = "html/" + fileName[0];
					templateFiles.put(filePath, filePath);
				}
				for (Resource resource : htmlFiles2) {
					String absPath = resource.getURI().toString().split("\\/templates\\/html\\/")[1];
					String[] fileName = absPath.split("\\.(?=[^\\.]+$)");
					String filePath = "html/" + fileName[0];
					templateFiles.put(filePath, filePath);
				}
				for (Resource resource : jsonFiles) {
					String absPath = resource.getURI().toString().split("\\/templates\\/json\\/")[1];
					String[] fileName = absPath.split("\\.(?=[^\\.]+$)");
					String filePath = "json/" + fileName[0];
					templateFiles.put(filePath, filePath);
				}
				for (Resource resource : jsonFiles2) {
					String absPath = resource.getURI().toString().split("\\/templates\\/json\\/")[1];
					String[] fileName = absPath.split("\\.(?=[^\\.]+$)");
					String filePath = "json/" + fileName[0];
					templateFiles.put(filePath, filePath);
				}
				for (Resource resource : jasperFiles) {
					String[] fileName = resource.getFilename().split("\\.(?=[^\\.]+$)");
					String filePath = "jasper/" + fileName[0];
					templateFiles.put(filePath, filePath);
				}
				for (Resource resource : jasperFiles2) {
					String[] fileName = resource.getFilename().split("\\.(?=[^\\.]+$)");
					String filePath = "jasper/" + fileName[0];
					templateFiles.put(filePath, filePath);
				}
				IS_TEMPLATE_SCANNED = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String fileCacheKey = String.format("%s:%s:%s:%s", file, locale, tnt, contactType);
		String specficFile = null;
		if (templateFiles.containsKey(fileCacheKey)) { // File is Scanned Already
			if (!CHECKED.equals(templateFiles.get(fileCacheKey))) { // File is already Scanned and Found
				return templateFiles.get(fileCacheKey);
			}
			// Template will be searched in External Folder
		} else { // File is Not scanned yet, this is first time
			specficFile = getValidTemplateFile(file, tnt, locale, contactType, false);
			if (ArgUtil.is(specficFile)) {
				templateFiles.put(fileCacheKey, specficFile);
				return specficFile;
			} else {
				specficFile = getValidTemplateFile(file, "shared", locale, contactType, false);
				if (ArgUtil.is(specficFile)) {
					templateFiles.put(fileCacheKey, specficFile);
					return specficFile;
				} else {
					templateFiles.put(fileCacheKey, CHECKED);
				}
			}
		}

		specficFile = templateFilesExternal.getIfPresent(fileCacheKey);
		if (ArgUtil.is(specficFile)) {
			return specficFile;
		}

		specficFile = getValidTemplateFile(file, tnt, locale, contactType, true);
		if (ArgUtil.is(specficFile)) {
			templateFilesExternal.put(fileCacheKey, jaxStaticContext + "/templates/" + specficFile);
			return jaxStaticContext + "/templates/" + specficFile;
		} else {
			specficFile = getValidTemplateFile(file, "shared", locale, contactType, true);
			if (ArgUtil.is(specficFile)) {
				templateFilesExternal.put(fileCacheKey, jaxStaticContext + "/templates/" + specficFile);
				return jaxStaticContext + "/templates/" + specficFile;
			} else {
				log.error("Template Not Found {}", fileCacheKey);
				if (!ArgUtil.is(fileCacheKey)) {
					throw new PostManException("Template Not Found");
				}
				return null;
			}
		}

	}

	private String getValidTemplateFile(String file, String tnt, Locale locale, ContactType contactType,
			boolean external) {
		String relativeFile = file;
		String folder = Constants.BLANK;
		String folderRoot = Constants.BLANK;
		String folderRootGlobal = Constants.BLANK;

		String specficFile = null;
		String ext = ".html";
		String subfolder = "html/";
		if (ArgUtil.is(contactType)) {
			if (file.startsWith("html/")) {
				relativeFile = file.replace("html/", Constants.BLANK);
			} else if (file.startsWith("json/")) {
				relativeFile = file.replace("json/", Constants.BLANK);
				subfolder = "json/";
				ext = ".json";
			} else if (file.startsWith("jasper/")) {
				relativeFile = file.replace("jasper/", Constants.BLANK);
				subfolder = "jasper/";
				ext = ".jrxml";
			}
			folder = subfolder + tnt + "/" + contactType.getShortCode() + "/";
			folderRoot = subfolder + tnt + "/";
			folderRootGlobal = subfolder + contactType.getShortCode() + "/";

			if (external) {
				specficFile = getValidTemplateFileExternal(folder, relativeFile, ext, tnt, locale);

				if (ArgUtil.isEmpty(specficFile)) {
					specficFile = getValidTemplateFileExternal(folderRoot, relativeFile, ext, tnt, locale);
				}

				if (ArgUtil.isEmpty(specficFile)) {
					specficFile = getValidTemplateFileExternal(folderRootGlobal, relativeFile, ext, tnt, locale);
				}

				if (ArgUtil.isEmpty(specficFile)) {
					return getValidTemplateFileExternal(Constants.BLANK, file, ext, tnt, locale);
				}
				return specficFile;
			}

			specficFile = getValidTemplateFileInternal(folder, relativeFile, tnt, locale);
			if (!ArgUtil.isEmpty(specficFile)) {
				return specficFile;
			}

			specficFile = getValidTemplateFileInternal(folderRoot, relativeFile, tnt, locale);
			if (!ArgUtil.isEmpty(specficFile)) {
				return specficFile;
			}

			specficFile = getValidTemplateFileInternal(folderRootGlobal, relativeFile, tnt, locale);
			if (!ArgUtil.isEmpty(specficFile)) {
				return specficFile;
			}
		}
		specficFile = getValidTemplateFileInternal(Constants.BLANK, file, tnt, locale);
		return specficFile;
	}

	private String getValidTemplateFileInternal(String folder, String relativeFile, String tnt, Locale locale) {
		String specficFile = String.format(folder + "%s_%s.%s", relativeFile, locale.getLanguage(),
				ArgUtil.parseAsString(tnt, Constants.BLANK).toLowerCase());

		if (templateFiles.containsKey(specficFile)) {
			return templateFiles.get(specficFile);
		}

		String tenantFile = String.format(folder + "%s.%s", relativeFile,
				ArgUtil.parseAsString(tnt, Constants.BLANK).toLowerCase());
		if (templateFiles.containsKey(tenantFile)) {
			templateFiles.put(specficFile, tenantFile);
			return tenantFile;
		}

		String localeFile = String.format(folder + "%s_%s", relativeFile, locale.getLanguage());
		if (templateFiles.containsKey(localeFile)) {
			templateFiles.put(specficFile, localeFile);
			return tenantFile;
		}

		String exactFile = folder + relativeFile;
		if (templateFiles.containsKey(exactFile)) {
			templateFiles.put(specficFile, exactFile);
			return exactFile;
		}

		return null;
	}

	private String getValidTemplateFileExternal(String folder, String relativeFile, String ext, String tnt,
			Locale locale) {
		String specficFile = String.format(folder + "%s_%s.%s", relativeFile, locale.getLanguage(),
				ArgUtil.parseAsString(tnt, Constants.BLANK).toLowerCase());

		Resource r = applicationContext.getResource("file:" + jaxStaticPath + "/templates/" + specficFile + ext);
		if (r != null && r.exists()) {
			return specficFile;
		}
		specficFile = String.format(folder + "%s.%s", relativeFile,
				ArgUtil.parseAsString(tnt, Constants.BLANK).toLowerCase());
		r = applicationContext.getResource("file:" + jaxStaticPath + "/templates/" + specficFile + ext);
		if (r != null && r.exists()) {
			return specficFile;
		}

		specficFile = String.format(folder + "%s_%s", relativeFile, locale.getLanguage());
		r = applicationContext.getResource("file:" + jaxStaticPath + "/templates/" + specficFile + ext);
		if (r != null && r.exists()) {
			return specficFile;
		}

		specficFile = folder + relativeFile;
		r = applicationContext.getResource("file:" + jaxStaticPath + "/templates/" + specficFile + ext);
		if (r != null && r.exists()) {
			return specficFile;
		}

		return null;
	}

	/**
	 * Prop.
	 *
	 * @param key the key
	 * @return the string
	 */
	public String prop(String key) {
		String value = tenantProperties.getProperties().getProperty(key);
		if (ArgUtil.isEmpty(value)) {
			value = env.getProperty(key);
		}
		return ArgUtil.parseAsString(value);
	}

	public String json(Object object) {
		return JsonUtil.toJson(object);
	}

	/**
	 * Image.
	 *
	 * @param key   the key
	 * @param clean the clean
	 * @return the string
	 */
	public String image(String key, boolean clean) {
		if (clean) {
			return this.prop(key).replace("data:image/png;base64,", "");
		}
		return this.prop(key);
	}

	/**
	 * Image jasper.
	 *
	 * @param key   the key
	 * @param clean the clean
	 * @return the image
	 * @throws JRException the JR exception
	 */
	public Image imageJasper(String key, boolean clean) throws JRException {
		return net.sf.jasperreports.engine.util.JRImageLoader.getInstance(new SimpleJasperReportsContext())
				.loadAwtImageFromBytes(javax.xml.bind.DatatypeConverter.parseBase64Binary(image(key, clean)));
	}

	/**
	 * Image.
	 *
	 * @param key the key
	 * @return the string
	 */
	public String image(String key) {
		return this.image(key, false);
	}

	/**
	 * Image jasper.
	 *
	 * @param key the key
	 * @return the image
	 * @throws JRException the JR exception
	 */
	public Image imageJasper(String key) throws JRException {
		return this.imageJasper(key, false);
	}

	/**
	 * Reverse flag.
	 *
	 * @param set the set
	 */
	public static void reverseFlag(boolean set) {
		ContextUtil.map().put("reverseflag", true);
	}

	/**
	 * Reverse flag.
	 *
	 * @return true, if successful
	 */
	public static boolean reverseFlag() {
		return ArgUtil.parseAsBoolean(ContextUtil.map().get("reverseflag"), false);
	}

	/**
	 * Reverse.
	 *
	 * @param str the str
	 * @return the string
	 */
	public String reverse(String str) {
		if (reverseFlag()) {
			return fixBiDi(str);
			// return new StringBuilder(str).reverse().toString();
		}
		return str;
	}

	/**
	 * Reverse.
	 *
	 * @return the string
	 */
	public String reverse() {
		return "-X-X-";
	}

	/**
	 * Fix bi di.
	 *
	 * @param wordTemp the word temp
	 * @return the string
	 */
	public static String fixBiDi(String wordTemp) {
		String word = ArgUtil.parseAsString(wordTemp, Constants.DEFAULT_STRING);
		Bidi bidi = new Bidi(word, -2);
		if (!bidi.isMixed() && bidi.getBaseLevel() == 0) {
			return word;
		} else {
			int runCount = bidi.getRunCount();
			byte[] levels = new byte[runCount];
			Integer[] runs = new Integer[runCount];

			for (int result = 0; result < runCount; ++result) {
				levels[result] = (byte) bidi.getRunLevel(result);
				runs[result] = Integer.valueOf(result);
			}

			Bidi.reorderVisually(levels, 0, runs, 0, runCount);
			StringBuilder bidiText = new StringBuilder();

			for (int i = 0; i < runCount; ++i) {
				int index = runs[i].intValue();
				int start = bidi.getRunStart(index);
				int end = bidi.getRunLimit(index);
				byte level = levels[index];
				if ((level & 1) != 0) {
					while (true) {
						--end;
						if (end < start) {
							break;
						}

						char character = word.charAt(end);
						if (Character.isMirrored(word.codePointAt(end))) {
							bidiText.append(character);
						} else {
							bidiText.append(character);
						}
					}
				} else {
					bidiText.append(word, start, end);
				}
			}

			return bidiText.toString();
		}
	}

	/**
	 * Read as base 64 string.
	 *
	 * @param contentId the content id
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String readAsBase64String(String contentId) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("data:image/png;base64,");
		String base64String = null;

		if (base64.containsKey(contentId)) {
			base64String = base64.get(contentId);
		} else if (contentId.startsWith(jaxStaticContext)) {
			byte[] imageByteArray = IoUtils.toByteArray(
					applicationContext.getResource("file:" + jaxStaticUrl + "/" + contentId).getInputStream());
			base64String = StringUtils.newStringUtf8(Base64.encodeBase64(imageByteArray, false));
		} else {
			byte[] imageByteArray = IoUtils
					.toByteArray(applicationContext.getResource("classpath:" + contentId).getInputStream());
			base64String = StringUtils.newStringUtf8(Base64.encodeBase64(imageByteArray, false));
			base64.put(contentId, base64String);
		}

		sb.append(base64String);
		return sb.toString();
	}

	/**
	 * Read as resource.
	 *
	 * @param contentId the content id
	 * @return the resource
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Resource readAsResource(String contentId) throws IOException {
		if (contentId.startsWith(jaxStaticContext)) {
			return applicationContext.getResource("file:" + jaxStaticUrl + "/" + contentId);
		} else {
			return applicationContext.getResource("classpath:" + contentId);
		}
	}

	/**
	 * Fix bi di check.
	 *
	 * @param parseAsString the parse as string
	 * @return the string
	 */
	public static String fixBiDiCheck(String parseAsString) {
		if (reverseFlag()) {
			return fixBiDi(parseAsString);
		}
		return parseAsString;
	}

	public static final Pattern PATTERN_CID = Pattern.compile("src=\"cid:(.*?)\"");

}
