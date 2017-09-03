package io.github.qianlixy.smartcache.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.qianlixy.smartcache.CollectionCacheClient;
import net.rubyeye.xmemcached.MemcachedClient;

@SuppressWarnings("unchecked")
public class CollectionMemcachedAdapter extends MemcachedAdapter implements CollectionCacheClient {
	
	public CollectionMemcachedAdapter(MemcachedClient client) {
		super(client);
	}

	@Override
	public Map<Object, Object> getMap(String key) throws ClassCastException {
		Object obj = get(key);
		return obj == null ? new HashMap<>() : (HashMap<Object, Object>) obj;
	}

	@Override
	public Object getMap(String mapKey, String key) {
		Map<Object, Object> map = getMap(mapKey);
		return map.get(key);
	}

	@Override
	public boolean putMap(String mapKey, String key, Object value) {
		Map<Object, Object> map = getMap(mapKey);
		map.put(key, value);
		return set(mapKey, map);
	}

	@Override
	public Set<Object> getSet(String key) {
		Object obj = get(key);
		return null == obj ? new HashSet<>() : (HashSet<Object>) obj;
	}

	@Override
	public List<Object> getList(String key) {
		Object obj = get(key);
		return null == obj ? new ArrayList<>() : (ArrayList<Object>) obj;
	}

	@Override
	public Object getList(String key, int index) {
		return getList(key).get(index);
	}

	@Override
	public Object removeMap(String mapKey, Object key) {
		Map<Object, Object> map = getMap(mapKey);
		return map.remove(key);
	}

	@Override
	public boolean putSet(String key, Object value) {
		Set<Object> set = getSet(key);
		set.add(value);
		return set(key, set);
	}
	
	@Override
	public boolean putList(String key, Object value) {
		List<Object> list = getList(key);
		list.add(value);
		return set(key, list);
	}

	@Override
	public boolean putList(String key, int index, Object value) {
		List<Object> list = getList(key);
		list.add(index, value);
		return set(key, list);
	}

	@Override
	public boolean removeMap(String mapKey) {
		return remove(mapKey);
	}

	@Override
	public Object removeSet(String key, Object value) {
		Set<Object> set = getSet(key);
		return set.remove(value);
	}

	@Override
	public boolean removeList(String key, Object value) {
		List<Object> list = getList(key);
		boolean isRemove = list.remove(value);
		set(key, list);
		return isRemove;
	}

	@Override
	public Object removeList(String key, int index) {
		List<Object> list = getList(key);
		Object isRemove = list.remove(index);
		set(key, list);
		return isRemove;
	}

	@Override
	public boolean putSet(String key, Collection<Object> values) {
		Set<Object> set = getSet(key);
		boolean isAdd = set.addAll(values);
		set(key, set);
		return isAdd;
	}

	@Override
	public boolean putList(String key, Collection<Object> values) {
		List<Object> list = getList(key);
		boolean isAdd = list.addAll(values);
		set(key, list);
		return isAdd;
	}

}
