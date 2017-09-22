package io.github.qianlixy.cache.filter;

import java.util.List;

/**
 * 方法匹配器
 * @author qianli_xy@163.com
 * @since 1.0.0
 */
public interface MethodMatcher {

	/**
	 * 注册规则。一次注册，多次匹配
	 * @param regulations 规则集合
	 */
	void register(List<String> regulations);
	
	/**
	 * 匹配方法
	 * @param clazz 方法类Class
	 * @param methodName 方法名称
	 * @return true - 匹配成功，false - 匹配失败
	 */
	boolean doMatch(Class<?> clazz, String methodName);
	
}
