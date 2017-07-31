package com.qianlixy.framework.cache.impl;

import java.io.IOException;

import com.qianlixy.framework.cache.CacheClient;
import com.qianlixy.framework.cache.CollectionCacheClient;

import net.rubyeye.xmemcached.MemcachedClient;

public class MemcachedCacheFactory extends AbstractCacheClientFactory<MemcachedClient> {

	@Override
	public CacheClient buildCacheClient() throws IOException {
		return new MemcachedAdapter(client);
	}

	@Override
	public CollectionCacheClient buildCollectionCacheClient() throws IOException {
		return new CollectionMemcachedAdapter(client);
	}

}
