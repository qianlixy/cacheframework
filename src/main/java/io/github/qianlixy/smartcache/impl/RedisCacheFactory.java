package io.github.qianlixy.smartcache.impl;

import io.github.qianlixy.smartcache.CacheClient;
import io.github.qianlixy.smartcache.CollectionCacheClient;
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
