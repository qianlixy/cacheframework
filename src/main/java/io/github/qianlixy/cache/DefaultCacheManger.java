package io.github.qianlixy.cache;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.context.CacheContext;
import io.github.qianlixy.cache.context.CacheKeyGenerator;
import io.github.qianlixy.cache.context.DefaultCacheContext;
import io.github.qianlixy.cache.context.MD5CacheKeyGenerator;
import io.github.qianlixy.cache.exception.ExecuteSourceMethodException;
import io.github.qianlixy.cache.exception.InitCacheManagerException;
import io.github.qianlixy.cache.impl.AbstractCacheAdapterFactory;
import io.github.qianlixy.cache.parse.DruidSQLParser;
import io.github.qianlixy.cache.wrapper.CacheProcesser;
import io.github.qianlixy.cache.wrapper.DefaultCacheProcesser;

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
		if(null == cacheConfig.getCacheClientFactory()) {
			if(null == cacheConfig.getCacheClient()) {
				throw new InitCacheManagerException("AbstractCacheClientFactory and CacheClientAdapter cannot be null at the same time");
			}
			cacheConfig.setCacheClientFactory(AbstractCacheAdapterFactory.buildFactory(cacheConfig.getCacheClient()));
		}
		AbstractCacheAdapterFactory.setApplicationFactory(cacheConfig.getCacheClientFactory());
		if(null == cacheConfig.getSQLParser()) {
			if(null == cacheConfig.getDataSource()) {
				throw new InitCacheManagerException("SQLParser and DataSource cannot be null at the same time");
			}
			DruidSQLParser SQLParser = new DruidSQLParser();
			SQLParser.setDataSource(cacheConfig.getDataSource());
			cacheConfig.setSQLParser(SQLParser);
		}
		
		//初始化缓存上下文信息
		cacheContext = new DefaultCacheContext(cacheConfig.getCacheClientFactory().buildCacheClient());
		
		//完善配置信息
		cacheConfig.getSQLParser().setCacheContext(cacheContext);
		
		//初始化缓存key生成策略
		if(null == cacheKeyGenerator) {
			cacheKeyGenerator = new MD5CacheKeyGenerator();
		}
	}

	@Override
	public Object doCache(ProceedingJoinPoint joinPoint) throws Throwable {
		CacheProcesser cacheProcesser = null;
		try {
			//注册拦截源方法
			cacheContext.register(joinPoint, cacheKeyGenerator);
			//生成源方法缓存操作包装类
			cacheProcesser = new DefaultCacheProcesser(joinPoint, cacheContext,
					cacheConfig.getCacheClientFactory().buildCacheClient(),
					cacheConfig.getDefaultCacheTime());
			Boolean isQuery = cacheContext.isQuery();
			if(null != isQuery && isQuery) {
				Object cache = cacheProcesser.getCache();
				LOGGER.debug("Use cache client data on [{}]", joinPoint.getSignature().toLongString());
				return cache;
			}
			return cacheProcesser.doProcessAndCache();
		} catch(ExecuteSourceMethodException e) {
			throw e;
		} catch(Throwable th) {
			return null == cacheProcesser ? joinPoint.proceed() : cacheProcesser.doProcessAndCache();
		}
	}
	
}
