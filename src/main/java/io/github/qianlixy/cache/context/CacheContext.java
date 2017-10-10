package io.github.qianlixy.cache.context;

import java.util.Collection;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.exception.ConsistentTimeException;

/**
 * 缓存的上下文信息接口
 * @author qianli_xy@163.com
 * @date 2017年10月3日上午11:38:34
 * @since 1.0.0
 */
public interface CacheContext {

	/**
	 * 赋值当前缓存方法执行的多个SQL
	 * @param sqls 当前缓存方法执行的SQL集合
	 */
	void setSqls(List<String> sqls);
	
	/**
	 * 获取当前缓存方法执行的多个SQL
	 * @return 当前缓存方法执行的SQL集合
	 */
	List<String> getSqls();
	
	/**
	 * 赋值当前缓存方法使用到的多个数据库表
	 * @param tables 当前缓存方法执行的SQL对应的table集合
	 */
	void setTables(Collection<String> tables);
	
	/**
	 * 获取当前缓存方法使用到的多个数据库表
	 * @return 当前缓存方法执行的SQL对应的table集合
	 */
	Collection<String> getTables();
	
	/**
	 * 当前方法是否是查询方法，SQL中不存在一条DML语句就判定当前方法为查询方法。
	 * @return true - 是查询方法，false - 不是查询方法
	 */
	boolean isQuery();
	
	/**
	 * 赋值当前方法是否是查询方法
	 * @param isQuery 当前方法是否是查询方法
	 */
	void setQuery(boolean isQuery);
	
	/**
	 * 获取当前方法最后的查询时间
	 * @return 当前方法最后的查询时间，不存在则返回0
	 */
	long getLastQueryTime();
	
	/**
	 * 赋值当前方法最后的查询时间戳
	 * @param lastQueryTime 当前方法最后的查询时间戳，一致性时间戳对象
	 * @throws ConsistentTimeException 
	 * @see ConsistentTime
	 */
	void setLastQueryTime(ConsistentTime lastQueryTime) throws ConsistentTimeException;
	
	/**
	 * 注册一个拦截到的方法
	 * @param joinPoint 拦截的方法信息
	 */
	void register(ProceedingJoinPoint joinPoint);
	
	/**
	 * 赋值数据库表最后的修改时间戳
	 * @param table 数据库表
	 * @param time 一致性时间戳对象
	 * @throws ConsistentTimeException 
	 * @see ConsistentTime
	 */
	void setTableLastAlterTime(String table, ConsistentTime time) throws ConsistentTimeException;
	
	/**
	 * 获取数据库表最后的修改时间戳
	 * @param table 数据库表名称
	 * @return 最后的修改时间戳
	 */
	long getTableLastAlterTime(String table);
	
	/**
	 * 获取当前方法的动态唯一标示
	 * @return 当前方法的动态唯一标示
	 */
	String getDynamicUniqueMark();
	
	/**
	 * 获取当前方法的静态唯一标示
	 * @return 当前方法的静态唯一标示
	 */
	String getStaticUniqueMark();
	
}
