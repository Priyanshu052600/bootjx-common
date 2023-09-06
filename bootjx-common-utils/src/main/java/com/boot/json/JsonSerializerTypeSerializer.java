package com.boot.json;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * The Class JsonSerializerTypeSerializer.
 */
@SuppressWarnings("rawtypes")
@JsonComponent
public class JsonSerializerTypeSerializer extends JsonSerializer<JsonSerializerType> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object,
	 * org.codehaus.jackson.JsonGenerator,
	 * org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(JsonSerializerType value, JsonGenerator jgen, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		if (value.cannotSerialize()) {
			jgen.writeRawValue(value.toString());
		} else {
			jgen.writeObject(value.toObject());
		}

	}
}
