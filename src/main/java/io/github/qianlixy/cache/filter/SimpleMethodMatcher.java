package io.github.qianlixy.cache.filter;

import java.util.List;
import java.util.regex.Pattern;

public class SimpleMethodMatcher implements MethodMatcher {
	
	private Pattern pattern;

	@Override
	public void register(List<String> regulations) {
		if(null == regulations || regulations.size() == 0)
			throw new NullPointerException("Param regulations is null");
		if(regulations.size() == 1) pattern = Pattern.compile(regulations.get(0));
		String patternStr = String.join("|", regulations);
		pattern = Pattern.compile(patternStr);
	}

	@Override
	public boolean doMatch(Class<?> clazz, String methodName) {
		String method = clazz.getName() + "." + methodName + "()";
		return pattern.matcher(method).find();
	}

}
