package io.github.qianlixy.cache;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.context.CacheContext;
import io.github.qianlixy.cache.context.CacheKeyGenerator;
import io.github.qianlixy.cache.context.DefaultCacheContext;
import io.github.qianlixy.cache.context.MD5CacheKeyGenerator;
import io.github.qianlixy.cache.exception.CacheIsNullException;
import io.github.qianlixy.cache.exception.CacheOutOfDateException;
import io.github.qianlixy.cache.exception.InitCacheManagerException;
import io.github.qianlixy.cache.impl.AbstractCacheAdapterFactory;
import io.github.qianlixy.cache.parse.DruidSQLParser;
import io.github.qianlixy.cache.wrapper.CacheProcesser;
import io.github.qianlixy.cache.wrapper.DefaultCacheProcesser;
import io.github.qianlixy.cache.wrapper.DefaultSourceProcesser;
import io.github.qianlixy.cache.wrapper.SourceProcesser;

/**
 * 默认的缓存管理器
 * 需要设置缓存配置{@link #cacheConfig}
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月9日 下午9:47:27
 */
public class DefaultCacheManger implements CacheManager {
	
	private CacheConfig cacheConfig;
	private CacheContext cacheContext;
	private CacheKeyGenerator cacheKeyGenerator;

	public void setCacheConfig(CacheConfig cacheConfig) {
		this.cacheConfig = cacheConfig;
	}

	@Override
	public void init() throws Exception {
		//初始化配置信息
		if(null == CacheConfig.getCacheClientFactory()) {
			if(null == cacheConfig.getCacheClient()) {
				throw new InitCacheManagerException("AbstractCacheClientFactory and CacheClientAdapter cannot be null at the same time");
			}
			cacheConfig.setCacheClientFactory(AbstractCacheAdapterFactory.buildFactory(cacheConfig.getCacheClient()));
		}
		if(null == cacheConfig.getSQLParser()) {
			if(null == cacheConfig.getDataSource()) {
				throw new InitCacheManagerException("SQLParser and DataSource cannot be null at the same time");
			}
			DruidSQLParser SQLParser = new DruidSQLParser();
			SQLParser.setDataSource(cacheConfig.getDataSource());
			cacheConfig.setSQLParser(SQLParser);
		}
		
		//初始化缓存上下文信息
		cacheContext = new DefaultCacheContext(CacheConfig.getCacheClientFactory().buildCacheClient());
		
		//完善配置信息
		cacheConfig.getSQLParser().setCacheContext(cacheContext);
		
		//初始化缓存key生成策略
		if(null == cacheKeyGenerator) {
			cacheKeyGenerator = new MD5CacheKeyGenerator();
		}
	}

	@Override
	public Object doCache(ProceedingJoinPoint joinPoint) throws Throwable {
		//注册拦截源方法
		cacheContext.register(joinPoint, cacheKeyGenerator);
		//生成源方法包装类
		SourceProcesser sourceProcesser = new DefaultSourceProcesser(joinPoint);
		//生成源方法缓存操作包装类
		CacheProcesser cacheProcesser = new DefaultCacheProcesser(cacheContext,
				CacheConfig.getCacheClientFactory().buildCacheClient(),
				cacheConfig.getDefaultCacheTime());
		boolean isQuery = false;
		try {
			isQuery = cacheContext.isQuery();
		} catch(CacheIsNullException e) {
			return execSourceMethodAndSetCache(sourceProcesser, cacheProcesser);
		}
		if(isQuery) {
			Object cache = null;
			try {
				cache = cacheProcesser.getCache();
			} catch (CacheOutOfDateException e) {
				return execSourceMethodAndSetCache(sourceProcesser, cacheProcesser);
			}
			LOGGER.debug("Use cache client data on [{}]", joinPoint.getSignature().toLongString());
			return cache;
		}
		return execSourceMethodAndSetCache(sourceProcesser, cacheProcesser);
	}
	
	private Object execSourceMethodAndSetCache(SourceProcesser sourceProcesser, CacheProcesser cacheProcesser) throws Throwable {
		Object source = sourceProcesser.doProcess();
		try {
			cacheProcesser.putCache(source);
		} catch (CacheIsNullException e) {
			LOGGER.warn("Exist null data while set cache, so cannot set cache on [{}]", sourceProcesser.getFullMethodName());
		}
		return source;
	}

}
