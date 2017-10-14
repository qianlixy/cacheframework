package io.github.qianlixy.cache.context;

/**
 * 缓存key的生成提供接口
 * @author qianli_xy@163.com
 * @since 1.0.0
 */
public interface CacheKeyProvider {

	String provideKey(String key);
	
}
