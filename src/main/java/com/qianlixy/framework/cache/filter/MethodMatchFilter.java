package com.qianlixy.framework.cache.filter;

import java.util.List;

import com.qianlixy.framework.cache.AbstractConfig;
import com.qianlixy.framework.cache.context.Context;
import com.qianlixy.framework.cache.wrapper.CacheProcesser;
import com.qianlixy.framework.cache.wrapper.SourceProcesser;

public class MethodMatchFilter implements Filter {
	
	List<String> patterns;

	@Override
	public void init(AbstractConfig config) {
		
	}

	@Override
	public Object doFilter(SourceProcesser sourceProcesser, CacheProcesser cacheProcesser, Context context,
			FilterChain chain) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
