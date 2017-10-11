package io.github.qianlixy.cache.wrapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.context.CacheClientConsistentTime;
import io.github.qianlixy.cache.context.CacheContext;
import io.github.qianlixy.cache.exception.CacheOperationException;
import io.github.qianlixy.cache.exception.CacheOutOfDateException;
import io.github.qianlixy.cache.exception.ConsistentTimeException;

public class DefaultCacheProcesser implements CacheProcesser {
	
	private CacheContext cacheContext;
	private CacheAdapter cacheAdapter;
	private int defaultCacheTime;
	
	public DefaultCacheProcesser(CacheContext cacheContext, CacheAdapter cacheAdapter,
			int defaultCacheTime) {
		this.cacheContext = cacheContext;
		this.cacheAdapter = cacheAdapter;
		this.defaultCacheTime = defaultCacheTime;
	}

	@Override
	public void putCache(Object source) throws CacheOperationException, ConsistentTimeException {
		putCache(source, defaultCacheTime);
	}
	
	@Override
	public void putCache(Object source, int time) throws CacheOperationException, ConsistentTimeException {
		if(null == source || !cacheContext.isQuery()) return;
		cacheAdapter.set(cacheContext.getDynamicUniqueMark(), source, time);
		cacheContext.setLastQueryTime(CacheClientConsistentTime.newInstance());
	}
	
	@Override
	public Object getCache() throws CacheOutOfDateException {
		//获取缓存
		Object cache = cacheAdapter.get(cacheContext.getDynamicUniqueMark());
		
		//获取源方法关联表的修改时间
		Set<Long> lastAlterTime = new HashSet<>();
		Collection<String> methodTalbeMap = cacheContext.getTables();
		//源方法关联的表一直没有修改过，直接返回缓存
		if(null == methodTalbeMap) return cache;
		
		for (String table : methodTalbeMap) {
			long time = cacheContext.getTableLastAlterTime(table);
			if(time > 0) lastAlterTime.add(time);
		}
		//源方法关联的表一直没有修改过，直接返回缓存
		if (lastAlterTime.size() == 0) return cache;
		
		//判断缓存是否超时失效
		long lastQueryTime = cacheContext.getLastQueryTime();
		for (Long alterTime : lastAlterTime) {
			if(alterTime > lastQueryTime) {
				LOGGER.debug("Cache is out of date on [{}]", cacheContext.getStaticUniqueMark());
				throw new CacheOutOfDateException();
			}
		}
		
		//缓存存在，没有超时失效，返回有效缓存
		return cache;
	}

	@Override
	public String toString() {
		return cacheContext.getStaticUniqueMark();
	}
	
}
