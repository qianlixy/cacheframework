package io.github.qianlixy.cache.impl;

import java.io.IOException;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.CollectionCacheClient;
import net.rubyeye.xmemcached.MemcachedClient;
import redis.clients.jedis.Jedis;

/**
 * 缓存适配器抽象工厂
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月9日 下午9:30:17
 * @param <T> 缓存客户端
 */
public abstract class AbstractCacheAdapterFactory<T> {
	
	protected T client;

	public void setClient(T client) {
		this.client = client;
	}
	
	public abstract CacheAdapter buildCacheClient() throws IOException;
	
	public abstract CollectionCacheClient buildCollectionCacheClient() throws IOException;
	
	public static AbstractCacheAdapterFactory<?> buildFactory(Object cacheClient) {
		if(cacheClient instanceof MemcachedClient) {
			MemcachedCacheAdapterFactory factory = new MemcachedCacheAdapterFactory();
			factory.setClient((MemcachedClient) cacheClient);
			return factory;
		}
		else if(cacheClient instanceof Jedis) {
			RedisCacheAdapterFactory factory = new RedisCacheAdapterFactory();
			factory.setClient((Jedis) cacheClient);
			return factory;
		}
		return null;
	}
	
}
