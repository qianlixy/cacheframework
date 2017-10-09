package io.github.qianlixy.cache.impl;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.CollectionCacheClient;
import redis.clients.jedis.Jedis;


/**
 * //TODO 待实现Redis缓存构建工厂类
 * 
 * @author qianli_xy@163.com
 * @since 1.7
 */
public class RedisCacheAdapterFactory extends AbstractCacheAdapterFactory<Jedis> {

	@Override
	public CacheAdapter buildCacheClient() {
		return null;
	}

	@Override
	public CollectionCacheClient buildCollectionCacheClient() {
		return null;
	}

}
