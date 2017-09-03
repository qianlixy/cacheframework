package io.github.qianlixy.framework.cache.wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.qianlixy.framework.cache.CacheManager;
import io.github.qianlixy.framework.cache.parse.SqlParser;

/**
 * <p>定义拦截到需要被缓存源方法的基本信息和操作的包装接口</p>
 * <p>该接口主要作用是：不暴露源方法的实例对象，防止通过实例反复执行源方法</p>
 *
 * @author qianli_xy@163.com
 * @since 1.7
 */
public interface SourceProcesser {
	
	/** 日志打印。该日志打印对象绑定到{@link CacheManager}类上，便于日志管理  */
	Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);
	
	/**
	 * 获取源方法的实例Class
	 * @return 源方法的实例Class
	 */
	Class<?> getTargetClass();
	
	/**
	 * <p>获取源方法的方法名称。形如：getMethodName</p>
	 * @return 源方法的方法名称
	 */
	String getMethodName();
	
	/**
	 * <p>获取源方法的参数，如果该方法没有参数，返回null</p>
	 * @return 源方法参数数组
	 */
	Object[] getArgs();
	
	/**
	 * <p>获取源方法的全名称，格式：[返回值类型] [类名].[方法名]([参数类型] ...)</p>
	 * <p>形如：java.lang.String java.lang.String.toString()</p>
	 * @return 源方法的全名称
	 */
	String getFullMethodName();

	/**
	 * <p>执行源方法，改方法只能执行源方法一次，防止对数据库造成额外压力</p>
	 * @return 返回源方法执行后的返回值（如果源方法没有返回值或者源方法返回值为null将返回null）
	 * @throws 会抛出源方法执行期间可能会出现的异常
	 */
	Object doProcess() throws Throwable;

	/**
	 * <p>源方法是否是对数据库进行insert、update、delete操作。</p>
	 * <p>无论源方法向数据库发送多少条SQL，只要存在一条SQL使用了insert、update、delete操作就认为该源方法是修改方法。修改方法是不能被缓存的</p>
	 * <p>注意：该方法是在{@link SqlParser}进行SQL解析之后才能调用，也就是执行{@link #doProcess()}方法后该方法才有效</p>
	 * @return true - 源方法是修改方法，false - 源方法是查询方法
	 * @see #isQuery()
	 */
	boolean isAlter();

	/**
	 * <p>源方法是否是对数据库进行select操作。</p>
	 * <p>注意：该方法是在{@link SqlParser}进行SQL解析之后才能调用，也就是执行{@link #doProcess()}方法后该方法才有效</p>
	 * @return true - 源方法是查询方法，false - 源方法是修改方法
	 * @see #isAlter()
	 */
	boolean isQuery();
	
}
