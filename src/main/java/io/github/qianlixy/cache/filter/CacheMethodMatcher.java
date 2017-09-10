package io.github.qianlixy.cache.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 缓存方法的匹配器
 * @author qianli_xy@163.com
 * @since 1.0
 */
public class CacheMethodMatcher {
		
		private Pattern methodPattern;
		private Map<Integer, Integer> methodCacheTime = new HashMap<>();
		private Pattern sourceMethodPattern;
		private Map<Integer, Integer> sourceMethodCacheTime = new HashMap<>();
		private int sourcePatternLimit = 5;
		
		public CacheMethodMatcher(List<String> methodPatterns) {
			methodPattern = register(methodPatterns, methodCacheTime);
		}
		
		public CacheMethodMatcher(List<String> methodPatterns, 
				List<String> sourcePatterns, int sourcePatternLimit) {
			this(methodPatterns);
			sourceMethodPattern = register(sourcePatterns, sourceMethodCacheTime);
			this.sourcePatternLimit = sourcePatternLimit;
		}
		
		private Pattern register(List<String> patterns, Map<Integer, Integer> times) {
			List<String> methods = new ArrayList<>();
			for (int i = 0; i < patterns.size(); i++) {
				String pattern = patterns.get(i);
				if(pattern.contains(":")) {
					String[] pat = pattern.split(":");
					methods.add(pat[0]);
					times.put(i+1, Integer.parseInt(pat[1]));
					continue;
				}
				methods.add(pattern);
			}
			if(methods.size() == 1) return Pattern.compile(methods.get(0));
			return Pattern.compile("(" + String.join(")|(", methods) + ")");
		}
		
		public int match(String methodFullName) {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			List<String> sources = new ArrayList<>();
			for (int i = 0; i < sourcePatternLimit; i++) {
				StackTraceElement stackTraceElement = stackTrace[i];
				sources.add(stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + "()");
			}
			String sourceStr = String.join(",", sources);
			Matcher matcher = sourceMethodPattern.matcher(sourceStr);
			String group0 = matcher.group(0);
			String group1 = matcher.group(1);
			String group2 = matcher.group(2);
			String group3 = matcher.group(3);
			String group4 = matcher.group(4);
			
			return 0;
		}
	}