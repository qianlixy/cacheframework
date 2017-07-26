package com.qianlixy.framework.cache.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qianlixy.framework.cache.CacheProcesser;
import com.qianlixy.framework.cache.SourceProcesser;
import com.qianlixy.framework.cache.context.Context;

public interface Filter {
	
	Logger LOGGER = LoggerFactory.getLogger(Filter.class);

	Object doFilter(SourceProcesser processer, CacheProcesser cacheProcesser, 
			Context context, FilterChain chain) throws Throwable;
}
