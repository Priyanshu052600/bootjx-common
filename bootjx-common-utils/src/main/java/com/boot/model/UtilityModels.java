package com.boot.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class UtilityModels {

	public interface Stringable {
		void fromString(String testString);
	}

	public interface Indexable {
		public String id();
	}

	public interface UniqueIndex<T> {
		public String uuid();

		public String uuid(String uuid);

		public T update(T fromObject);
	}

	/**
	 * While De-Serialization
	 * 
	 * @author lalittanwar
	 *
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public interface JsonIgnoreUnknown extends Serializable {
	}

	/**
	 * While Serialization
	 * 
	 * @author lalittanwar
	 *
	 */
	@JsonInclude(Include.NON_NULL)
	public interface JsonIgnoreNull extends Serializable {
	}

	public interface JsonStringify {
		String toJsonString();
	}

	public interface JsonObject {
		default Object jsonObject() {
			return this;
		}
	}

	public static interface PublicJsonProperty {
	}

	public static interface ProtectedJsonProperty extends PublicJsonProperty {
	}

	public static interface OneTimeVisibleJsonProperty extends ProtectedJsonProperty {
	}
}
