package com.qianlixy.framework.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface SourceProcesser {
	
	Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);
	
	Class<?> getTargetClass();
	
	String getMethodName();
	
	Object[] getArgs();
	
	String getFullMethodName();

	Object doProcess() throws Throwable;

	boolean isAlter();

	boolean isQuery();
	
}
