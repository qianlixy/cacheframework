package io.github.qianlixy.framework.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CollectionCacheClient extends CacheClient {

	Map<Object, Object> getMap(String key);
	
	Object getMap(String mapKey, String key);
	
	boolean putMap(String mapKey, String key, Object value);
	
	boolean removeMap(String mapKey);
	
	Object removeMap(String mapKey, Object key);
	
	Set<Object> getSet(String key);
	
	boolean putSet(String key, Object value);
	
	boolean putSet(String key, Collection<Object> values);
	
	Object removeSet(String key, Object value);
	
	List<Object> getList(String key);
	
	Object getList(String key, int index);
	
	boolean putList(String key, Object value);
	
	boolean putList(String key, Collection<Object> values);
	
	boolean putList(String key, int index, Object value);
	
	boolean removeList(String key, Object value);
	
	Object removeList(String key, int index);
	
}
