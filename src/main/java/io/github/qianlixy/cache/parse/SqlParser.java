package io.github.qianlixy.cache.parse;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.qianlixy.cache.context.SqlParseContext;
import io.github.qianlixy.cache.context_new.MethodContext;

/**
 * <p>SQL解析器 </p>
 * <p>SQL解析器采用对数据库连接池操作，获取SQL信息。可以实现该接口以适配不同数据库连接池 </p>
 * <p>解析器通过通知形式将解析结果保存在{@link SqlParseContext}实例中，供缓存管理器使用 </p>
 * @see SqlParseContext 需要的解析结果
 * @author qianli_xy@163.com
 * @since 1.7
 */
public interface SqlParser {
	
	/** 日志打印。子类使用该日志对象打印日志，便于日志打印管理 */
	Logger LOGGER = LoggerFactory.getLogger(SqlParser.class);
	
	/**
	 * 设置数据库连接池
	 * @param dataSource 数据库连接池
	 * @throws ClassCastException 不同SQL解析器需要不同的数据库连接池，错误的数据库连接池类型将抛出异常
	 */
	void setDataSource(DataSource dataSource) throws ClassCastException;
	
	/**
	 * 设置SQL解析上下文对象，用于存放SQL解析结果
	 * @param context SQL解析上下文
	 */
	void setMethodContext(MethodContext context);
	
}
