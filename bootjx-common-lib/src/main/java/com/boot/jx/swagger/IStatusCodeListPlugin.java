package com.boot.jx.swagger;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.core.annotation.AnnotationUtils;

import com.boot.jx.exception.IExceptionEnum;
import com.boot.jx.logger.LoggerService;

import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

/**
 * 
 * @author lalittanwar
 *
 * @param <S>
 * @param <A>
 */
public abstract class IStatusCodeListPlugin<S extends IExceptionEnum, A extends Annotation>
	implements OperationBuilderPlugin {

    Logger logger = LoggerService.getLogger(getClass());

    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApiStatusService {
	Class<?>[] value() default IService.class;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
	return true;
    }

    public abstract Class<A> getAnnotionClass();

    public abstract S[] getValues(A annotation);

    private Method getMothod(Class<?> clazz, String methodName, int methodParamCount) {
	Method methodThis = null;
	for (Method method : clazz.getMethods()) {
	    if (methodName.equals(method.getName())
		    && (methodThis == null || methodParamCount == method.getParameterCount())) {
		methodThis = method;
	    }
	}
	return methodThis;
    }

    @Override
    public void apply(OperationContext context) {
	Set<ResponseMessage> respSt = new HashSet<ResponseMessage>();

	Optional<ApiStatusService> classAnnotation = com.google.common.base.Optional
		.toJavaUtil(context.findControllerAnnotation(ApiStatusService.class));

	if (classAnnotation.isPresent()) {
	    String methodName = context.getName();
	    int methodParamCount = context.getParameters().size();
	    Class<?>[] clazzes = classAnnotation.get().value();
	    for (Class<?> clazz : clazzes) {
		Method methodThis = getMothod(clazz, methodName, methodParamCount);
		if (methodThis != null) {
		    A ann = AnnotationUtils.findAnnotation(methodThis, getAnnotionClass());
		    if (ann != null) {
			S[] value = getValues(ann);
			for (S amxError : value) {
			    respSt.add(new ResponseMessageBuilder().code(amxError.getStatusCode())
				    .message(amxError.getStatusKey()).build());
			}
		    }
		}

	    }
	}

	Optional<A> annotation = com.google.common.base.Optional.toJavaUtil(context.findAnnotation(getAnnotionClass()));
	if (annotation.isPresent() && getValues(annotation.get()) != null) {
	    S[] value = getValues(annotation.get());
	    for (S amxError : value) {
		respSt.add(new ResponseMessageBuilder().code(amxError.getStatusCode()).message(amxError.getStatusKey())
			.build());
	    }
	}

	context.operationBuilder().responseMessages(respSt);
    }

}
