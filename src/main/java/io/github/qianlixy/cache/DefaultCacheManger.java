package io.github.qianlixy.cache;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.context_new.CacheContext;
import io.github.qianlixy.cache.context_new.DefaultCacheContext;
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
	}

	@Override
	public Object doCache(ProceedingJoinPoint joinPoint) throws Throwable {
		//注册拦截源方法
		cacheContext.register(joinPoint);
		//生成源方法包装类
		SourceProcesser sourceProcesser = new DefaultSourceProcesser(joinPoint, cacheContext);
		//生成源方法缓存操作包装类
		CacheProcesser cacheProcesser = new DefaultCacheProcesser(cacheContext,
				CacheConfig.getCacheClientFactory().buildCacheClient(),
				cacheConfig.getDefaultCacheTime());
		boolean isQuery = false;
		try {
			isQuery = cacheContext.isQuery();
		} catch(NullPointerException e) {
			Object source = sourceProcesser.doProcess();
			cacheProcesser.putCache(source);
			return source;
		}
		if(isQuery) {
			Object cache = cacheProcesser.getCache();
			if(null == cache) {
				Object source = sourceProcesser.doProcess();
				cacheProcesser.putCache(source);
				return source;
			}
			LOGGER.debug("Use cache client data on [{}]", joinPoint.getSignature().toLongString());
			return cache;
		}
		return sourceProcesser.doProcess();
	}

}
