package com.boot.jx.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.boot.jx.validation.ValidationAnnotations.ValidPhone;
import com.boot.utils.ArgUtil;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    public static final Pattern P1 = Pattern.compile("^\\d{10}$");
    public static final Pattern P2 = Pattern.compile("^[1-9]{1}[0-9]{3,14}$");
    // XXX-XXX-XXXX , XXX.XXX.XXXX, XXX XXX XXXX
    public static final Pattern P3 = Pattern.compile("^\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$");
    // +XX-XXXX-XXXX, +XX.XXXX.XXXX, +XX XXXX XXXX
    public static final Pattern P4 = Pattern.compile("^\\+?([0-9]{2})\\)?[-. ]?([0-9]{4})[-. ]?([0-9]{4})");
    public static final Pattern P5 = Pattern
	    .compile("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$");
    public static final Pattern P6 = Pattern.compile("^(\\d{3}[- .]?){2}\\d{4}$");
    public static final Pattern P7 = Pattern.compile("^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$");

    @Override
    public void initialize(ValidPhone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

	if (!ArgUtil.is(value)) {
	    return false;
	}

	if (false || P1.matcher(value).matches() || P2.matcher(value).matches() || P3.matcher(value).matches()
		|| P4.matcher(value).matches() || P5.matcher(value).matches() || P6.matcher(value).matches()
		|| P7.matcher(value).matches()) {
	    return true;
	}
	String noSpaceValue = value.replaceAll("[\\ \\+]", "");

	if (false || P1.matcher(noSpaceValue).matches() || P2.matcher(noSpaceValue).matches()
		|| P3.matcher(noSpaceValue).matches() || P4.matcher(noSpaceValue).matches()
		|| P5.matcher(noSpaceValue).matches() || P6.matcher(noSpaceValue).matches()
		|| P7.matcher(noSpaceValue).matches()) {
	    return true;
	}

	return false;
    }

}
