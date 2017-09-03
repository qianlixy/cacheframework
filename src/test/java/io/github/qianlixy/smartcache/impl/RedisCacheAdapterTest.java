package io.github.qianlixy.smartcache.impl;

import java.util.Date;

import org.junit.Test;

import io.github.qianlixy.smartcache.exception.ConsistentTimeException;
import io.github.qianlixy.smartcache.impl.RedisCacheAdapter;
import redis.clients.jedis.Jedis;

public class RedisCacheAdapterTest {

	@Test
	public void testNewInstance() {
		Jedis jedis = new Jedis("192.168.206.100", 6379);
		RedisCacheAdapter adapter = new RedisCacheAdapter(jedis);
		try {
			System.out.println(adapter.consistentTime());
			System.out.println(adapter.consistentTime());
			System.out.println(new Date().getTime());
		} catch (ConsistentTimeException e) {
			e.printStackTrace();
		}
	}
	
}
