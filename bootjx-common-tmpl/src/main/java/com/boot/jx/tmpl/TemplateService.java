package com.boot.jx.tmpl;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.boot.jx.AppConfig;
import com.boot.jx.AppContextUtil;
import com.boot.jx.dict.ContactType;
import com.boot.jx.dict.FileFormat;
import com.boot.jx.model.CommonFile;
import com.boot.jx.postman.model.ITemplates;
import com.boot.jx.postman.model.ITemplates.ITemplate;
import com.boot.jx.tmpl.custom.HelloDialect;
import com.boot.utils.ArgUtil;
import com.boot.utils.Constants;
import com.boot.utils.IoUtils;
import com.boot.utils.StringUtils;

/**
 * The Class TemplateService.
 */
@Component
public class TemplateService {

	/** The log. */
	private Logger log = Logger.getLogger(getClass());

	/** The application context. */
	@Autowired
	private ApplicationContext applicationContext;

	/** The template engine. */
	@Autowired
	@Qualifier("messageTemplateEngine")
	private SpringTemplateEngine templateEngine;

	/** The text template engine. */
	@Autowired
	private SpringTemplateEngine textTemplateEngine;

	/** The message source. */
	@Autowired
	private MessageSource messageSource;

	/** The template utils. */
	@Autowired
	private TemplateUtils templateUtils;

	/** The post man config. */
	@Autowired
	private TmplConfig postManConfig;

	@Autowired
	private TemplateModelCache templateModelCache;

	@Autowired
	private AppConfig appConfig;

	/**
	 * Instantiates a new template service.
	 *
	 * @param templateEngine the template engine
	 */
	@Autowired
	TemplateService(TemplateEngine templateEngine) {
		templateEngine.addDialect(new HelloDialect());
	}

	/**
	 * Process html.
	 *
	 * @param template    the template
	 * @param context     the context
	 * @param contactType
	 * @return the string
	 */
	public String processHtml(ITemplate template, Context context, Locale locale, ContactType contactType) {
		String tmplt = templateUtils.getTemplateFile(template.getHtmlFile(), AppContextUtil.getTenant(), locale,
				contactType);
		if (!ArgUtil.is(tmplt)) {
			return Constants.BLANK;
		}
		String rawStr = templateEngine.process(tmplt, context);
		Pattern p = Pattern.compile("src=\"inline:(.*?)\"");
		Matcher m = p.matcher(rawStr);
		while (m.find()) {
			String contentId = m.group(1);
			try {
				rawStr = rawStr.replace("src=\"inline:" + contentId + "\"",
						"src=\"" + templateUtils.readAsBase64String(contentId) + "\"");
			} catch (IOException e) {
				log.error("Template parsing Error : " + template.getFileName(), e);
			}
		}

		return rawStr;
	}

	public String processSMS(ITemplate template, Context context, Locale locale, ContactType contactType) {
		String rawStr = templateEngine.process(
				templateUtils.getTemplateFile(template.getSMSFile(), AppContextUtil.getTenant(), locale, contactType),
				context);

		Pattern p = Pattern.compile("src=\"inline:(.*?)\"");
		Matcher m = p.matcher(rawStr);
		while (m.find()) {
			String contentId = m.group(1);
			try {
				rawStr = rawStr.replace("src=\"inline:" + contentId + "\"",
						"src=\"" + templateUtils.readAsBase64String(contentId) + "\"");
			} catch (IOException e) {
				log.error("Template parsing Error : " + template.getFileName(), e);
			}
		}
		return rawStr;
	}

	public String processJson(ITemplate template, Context context, Locale locale, ContactType contactType) {
		return templateEngine.process(
				templateUtils.getTemplateFile(template.getJsonFile(), AppContextUtil.getTenant(), locale, contactType),
				context);
	}

	/**
	 * Gets the local.
	 *
	 * @param file the file
	 * @return the local
	 */
	private Locale getLocal(CommonFile file) {
		return postManConfig.getLocal(file);
	}

	/**
	 * Parses file.template and creates content;
	 * 
	 * @param file the file
	 * @return the file
	 */
	public CommonFile process(CommonFile file) {
		return this.process(file, null);
	}

	public CommonFile process(CommonFile file, ContactType contactType) {
		Locale locale = getLocal(file);

		if (file.getFileFormat() == FileFormat.PDF) {
			String reverse = messageSource.getMessage("flag.reverse.char", null, locale);
			if (("true".equalsIgnoreCase(reverse))) {
				TemplateUtils.reverseFlag(true);
			}
			if (log.isDebugEnabled()) {
				log.debug("====" + locale.toString() + "======" + reverse + "   " + TemplateUtils.reverseFlag());
			}
		}

		Context context = new Context(locale);

		context.setVariable("_tu", templateUtils);
		context.setVariable("_type", ArgUtil.parseAsString(contactType, Constants.BLANK));

		context.setVariables(file.getModel());

		ITemplate iTemplate = ITemplates.getTemplate(
				ArgUtil.is(file.template().getCode()) ? file.template().getCode() : file.template().getId());

//		if (!appConfig.isProdMode() && appConfig.isCache()) {
//			templateModelCache.put(iTemplate.getSampleJSON(), file.getModel());
//		}

		AppContextUtil.set("template_locale", locale);
		AppContextUtil.set("template_contactType", contactType);

		if (iTemplate.isThymleaf()) {
			String content;
			if (file.getFileFormat() == FileFormat.JSON || ContactType.PUSH == contactType) {
				content = this.processJson(iTemplate, context, locale, contactType);
			} else {
				content = this.processHtml(iTemplate, context, locale, contactType);
			}
			if (ArgUtil.is(content)) {
				String[] x = content.split("---options---");
				file.setContent(x[0]);
				if (x.length > 1 && ArgUtil.is(x[1])) {
					file.options().putAll(StringUtils.toMap(x[1]));
				}
			}
		}
		return file;
	}

	/**
	 * Process text.
	 *
	 * @param template the template
	 * @param context  the context
	 * @return the string
	 */
	public String processText(ITemplate template, Context context) {
		return textTemplateEngine.process(template.getFileName(), context);
	}

	/**
	 * Read image as input stream source.
	 *
	 * @param contentId the content id
	 * @return the input stream source
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public InputStreamSource readImageAsInputStreamSource(String contentId) throws IOException {
		InputStreamSource imageSource = new ByteArrayResource(
				IoUtils.toByteArray(applicationContext.getResource("classpath:" + contentId).getInputStream()));
		return imageSource;
	}

}
