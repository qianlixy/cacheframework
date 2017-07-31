package com.qianlixy.framework.cache.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.qianlixy.framework.cache.CollectionCacheClient;

import redis.clients.jedis.Jedis;

/**
 * //TODO 待实现集合类Redis缓存适配器
 * 
 * @author qianli_xy@163.com
 * @since 1.7
 */
public class CollectionRedisCacheAdapter extends RedisCacheAdapter implements CollectionCacheClient {

	public CollectionRedisCacheAdapter(Jedis jedis) {
		super(jedis);
	}

	@Override
	public boolean set(String key, Object value) {
		// TODO Auto-generated method stub
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
	public Map<Object, Object> getMap(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getMap(String mapKey, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean putMap(String mapKey, String key, Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeMap(String mapKey) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object removeMap(String mapKey, Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Object> getSet(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean putSet(String key, Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putSet(String key, Collection<Object> values) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object removeSet(String key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> getList(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getList(String key, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean putList(String key, Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putList(String key, Collection<Object> values) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putList(String key, int index, Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeList(String key, Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object removeList(String key, int index) {
		// TODO Auto-generated method stub
		return null;
	}

}
