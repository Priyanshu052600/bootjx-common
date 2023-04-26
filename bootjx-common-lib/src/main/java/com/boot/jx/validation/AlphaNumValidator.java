package com.boot.jx.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.boot.jx.validation.AlphaNumValidator.ValidAlphaNum;
import com.boot.utils.ArgUtil;

public class AlphaNumValidator implements ConstraintValidator<ValidAlphaNum, String> {

    public static final Pattern P1 = Pattern.compile("^[a-zA-Z0-9]*$");

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @Constraint(validatedBy = AlphaNumValidator.class)
    public @interface ValidAlphaNum {
	/**
	 * @return the error message template
	 */
	String message() default "Enter valid alpha numeric";

	/**
	 * @return the groups the constraint belongs to
	 */
	Class<?>[] groups() default {};

	/**
	 * @return the payload associated to the constraint
	 */
	Class<? extends Payload>[] payload() default {};
    }

    @Override
    public void initialize(ValidAlphaNum constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
	if (!ArgUtil.is(value)) {
	    return false;
	}
	if (false || P1.matcher(value).matches()) {
	    return true;
	}
	return false;
    }

}
