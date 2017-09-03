package io.github.qianlixy.framework.cache.impl;

import java.io.IOException;

import io.github.qianlixy.framework.cache.CacheClient;
import io.github.qianlixy.framework.cache.CollectionCacheClient;

public abstract class AbstractCacheClientFactory<T> {
	
	protected T client;

	public void setClient(T client) {
		this.client = client;
	}
	
	public abstract CacheClient buildCacheClient() throws IOException;
	
	public abstract CollectionCacheClient buildCollectionCacheClient() throws IOException;
	
}
