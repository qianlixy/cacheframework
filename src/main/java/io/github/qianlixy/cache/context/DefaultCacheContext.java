package io.github.qianlixy.cache.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.CacheAdapter;
import io.github.qianlixy.cache.exception.CacheIsNullException;
import io.github.qianlixy.cache.exception.ConsistentTimeException;
import io.github.qianlixy.cache.utils.UniqueMethodMarkUtil;

@SuppressWarnings("unchecked")
public class DefaultCacheContext implements CacheContext {
	
	private static ThreadLocal<Map<Object, Object>> threadLocal = new ThreadLocal<>();
	
	/** 源方法的静态唯一标示key */
	private String STATIC_UNIQUE_MARK = "STATIC_UNIQUE_MARK";
	private String STATIC_UNIQUE_FULL_MARK = "STATIC_UNIQUE_FULL_MARK";
	/** 源方法的动态唯一标示key */
	private String DYNAMIC_UNIQUE_MARK = "DYNAMIC_UNIQUE_MARK";
	/** 是否源方法是查询方法key */
	private String IS_QUERY_METHOD = "IS_QUERY_METHOD";
	/** 源方法的最后查询时间的映射key */
	private String LAST_QUERY_TIME = "LAST_QUERY_TIME";
	/** 数据库表的最后修改时间的映射key */
	private String LAST_ALTER_TIME = "LAST_ALTER_TIME";
	
	private CacheAdapter cacheContext;

	public DefaultCacheContext(CacheAdapter cacheAdapter) {
		this.cacheContext = cacheAdapter;
	}

	@Override
	public void setSqls(List<String> sqls) {
		if(null == sqls || sqls.size() < 0) return;
		cacheContext.set(get(STATIC_UNIQUE_MARK).toString(), sqls);
	}

	@Override
	public List<String> getSqls() {
		return (List<String>) cacheContext.get(get(STATIC_UNIQUE_MARK).toString());
	}

	@Override
	public void setTables(Collection<String> tables) {
		if(null == tables || tables.size() < 0) return;
		cacheContext.set(get(STATIC_UNIQUE_MARK).toString(), tables);
	}

	@Override
	public Collection<String> getTables() {
		return (Collection<String>) cacheContext.get(get(STATIC_UNIQUE_MARK).toString());
	}

	@Override
	public boolean isQuery() {
		Map<String, Boolean> isQueryMap = (Map<String, Boolean>) cacheContext.get(IS_QUERY_METHOD);
		if(null == isQueryMap) throw new CacheIsNullException();
		Boolean isQuery = isQueryMap.get(get(DYNAMIC_UNIQUE_MARK));
		if(null == isQuery) throw new CacheIsNullException();
		return isQuery;
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
	public void setLastQueryTime(ConsistentTime lastQueryTime) throws ConsistentTimeException {
		Map<String, Long> isQueryMap = (Map<String, Long>) cacheContext.get(LAST_QUERY_TIME);
		if(null == isQueryMap) isQueryMap = new HashMap<>();
		isQueryMap.put(String.valueOf(get(DYNAMIC_UNIQUE_MARK)), lastQueryTime.time());
		cacheContext.set(LAST_QUERY_TIME, isQueryMap);
	}

	@Override
	public void register(ProceedingJoinPoint joinPoint) {
		set(STATIC_UNIQUE_MARK, String.valueOf(joinPoint.getSignature().toLongString().hashCode()));
		set(STATIC_UNIQUE_FULL_MARK, joinPoint.getSignature().toLongString());
		set(DYNAMIC_UNIQUE_MARK, String.valueOf(UniqueMethodMarkUtil.uniqueMark(joinPoint).hashCode()));
	}

	@Override
	public void setTableLastAlterTime(String table, ConsistentTime time) throws ConsistentTimeException {
		Map<String, Long> isQueryMap = (Map<String, Long>) cacheContext.get(LAST_ALTER_TIME);
		if(null == isQueryMap) isQueryMap = new HashMap<>();
		isQueryMap.put(table, time.time());
		cacheContext.set(LAST_ALTER_TIME, isQueryMap);
	}

	@Override
	public long getTableLastAlterTime(String table) {
		Map<String, Long> isQueryMap = (Map<String, Long>) cacheContext.get(LAST_ALTER_TIME);
		if(null == isQueryMap) return 0L;
		Long time = isQueryMap.get(table);
		return null == time ? 0L : time;
	}

	public void set(Object key, Object value) {
		get().put(key, value);
	}

	public Object get(Object key) {
		return get().get(key);
	}

	public void remove(Object key) {
		get().remove(key);
	}

	private Map<Object, Object> get() {
		Map<Object, Object> map = threadLocal.get();
		if(null == map) {
			map = new HashMap<>();
			threadLocal.set(map);
		}
		return map;
	}

	@Override
	public String getDynamicUniqueMark() {
		return get(DYNAMIC_UNIQUE_MARK).toString();
	}

	@Override
	public String getStaticUniqueMark() {
		return get(STATIC_UNIQUE_MARK).toString();
	}

	@Override
	public String toString() {
		return get(STATIC_UNIQUE_FULL_MARK).toString();
	}
	
}
