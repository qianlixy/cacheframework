package io.github.qianlixy.cache.filter;

import io.github.qianlixy.cache.AbstractConfig;
import io.github.qianlixy.cache.context.Context;
import io.github.qianlixy.cache.wrapper.CacheProcesser;
import io.github.qianlixy.cache.wrapper.SourceProcesser;

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

	@Override
	public void init(AbstractConfig config) {
		
	}

}
