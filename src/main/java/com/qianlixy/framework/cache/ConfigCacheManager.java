package com.qianlixy.framework.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.qianlixy.framework.cache.context.CacheMethodContext;
import com.qianlixy.framework.cache.context.Context;
import com.qianlixy.framework.cache.context.SqlParseContext;
import com.qianlixy.framework.cache.context.ThreadLocalContext;
import com.qianlixy.framework.cache.exception.CacheOperationException;
import com.qianlixy.framework.cache.exception.NonImplToStringException;
import com.qianlixy.framework.cache.exception.SqlParseException;
import com.qianlixy.framework.cache.filter.FilterChain;
import com.qianlixy.framework.cache.filter.ReturnExistValidCacheFilter;
import com.qianlixy.framework.cache.filter.ReturnSourceNotExistValidCacheFilter;

public class ConfigCacheManager extends AbstractConfig implements CacheManager {
	
	private ThreadLocalContext threadLocalContext;
	private CacheMethodContext cacheMethodContext;
	private FilterChain filterChain;
	private CacheClient cacheClient;
	private CollectionCacheClient collectionCacheClient;
	
	public ConfigCacheManager() {
		// 赋默认值
		this.filters = new ArrayList<>();
		this.filters.add(new ReturnSourceNotExistValidCacheFilter());
		this.filters.add(new ReturnExistValidCacheFilter());
	}
	
	@Override
	public void init() throws Exception {
		try {
			cacheClient = cacheFactory.buildCacheClient();
			collectionCacheClient = cacheFactory.buildCollectionCacheClient();
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
	public Object doCache(JoinPoint joinPoint) throws Throwable {
		ProceedingJoinPoint pjp = (ProceedingJoinPoint) joinPoint;
		if(!isProxyCached) {
			return pjp.proceed();
		}
		LOGGER.debug("Start intercepte method [{}]", joinPoint.getSignature().toLongString());
		long startTime = new Date().getTime();
		SourceProcesser sourceProcesser = new DefaultSourceProcesser(pjp, threadLocalContext);
		try {
			// 注册缓存方法
			cacheMethodContext.registerCacheMethod(pjp);
			// 拦截器依次拦截
			return filterChain.doFilter(sourceProcesser, 
					new DefaultCacheProcesser(cacheMethodContext, sourceProcesser, 
							cacheFactory.buildCollectionCacheClient(), defaultCacheTime), 
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
	
	public final SourceProcesser buildSourceProcesser(ProceedingJoinPoint pjp) {
		return new SourceProcesser() {
			
			private Object source = null;
			private boolean isNull = false;
			
			@Override
			public Class<?> getTargetClass() {
				return pjp.getTarget().getClass();
			}
			
			@Override
			public String getMethodName() {
				return pjp.getSignature().getName();
			}
			
			@Override
			public Object[] getArgs() {
				return pjp.getArgs();
			}
			
			@Override
			public Object doProcess() throws Throwable {
				if(null == source && !isNull) {
					source = pjp.proceed();
					Boolean isFinishSqlParse = (Boolean) threadLocalContext.getAttribute(Context.THREAD_LOCAL_KEY_IS_FINISH_SQL_PARSE);
					if(null == isFinishSqlParse) isFinishSqlParse = true;
					if (!isFinishSqlParse) {
						throw new SqlParseException((Throwable)threadLocalContext.getAttribute(Context.THREAD_LOCAL_KEY_THROWABLE));
					}
					if(null == source) isNull = true;
					LOGGER.debug("Execute source method [{}]", getFullMethodName());
				}
				return source;
			}
			
			@Override
			public boolean isAlter() {
				return (boolean) threadLocalContext.getAttribute(
						Context.THREAD_LOCAL_KEY_IS_ALTER_METHOD);
			}
			
			@Override
			public boolean isQuery() {
				return (boolean) threadLocalContext.getAttribute(
						Context.THREAD_LOCAL_KEY_IS_QUERY_METHOD);
			}

			@Override
			public String getFullMethodName() {
				return pjp.getSignature().toLongString();
			}
		};
	}
	
	public CacheProcesser buildCacheProcesser(SourceProcesser sourceProcesser) {
		return new CacheProcesser() {
			
			@Override
			public void putCache(Object source) {
				putCache(source, defaultCacheTime);
			}
			
			@Override
			public void putCache(Object source, int time) throws CacheOperationException {
				String dynamicUniqueMark = cacheMethodContext.getCacheMethodDynamicUniqueMark();
				if(sourceProcesser.isAlter()) return;
				if(null == source) {
					LOGGER.warn("DB data is null by method [{}]", sourceProcesser.getFullMethodName());
					return;
				}
				if(sourceProcesser.isQuery()) {
					cacheClient.put(dynamicUniqueMark, source, time);
					try {
						collectionCacheClient.putMap(Context.CACHE_KEY_METHOD_LAST_QUERY_TIME_MAP, 
								dynamicUniqueMark, collectionCacheClient.consistentTime());
					} catch (Exception e) {
						throw new CacheOperationException(e);
					}
				}
				cacheClient.put(dynamicUniqueMark, source, time);
			}
			
			@Override
			public CacheClient getCacheClient() {
				return cacheClient;
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public Object getCache() {
				String cacheKey = cacheMethodContext.getCacheMethodDynamicUniqueMark();
				String staticKey = cacheMethodContext.getCacheMethodStaticUniqueMark();
				Object cache = cacheClient.get(cacheKey);
				if (null == cache) {
					return null;
				}
				Set<Long> lastAlterTime = new HashSet<>();
				Set<String> methodTalbeMap = (Set<String>) collectionCacheClient
						.getMap(Context.CACHE_KEY_METHOD_TABLE_MAP, staticKey);
				for (String table : methodTalbeMap) {
					Long time = (Long) collectionCacheClient
							.getMap(Context.CACHE_KEY_TABLE_LAST_ALERT_TIME_MAP, table);
					if(null != time) {
						lastAlterTime.add(time);
					}
				}
				if (null == lastAlterTime || lastAlterTime.size() <= 0) {
					return cache;
				}
				Long lastQueryTime = (Long) collectionCacheClient.getMap(
						Context.CACHE_KEY_METHOD_LAST_QUERY_TIME_MAP, cacheKey);
				if (null == lastQueryTime) {
					LOGGER.warn("Cache is exist, but times of query is null, return database data");
					return null;
				}
				for (Long alterTime : lastAlterTime) {
					if(alterTime > lastQueryTime) {
						LOGGER.debug("Cache exist but cache time is out of date");
						return null;
					}
				}
				return cache;
			}
		};
	}
	
}
