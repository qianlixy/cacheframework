package io.github.qianlixy.cache.context;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalContext implements Context {
	
	private static ThreadLocal<Map<Object, Object>> threadLocal = new ThreadLocal<>();

	@Override
	public void setAttribute(Object key, Object value) {
		get().put(key, value);
	}

	@Override
	public Object getAttribute(Object key) {
		return get().get(key);
	}

	@Override
	public void removeAttribute(Object key) {
		get().remove(key);
	}
	
	private Map<Object, Object> get() {
		Map<Object, Object> map = threadLocal.get();
		if(null == map) {
			map = new HashMap<>();
			threadLocal.set(map);
		}
		return map;
	}

	@Override
	public void clear() {
		get().clear();
	}

	@Override
	public boolean isValid() {
		return null == threadLocal.get() ? false : true;
	}

}
