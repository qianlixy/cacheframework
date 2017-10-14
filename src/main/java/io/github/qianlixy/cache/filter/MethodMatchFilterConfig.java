package io.github.qianlixy.cache.filter;

import java.util.Collection;

public class MethodMatchFilterConfig implements FilterRequiredConfig {

	/**
	 * 匹配来源方法时，查询线程栈的最高层级。默认为10
	 */
	private int level = 10;
	
	private Collection<MethodMatchFilterConfigBean> configBeans;

	/**
	 * 匹配来源方法时，查询线程栈的最高层级
	 * @return 层级数。默认为10
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 匹配来源方法时，查询线程栈的最高层级
	 * @param level 层级数。默认为10
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	public Collection<MethodMatchFilterConfigBean> getConfigBeans() {
		return configBeans;
	}

	/**
	 * 匹配路径拦截源方法进行缓存处理。不设置该项配置将全部拦截
	 * @param configBeans 路径拦截的参数配置集合
	 */
	public void setConfigBeans(Collection<MethodMatchFilterConfigBean> configBeans) {
		this.configBeans = configBeans;
	}

	@Override
	public String toString() {
		return "MethodMatchFilterConfig [level=" + level + ", configBeans=" + configBeans + "]";
	}
	
}
