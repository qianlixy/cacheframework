package io.github.qianlixy.framework.cache.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 缓存上下文接口
 * @author qianli_xy@163.com
 * @since 1.7
 */
public interface Context {
	
	/** 日志打印。子类使用该日志对象打印日志，便于日志打印管理 */
	Logger LOGGER = LoggerFactory.getLogger(Context.class);
	
	/** 存放当前线程中，拦截的源方法的实例对象key */
	int THREAD_LOCAL_KEY_TARGET = 0x1;
	/** 存放当前线程中，拦截的源方法名称key */
	int THREAD_LOCAL_KEY_METHOD = 0x2;
	/** 存放当前线程中，拦截的源方法参数key */
	int THREAD_LOCAL_KEY_PARAMS = 0x3;
	
	/** 存放在当前线程中，是否完成SQL解析key */
	int THREAD_LOCAL_KEY_IS_FINISH_SQL_PARSE = 0x4;
	/** 存放在当前线程中，解析SQL时产生的异常key */
	int THREAD_LOCAL_KEY_PARSING_THROWABLE = 0x5;
	
	/** 存放在当前线程中，源方法的静态唯一标示key */
	int THREAD_LOCAL_KEY_METHOD_STATIC_UNIQUE_MARK = 0x6;
	/** 存放在当前线程中，源方法的动态唯一标示key */
	int THREAD_LOCAL_KEY_METHOD_DYNAMIC_UNIQUE_MARK = 0x7;
	
	/** 存放在当前线程中，是否源方法是修改方法key */
	int THREAD_LOCAL_KEY_IS_ALTER_METHOD = 0x8;
	/** 存放在当前线程中，是否源方法是查询方法key */
	int THREAD_LOCAL_KEY_IS_QUERY_METHOD = 0x9;
	
	/** 存放在缓存中，源方法和SQL的映射key */
	String CACHE_KEY_METHOD_SQL_MAP = Context.class.getClass().getName() + "METHOD_SQL_MAP";
	/** 存放在缓存中，源方法和数据库表（table）的映射key */
	String CACHE_KEY_METHOD_TABLE_MAP = Context.class.getClass().getName() + "METHOD_TABLE_MAP";
	/** 存放在缓存中，源方法的最后查询时间的映射key */
	String CACHE_KEY_METHOD_LAST_QUERY_TIME_MAP = Context.class.getClass().getName() + "METHOD_LAST_QUERY_TIME_MAP";
	/** 存放在缓存中，源方法的最后修改时间的映射key */
	String CACHE_KEY_TABLE_LAST_ALERT_TIME_MAP = Context.class.getClass().getName() + "TABLE_LAST_ALERT_TIME_MAP";
	
	/** 
	 * 设置到上下文中值 
	 * @param key 标示key
	 * @param value 值
	 */
	void setAttribute(Object key, Object value);
	
	/**
	 * 获取上下文中的值
	 * @param key 标示key
	 */
	Object getAttribute(Object key);
	
	/**
	 * 删除上下文中的值
	 * @param key 标示key
	 */
	void removeAttribute(Object key);
}
