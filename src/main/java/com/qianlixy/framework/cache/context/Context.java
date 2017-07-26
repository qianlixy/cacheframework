package com.qianlixy.framework.cache.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Context {
	
	Logger LOGGER = LoggerFactory.getLogger(Context.class);
	
	int THREAD_LOCAL_KEY_TARGET = 0x1;
	int THREAD_LOCAL_KEY_METHOD = 0x2;
	int THREAD_LOCAL_KEY_PARAMS = 0x3;
	int THREAD_LOCAL_KEY_IS_FINISH_SQL_PARSE = 0x4;
	int THREAD_LOCAL_KEY_THROWABLE = 0x5;
	
	String THREAD_LOCAL_KEY_METHOD_STATIC_UNIQUE_MARK = "METHOD_STATIC_UNIQUE_MARK";
	String THREAD_LOCAL_KEY_METHOD_DYNAMIC_UNIQUE_MARK = "METHOD_DYNAMIC_UNIQUE_MARK";
	
	String THREAD_LOCAL_KEY_IS_ALTER_METHOD = "IS_ALTER_METHOD";
	String THREAD_LOCAL_KEY_IS_QUERY_METHOD = "IS_QUERY_METHOD";
	
	String CACHE_KEY_METHOD_SQL_MAP = "METHOD_SQL_MAP";
	String CACHE_KEY_METHOD_TABLE_MAP = "METHOD_TABLE_MAP";
	String CACHE_KEY_METHOD_LAST_QUERY_TIME_MAP = "METHOD_LAST_QUERY_TIME_MAP";
	String CACHE_KEY_TABLE_LAST_ALERT_TIME_MAP = "TABLE_LAST_ALERT_TIME_MAP";


	void setAttribute(Object key, Object value);
	
	Object getAttribute(Object key);
	
	void removeAttribute(Object key);
}
