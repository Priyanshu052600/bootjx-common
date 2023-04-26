package com.boot.jx.tmpl.custom;

import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresource.FileTemplateResource;
import org.thymeleaf.templateresource.ITemplateResource;

import com.boot.jx.AppContextUtil;
import com.boot.jx.dict.ContactType;
import com.boot.jx.dict.Language;
import com.boot.jx.tmpl.TemplateUtils;
import com.boot.utils.ArgUtil;
import com.boot.utils.ContextUtil;

public class CustomFileTemplateResolver extends AbstractConfigurableTemplateResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomFileTemplateResolver.class);

    TemplateUtils templateUtils;

    String staticUrl;

    String staticContext;

    String jsonDirectory;
    String htmlDirectory;

    public CustomFileTemplateResolver() {
	super();

    }

    public CustomFileTemplateResolver templateUtils(TemplateUtils templateUtils) {
	this.templateUtils = templateUtils;
	return this;
    }

    public CustomFileTemplateResolver staticPath(String staticUrl, String staticContext) {
	this.staticUrl = staticUrl;
	if (!staticUrl.endsWith("/")) {
	    this.staticUrl = this.staticUrl + "/";
	}
	this.staticContext = staticContext;
	this.jsonDirectory = this.staticContext + "/templates/json/";
	this.htmlDirectory = this.staticContext + "/templates/html/";
	return this;
    }

    @Override
    protected ITemplateResource computeTemplateResource(final IEngineConfiguration configuration,
	    final String ownerTemplate, final String template, final String resourceName,
	    final String characterEncoding, final Map<String, Object> templateResolutionAttributes) {

	Locale locale = ArgUtil.parseAsT(ContextUtil.map().get("template_locale"), new Locale(Language.EN.getCode()),
		false);

	ContactType contactType = ArgUtil.parseAsEnumT(ContextUtil.map().get("template_contactType"), ContactType.EMAIL,
		ContactType.class);
	String fileName = templateUtils.getTemplateFile(resourceName, AppContextUtil.getTenant(), locale, contactType);

	String fileNameFull = fileName;
	if (ArgUtil.is(fileNameFull)) {
	    if (fileName.startsWith(this.jsonDirectory)) {
		fileNameFull = this.staticUrl + fileName + ".json";
	    } else {
		fileNameFull = this.staticUrl + fileName + ".html";
	    }
	} else {
	    LOGGER.warn("No file for" + " ownerTemplate:{}   template:{}  resourceName:{}", ownerTemplate, template,
		    resourceName);
	}

	return new FileTemplateResource(fileNameFull, characterEncoding);
    }

}
