package com.qianlixy.framework.cache.business.test;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.qianlixy.framework.cache.business.service.UserService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserSerivceTest extends BaseBusinessTest {

	@Autowired
	private UserService userService;
	
	@Test
	public void a_testSave() {
		int id = userService.save("paul", "000", "Paul");
		assertTrue(id >= 0);
	}
	
	@Test
	public void b_testFindAll() {
		assertTrue(userService.findAll().size() == 1);
		assertTrue(userService.findAll().size() == 1);
	}
	
	@Test
	public void c_testFind() {
		Map<String, Object> user = userService.find(1);
		assertTrue("paul".equals(user.get("USERNAME")));
		assertTrue("000".equals(user.get("PASSWORD")));
		assertTrue("Paul".equals(user.get("REALNAME")));
	}
	
	@Test
	public void d_testDelete() {
		userService.delete(1);
		assertTrue(userService.find(1) == null);
	}
}
