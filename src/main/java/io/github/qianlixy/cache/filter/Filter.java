package io.github.qianlixy.cache.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.qianlixy.cache.CacheConfig;
import io.github.qianlixy.cache.wrapper.CacheProcesser;

/**
 * <p>缓存过滤器接口</p>
 * <p>泛型为{@link FilterConfig}时，{@link #setConfig(FilterConfig)}将在传入配置对象不为null时调用</p>
 * <p>泛型为{@link FilterRequiredConfig}时，{@link #setConfig(FilterConfig)}会调用，并且传入的对象不能为空</p>
 * @author qianli_xy@163.com
 * @since 1.0.0
 * @date 2017年10月14日 上午9:21:10
 */
public interface Filter<FC extends FilterConfig> {
	
	Logger LOGGER = LoggerFactory.getLogger(Filter.class);

	/**
	 * 过滤器执行的优先级。该值越小，则优先级越高
	 * @return 执行的优先级
	 */
	int order();
	
	/**
	 * 传入该过滤器所需要类型的配置对象
	 * @param config 配置对象，通过缓存配置参数获取{@link CacheConfig#getFilterConfigs()}
	 * @see CacheConfig#getFilterConfigs()
	 */
	void setConfig(FC config);
	
	/**
	 * 过滤缓存，可对缓存进行相应操作。一旦返回则终止后面过滤器的执行。
	 * @param cacheProcesser 缓存包装类，获取源方法信息、缓存操作
	 * @param filterChain 过滤器链，向下一个过滤器传递执行
	 * @return 返回缓存数据
	 * @throws Throwable 执行期间遇到的异常信息
	 */
	Object doFilter(CacheProcesser cacheProcesser, FilterChain filterChain) throws Throwable;
	
}
