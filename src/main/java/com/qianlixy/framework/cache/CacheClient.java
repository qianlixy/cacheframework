package com.qianlixy.framework.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qianlixy.framework.cache.exception.ConsistentTimeException;

public interface CacheClient {
	
	Logger LOGGER = LoggerFactory.getLogger(CacheClient.class);

	boolean put(String key, Object value);
	
	boolean put(String key, Object value, int time);
	
	Object get(String key);
	
	boolean remove(String key);
	
	long consistentTime() throws ConsistentTimeException;
	
}
