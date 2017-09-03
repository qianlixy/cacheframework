package io.github.qianlixy.framework.cache.context;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.github.qianlixy.framework.cache.CollectionCacheClient;
import io.github.qianlixy.framework.cache.exception.ConsistentTimeException;

@SuppressWarnings("unchecked")
public class SqlParseContext extends ContextDecorator {
	
	private CollectionCacheClient cacheClient;

	private CacheMethodContext cacheMethodContext;

	public SqlParseContext(CacheMethodContext context, CollectionCacheClient cacheClient) {
		super(context);
		this.cacheMethodContext = context; 
		this.cacheClient = cacheClient;
	}
	
	public void addCacheMethodSql(String sql) {
		Map<Object, Object> sqlMap = cacheClient.getMap(CACHE_KEY_METHOD_SQL_MAP);
		Set<String> sqls = (Set<String>) sqlMap.get(cacheMethodContext.getCacheMethodStaticUniqueMark());
		if (null == sqls) {
			sqls = new HashSet<String>();
		}
		sqls.add(sql);
		sqlMap.put(cacheMethodContext.getCacheMethodStaticUniqueMark(), sqls);
		cacheClient.set(CACHE_KEY_METHOD_SQL_MAP, sqlMap);
	}

	public void addCacheMethodTable(String table) {
		Map<Object, Object> tableMap = cacheClient.getMap(CACHE_KEY_METHOD_TABLE_MAP);
		Set<String> tables = (Set<String>) tableMap.get(cacheMethodContext.getCacheMethodStaticUniqueMark());
		if(null == tables) {
			tables = new HashSet<String>();
		}
		tables.add(table);
		tableMap.put(cacheMethodContext.getCacheMethodStaticUniqueMark(), tables);
		cacheClient.set(CACHE_KEY_METHOD_TABLE_MAP, tableMap);
	}
	
	public void setTableLastAlterTime(String tabel, long time) {
		cacheClient.putMap(CACHE_KEY_TABLE_LAST_ALERT_TIME_MAP, tabel, time);
	}
	
	public void setIsAlter(boolean isAlter) {
		setAttribute(THREAD_LOCAL_KEY_IS_ALTER_METHOD, isAlter);
	}

	public void setIsQuery(boolean isQuery) {
		setAttribute(THREAD_LOCAL_KEY_IS_QUERY_METHOD, isQuery);
	}
	
	public long getConsistentTime() throws ConsistentTimeException {
		return cacheClient.consistentTime();
	}
	
	public void SetIsFinishSqlParse(boolean isFinishSqlParse) {
		setAttribute(THREAD_LOCAL_KEY_IS_FINISH_SQL_PARSE, isFinishSqlParse);
	}
	
	public void setThrowable(Throwable th) {
		setAttribute(THREAD_LOCAL_KEY_PARSING_THROWABLE, th);
	}

}
