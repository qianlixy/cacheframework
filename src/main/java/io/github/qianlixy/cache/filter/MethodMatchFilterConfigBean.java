package io.github.qianlixy.cache.filter;

import java.util.regex.Pattern;

public class MethodMatchFilterConfigBean {

	private Pattern pattern;
	private boolean isFrom = false;
	private int cacheTime = -1;
	private boolean isKeep = false;

	public MethodMatchFilterConfigBean() {}

	public MethodMatchFilterConfigBean(Pattern pattern, 
			boolean isFrom, int cacheTime, boolean isKeep) {
		this.pattern = pattern;
		this.isFrom = isFrom;
		this.cacheTime = cacheTime;
		this.isKeep = isKeep;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public boolean isFrom() {
		return isFrom;
	}

	public void setFrom(boolean isFrom) {
		this.isFrom = isFrom;
	}

	public int getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
	}

	public boolean isKeep() {
		return isKeep;
	}

	public void setKeep(boolean isKeep) {
		this.isKeep = isKeep;
	}

	@Override
	public String toString() {
		return "MethodMatchFilterConfigBean [pattern=" + pattern + ", isFrom=" + isFrom + ", cacheTime=" + cacheTime
				+ ", isKeep=" + isKeep + "]";
	}
	
}
