package com.boot.jx.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.Assert;

import com.boot.utils.ArgUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public final class StringToEnumIgnoringCaseConverterFactory implements ConverterFactory<String, Enum> {

	@Override
	public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
		Class<?> enumType = targetType;
		while (enumType != null && !enumType.isEnum()) {
			enumType = enumType.getSuperclass();
		}
		Assert.notNull(enumType, "The target type " + targetType.getName() + " does not refer to an enum");
		return new StringToEnum(enumType);
	}

	private class StringToEnum<T extends Enum> implements Converter<String, T> {

		private final Class<T> enumType;

		StringToEnum(Class<T> enumType) {
			this.enumType = enumType;
		}

		@Override
		public T convert(String source) {
			return ArgUtil.parseAsEnumIgnoreCase(source, this.enumType);
		}

	}

}