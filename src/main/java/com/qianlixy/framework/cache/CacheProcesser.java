package com.qianlixy.framework.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface CacheProcesser {
	
	Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);

	Object getCache();
	
	void putCache(Object cache);
	
	void putCache(Object cache, int time);
	
	CacheClient getCacheClient();

}
