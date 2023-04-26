package com.boot.jx.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boot.jx.AppContextUtil;
import com.boot.utils.ArgUtil;

/**
 * TO start with we are using RCache from Redisson, so following all conventions
 * from there
 * 
 * @author lalittanwar
 *
 * @param <T>
 */
public abstract class ATxCacheBox<T> extends ACtxCacheBox<T> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	protected String getContextId() {
		String key = AppContextUtil.getTranxId();
		if (ArgUtil.isEmptyString(key)) {
			key = AppContextUtil.getTraceId();
			AppContextUtil.setTranxId(key);
			LOGGER.info("************ Creating New Tranx Id {} *******************", key);
		} else {
			LOGGER.debug("************ Exisitng Tranx Id {} *******************", key);
		}
		return key;
	}

	private T getByTxId(String txId) {
		return super.getByCtxId(txId);
	};

	public static class Tx<T> {
		T model;
		String txId;

		public Tx(String txId, T model) {
			this.txId = txId;
			this.model = model;
		}

		public T get() {
			return model;
		}

		public String getTxId() {
			return txId;
		}

		public void setTxId(String txId) {
			this.txId = txId;
		}
	}

	public Tx<T> getX(String txId) {
		T model = this.getByTxId(txId);
		return new Tx<T>(txId, model);
	}

	public Tx<T> getX() {
		return this.getX(getContextId());
	}

	public Tx<T> commitX(Tx<T> tx) {
		T model = this.getCacheBox().put(getCacheKey(tx.getTxId()), tx.get());
		return new Tx<T>(tx.getTxId(), model);
	}

}
