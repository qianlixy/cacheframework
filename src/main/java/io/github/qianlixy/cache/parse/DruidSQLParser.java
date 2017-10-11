package io.github.qianlixy.cache.parse;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.stat.TableStat.Name;

import io.github.qianlixy.cache.context.CacheClientConsistentTime;
import io.github.qianlixy.cache.context.CacheContext;

public class DruidSQLParser extends FilterEventAdapter implements SQLParser {
	
	private CacheContext context;

	@Override
	protected void statementExecuteUpdateAfter(StatementProxy statement, String sql, int updateCount) {
		handleSql(statement, sql);
	}

	@Override
	protected void statementExecuteQueryAfter(StatementProxy statement, String sql, ResultSetProxy resultSet) {
		handleSql(statement, sql);
	}

	@Override
	protected void statementExecuteAfter(StatementProxy statement, String sql, boolean result) {
		handleSql(statement, sql);
	}

	@Override
	protected void statementExecuteBatchAfter(StatementProxy statement, int[] result) {
		for(String sql : statement.getBatchSqlList()) {
			handleSql(statement, sql);
		}
	}

	private void handleSql(StatementProxy statement, String sql) {
		try {
			String dbType = getDbType(statement);
			boolean isAlter = false;
			LOGGER.debug("Intercepted SQL : [{}]", sql);
			//context.addCacheMethodSql(sql);
			List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType.toLowerCase());
			List<String> tables = new ArrayList<>();
			for (SQLStatement sqlStatement : stmtList) {
				MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
				sqlStatement.accept(visitor);
				Map<Name, TableStat> tableMap = visitor.getTables();
//				LOGGER.debug("Intercepted table stat : {}", tableMap);
				for (Name name : tableMap.keySet()) {
					String tableName = name.getName();
//					LOGGER.debug("Intercepted table : {}", tableName);
					tables.add(tableName.toLowerCase());
					TableStat tableStat = tableMap.get(name);
					if (tableStat.getInsertCount() > 0
							|| tableStat.getUpdateCount() > 0
							|| tableStat.getDeleteCount() > 0) {
						context.setTableLastAlterTime(tableName.toLowerCase(), CacheClientConsistentTime.newInstance());
						if (!isAlter) isAlter = true;
					}
				}
			}
			//TODO 多张表的处理
			context.setTables(tables);
			context.setQuery(!isAlter);
		} catch (Throwable th) {
			LOGGER.error("Occur exception while parse SQL. exception message : {}", th.getMessage());
		}
	}
	
	private String getDbType(StatementProxy statement) throws SQLException {
		try {
			DatabaseMetaData metaData = statement.getConnectionProxy().getMetaData();
			return metaData.getDatabaseProductName();
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public void setDataSource(DataSource dataSource) {
		if(!(dataSource instanceof DruidDataSource)) {
			throw new ClassCastException("DruidSqlParser need [" 
					+ DruidDataSource.class.getName() 
					+ "] but get [" 
					+ dataSource.getClass().getName() + "]");
		}
		DruidDataSource dds = (DruidDataSource) dataSource;
		List<Filter> filters = new ArrayList<>();
		filters.add(this);
		dds.setProxyFilters(filters);
	}

	@Override
	public void setCacheContext(CacheContext context) {
		this.context = context;
	}
	
}
