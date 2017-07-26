package com.qianlixy.framework.cache;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface CacheManager {

	Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);
	
	void init() throws Exception;

	Object doCache(JoinPoint joinPoint) throws Throwable;

}