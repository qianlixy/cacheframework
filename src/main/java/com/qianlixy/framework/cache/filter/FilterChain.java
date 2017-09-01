package com.qianlixy.framework.cache.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.qianlixy.framework.cache.AbstractConfig;
import com.qianlixy.framework.cache.context.Context;
import com.qianlixy.framework.cache.wrapper.CacheProcesser;
import com.qianlixy.framework.cache.wrapper.SourceProcesser;

/**
 * 过滤器链发起者，依次调用过滤器
 * @author qianli_xy@163.com
 * @since 1.7
 */
public class FilterChain implements Filter {

	// 当前线程正在执行过滤器所在链的位置索引的上下文key
	private static final String FILTER_INDEX = UUID.randomUUID().toString();
	
	// 过滤器集合
	private List<Filter> filters;
	
	@Override
	public Object doFilter(SourceProcesser sourceProcesser, CacheProcesser cacheProcesser, 
			Context context, FilterChain chain) throws Throwable {
		if(null == filters || filters.size() <= 0) return sourceProcesser.doProcess();
		Integer index = (Integer) context.getAttribute(FILTER_INDEX);
		if (null == index) {
			index = 0;
		}
		if (filters.size() > index) {
			Filter filter = filters.get(index);
			context.setAttribute(FILTER_INDEX, ++index);
			Object data = filter.doFilter(sourceProcesser, cacheProcesser, context, chain);
			if (null != data) {
				context.removeAttribute(FILTER_INDEX);
				return data;
			}
		}
		context.removeAttribute(FILTER_INDEX);
		return sourceProcesser.doProcess();
	}

	@Override
	public void init(AbstractConfig config) {
		// 注册过滤器
		filters = new ArrayList<>();
		filters.add(new ReturnSourceNotExistValidCacheFilter());
		filters.add(new ReturnExistValidCacheFilter());
		
		for (Filter filter : filters) {
			filter.init(config);
		}
	}

}
