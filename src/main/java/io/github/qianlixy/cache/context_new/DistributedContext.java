package io.github.qianlixy.cache.context_new;

import io.github.qianlixy.cache.CacheClient;
import io.github.qianlixy.cache.exception.ClearCacheException;

/**
 * 分布式上下文信息，保证分布式系统数据一致性
 * 使用缓存客户端实现。
 * @author qianli_xy@163.com
 * @date 2017年10月3日下午8:41:45
 * @since 1.0.0
 */
public class DistributedContext implements Context {
	
	private CacheClient cacheClient;
	
	public DistributedContext(CacheClient cacheClient) {
		this.cacheClient = cacheClient;
	}

	@Override
	public void set(Object key, Object value) {
		cacheClient.set(String.valueOf(key), value);
	}

	@Override
	public Object get(Object key) {
		return cacheClient.get(String.valueOf(key));
	}

	@Override
	public void remove(Object key) {
		cacheClient.remove(String.valueOf(key));
	}

	@Override
	public void clear() {
		// 不能进行缓存清空操作
		throw new ClearCacheException();
	}

}
