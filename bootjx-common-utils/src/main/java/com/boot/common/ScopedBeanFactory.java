package com.boot.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boot.utils.ArgUtil;
import com.boot.utils.ContextUtil;

/**
 * A factory for creating ScopedBean objects.
 *
 * @author lalittanwar
 * @param <E> Must have toString() function which returns unique id for each
 *            member;
 * @param <T> the generic type
 */
public abstract class ScopedBeanFactory<E, T> implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8272275904163644429L;

	/** The logger. */
	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	/** The libs by code. */
	private final Map<String, T> libsByCode = new HashMap<>();

	/**
	 * Instantiates a new scoped bean factory.
	 *
	 * @param beans the beans
	 */
	public ScopedBeanFactory(List<T> beans) {
		if (ArgUtil.is(beans)) {
			for (T bean : beans) {
				E[] keys = getKeys(bean);
				if (ArgUtil.is(keys)) {
					for (E key : keys) {
						if (key != null) {
							register(key, bean);
						}
					}
				} else if (ArgUtil.is(getDefaultKey())) {
					register(getDefaultKey(), bean);
				}
			}
		}
	}

	/**
	 * Returns keys against bean which will be used to map against it.
	 *
	 * @param bean the bean
	 * @return the keys
	 */
	abstract public E[] getKeys(T bean);

	/**
	 * Returns key against bean which is either default or current, set by setKey.
	 *
	 * @return the key
	 */
	@SuppressWarnings("unchecked")
	public E getKey() {
		return (E) ContextUtil.map().get(this.getClass().getName());
	};

	/**
	 * Any bean with this specifier will be returned, in case no bean found
	 * 
	 * @return
	 */
	public E getDefaultKey() {
		return null;
	};

	/**
	 * 
	 * @param serviceCode
	 */
	public void setKey(E key) {
		ContextUtil.map().put(this.getClass().getName(), key);
	}

	/**
	 * Register key and bean.
	 *
	 * @param key  the key
	 * @param bean the bean
	 */
	public void register(E key, T bean) {
		this.libsByCode.put(key.toString().toLowerCase(), bean);
	}

	/**
	 * Returns bean against key.
	 *
	 * @param key the key
	 * @return the t
	 */
	public T get(E key) {
		if (this.libsByCode.containsKey(key.toString().toLowerCase())) {
			return this.libsByCode.get(key.toString().toLowerCase());
		}
		LOGGER.debug("libsByCode Not Exists for Code== {}", key);
		key = getDefaultKey();
		if (ArgUtil.is(key)) {
			if (this.libsByCode.containsKey(key.toString().toLowerCase())) {
				return this.libsByCode.get(key.toString().toLowerCase());
			}
			LOGGER.debug("libsByCode Not Exists for Code== {}", key);
		}
		return null;
	}

	/**
	 * Returns current Value of Key.
	 *
	 * @return the t
	 */
	public T get() {
		return this.get(getKey());
	}

}
