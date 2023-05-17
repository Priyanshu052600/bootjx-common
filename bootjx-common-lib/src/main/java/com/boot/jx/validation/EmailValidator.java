package com.boot.jx.validation;

import java.util.regex.Pattern;

import com.boot.jx.validation.ValidationAnnotations.ValidEmail;
import com.boot.utils.ArgUtil;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    public static final Pattern P1 = Pattern
	    .compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}");

    @Override
    public void initialize(ValidEmail constraintAnnotation) {

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
