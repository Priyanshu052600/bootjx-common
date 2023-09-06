package com.boot.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.boot.jackson.JsonComponent;

import com.boot.model.MapModel;
import com.boot.utils.ArgUtil;
import com.boot.utils.ArgUtil.EnumById;
import com.boot.utils.EnumType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class CommonSerilizers {
	@JsonComponent
	public static class EnumByIdSerializer extends JsonSerializer<EnumById> {

		@Override
		public void serialize(EnumById value, JsonGenerator gen, SerializerProvider serializers)
				throws IOException, JsonProcessingException {
			gen.writeString(value.getId());
		}
	}

	@JsonComponent
	public static class EnumTypeSerializer extends JsonSerializer<EnumType> {

		@Override
		public void serialize(EnumType value, JsonGenerator gen, SerializerProvider serializers)
				throws IOException, JsonProcessingException {
			if (!ArgUtil.isEmpty(value)) {
				// super.serialize(value, gen, serializers);
				gen.writeString(value.enumValue().name());
				// defaultSerializer.serialize(value, gen, serializers);
				// serializers.defaultSerialize(value, gen);
			}
		}
	}

	@JsonComponent
	public static class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

		@Override
		public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers)
				throws IOException, JsonProcessingException {
			if (!ArgUtil.isEmpty(value)) {
				// gen.writeString(value.toPlainString());
				// gen.writeNumber(value);
				// gen.writeNumber(value.doubleValue());
				gen.writeNumber(value.toPlainString());
			}
		}

		@Override
		public void serializeWithType(BigDecimal value, JsonGenerator gen, SerializerProvider provider,
				TypeSerializer typeSer) throws IOException {
			// typeSer.writeTypePrefixForObject(value, gen);
			serialize(value, gen, provider); // call your customized serialize method
			// typeSer.writeTypeSuffixForObject(value, gen);
			// super.serializeWithType(value, gen, provider, typeSer);
		}
	}

	@JsonComponent
	public static class MapModelDeSerializer extends JsonDeserializer<MapModel> {

		@Override
		public MapModel deserialize(JsonParser jp, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			return MapModel.from(jp.getValueAsString("{}"));
		}
	}

	/**
	 * Not Used Yet
	 * 
	 * @author lalittanwar
	 *
	 */
	public class BigDecimalDeSerializer extends JsonDeserializer<BigDecimal> {

		private NumberDeserializers.BigDecimalDeserializer delegate = NumberDeserializers.BigDecimalDeserializer.instance;

		@Override
		public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			// return ArgUtil.parseAsBigDecimal(jp.getDecimalValue());

			BigDecimal bd = delegate.deserialize(jp, ctxt);
			bd = bd.setScale(2, RoundingMode.HALF_UP);
			return bd;
		}
	}
}
