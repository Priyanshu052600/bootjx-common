package com.boot.jx.rbac.session;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class FailSafeRedisObjectSerializer implements RedisSerializer<Object> {

	static final byte[] EMPTY_ARRAY = new byte[0];

	static boolean isEmpty(@Nullable byte[] data) {
		return (data == null || data.length == 0);
	}

	private final Converter<Object, byte[]> serializer;
	private final Converter<byte[], Object> deserializer;

	/**
	 * Creates a new {@link JdkSerializationRedisSerializer} using the default class
	 * loader.
	 */
	public FailSafeRedisObjectSerializer() {
		this(new SerializingConverter(), new DeserializingConverter());
	}

	/**
	 * Creates a new {@link JdkSerializationRedisSerializer} using a
	 * {@link ClassLoader}.
	 *
	 * @param classLoader
	 * @since 1.7
	 */
	public FailSafeRedisObjectSerializer(ClassLoader classLoader) {
		this(new SerializingConverter(), new DeserializingConverter(classLoader));
	}

	/**
	 * Creates a new {@link JdkSerializationRedisSerializer} using a
	 * {@link Converter converters} to serialize and deserialize objects.
	 *
	 * @param serializer   must not be {@literal null}
	 * @param deserializer must not be {@literal null}
	 * @since 1.7
	 */
	public FailSafeRedisObjectSerializer(Converter<Object, byte[]> serializer, Converter<byte[], Object> deserializer) {

		Assert.notNull(serializer, "Serializer must not be null!");
		Assert.notNull(deserializer, "Deserializer must not be null!");

		this.serializer = serializer;
		this.deserializer = deserializer;
	}

	public Object deserialize(@Nullable byte[] bytes) {

		if (isEmpty(bytes)) {
			return null;
		}

		try {
			return deserializer.convert(bytes);
		} catch (Exception ex) {
			// throw new SerializationException("Cannot deserialize", ex);
			return null;
		}
	}

	@Override
	public byte[] serialize(@Nullable Object object) {
		if (object == null) {
			return EMPTY_ARRAY;
		}
		try {
			return serializer.convert(object);
		} catch (Exception ex) {
			throw new SerializationException("Cannot serialize", ex);
		}
	}
}
