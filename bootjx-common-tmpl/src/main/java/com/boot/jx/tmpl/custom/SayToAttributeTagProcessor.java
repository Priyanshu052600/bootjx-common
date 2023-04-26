package com.boot.jx.tmpl.custom;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.LazyEscapingCharSequence;
import org.unbescape.html.HtmlEscape;
import org.unbescape.xml.XmlEscape;

import com.boot.jx.tmpl.TemplateUtils;
import com.boot.utils.ArgUtil;

/**
 * The Class SayToAttributeTagProcessor.
 * 
 * @author lalittanwar
 *
 */
public class SayToAttributeTagProcessor extends AbstractStandardExpressionAttributeTagProcessor {

	/** The Constant PRECEDENCE. */
	public static final int PRECEDENCE = 1300;

	/** The Constant ATTR_NAME. */
	public static final String ATTR_NAME = "pftext";

	/**
	 * Instantiates a new say to attribute tag processor.
	 *
	 * @param templateMode
	 *            the template mode
	 * @param dialectPrefix
	 *            the dialect prefix
	 */
	public SayToAttributeTagProcessor(final TemplateMode templateMode, final String dialectPrefix) {
		super(templateMode, dialectPrefix, ATTR_NAME, PRECEDENCE, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.thymeleaf.standard.processor.
	 * AbstractStandardExpressionAttributeTagProcessor#doProcess(org.thymeleaf.
	 * context.ITemplateContext, org.thymeleaf.model.IProcessableElementTag,
	 * org.thymeleaf.engine.AttributeName, java.lang.String, java.lang.Object,
	 * org.thymeleaf.processor.element.IElementTagStructureHandler)
	 */
	@Override
	protected void doProcess(final ITemplateContext context, final IProcessableElementTag tag,
			final AttributeName attributeName, final String attributeValue, final Object expressionResult2,
			final IElementTagStructureHandler structureHandler) {

		final TemplateMode templateMode = getTemplateMode();

		/*
		 * Depending on the template mode and the length of the text to be output
		 * escaped, we will try to opt for the most resource-efficient alternative.
		 *
		 * * If we are outputting RAW, there is no escape to do, just pass through. * If
		 * we are outputting HTML, XML or TEXT we know output will be textual (result of
		 * calling .toString() on the expression result), and therefore we can decide
		 * between an immediate vs lazy escaping alternative depending on size. We will
		 * perform lazy escaping, writing directly to output Writer, if length > 100. *
		 * If we are outputting JAVASCRIPT or CSS, we will always pass the expression
		 * result unchanged to a lazy escape processor, so that whatever the JS/CSS
		 * serializer wants to do, it does it directly on the output Writer and the
		 * entire results are never really needed in memory.
		 */
		
		Object expressionResult = TemplateUtils.fixBiDiCheck(ArgUtil.parseAsString(expressionResult2));

		final CharSequence text;

		if (templateMode != TemplateMode.JAVASCRIPT && templateMode != TemplateMode.CSS) {

			final String input = (expressionResult == null ? "" : expressionResult.toString());

			if (templateMode == TemplateMode.RAW) {
				// RAW -> just output

				text = input;

			} else {

				if (input.length() > 100) {
					// Might be a large text -> Lazy escaping on the output Writer
					text = new LazyEscapingCharSequence(context.getConfiguration(), templateMode, input);
				} else {
					// Not large -> better use a bit more of memory, but be faster
					text = produceEscapedOutput(templateMode, input);
				}

			}

		} else {
			// JavaScript and CSS serializers always work directly on the output Writer, no
			// need to store the entire
			// serialized contents in memory (unless the Writer itself wants to do so).

			text = new LazyEscapingCharSequence(context.getConfiguration(), templateMode, expressionResult);

		}

		// Report the result to the engine, whichever the type of process we have
		// applied
		structureHandler.setBody(text, false);

	}

	/**
	 * Produce escaped output.
	 *
	 * @param templateMode
	 *            the template mode
	 * @param input
	 *            the input
	 * @return the string
	 */
	private static String produceEscapedOutput(final TemplateMode templateMode, final String input) {

		switch (templateMode) {

		case TEXT:
			// fall-through
		case HTML:
			return HtmlEscape.escapeHtml4Xml(input);
		case XML:
			return XmlEscape.escapeXml10(input);
		default:
			throw new TemplateProcessingException("Unrecognized template mode " + templateMode
					+ ". Cannot produce escaped output for " + "this template mode.");
		}

	}

}
