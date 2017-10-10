package io.github.qianlixy.cache.context;

import java.io.IOException;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.CacheConfig;
import io.github.qianlixy.cache.exception.ConsistentTimeException;

/**
 * 使用缓存客户端实现的一致性时间
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月9日 下午11:05:09
 */
public class CacheClientConsistentTime implements ConsistentTime {
	
	private static CacheClientConsistentTime instance = new CacheClientConsistentTime();
	
	private CacheAdapter cacheAdapter;

	private CacheClientConsistentTime() {
		try {
			cacheAdapter = CacheConfig.getCacheClientFactory().buildCacheClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static CacheClientConsistentTime newInstance() {
		return instance;
	}
	
	@Override
	public long time() throws ConsistentTimeException {
		return cacheAdapter.consistentTime();
	}

}
