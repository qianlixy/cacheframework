package io.github.qianlixy.cache;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

import io.github.qianlixy.cache.impl.AbstractCacheAdapterFactory;
import io.github.qianlixy.cache.parse.SQLParser;

/**
 * 缓存配置
 * 
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月9日 下午9:08:50
 */
public class CacheConfig {
	
	/** 缓存客户端 */
	private Object cacheClient;
	
	/** 缓存客户端抽象工厂 */
	private static AbstractCacheAdapterFactory<?> cacheClientFactory;
	
	/** 数据库连接池。注意：暂支持DruidDataSource */
	private DataSource dataSource;
	
	/** SQL解析器 */
	private SQLParser SQLParser;
	
	/** 全局默认缓存时间，单位：秒 */
	private int defaultCacheTime = 3600;

	public Object getCacheClient() {
		return cacheClient;
	}

	public void setCacheClient(Object cacheClient) {
		this.cacheClient = cacheClient;
	}

	public static AbstractCacheAdapterFactory<?> getCacheClientFactory() {
		return cacheClientFactory;
	}

	public void setCacheClientFactory(AbstractCacheAdapterFactory<?> cacheClientFactory) {
		CacheConfig.cacheClientFactory = cacheClientFactory;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * 设置数据库连接池，用于生成SQLParser。
	 * 注意：暂支持DruidDataSource
	 * @param dataSource 数据库连接池
	 * @see DruidDataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public SQLParser getSQLParser() {
		return SQLParser;
	}

	public void setSQLParser(SQLParser sQLParser) {
		SQLParser = sQLParser;
	}

	public int getDefaultCacheTime() {
		return defaultCacheTime;
	}

	public void setDefaultCacheTime(int defaultCacheTime) {
		this.defaultCacheTime = defaultCacheTime;
	}
	
}
