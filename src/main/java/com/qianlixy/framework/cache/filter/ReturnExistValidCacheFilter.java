package com.qianlixy.framework.cache.filter;

import com.qianlixy.framework.cache.CacheProcesser;
import com.qianlixy.framework.cache.SourceProcesser;
import com.qianlixy.framework.cache.context.Context;

public class ReturnExistValidCacheFilter implements Filter {
	
	@Override
	public Object doFilter(SourceProcesser sourceProcesser, CacheProcesser cacheProcesser, 
			Context context, FilterChain chain) throws Throwable {
		Object cache = cacheProcesser.getCache();
		if(null == cache) {
			return sourceProcesser.doProcess();
		}
		LOGGER.debug("Use cache data to replace DB data on method [{}]", 
				sourceProcesser.getFullMethodName());
		return cache;
	}

}
