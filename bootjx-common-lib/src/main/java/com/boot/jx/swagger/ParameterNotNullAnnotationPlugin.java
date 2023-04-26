package com.boot.jx.swagger;

import static com.boot.jx.swagger.ApiMockModelProperties.findApiModePropertyAnnotation;
import static com.boot.jx.swagger.ApiMockModelProperties.toAllowableValues;
import static com.boot.jx.swagger.ApiMockModelProperties.toDescription;
import static com.boot.jx.swagger.ApiMockModelProperties.toExample;
import static com.boot.jx.swagger.ApiMockModelProperties.toHidden;
import static com.boot.jx.swagger.ApiMockModelProperties.toIsReadOnly;
import static com.boot.jx.swagger.ApiMockModelProperties.toIsRequired;
import static com.boot.jx.swagger.ApiMockModelProperties.toPosition;
import static com.boot.jx.swagger.ApiMockModelProperties.toType;
import static springfox.documentation.schema.Annotations.findPropertyAnnotation;

import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.boot.utils.ArgUtil;
import com.boot.utils.CollectionUtil;
import com.google.common.base.Optional;

import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * 
 * @author lalittanwar
 *
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ParameterNotNullAnnotationPlugin implements ModelPropertyBuilderPlugin {

    @Override
    public void apply(ModelPropertyContext context) {
	com.google.common.base.Optional<ApiMockModelProperty> annotation = com.google.common.base.Optional.absent();

	if (context.getAnnotatedElement().isPresent()) {
	    annotation = annotation
		    .or(Optional.fromJavaUtil(findApiModePropertyAnnotation(context.getAnnotatedElement().get())));
	}
	if (context.getBeanPropertyDefinition().isPresent()) {
	    annotation = annotation.or(
		    (findPropertyAnnotation(context.getBeanPropertyDefinition().get(), ApiMockModelProperty.class)));
	}

	if (annotation.isPresent()) {
	    context.getBuilder()
		    // .name(annotation.transform(toName()).orNull())
		    .allowableValues(annotation.transform(toAllowableValues()).orNull())
		    .required(annotation.transform(toIsRequired()).or(true))
		    // .required(true)
		    .readOnly(annotation.transform(toIsReadOnly()).or(false))
		    .description(annotation.transform(toDescription()).orNull())
		    .isHidden(annotation.transform(toHidden()).or(false))
		    .type(annotation.transform(toType(context.getResolver())).orNull())
		    .position(annotation.transform(toPosition()).or(0))
		    .example(annotation.transform(toExample()).orNull());

	    if (ArgUtil.is(annotation.get().notes())) {
		List<VendorExtension> notesExtensions = CollectionUtil
			.getList(new StringVendorExtension("notes", annotation.get().notes()));
		context.getBuilder().extensions(notesExtensions);
	    }

	} else {
	    context.getBuilder()
		    // .allowableValues(annotation.transform(toAllowableValues()).orNull())
		    .required(annotation.transform(toIsRequired()).or(true))
		    .isHidden(annotation.transform(toHidden()).or(false))
	    // .required(true)
	    // .readOnly(annotation.transform(toIsReadOnly()).or(false))
	    // .description(annotation.transform(toDescription()).orNull())
	    // .isHidden(annotation.transform(toHidden()).or(false))
	    // .type(annotation.transform(toType(context.getResolver())).orNull())
	    // .position(annotation.transform(toPosition()).or(0))
	    // .example(annotation.transform(toExample()).orNull())
	    ;
	}
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
	return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
