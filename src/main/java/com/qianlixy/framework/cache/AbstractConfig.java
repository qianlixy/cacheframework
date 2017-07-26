package com.qianlixy.framework.cache;

import java.util.List;

import com.qianlixy.framework.cache.filter.Filter;
import com.qianlixy.framework.cache.impl.AbstractCacheFactory;
import com.qianlixy.framework.cache.parse.SqlParser;

public abstract class AbstractConfig {

	protected AbstractCacheFactory<?> cacheFactory;
	protected SqlParser sqlParser;
	/** 缓存时间，默认为1小时 */
	protected int defaultCacheTime = 3600;
	protected List<Filter> filters;
	protected Boolean isProxyCached = true;
	
	public void setCacheFactory(AbstractCacheFactory<?> cacheFactory) {
		this.cacheFactory = cacheFactory;
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
