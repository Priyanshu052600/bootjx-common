package com.boot.utils;

import java.util.List;


/**
 * The Class ListManager.
 *
 * @param <T> the generic type
 */
public class ListManager<T> {

	/** The list. */
	List<T> list = null;

	/** The index. */
	Integer index = 0;

	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * Sets the index.
	 *
	 * @param index the new index
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * Instantiates a new list manager.
	 *
	 * @param list the list
	 */
	public ListManager(List<T> list) {
		this.list = list;
	}

	/**
	 * Pick random.
	 *
	 * @return the t
	 */
	public T pickRandom() {
		if (list != null) {
			return list.get(Random.getInt(0, list.size() - 1));
		}
		return null;
	}

	/**
	 * Pick next.
	 *
	 * @param offset the offset
	 * @return the t
	 */
	public T pickNext(int offset) {
		if (list != null) {
			int size = list.size();
			int next = (offset + 1) % size;
			return list.get(next);
		}
		return null;
	}

}
