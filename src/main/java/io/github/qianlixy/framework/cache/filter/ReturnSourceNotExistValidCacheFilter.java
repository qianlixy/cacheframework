package io.github.qianlixy.framework.cache.filter;

import io.github.qianlixy.framework.cache.AbstractConfig;
import io.github.qianlixy.framework.cache.context.Context;
import io.github.qianlixy.framework.cache.wrapper.CacheProcesser;
import io.github.qianlixy.framework.cache.wrapper.SourceProcesser;
import io.github.qianlixy.framework.cache.wrapper.TypeWrapper;

/**
 * 如果不存在有效缓存，将查询并返回源数据。中断过滤器链
 * @author qianli_xy@163.com
 * @since 1.7
 */
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
		// 如果缓存为包装类NULL，说明缓存有效且源数据为null，直接返回null
		if(TypeWrapper.NULL == cache) return null;
		return chain.doFilter(sourceProcesser, cacheProcesser, context, chain);
	}

	@Override
	public void init(AbstractConfig config) {
		
	}
	
}
