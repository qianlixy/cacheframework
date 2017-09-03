package io.github.qianlixy.cache.impl;

import java.io.IOException;

import io.github.qianlixy.cache.CacheClient;
import io.github.qianlixy.cache.CollectionCacheClient;
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
