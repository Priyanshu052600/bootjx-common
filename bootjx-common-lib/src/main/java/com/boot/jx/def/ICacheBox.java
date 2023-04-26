package com.boot.jx.def;

import java.util.Map;
import java.util.Set;

/**
 * TO start with we are using RCache from Redisson, so following all conventions
 * from there
 * 
 * @author lalittanwar
 *
 * @param <T>
 */
public interface ICacheBox<T> {

	/**
	 * Returns the value to which the specified key is mapped, or {@code null} if
	 * this map contains no mapping for the key.
	 * <p>
	 * If map doesn't contain value for specified key and {@link MapLoader} is
	 * defined then value will be loaded in read-through mode.
	 *
	 * @param key
	 *            the key whose associated value is to be returned
	 * @return the value to which the specified key is mapped, or {@code null} if
	 *         this map contains no mapping for the key
	 */
	T get(String key);

	/**
	 * Associates the specified <code>value</code> with the specified
	 * <code>key</code> in async manner.
	 * <p>
	 * If {@link MapWriter} is defined then new map entry is stored in write-through
	 * mode.
	 *
	 * @param key
	 *            - map key
	 * @param value
	 *            - map value
	 * @return previous associated value
	 */
	T put(String key, T value);

	/**
	 * Associates the specified <code>value</code> with the specified
	 * <code>key</code> only if there is no any association with
	 * specified<code>key</code>.
	 * <p>
	 * If {@link MapWriter} is defined then new map entry is stored in write-through
	 * mode.
	 *
	 * @param key
	 *            - map key
	 * @param value
	 *            - map value
	 * @return <code>null</code> if key is a new one in the hash and value was set.
	 *         Previous value if key already exists in the hash and change hasn't
	 *         been made.
	 */
	T putIfAbsent(String key, T value);

	T remove(String key);

	/**
	 * Replaces previous value with a new <code>value</code> associated with the
	 * <code>key</code>. If there wasn't any association before then method returns
	 * <code>null</code>.
	 * <p>
	 * If {@link MapWriter} is defined then new <code>value</code>is written in
	 * write-through mode.
	 *
	 * @param key
	 *            - map key
	 * @param value
	 *            - map value
	 * @return previous associated value or <code>null</code> if there wasn't any
	 *         association and change hasn't been made
	 */
	T replace(String key, T value);

	/**
	 * Replaces previous <code>oldValue</code> with a <code>newValue</code>
	 * associated with the <code>key</code>. If previous value doesn't exist or
	 * equal to <code>oldValue</code> then method returns <code>false</code>.
	 * <p>
	 * If {@link MapWriter} is defined then <code>newValue</code>is written in
	 * write-through mode.
	 * 
	 * @param key
	 *            - map key
	 * @param oldValue
	 *            - map old value
	 * @param newValue
	 *            - map new value
	 * @return <code>true</code> if value has been replaced otherwise
	 *         <code>false</code>.
	 */
	boolean replace(String key, T oldValue, T newValue);

	/**
	 * Associates the specified <code>value</code> with the specified
	 * <code>key</code> in batch.
	 * <p>
	 * If {@link MapWriter} is defined then new map entries will be stored in
	 * write-through mode.
	 *
	 * @param map
	 *            mappings to be stored in this map
	 */
	void putAll(java.util.Map<? extends String, ? extends T> map);

	/**
	 * Gets a map slice contained the mappings with defined <code>keys</code> by one
	 * operation.
	 * <p>
	 * If map doesn't contain value/values for specified key/keys and
	 * {@link MapLoader} is defined then value/values will be loaded in read-through
	 * mode.
	 * <p>
	 * The returned map is <b>NOT</b> backed by the original map.
	 *
	 * @param keys
	 *            - map keys
	 * @return Map slice
	 */
	Map<String, T> getAll(Set<String> keys);

	/**
	 * Removes <code>keys</code> from map by one operation
	 * <p>
	 * Works faster than <code>{@link RMap#remove(Object)}</code> but not returning
	 * the value associated with <code>key</code>
	 * <p>
	 * If {@link MapWriter} is defined then <code>keys</code>are deleted in
	 * write-through mode.
	 *
	 * @param keys
	 *            - map keys
	 * @return the number of keys that were removed from the hash, not including
	 *         specified but non existing keys
	 */
	long fastRemove(String... keys);

	/**
	 * Associates the specified <code>value</code> with the specified
	 * <code>key</code>.
	 * <p>
	 * Works faster than <code>{@link RMap#put(Object, Object)}</code> but not
	 * returning the previous value associated with <code>key</code>
	 * <p>
	 * If {@link MapWriter} is defined then new map entry is stored in write-through
	 * mode.
	 *
	 * @param key
	 *            - map key
	 * @param value
	 *            - map value
	 * @return <code>true</code> if key is a new key in the hash and value was set.
	 *         <code>false</code> if key already exists in the hash and the value
	 *         was updated.
	 */
	boolean fastPut(String key, T value);

	/**
	 * Associates the specified <code>value</code> with the specified
	 * <code>key</code> only if there is no any association with
	 * specified<code>key</code>.
	 * <p>
	 * Works faster than <code>{@link RMap#putIfAbsent(Object, Object)}</code> but
	 * not returning the previous value associated with <code>key</code>
	 * <p>
	 * If {@link MapWriter} is defined then new map entry is stored in write-through
	 * mode.
	 *
	 * @param key
	 *            - map key
	 * @param value
	 *            - map value
	 * @return <code>true</code> if key is a new one in the hash and value was set.
	 *         <code>false</code> if key already exists in the hash and change
	 *         hasn't been made.
	 */
	boolean fastPutIfAbsent(String key, T value);

	/**
	 * Removes <code>key</code> from map only if it associated with
	 * <code>value</code>.
	 * <p>
	 * If {@link MapWriter} is defined then <code>key</code>is deleted in
	 * write-through mode.
	 *
	 * @param key
	 *            - map key
	 * @param value
	 *            - map value
	 * @return <code>true</code> if map entry has been replaced otherwise
	 *         <code>false</code>.
	 */
	boolean remove(String key, Object value);

	/**
	 * {@inheritDoc}
	 *
	 * @implNote This implementation assumes that the ConcurrentMap cannot contain
	 *           null values and {@code get()} returning null unambiguously means
	 *           the key is absent. Implementations which support null values
	 *           <strong>must</strong> override this default implementation.
	 *
	 * @throws ClassCastException
	 *             {@inheritDoc}
	 * @throws NullPointerException
	 *             {@inheritDoc}
	 * @since 1.8
	 */
	T getOrDefault(String key, T defaultValue);

	T getOrDefault(String key);

	T getDefault();
}
