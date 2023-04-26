package com.boot.jx.swagger;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.util.StringUtils.hasText;

import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.core.annotation.AnnotationUtils;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
//import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableRangeValues;
import springfox.documentation.service.AllowableValues;

/**
 * 
 * @author lalittanwar
 *
 */
public class ApiMockModelProperties {
    private ApiMockModelProperties() {
	throw new UnsupportedOperationException();
    }

    public static Function<ApiMockModelProperty, AllowableValues> toAllowableValues() {
	return new Function<ApiMockModelProperty, AllowableValues>() {
	    @Override
	    public AllowableValues apply(ApiMockModelProperty annotation) {
		return allowableValueFromString(annotation.allowableValues());
	    }
	};
    }

    public static AllowableValues allowableValueFromString(String allowableValueString) {
	AllowableValues allowableValues = new AllowableListValues(Lists.<String>newArrayList(), "LIST");
	String trimmed = allowableValueString.trim();
	if (trimmed.startsWith("range[")) {
	    trimmed = trimmed.replaceAll("range\\[", "").replaceAll("]", "");
	    Iterable<String> split = Splitter.on(',').trimResults().omitEmptyStrings().split(trimmed);
	    List<String> ranges = newArrayList(split);
	    allowableValues = new AllowableRangeValues(ranges.get(0), ranges.get(1));
	} else if (trimmed.contains(",")) {
	    Iterable<String> split = Splitter.on(',').trimResults().omitEmptyStrings().split(trimmed);
	    allowableValues = new AllowableListValues(newArrayList(split), "LIST");
	} else if (hasText(trimmed)) {
	    List<String> singleVal = Collections.singletonList(trimmed);
	    allowableValues = new AllowableListValues(singleVal, "LIST");
	}
	return allowableValues;
    }

    public static Function<ApiMockModelProperty, Boolean> toIsRequired() {
	return new Function<ApiMockModelProperty, Boolean>() {
	    @Override
	    public Boolean apply(ApiMockModelProperty annotation) {
		return annotation.required();
	    }
	};
    }

    public static Function<ApiMockModelProperty, Integer> toPosition() {
	return new Function<ApiMockModelProperty, Integer>() {
	    @Override
	    public Integer apply(ApiMockModelProperty annotation) {
		return annotation.position();
	    }
	};
    }

    public static Function<ApiMockModelProperty, Boolean> toIsReadOnly() {
	return new Function<ApiMockModelProperty, Boolean>() {
	    @Override
	    public Boolean apply(ApiMockModelProperty annotation) {
		return annotation.readOnly();
	    }
	};
    }

    public static Function<ApiMockModelProperty, String> toDescription() {
	return new Function<ApiMockModelProperty, String>() {
	    @Override
	    public String apply(ApiMockModelProperty annotation) {
		String description = "";
		if (!Strings.isNullOrEmpty(annotation.value())) {
		    description = annotation.value();
		} else if (!Strings.isNullOrEmpty(annotation.notes())) {
		    description = annotation.notes();
		}
		return description;
	    }
	};
    }

    public static Function<ApiMockModelProperty, String> toName() {
	return new Function<ApiMockModelProperty, String>() {
	    @Override
	    public String apply(ApiMockModelProperty annotation) {
		String name = null;
		if (!Strings.isNullOrEmpty(annotation.name())) {
		    name = annotation.name();
		}
		return name;
	    }
	};
    }

    public static Function<ApiMockModelProperty, ResolvedType> toType(final TypeResolver resolver) {
	return new Function<ApiMockModelProperty, ResolvedType>() {
	    @Override
	    public ResolvedType apply(ApiMockModelProperty annotation) {
		try {
		    return resolver.resolve(Class.forName(annotation.dataType()));
		} catch (ClassNotFoundException e) {
		    return resolver.resolve(Object.class);
		}
	    }
	};
    }

    public static Optional<ApiMockModelProperty> findApiModePropertyAnnotation(AnnotatedElement annotated) {
	return Optional.ofNullable(AnnotationUtils.getAnnotation(annotated, ApiMockModelProperty.class));
    }

    public static Function<ApiMockModelProperty, Boolean> toHidden() {
	return new Function<ApiMockModelProperty, Boolean>() {
	    @Override
	    public Boolean apply(ApiMockModelProperty annotation) {
		return annotation.hidden();
	    }
	};
    }

    public static Function<ApiMockModelProperty, String> toExample() {
	return new Function<ApiMockModelProperty, String>() {
	    @Override
	    public String apply(ApiMockModelProperty annotation) {
		String example = "";
		if (!Strings.isNullOrEmpty(annotation.example())) {
		    example = annotation.example();
		}
		return example;
	    }
	};
    }
}
