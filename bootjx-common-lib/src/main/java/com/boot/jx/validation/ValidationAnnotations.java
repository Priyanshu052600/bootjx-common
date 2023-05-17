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

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

public class ValidationAnnotations {

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @Constraint(validatedBy = EmailValidator.class)
    public @interface ValidEmail {
	/**
	 * @return the regular expression to match
	 */
	String regexp() default "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";

	/**
	 * @return the error message template
	 */
	String message() default "Must be a valid email address";

	/**
	 * @return the groups the constraint belongs to
	 */
	Class<?>[] groups() default {};

	/**
	 * @return the payload associated to the constraint
	 */
	Class<? extends Payload>[] payload() default {};
    }

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @Constraint(validatedBy = PhoneValidator.class)
    public @interface ValidPhone {
	/**
	 * @return the error message template
	 */
	String message() default "Enter valid mobile number eg +91 XXXXX XXXXX'";

	/**
	 * @return the groups the constraint belongs to
	 */
	Class<?>[] groups() default {};

	/**
	 * @return the payload associated to the constraint
	 */
	Class<? extends Payload>[] payload() default {};
    }

}
