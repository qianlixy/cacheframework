package io.github.qianlixy.cache.filter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CacheMethodMatcherTest {

	private CacheMethodMatcher matcher;
	
	@Before
	public void before() {
		int sourcePatternLimit = 5;
		List<String> sourcePatterns = new ArrayList<>();
//		sourcePatterns.add("io.github.qianlixy.controller.student");
//		sourcePatterns.add("io.github.qianlixy.controller.teacher:60");
		sourcePatterns.add("io\\.github\\.qianlixy\\.cache\\.filter\\.CacheMethodMatcherTest");
		List<String> methodPatterns = new ArrayList<>();
		methodPatterns.add("io.github.qianlixy.dao.student");
		methodPatterns.add("io.github.qianlixy.dao.teacher:60");
		methodPatterns.add("io.github.qianlixy.dao.user");
		matcher = new CacheMethodMatcher(methodPatterns, sourcePatterns, sourcePatternLimit);
	}
	
	@Test
	public void testMatch() {
		assertEquals(matcher.match("public int io.github.qianlixy.dao.user.findAll()"), 0);
	}
	
}
