package io.github.qianlixy.cache.impl;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import io.github.qianlixy.cache.impl.MemcachedAdapter;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;

public class MemcachedAdapterTest {

	@Test
	public void testNewOneInstance() throws Throwable {
		List<InetSocketAddress> addresses = new ArrayList<>();
		addresses.add(new InetSocketAddress("192.168.206.100", 11211));
		MemcachedClient client = new XMemcachedClientBuilder(addresses).build();
		new MemcachedAdapter(client);
	}
	
}
