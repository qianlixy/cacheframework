package io.github.qianlixy.cache.utils;

import org.aspectj.lang.ProceedingJoinPoint;

import io.github.qianlixy.cache.exception.NonImplToStringException;

public class UniqueMethodMarkUtil {
	
	private static final String NULL = "null";
	
	private static String paramValue(Object param) throws NonImplToStringException {
		if (null == param || null == param.toString()) {
			return NULL;
		} else if (param.toString().startsWith(param.getClass().getName())) {
			throw new NonImplToStringException();
		} else {
			return param.toString(); 
		}
	}
	
	public static final String uniqueMark(ProceedingJoinPoint pjp) throws NonImplToStringException {
		String methodName = pjp.getSignature().getName();
		Object[] params = pjp.getArgs();
		StringBuilder mark = new StringBuilder(pjp.getTarget().getClass().getName());
		mark.append(".").append(methodName).append("(");
		if (null != params) {
			for (Object val : params) {
				if (null == val || null == val.toString()) {
					mark.append(NULL).append(",");
				} else if (val.getClass().isArray()) {
					mark.append("[");
					int arrayLength = BaseDataTypeArrayUtil.arrayLength(val);
					for (int i=0; i<arrayLength; i++) {
						mark.append(BaseDataTypeArrayUtil.arrayValue(val, i)).append(",");
					}
					if (arrayLength > 0)
						mark.deleteCharAt(mark.length() - 1);
					mark.append("],");
				} else {
					try {
						mark.append(paramValue(val)).append(",");
					} catch (NonImplToStringException e) {
						throw new NonImplToStringException("Cannot generate unique mark because [" 
								+ val.getClass().getName() + "] not implement toString() method");
					}
				}
			}
			if (params.length > 0)
				mark.deleteCharAt(mark.length() - 1);
		}
		mark.append(")");
		return mark.toString();
	}
	
}
