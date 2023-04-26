package com.boot.jx.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boot.utils.ArgUtil;
import com.boot.utils.ClazzUtil;
import com.boot.utils.ContextUtil;

/**
 * TO start with we are using RCache from Redisson, so following all conventions
 * from there
 * 
 * @author lalittanwar
 *
 * @param <T>
 */
public abstract class ACtxCacheBox<T> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public abstract ICacheBox<T> getCacheBox();

	protected String getContextId() {
		return ArgUtil.parseAsString(ContextUtil.map().get(getClazzName()));
	}

	public void setContextId(String contextId) {
		ContextUtil.map().put(getClazzName(), contextId);
	}

	String clazzName = null;

	public String getClazzName() {
		if (this.clazzName == null) {
			this.clazzName = ClazzUtil.getClassName(this);
		}
		return clazzName;
	}

	protected String getCacheKey(String key) {
		return key;
	}

	protected T getByCtxId(String txId) {
		T x = this.getCacheBox().getOrDefault(getCacheKey(txId), null);
		if (ArgUtil.isEmpty(x)) {
			return getDefault();
		}
		return x;
	};

	/**
	 * Returns the value to which the specified key is mapped, or {@code null} if
	 * this map contains no mapping for the key.
	 * <p>
	 * If map doesn't contain value for specified key and {@link MapLoader} is
	 * defined then value will be loaded in read-through mode.
	 *
	 * @return the value to which the specified key is mapped, or {@code null} if
	 *         this map contains no mapping for the key
	 */
	public T get() {
		return this.getByCtxId(getContextId());
	};

	/**
	 * Associates the specified <code>value</code> with the specified
	 * <code>key</code> in async manner.
	 * <p>
	 * If {@link MapWriter} is defined then new map entry is stored in write-through
	 * mode.
	 *
	 * @param value - map value
	 * @return previous associated value
	 */
	public T put(T value) {
		return this.getCacheBox().put(getCacheKey(getContextId()), value);
	};

	/**
	 * Associates the specified <code>value</code> with the specified
	 * <code>key</code> only if there is no any association with
	 * specified<code>key</code>.
	 * <p>
	 * If {@link MapWriter} is defined then new map entry is stored in write-through
	 * mode.
	 *
	 * @param value - map value
	 * @return <code>null</code> if key is a new one in the hash and value was set.
	 *         Previous value if key already exists in the hash and change hasn't
	 *         been made.
	 */
	public T putIfAbsent(T value) {
		return this.getCacheBox().putIfAbsent(getCacheKey(getContextId()), value);
	};

	/**
	 * Removes <code>key</code> from map only if it associated with
	 * <code>value</code>.
	 * <p>
	 * If {@link MapWriter} is defined then <code>key</code>is deleted in
	 * write-through mode.
	 *
	 * @return <code>true</code> if map entry has been replaced otherwise
	 *         <code>false</code>.
	 */
	public T remove() {
		return this.getCacheBox().remove(getCacheKey(getContextId()));
	};

	/**
	 * Replaces previous value with a new <code>value</code> associated with the
	 * <code>key</code>. If there wasn't any association before then method returns
	 * <code>null</code>.
	 * <p>
	 * If {@link MapWriter} is defined then new <code>value</code>is written in
	 * write-through mode.
	 *
	 * @param value - map value
	 * @return previous associated value or <code>null</code> if there wasn't any
	 *         association and change hasn't been made
	 */
	public T replace(T value) {
		return this.getCacheBox().put(getCacheKey(getContextId()), value);
	};

	/**
	 * Replaces previous <code>oldValue</code> with a <code>newValue</code>
	 * associated with the <code>key</code>. If previous value doesn't exist or
	 * equal to <code>oldValue</code> then method returns <code>false</code>.
	 * <p>
	 * If {@link MapWriter} is defined then <code>newValue</code>is written in
	 * write-through mode.
	 * 
	 * @param oldValue - map old value
	 * @param newValue - map new value
	 * @return <code>true</code> if value has been replaced otherwise
	 *         <code>false</code>.
	 */
	public boolean replace(T oldValue, T newValue) {
		return this.getCacheBox().replace(getCacheKey(getContextId()), oldValue, newValue);
	};

	/**
	 * Removes <code>keys</code> from map by one operation
	 * <p>
	 * Works faster than <code>{@link RMap#remove(Object)}</code> but not returning
	 * the value associated with <code>key</code>
	 * <p>
	 * If {@link MapWriter} is defined then <code>keys</code>are deleted in
	 * write-through mode.
	 *
	 * @return the number of keys that were removed from the hash, not including
	 *         specified but non existing keys
	 */
	public long fastRemove() {
		return this.getCacheBox().fastRemove(getCacheKey(getContextId()));
	};

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
	 * @param value - map value
	 * @return <code>true</code> if key is a new key in the hash and value was set.
	 *         <code>false</code> if key already exists in the hash and the value
	 *         was updated.
	 */
	public boolean fastPut(T value) {
		return this.getCacheBox().fastPut(getCacheKey(getContextId()), value);
	};

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
	 * @param value - map value
	 * @return <code>true</code> if key is a new one in the hash and value was set.
	 *         <code>false</code> if key already exists in the hash and change
	 *         hasn't been made.
	 */
	public boolean fastPutIfAbsent(T value) {
		return this.getCacheBox().fastPutIfAbsent(getCacheKey(getContextId()), value);
	};

	/**
	 * Removes <code>key</code> from map only if it associated with
	 * <code>value</code>.
	 * <p>
	 * If {@link MapWriter} is defined then <code>key</code>is deleted in
	 * write-through mode.
	 *
	 * @param value - map value
	 * @return <code>true</code> if map entry has been replaced otherwise
	 *         <code>false</code>.
	 */
	public boolean remove(Object value) {
		return this.getCacheBox().remove(getCacheKey(getContextId()), value);
	};

	/**
	 * {@inheritDoc}
	 *
	 * @implNote This implementation assumes that the ConcurrentMap cannot contain
	 *           null values and {@code get()} returning null unambiguously means
	 *           the key is absent. Implementations which support null values
	 *           <strong>must</strong> override this default implementation.
	 *
	 * @throws ClassCastException   {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 * @since 1.8
	 */
	public T getOrDefault(T defaultValue) {
		return this.getCacheBox().getOrDefault(getCacheKey(getContextId()), defaultValue);
	};

	public T getOrDefault() {
		return this.getByCtxId(getContextId());
	}

	public T getDefault() {
		return null;
	}
}
