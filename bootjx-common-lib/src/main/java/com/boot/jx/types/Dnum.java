package com.boot.jx.types;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonFormat(shape = JsonFormat.Shape.ANY)
public class Dnum<E extends Dnum<E>> {
	private static Map<Class<? extends Dnum<?>>, Map<String, Dnum<?>>> elements = new LinkedHashMap<Class<? extends Dnum<?>>, Map<String, Dnum<?>>>();

	private final String name;

	public final String name() {
		return name;
	}

	private final int ordinal;

	public final int ordinal() {
		return ordinal;
	}

	protected Dnum(String name, int ordinal) {
		this.name = name;
		this.ordinal = ordinal;
		Map<String, Dnum<?>> typeElements = elements.get(getClass());
		if (typeElements == null) {
			typeElements = new LinkedHashMap<String, Dnum<?>>();
			elements.put(getDynaEnumClass(), typeElements);
		}
		typeElements.put(name, this);
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Dnum<?>> getDynaEnumClass() {
		return (Class<? extends Dnum<?>>) getClass();
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public final boolean equals(Object other) {
		return this == other;
	}

	@Override
	public final int hashCode() {
		return super.hashCode();
	}

	@Override
	protected final Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public final int compareTo(E other) {
		Dnum<?> self = this;
		if (self.getClass() != other.getClass() && // optimization
				self.getDeclaringClass() != other.getDeclaringClass())
			throw new ClassCastException();
		return self.ordinal - other.ordinal();
	}

	@JsonIgnore
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final Class<E> getDeclaringClass() {
		Class clazz = getClass();
		Class zuper = clazz.getSuperclass();
		return (zuper == Dnum.class) ? clazz : zuper;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Dnum<T>> T valueOf(Class<T> enumType, String name) {
		return (T) elements.get(enumType).get(name);
	}

	@SuppressWarnings("unused")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		throw new InvalidObjectException("can't deserialize enum");
	}

	@SuppressWarnings("unused")
	private void readObjectNoData() throws ObjectStreamException {
		throw new InvalidObjectException("can't deserialize enum");
	}

	@Override
	protected final void finalize() {
	}

	public static <E> Dnum<? extends Dnum<?>>[] values() {
		throw new IllegalStateException("Sub class of DynaEnum must implement method valus()");
	}

	@SuppressWarnings("unchecked")
	public static <E> E[] values(Class<E> enumType) {
		Collection<Dnum<?>> values = elements.get(enumType).values();
		int n = values.size();
		E[] typedValues = (E[]) Array.newInstance(enumType, n);
		int i = 0;
		for (Dnum<?> value : values) {
			Array.set(typedValues, i, value);
			i++;
		}

		return typedValues;
	}

	@SuppressWarnings("unchecked")
	public static <E> E fromString(Class<E> enumType, String name) {
		return (E) elements.get(enumType).get(name);
	}

}
