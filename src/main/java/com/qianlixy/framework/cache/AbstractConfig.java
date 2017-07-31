package com.qianlixy.framework.cache;

import java.util.List;

import com.qianlixy.framework.cache.filter.Filter;
import com.qianlixy.framework.cache.impl.AbstractCacheClientFactory;
import com.qianlixy.framework.cache.parse.SqlParser;

/**
 * 缓存管理配置信息
 * @author qianli_xy@163.com
 * @since 1.7
 */
public abstract class AbstractConfig {

	/** 缓存客户端抽象工厂类，用于构建缓存客户端 */
	protected AbstractCacheClientFactory<?> cacheClientFactory;
	
	/** SQL解析器 */
	protected SqlParser sqlParser;
	
	/** 全局默认缓存时间，默认为1小时 */
	protected int defaultCacheTime = 3600;
	
	/**
	 * 对源方法的过滤器,具体的缓存策略都在过滤器中实现
	 * @see Filter
	 */
	protected List<Filter> filters;
	
	/**
	 * <p>是否过滤源方法，不过滤源方法意味着不对源方法做缓存处理 </p>
	 * <p>可能存在一些应用不需要缓存处理，可以将该参数设置为false。注意，SqlParser还是会解析SQL，因为要通知其他缓存过滤器做缓存处理 </p>
	 * <p>默认值为true </p>
	 */
	protected Boolean isProxyCached = true;
	
	public void setCacheFactory(AbstractCacheClientFactory<?> cacheFactory) {
		this.cacheClientFactory = cacheFactory;
	}

	public void setSqlParser(SqlParser sqlParser) {
		this.sqlParser = sqlParser;
	}

	public void setDefaultCacheTime(int defaultCacheTime) {
		this.defaultCacheTime = defaultCacheTime;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	public void setIsProxyCached(Boolean isProxyCached) {
		this.isProxyCached = isProxyCached;
	}
}
