package io.github.qianlixy.cache.filter;

import java.util.List;

import io.github.qianlixy.cache.AbstractConfig;
import io.github.qianlixy.cache.context.Context;
import io.github.qianlixy.cache.wrapper.CacheProcesser;
import io.github.qianlixy.cache.wrapper.SourceProcesser;

public class MethodMatchFilter implements Filter {
	
	public static final String CONTEXT_KEY_METHOD_CACHE_TIME = "METHOD_CACHE_TIME";
	
	private CacheMethodMatcher methodMatcher = null;
	private int defaultCacheTime;
	
	@Override
	public void init(AbstractConfig config) {
		if(isArrayNotBlank(config.getCacheMethods()) && isArrayNotBlank(config.getSourceMethods())) {
			methodMatcher = new CacheMethodMatcher(config.getCacheMethods(), 
					config.getSourceMethods(), config.getSourceMethodsLimit());
		} else if(!isArrayNotBlank(config.getSourceMethods())) {
			methodMatcher = new CacheMethodMatcher(config.getCacheMethods());
		}
		defaultCacheTime = config.getDefaultCacheTime();
	}
	
	private boolean isArrayNotBlank(List<?> list) {
		return null != list && list.size() > 0;
	}

	@Override
	public Object doFilter(SourceProcesser sourceProcesser, CacheProcesser cacheProcesser, Context context,
			FilterChain chain) throws Throwable {
		if(null != methodMatcher) {
			int time = methodMatcher.match(sourceProcesser.getFullMethodName());
			if(time == 0) {
				context.setAttribute(CONTEXT_KEY_METHOD_CACHE_TIME, defaultCacheTime);
			} else if(time > 0) {
				context.setAttribute(CONTEXT_KEY_METHOD_CACHE_TIME, time);
			} else {
				LOGGER.debug("Cannot match method [{}]", sourceProcesser.getFullMethodName());
				return sourceProcesser.doProcess();
			}
		}
		return chain.doFilter(sourceProcesser, cacheProcesser, context, chain);
	}
	
	

}
