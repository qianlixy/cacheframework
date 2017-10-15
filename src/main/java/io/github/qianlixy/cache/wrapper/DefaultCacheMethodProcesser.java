package io.github.qianlixy.cache.wrapper;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.context.ApplicationContext;
import io.github.qianlixy.cache.context.CacheContext;
import io.github.qianlixy.cache.exception.CacheNotExistException;
import io.github.qianlixy.cache.exception.CacheOutOfDateException;
import io.github.qianlixy.cache.exception.ConsistentTimeException;
import io.github.qianlixy.cache.exception.ExecuteSourceMethodException;

/**
 * 默认的缓存方法包装类
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月14日 下午10:34:59
 */
public class DefaultCacheMethodProcesser implements CacheMethodProcesser {
	
	//TODO 需要重构一个线程中只能执行一次源方法
	private ThreadLocal<Object> executResult = new ThreadLocal<>();
	
	private ProceedingJoinPoint joinPoint;
	private CacheContext cacheContext;
	private CacheAdapter cacheAdapter;
	private int cacheTime;
	
	public DefaultCacheMethodProcesser(ProceedingJoinPoint joinPoint, 
			CacheContext cacheContext) throws IOException {
		this.joinPoint = joinPoint;
		this.cacheContext = cacheContext;
		this.cacheAdapter = ApplicationContext.getCacheAdaperFactory().buildCacheAdapter();
		this.cacheTime = ApplicationContext.getDefaultCacheTime();
	}

	@Override
	public void putCache(Object source) throws ConsistentTimeException {
		putCache(source, ApplicationContext.getDefaultCacheTime());
	}
	
	@Override
	public void putCache(Object source, int time) throws ConsistentTimeException {
		cacheAdapter.set(cacheContext.getDynamicUniqueMark(), 
				null == source ? new Null() : source, time);
		cacheContext.setLastQueryTime(ApplicationContext.getConsistentTimeProvider().newInstance());
	}
	
	@Override
	public Object getCache() throws CacheOutOfDateException, CacheNotExistException {
		//获取缓存
		Object cache = cacheAdapter.get(cacheContext.getDynamicUniqueMark());
		
		//缓存为null，抛出缓存不存在异常
		if(null == cache) throw new CacheNotExistException();
		
		//获取源方法关联表的修改时间
		Set<Long> lastAlterTime = new HashSet<>();
		Collection<String> methodTalbeMap = cacheContext.getTables();
		
		//找不到源方法对应的表，说明还没有查询过，所以直接返回null
		if(null == methodTalbeMap) return null;
		
		//判断缓存是否超时失效
		for (String table : methodTalbeMap) {
			long time = cacheContext.getTableLastAlterTime(table);
			if(time > 0) lastAlterTime.add(time);
		}
		if (lastAlterTime.size() > 0) {
			long lastQueryTime = cacheContext.getLastQueryTime();
			for (Long alterTime : lastAlterTime) {
				if(alterTime > lastQueryTime) {
					LOGGER.debug("Cached data is out of date on [{}]", cacheContext.toString());
					throw new CacheOutOfDateException();
				}
			}
		}
		
		//缓存存在，没有超时失效，返回有效缓存
		return cache instanceof Null ? null : cache;
	}

	@Override
	public String toString() {
		return getFullMethodName();
	}

	@Override
	public Class<?> getTargetClass() {
		return joinPoint.getTarget().getClass();
	}

	@Override
	public String getMethodName() {
		return joinPoint.getSignature().getName();
	}

	@Override
	public Object[] getArgs() {
		return joinPoint.getArgs();
	}
	
	@Override
	public String getFullMethodName() {
		return joinPoint.getSignature().toLongString();
	}

	@Override
	public Object doProcess() throws ExecuteSourceMethodException {
		if(null == executResult.get()) {
			LOGGER.debug("Start execute source method [{}]", getFullMethodName());
			Object result = null;
			try {
				result = joinPoint.proceed();
			} catch (Throwable e) {
				throw new ExecuteSourceMethodException(e);
			}
			executResult.set(result);
		}
		return executResult.get();
	}


	@Override
	public Object doProcessAndCache() throws ConsistentTimeException, ExecuteSourceMethodException {
		return doProcessAndCache(ApplicationContext.getDefaultCacheTime());
	}

	@Override
	public Object doProcessAndCache(int time) throws ConsistentTimeException, ExecuteSourceMethodException {
		Object source = doProcess();
		Boolean isQuery = cacheContext.isQuery();
		if(null != isQuery && isQuery)
			putCache(source, time);
		return source;
	}
	
	@Override
	public int getCacheTime() {
		return cacheTime;
	}

	@Override
	public void setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
	}
	
}
