package com.qianlixy.framework.cache.filter;

import java.util.List;
import java.util.UUID;

import com.qianlixy.framework.cache.CacheProcesser;
import com.qianlixy.framework.cache.SourceProcesser;
import com.qianlixy.framework.cache.context.Context;

public class FilterChain implements Filter {
	
	private static final String FILTER_INDEX = UUID.randomUUID().toString();
	
	private List<Filter> filters;
	
	public FilterChain() {}
	
	public FilterChain(List<Filter> filters) {
		this.filters = filters;
	}
	
	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

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

}
