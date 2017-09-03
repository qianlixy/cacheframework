package io.github.qianlixy.cache;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.context.CacheMethodContext;
import io.github.qianlixy.cache.context.SqlParseContext;
import io.github.qianlixy.cache.context.ThreadLocalContext;
import io.github.qianlixy.cache.exception.NonImplToStringException;
import io.github.qianlixy.cache.filter.FilterChain;
import io.github.qianlixy.cache.wrapper.DefaultCacheProcesser;
import io.github.qianlixy.cache.wrapper.DefaultSourceProcesser;
import io.github.qianlixy.cache.wrapper.SourceProcesser;

/**
 * 缓存管理类
 * @author qianli_xy@163.com
 * @since 1.7
 */
public class ConfigCacheManager extends AbstractConfig implements CacheManager {
	
	/*
	 * 线程区域上下文对象，主要用于存放SQL解析结果和Filter线程变量
	 */
	private ThreadLocalContext threadLocalContext;
	private CacheMethodContext cacheMethodContext;
	private FilterChain filterChain;
	
	@Override
	public void init() throws Exception {
		// 初始化缓存客户端
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
		
		// 初始化Sql解析器
		sqlParser.setSqlParseContext(new SqlParseContext(cacheMethodContext, collectionCacheClient));
		
		// 初始化拦截器链
		filterChain = new FilterChain();
		filterChain.init(this);
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
				LOGGER.debug("Intercepte method finished and cache method's total exec time is {} ms", 
						endTime - startTime);
			}
		}
	}
	
}
