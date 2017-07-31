package com.qianlixy.framework.cache.impl;

import java.util.List;

import com.qianlixy.framework.cache.CacheClient;
import com.qianlixy.framework.cache.exception.ConsistentTimeException;

import redis.clients.jedis.Jedis;

/**
 * //TODO 待实现Redis缓存适配器
 * 
 * @author qianli_xy@163.com
 * @since 1.7
 */
public class RedisCacheAdapter implements CacheClient {
	
	protected Jedis jedis;

	public RedisCacheAdapter(Jedis jedis) {
		this.jedis = jedis;
	}

	@Override
	public boolean set(String key, Object value) {
		return false;
	}

	@Override
	public boolean set(String key, Object value, int time) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object get(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long consistentTime() throws ConsistentTimeException {
		try {
			List<String> time = jedis.time();
			return Long.valueOf(time.get(0) + time.get(1));
		} catch (Exception e) {
			throw new ConsistentTimeException(e);
		}
	}

}
