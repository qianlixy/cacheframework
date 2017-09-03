package io.github.qianlixy.framework.cache.context;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.framework.cache.utils.UniqueMethodMarkUtil;

public class CacheMethodContext extends ContextDecorator {
	
	public CacheMethodContext(ThreadLocalContext context) {
		super(context);
	}
	
	public final void registerCacheMethod(ProceedingJoinPoint joinPoint) {
		setAttribute(THREAD_LOCAL_KEY_METHOD_STATIC_UNIQUE_MARK, String.valueOf(joinPoint.getSignature().toLongString().hashCode()));
		setAttribute(THREAD_LOCAL_KEY_METHOD_DYNAMIC_UNIQUE_MARK, String.valueOf(UniqueMethodMarkUtil.uniqueMark(joinPoint).hashCode()));
		setAttribute(THREAD_LOCAL_KEY_TARGET, joinPoint.getTarget().getClass());
		setAttribute(THREAD_LOCAL_KEY_METHOD, joinPoint.getSignature().getName());
		setAttribute(THREAD_LOCAL_KEY_PARAMS, joinPoint.getArgs());
	}
	
	public Class<?> getTargetClass() {
		return getAutoAttribute(THREAD_LOCAL_KEY_TARGET);
	}
	
	public String getMethodName() {
		return getAutoAttribute(THREAD_LOCAL_KEY_METHOD);
	}
	
	public String getArgs() {
		return getAutoAttribute(THREAD_LOCAL_KEY_PARAMS);
	}
	
	public String getCacheMethodDynamicUniqueMark() {
		return getAutoAttribute(THREAD_LOCAL_KEY_METHOD_DYNAMIC_UNIQUE_MARK).toString();
	}
	
	public String getCacheMethodStaticUniqueMark() {
		return getAutoAttribute(THREAD_LOCAL_KEY_METHOD_STATIC_UNIQUE_MARK).toString();
	}

}
