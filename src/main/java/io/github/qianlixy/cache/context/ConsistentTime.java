package io.github.qianlixy.cache.context;

import io.github.qianlixy.cache.exception.ConsistentTimeException;

/**
 * 一致性时间，需保证线程安全
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月9日 下午11:02:07
 */
public interface ConsistentTime {
	
	/**
	 * 获取当前时间戳
	 * @return 当前时间戳
	 * @throws ConsistentTimeException
	 */
	long time() throws ConsistentTimeException;
	
}
