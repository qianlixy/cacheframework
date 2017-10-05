package io.github.qianlixy.cache.context_new;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.utils.UniqueMethodMarkUtil;

@SuppressWarnings("unchecked")
public class DefaultMethodContext implements MethodContext {
	
	/** 源方法的静态唯一标示key */
	int STATIC_UNIQUE_MARK = 0x6;
	/** 源方法的动态唯一标示key */
	int DYNAMIC_UNIQUE_MARK = 0x7;
	/** 是否源方法是查询方法key */
	int IS_QUERY_METHOD = 0x9;
	/** 源方法的最后查询时间的映射key */
	int LAST_QUERY_TIME = 0x10;
	/** 数据库表的最后修改时间的映射key */
	int LAST_ALTER_TIME = 0X11;
	
	private Context threadContext;
	private Context cacheContext;

	public DefaultMethodContext(Context threadContext, Context cacheContext) {
		this.threadContext = threadContext;
		this.cacheContext = cacheContext;
	}

	@Override
	public void set(Object key, Object value) {
		threadContext.set(key, value);
	}

	@Override
	public Object get(Object key) {
		return threadContext.get(key);
	}

	@Override
	public void remove(Object key) {
		threadContext.remove(key);
	}

	@Override
	public void clear() {
		threadContext.clear();
	}

	@Override
	public void setSqls(List<String> sqls) {
		if(null == sqls || sqls.size() < 0) return;
		cacheContext.set(get(STATIC_UNIQUE_MARK), sqls);
	}

	@Override
	public List<String> getSqls() {
		return (List<String>) cacheContext.get(get(STATIC_UNIQUE_MARK));
	}

	@Override
	public void setTables(Collection<String> tables) {
		if(null == tables || tables.size() < 0) return;
		cacheContext.set(get(STATIC_UNIQUE_MARK), tables);
	}

	@Override
	public Collection<String> getTables() {
		return (Collection<String>) cacheContext.get(get(STATIC_UNIQUE_MARK));
	}

	@Override
	public boolean isQuery() {
		Map<String, Boolean> isQueryMap = (Map<String, Boolean>) cacheContext.get(IS_QUERY_METHOD);
		if(null == isQueryMap) return false;
		return isQueryMap.get(get(isQueryMap));
	}

	@Override
	public void setQuery(boolean isQuery) {
		Map<String, Boolean> isQueryMap = (Map<String, Boolean>) cacheContext.get(IS_QUERY_METHOD);
		if(null == isQueryMap) {
			isQueryMap = new HashMap<>();
		}
		isQueryMap.put(String.valueOf(get(DYNAMIC_UNIQUE_MARK)), isQuery);
		cacheContext.set(IS_QUERY_METHOD, isQueryMap);
	}

	@Override
	public long getLastQueryTime() {
		Map<String, Long> isQueryMap = (Map<String, Long>) cacheContext.get(LAST_QUERY_TIME);
		if(null == isQueryMap) return 0L;
		Long time = isQueryMap.get(get(DYNAMIC_UNIQUE_MARK));
		return null == time ? 0L : time;
	}

	@Override
	public void setLastQueryTime(long lastQueryTime) {
		Map<String, Long> isQueryMap = (Map<String, Long>) cacheContext.get(LAST_QUERY_TIME);
		if(null == isQueryMap) isQueryMap = new HashMap<>();
		isQueryMap.put(String.valueOf(get(DYNAMIC_UNIQUE_MARK)), lastQueryTime);
		cacheContext.set(LAST_QUERY_TIME, isQueryMap);
	}

	@Override
	public void register(ProceedingJoinPoint joinPoint) {
		set(STATIC_UNIQUE_MARK, String.valueOf(joinPoint.getSignature().toLongString().hashCode()));
		set(DYNAMIC_UNIQUE_MARK, String.valueOf(UniqueMethodMarkUtil.uniqueMark(joinPoint).hashCode()));
	}

	@Override
	public void setTableLastAlterTime(String table, long time) {
		Map<String, Long> isQueryMap = (Map<String, Long>) cacheContext.get(LAST_ALTER_TIME);
		if(null == isQueryMap) isQueryMap = new HashMap<>();
		isQueryMap.put(table, time);
		cacheContext.set(LAST_ALTER_TIME, isQueryMap);
	}

	@Override
	public long getTableLastAlterTime(String table) {
		Map<String, Long> isQueryMap = (Map<String, Long>) cacheContext.get(LAST_ALTER_TIME);
		if(null == isQueryMap) return 0L;
		Long time = isQueryMap.get(table);
		return null == time ? 0L : time;
	}

}
