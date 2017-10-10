package io.github.qianlixy.cache.wrapper;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * <p>源方法包装类的默认实现</p>
 * <p>每拦截到源方法需要生成一个新的实例，否则存在线程安全问题</p>
 * @author qianli_xy@163.com
 * @since 1.7
 */
public class DefaultSourceProcesser implements SourceProcesser {
	
	private ThreadLocal<Object> executResult = new ThreadLocal<>();

	private ProceedingJoinPoint joinPoint;
	
	public DefaultSourceProcesser(ProceedingJoinPoint joinPoint) {
		this.joinPoint = joinPoint;
	}

	@Override
	public Class<?> getTargetClass() {
		return joinPoint.getTarget().getClass();
	}

	@Override
	public String getMethodName() {
		return joinPoint.getSignature().getName();
	}

	@Override
	public Object[] getArgs() {
		return joinPoint.getArgs();
	}

	@Override
	public Object doProcess() throws Throwable {
		if(null == executResult.get()) {
			LOGGER.debug("Start execute source method [{}]", getFullMethodName());
			executResult.set(joinPoint.proceed());
		}
		return executResult.get();
	}

	@Override
	public String getFullMethodName() {
		return joinPoint.getSignature().toLongString();
	}

	@Override
	public String toString() {
		return getFullMethodName();
	}
	
}
