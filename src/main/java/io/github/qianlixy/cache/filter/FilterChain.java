package io.github.qianlixy.cache.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.qianlixy.cache.AbstractConfig;
import io.github.qianlixy.cache.context.Context;
import io.github.qianlixy.cache.wrapper.CacheProcesser;
import io.github.qianlixy.cache.wrapper.SourceProcesser;
import io.github.qianlixy.cache.wrapper.TypeWrapper;

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
		// 作用链为空时，直接返回源方法执行的结果
		if(null == filters || filters.size() <= 0) {
			return sourceProcesser.doProcess();
		}
		
		// 当前线程将被执行的过滤器位置索引
		Integer index = (Integer) context.getAttribute(FILTER_INDEX);
		if (null == index) index = 0;
		
		// 过滤器链还没有执行完
		if (filters.size() > index) {
			Filter filter = filters.get(index);
			context.setAttribute(FILTER_INDEX, ++index);
			Object result = filter.doFilter(sourceProcesser, cacheProcesser, context, chain);
			if(result != null) {
				context.removeAttribute(FILTER_INDEX); 
				return result == TypeWrapper.NULL ? null : result;
			}
		}

		// 如果过滤器链执行结束后仍然没有返回数据，将返回默认数据：cache > source
		context.removeAttribute(FILTER_INDEX); 
		Object cache = cacheProcesser.getCache();
		if(null == cache) {
			return sourceProcesser.doProcess();
		} else {
			// 如果缓存为包装类NULL，说明缓存有效且源数据为null，直接返回null
			LOGGER.debug("*** Use cache data to replace DB data on method [{}]", 
					sourceProcesser.getFullMethodName());
			return cache == TypeWrapper.NULL ? null : cache;
		}
	}

	@Override
	public void init(AbstractConfig config) {
		// 注册过滤器
		filters = new ArrayList<>();
//		filters.add(new MethodMatchFilter());
		filters.add(new ReturnSourceNotExistValidCacheFilter());
		
		for (Filter filter : filters) {
			filter.init(config);
		}
	}

}
