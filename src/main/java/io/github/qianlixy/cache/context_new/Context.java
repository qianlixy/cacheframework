package io.github.qianlixy.cache.context_new;

public interface Context {

	/** 
	 * 设置到上下文中值 
	 * @param key 标示key
	 * @param value 值
	 */
	void set(Object key, Object value);
	
	/**
	 * 获取上下文中的值
	 * @param key 标示key
	 */
	Object get(Object key);
	
	/**
	 * 删除上下文中的值
	 * @param key 标示key
	 */
	void remove(Object key);
	
	/**
	 * 清空上下文数据
	 */
	void clear();
	
}
