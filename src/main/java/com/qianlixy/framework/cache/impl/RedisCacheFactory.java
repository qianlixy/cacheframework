package com.qianlixy.framework.cache.impl;

import com.qianlixy.framework.cache.CacheClient;
import com.qianlixy.framework.cache.CollectionCacheClient;

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
