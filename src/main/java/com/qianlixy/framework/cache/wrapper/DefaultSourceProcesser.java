package com.qianlixy.framework.cache.wrapper;

import org.aspectj.lang.ProceedingJoinPoint;

import com.qianlixy.framework.cache.context.Context;
import com.qianlixy.framework.cache.context.ThreadLocalContext;
import com.qianlixy.framework.cache.exception.SqlParseException;

/**
 * <p>源方法包装类的默认实现</p>
 * <p>每拦截到源方法需要生成一个新的实例，否则存在线程安全问题</p>
 * @author qianli_xy@163.com
 * @since 1.7
 */
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
			LOGGER.debug("Execute source method [{}]", getFullMethodName());
			source = pjp.proceed();
			Boolean isFinishSqlParse = (Boolean) threadLocalContext
					.getAttribute(Context.THREAD_LOCAL_KEY_IS_FINISH_SQL_PARSE);
			if (null == isFinishSqlParse)
				isFinishSqlParse = true;
			if (!isFinishSqlParse) {
				throw new SqlParseException(
						(Throwable) threadLocalContext.getAttribute(Context.THREAD_LOCAL_KEY_PARSING_THROWABLE));
			}
			if (null == source) {
				// 如果源数据为null，赋值为包装类型NULL，以使缓存生效 
				source = TypeWrapper.NULL;
			}
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
