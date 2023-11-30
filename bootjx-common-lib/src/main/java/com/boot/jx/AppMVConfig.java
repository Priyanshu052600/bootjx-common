package com.boot.jx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.boot.jx.convertor.StringToEnumIgnoringCaseConverterFactory;
import com.boot.jx.convertor.StringToTimePeriod;
import com.boot.jx.filter.AppRequestInterceptor;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppMVConfig implements WebMvcConfigurer {

    @Autowired
    private AppRequestInterceptor appRequestInterceptor;
    /** Pattern relative to templates base used to match text templates. */
    public static final String TEXT_TEMPLATES_RESOLVE_PATTERN = "text/*";
    /** Pattern relative to templates base used to match JSON templates. */
    public static final String JSON_TEMPLATES_RESOLVE_PATTERN = "json/*";
    /** Pattern relative to templates base used to match JSON templates. */
    public static final String JS_TEMPLATES_RESOLVE_PATTERN = "js/*";
    /** Pattern relative to templates base used to match XML templates. */
    public static final String XML_TEMPLATES_RESOLVE_PATTERN = "xml/*";
    public static final String TEMPLATES_BASE = "classpath:/templates/";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
		if (appRequestInterceptor != null) {
			registry.addInterceptor(appRequestInterceptor);
		}
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
	registry.addConverterFactory(new StringToEnumIgnoringCaseConverterFactory());
	registry.addConverter(new StringToTimePeriod());
    }

}
