package com.qianlixy.framework.cache.parse;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qianlixy.framework.cache.context.SqlParseContext;

public interface SqlParser {
	
	Logger LOGGER = LoggerFactory.getLogger(SqlParser.class);
	
	void setDataSource(DataSource dataSource);
	
	void setSqlParseContext(SqlParseContext context);
	
}
