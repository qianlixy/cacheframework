package io.github.qianlixy.cache.wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.qianlixy.cache.AbstractConfig;
import io.github.qianlixy.cache.CacheManager;
import io.github.qianlixy.cache.exception.CacheOperationException;
import io.github.qianlixy.cache.exception.ConsistentTimeException;

/**
 * <p>定义对缓存的处理包装接口</p>
 * <p>获取源方法的缓存，如果源方法的缓存不存在获取已失效返回null</p>
 * <p>设置源方法的缓存。给源方法设置缓存的入口只存在该接口中 </p>
 * <p>可以获取到缓存客户端做其他操作 </p>
 * 
 * @author qianli_xy@163.com
 * @since 1.7
 */
public interface CacheProcesser {
	
	/**
	 * 便于子类打印日志。该日志打印对象绑定到{@link CacheManager}类上，便于日志管理
	 */
	Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);

	/**
	 * 获取源方法的缓存
	 * @return 源方法的缓存（如果存在并且未失效）
	 */
	Object getCache();
	
	/**
	 * 设置源方法的缓存。缓存时间使用默认缓存时间{@link AbstractConfig#defaultCacheTime}
	 * @param cache 源方法的缓存对象
	 * @throws ConsistentTimeException 
	 * @throws CacheOperationException 
	 * @see #putCache(Object, int)
	 * @see AbstractConfig#defaultCacheTime
	 */
	void putCache(Object cache) throws CacheOperationException, ConsistentTimeException;
	
	/**
	 * 设置源方法的缓存
	 * @param cache 源方法的缓存对象
	 * @param time 缓存有效时间
	 * @throws ConsistentTimeException 
	 * @throws CacheOperationException 
	 * @see #putCache(Object)
	 */
	void putCache(Object cache, int time) throws CacheOperationException, ConsistentTimeException;
	
}
