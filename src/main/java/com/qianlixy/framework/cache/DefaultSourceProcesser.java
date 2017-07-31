package com.qianlixy.framework.cache;

import org.aspectj.lang.ProceedingJoinPoint;

import com.qianlixy.framework.cache.context.Context;
import com.qianlixy.framework.cache.context.ThreadLocalContext;
import com.qianlixy.framework.cache.exception.SqlParseException;

public class DefaultSourceProcesser implements SourceProcesser {

	private Object source = null;
	private boolean isNull = false;
	
	private ProceedingJoinPoint pjp;
	private ThreadLocalContext threadLocalContext;
	
	public DefaultSourceProcesser(ProceedingJoinPoint pjp, 
			ThreadLocalContext threadLocalContext) {
		this.pjp = pjp;
		this.threadLocalContext = threadLocalContext;
	}

	@Override
	public Class<?> getTargetClass() {
		return pjp.getTarget().getClass();
	}

	@Override
	public String getMethodName() {
		return pjp.getSignature().getName();
	}

	@Override
	public Object[] getArgs() {
		return pjp.getArgs();
	}

	@Override
	public Object doProcess() throws Throwable {
		if (null == source && !isNull) {
			source = pjp.proceed();
			Boolean isFinishSqlParse = (Boolean) threadLocalContext
					.getAttribute(Context.THREAD_LOCAL_KEY_IS_FINISH_SQL_PARSE);
			if (null == isFinishSqlParse)
				isFinishSqlParse = true;
			if (!isFinishSqlParse) {
				throw new SqlParseException(
						(Throwable) threadLocalContext.getAttribute(Context.THREAD_LOCAL_KEY_PARSING_THROWABLE));
			}
			if (null == source)
				isNull = true;
			LOGGER.debug("Execute source method [{}]", getFullMethodName());
		}
		return source;
	}

	@Override
	public boolean isAlter() {
		return (boolean) threadLocalContext.getAttribute(Context.THREAD_LOCAL_KEY_IS_ALTER_METHOD);
	}

	@Override
	public boolean isQuery() {
		return (boolean) threadLocalContext.getAttribute(Context.THREAD_LOCAL_KEY_IS_QUERY_METHOD);
	}

	@Override
	public String getFullMethodName() {
		return pjp.getSignature().toLongString();
	}
}
