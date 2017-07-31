package com.qianlixy.framework.cache;

import java.util.HashSet;
import java.util.Set;

import com.qianlixy.framework.cache.context.CacheMethodContext;
import com.qianlixy.framework.cache.context.Context;
import com.qianlixy.framework.cache.exception.CacheOperationException;

public class DefaultCacheProcesser implements CacheProcesser {
	
	private CacheMethodContext cacheMethodContext;
	private SourceProcesser sourceProcesser;
	private CollectionCacheClient collectionCacheClient;
	private int defaultCacheTime;
	
	public DefaultCacheProcesser(CacheMethodContext cacheMethodContext, SourceProcesser sourceProcesser,
			CollectionCacheClient collectionCacheClient, int defaultCacheTime) {
		this.cacheMethodContext = cacheMethodContext;
		this.sourceProcesser = sourceProcesser;
		this.collectionCacheClient = collectionCacheClient;
		this.defaultCacheTime = defaultCacheTime;
	}

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
			collectionCacheClient.set(dynamicUniqueMark, source, time);
			try {
				collectionCacheClient.putMap(Context.CACHE_KEY_METHOD_LAST_QUERY_TIME_MAP, 
						dynamicUniqueMark, collectionCacheClient.consistentTime());
			} catch (Exception e) {
				throw new CacheOperationException(e);
			}
		}
		collectionCacheClient.set(dynamicUniqueMark, source, time);
	}
	
	@Override
	public CacheClient getCacheClient() {
		return collectionCacheClient;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object getCache() {
		String cacheKey = cacheMethodContext.getCacheMethodDynamicUniqueMark();
		String staticKey = cacheMethodContext.getCacheMethodStaticUniqueMark();
		Object cache = collectionCacheClient.get(cacheKey);
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
	
}
