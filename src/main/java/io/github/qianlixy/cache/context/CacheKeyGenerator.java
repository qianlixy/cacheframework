package io.github.qianlixy.cache.context;

/**
 * 缓存可以的生成机制
 * @author qianli_xy@163.com
 * @since 1.0.0
 */
public interface CacheKeyGenerator {

	String generate(String key);
	
}
