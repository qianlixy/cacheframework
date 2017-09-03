package io.github.qianlixy.smartcache.impl;

import java.io.IOException;

import io.github.qianlixy.smartcache.CacheClient;
import io.github.qianlixy.smartcache.CollectionCacheClient;

public abstract class AbstractCacheClientFactory<T> {
	
	protected T client;

	public void setClient(T client) {
		this.client = client;
	}
	
	public abstract CacheClient buildCacheClient() throws IOException;
	
	public abstract CollectionCacheClient buildCollectionCacheClient() throws IOException;
	
}
