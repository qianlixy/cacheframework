package io.github.qianlixy.cache.context_new;

import java.util.HashMap;
import java.util.Map;

/**
 * 单机单个线程间的上下文信息，线程间信息安全隔离。
 * 使用{@link ThreadLocal}具体实现。
 * @author qianli_xy@163.com
 * @date 2017年10月3日下午8:38:53
 * @since 1.0.0
 */
public class ThreadContext implements Context {

	private static ThreadLocal<Map<Object, Object>> threadLocal = new ThreadLocal<>();
	
	@Override
	public void set(Object key, Object value) {
		get().put(key, value);
	}

	@Override
	public Object get(Object key) {
		return get().get(key);
	}

	@Override
	public void remove(Object key) {
		get().remove(key);
	}

	@Override
	public void clear() {
		threadLocal.remove();
	}
	
	private Map<Object, Object> get() {
		Map<Object, Object> map = threadLocal.get();
		if(null == map) {
			map = new HashMap<>();
			threadLocal.set(map);
		}
		return map;
	}

}
