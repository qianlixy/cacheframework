package com.qianlixy.framework.cache.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qianlixy.framework.cache.CacheProcesser;
import com.qianlixy.framework.cache.SourceProcesser;
import com.qianlixy.framework.cache.context.Context;

/**
 * <h1>过滤器 </h1>
 * <p>对源方法进行过滤，具体缓存处理将在该实现类中进行 </p>
 * @author qianli_xy@163.com
 * @since 1.7
 */
public interface Filter {
	
	/** 日志打印。子类使用该日志对象打印日志，便于日志打印管理 */
	Logger LOGGER = LoggerFactory.getLogger(Filter.class);

	/**
	 * <p>源方法过滤处理 </p>
	 * <p>如果满足处理条件，直接返回处理结果，过滤器链将不会继续执行 </p>
	 * <p>如果不满足处理条件，返回<code>chain.doFilter(processer, cacheProcesser, context, chain)</code>继续执行过滤器链 </p>
	 * @param sourceProcesser 源方法包装对象
	 * @param cacheProcesser 缓存处理包装对象
	 * @param context 上下文
	 * @param chain 过滤器链
	 * @return 处理后的结果，可能是执行源方法返回的值，也可能是缓存客户端返回的缓存
	 * @throws Throwable 过滤器处理缓存时发生异常时会抛出
	 */
	Object doFilter(SourceProcesser sourceProcesser, CacheProcesser cacheProcesser, 
			Context context, FilterChain chain) throws Throwable;
}
