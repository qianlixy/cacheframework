package io.github.qianlixy.framework.cache.filter;

import java.util.List;
import java.util.regex.Pattern;

import io.github.qianlixy.framework.cache.AbstractConfig;
import io.github.qianlixy.framework.cache.context.Context;
import io.github.qianlixy.framework.cache.wrapper.CacheProcesser;
import io.github.qianlixy.framework.cache.wrapper.SourceProcesser;

public class MethodMatchFilter implements Filter {
	
	private Pattern pattern;
	
	@Override
	public void init(AbstractConfig config) {
		List<String> cacheMethods = config.getCacheMethods();
		StringBuilder sb = new StringBuilder();
		for (String string : cacheMethods) {
			sb.append(string).append("|");
		}
		pattern = Pattern.compile(sb.substring(0, sb.length() - 2));
	}

	@Override
	public Object doFilter(SourceProcesser sourceProcesser, CacheProcesser cacheProcesser, Context context,
			FilterChain chain) throws Throwable {
		//TODO 待测试
		if(pattern.matcher(sourceProcesser.getFullMethodName()).find()) {
			chain.doFilter(sourceProcesser, cacheProcesser, context, chain);
		}
		return sourceProcesser.doProcess();
	}

}
