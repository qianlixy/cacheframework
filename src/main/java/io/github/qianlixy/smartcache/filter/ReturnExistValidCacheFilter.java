package io.github.qianlixy.smartcache.filter;

import io.github.qianlixy.smartcache.AbstractConfig;
import io.github.qianlixy.smartcache.context.Context;
import io.github.qianlixy.smartcache.wrapper.CacheProcesser;
import io.github.qianlixy.smartcache.wrapper.SourceProcesser;

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
