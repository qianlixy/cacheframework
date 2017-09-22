package io.github.qianlixy.cache.filter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import io.github.qianlixy.cache.business.dao.UserDaoImpl;

public class CacheMethodMatcherTest {

	private MethodMatcher matcher = new SimpleMethodMatcher();
	
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
		methodPatterns.add("io.*\\.dao");
		matcher.register(methodPatterns);
	}
	
	@Test
	public void testMatch() {
		assertEquals(matcher.doMatch(UserDaoImpl.class, "findAll"), true);
	}
	
}
