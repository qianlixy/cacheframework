package com.qianlixy.framework.cache.filter;

import com.qianlixy.framework.cache.CacheProcesser;
import com.qianlixy.framework.cache.SourceProcesser;
import com.qianlixy.framework.cache.context.Context;

public class ReturnSourceNotExistValidCacheFilter implements Filter {
	
	@Override
	public Object doFilter(SourceProcesser sourceProcesser, CacheProcesser cacheProcesser, 
			Context context, FilterChain chain) throws Throwable {
		Object cache = cacheProcesser.getCache();
		if (null == cache) {
			Object source = sourceProcesser.doProcess();
			if(sourceProcesser.isAlter()) return source;
			cacheProcesser.putCache(source);
			return source;
		}
		return chain.doFilter(sourceProcesser, cacheProcesser, context, chain);
	}
	
}
