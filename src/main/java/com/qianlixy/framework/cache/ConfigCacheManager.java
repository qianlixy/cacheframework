package com.qianlixy.framework.cache;

import java.util.ArrayList;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;

import com.qianlixy.framework.cache.context.CacheMethodContext;
import com.qianlixy.framework.cache.context.SqlParseContext;
import com.qianlixy.framework.cache.context.ThreadLocalContext;
import com.qianlixy.framework.cache.exception.NonImplToStringException;
import com.qianlixy.framework.cache.filter.FilterChain;
import com.qianlixy.framework.cache.filter.ReturnExistValidCacheFilter;
import com.qianlixy.framework.cache.filter.ReturnSourceNotExistValidCacheFilter;

public class ConfigCacheManager extends AbstractConfig implements CacheManager {
	
	private ThreadLocalContext threadLocalContext;
	private CacheMethodContext cacheMethodContext;
	private FilterChain filterChain;
	
	public ConfigCacheManager() {
		// 赋默认值
		this.filters = new ArrayList<>();
		this.filters.add(new ReturnSourceNotExistValidCacheFilter());
		this.filters.add(new ReturnExistValidCacheFilter());
	}
	
	@Override
	public void init() throws Exception {
		CollectionCacheClient collectionCacheClient = null;
		try {
			collectionCacheClient = cacheClientFactory.buildCollectionCacheClient();
		} catch (Exception e) {
			LOGGER.error("Cache manager init fail while build cache client", e);
			throw e;
		}
		
		// 初始化上下文
		threadLocalContext = new ThreadLocalContext();
		cacheMethodContext = new CacheMethodContext(threadLocalContext);
		
		sqlParser.setSqlParseContext(new SqlParseContext(cacheMethodContext, collectionCacheClient));
		
		// 初始化拦截器链
		filterChain = new FilterChain(filters);
	}

	@Override
	public Object doCache(ProceedingJoinPoint joinPoint) throws Throwable {
		if(!isProxyCached) {
			return joinPoint.proceed();
		}
		LOGGER.debug("Start intercepte method [{}]", joinPoint.getSignature().toLongString());
		long startTime = new Date().getTime();
		SourceProcesser sourceProcesser = new DefaultSourceProcesser(joinPoint, threadLocalContext);
		try {
			// 注册缓存方法
			cacheMethodContext.registerCacheMethod(joinPoint);
			// 拦截器依次拦截
			return filterChain.doFilter(sourceProcesser, 
					new DefaultCacheProcesser(cacheMethodContext, sourceProcesser, 
							cacheClientFactory.buildCollectionCacheClient(), defaultCacheTime), 
					threadLocalContext, filterChain);
		} catch (NonImplToStringException e) {
			LOGGER.warn("Cannot get cache by {}", e.getMessage());
			return sourceProcesser.doProcess();
		} catch (Throwable th) {
			LOGGER.error("Occur exception while intercepte cache method [{}]", 
					sourceProcesser.getFullMethodName(), th);
			return sourceProcesser.doProcess();
		} finally {
			if(LOGGER.isDebugEnabled()) {
				long endTime = new Date().getTime();
				LOGGER.debug("Intercepted method end and cache method's total exec time is {} ms", 
						endTime - startTime);
			}
		}
	}
	
}
