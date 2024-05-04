package com.boot.jx.convertor;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;

import com.boot.utils.TimeUtils.TimePeriod;

@ConfigurationPropertiesBinding
public class StringToTimePeriod implements Converter<String, TimePeriod> {
	@Override
	public TimePeriod convert(String source) {
		TimePeriod period = new TimePeriod();
		period.fromString(source);
		return period;
	}

}