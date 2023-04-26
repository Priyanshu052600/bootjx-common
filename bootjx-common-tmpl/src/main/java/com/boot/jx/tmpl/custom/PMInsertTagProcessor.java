package com.boot.jx.tmpl.custom;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardFragmentInsertionTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * The Class SayToAttributeTagProcessor.
 * 
 * @author lalittanwar
 *
 */
public class PMInsertTagProcessor extends AbstractStandardFragmentInsertionTagProcessor {

	public static final int PRECEDENCE = 100;
	public static final String ATTR_NAME = "insert";

	public PMInsertTagProcessor(final TemplateMode templateMode, final String dialectPrefix) {
		super(templateMode, dialectPrefix, ATTR_NAME, PRECEDENCE, false);
	}

	@Override
	protected void doProcess(
			final ITemplateContext context,
			final IProcessableElementTag tag,
			final AttributeName attributeName, final String attributeValue,
			final IElementTagStructureHandler structureHandler) {
		super.doProcess(context, tag, attributeName, attributeValue, structureHandler);
	}

}
