package com.qianlixy.framework.cache.parse;

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
import com.qianlixy.framework.cache.context.SqlParseContext;

public class DruidSqlParser extends FilterEventAdapter implements SqlParser {
	
	private SqlParseContext context;

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
			LOGGER.debug("Intercepted sql : [{}]", sql);
			//context.addCacheMethodSql(sql);
			List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType.toLowerCase());
			for (SQLStatement sqlStatement : stmtList) {
				MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
				sqlStatement.accept(visitor);
				Map<Name, TableStat> tableMap = visitor.getTables();
//				LOGGER.debug("Intercepted table stat : {}", tableMap);
				for (Name name : tableMap.keySet()) {
					String tableName = name.getName();
//					LOGGER.debug("Intercepted table : {}", tableName);
					context.addCacheMethodTable(tableName.toLowerCase());
					TableStat tableStat = tableMap.get(name);
					if (tableStat.getInsertCount() > 0
							|| tableStat.getUpdateCount() > 0
							|| tableStat.getDeleteCount() > 0) {
						context.setTableLastAlterTime(tableName.toLowerCase(), context.getConsistentTime());
						if (!isAlter) isAlter = true;
					}
				}
			}
			context.setIsAlter(isAlter);
			context.setIsQuery(!isAlter);
		} catch (Throwable th) {
			LOGGER.error("Occur exception while parse sql", th);
			context.SetIsFinishSqlParse(false);
			context.setThrowable(th);
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
	public void setSqlParseContext(SqlParseContext context) {
		this.context = context;
	}
	
}
