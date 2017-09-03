package io.github.qianlixy.cache.impl;

import java.io.IOException;

import io.github.qianlixy.cache.CacheClient;
import io.github.qianlixy.cache.CollectionCacheClient;

public abstract class AbstractCacheClientFactory<T> {
	
	protected T client;

	public void setClient(T client) {
		this.client = client;
	}
	
	public abstract CacheClient buildCacheClient() throws IOException;
	
	public abstract CollectionCacheClient buildCollectionCacheClient() throws IOException;
	
}
