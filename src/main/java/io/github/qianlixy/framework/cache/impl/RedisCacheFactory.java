package io.github.qianlixy.framework.cache.impl;

import io.github.qianlixy.framework.cache.CacheClient;
import io.github.qianlixy.framework.cache.CollectionCacheClient;
import redis.clients.jedis.Jedis;


/**
 * //TODO 待实现Redis缓存构建工厂类
 * 
 * @author qianli_xy@163.com
 * @since 1.7
 */
public class RedisCacheFactory extends AbstractCacheClientFactory<Jedis> {

	@Override
	public CacheClient buildCacheClient() {
		return null;
	}

	@Override
	public CollectionCacheClient buildCollectionCacheClient() {
		return null;
	}

}
