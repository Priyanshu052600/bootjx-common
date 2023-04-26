package com.boot.jx.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerService {

	public static Logger getLogger(Class<?> clazz) {
		return LoggerFactory.getLogger(clazz);
	}

}
