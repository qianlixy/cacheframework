package io.github.qianlixy.cache.filter;

import java.util.List;
import java.util.regex.Pattern;

import io.github.qianlixy.cache.AbstractConfig;
import io.github.qianlixy.cache.context.Context;
import io.github.qianlixy.cache.wrapper.CacheProcesser;
import io.github.qianlixy.cache.wrapper.SourceProcesser;

public class MethodMatchFilter implements Filter {
	
	private Pattern pattern;
	
	@Override
	public void init(AbstractConfig config) {
		List<String> cacheMethods = config.getCacheMethods();
		if(null == cacheMethods) return;
		StringBuilder sb = new StringBuilder();
		for (String string : cacheMethods) {
			sb.append(string).append("|");
		}
		pattern = Pattern.compile(sb.substring(0, sb.length() - 2));
	}

	@Override
	public Object doFilter(SourceProcesser sourceProcesser, CacheProcesser cacheProcesser, Context context,
			FilterChain chain) throws Throwable {
		if(null != pattern) {
			if(pattern.matcher(sourceProcesser.getFullMethodName()).find()) {
				return chain.doFilter(sourceProcesser, cacheProcesser, context, chain);
			}
			LOGGER.debug("Cannot match method [{}]", sourceProcesser.getFullMethodName());
			return sourceProcesser.doProcess();
		}
		return chain.doFilter(sourceProcesser, cacheProcesser, context, chain);
	}

}
