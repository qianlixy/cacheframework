package com.qianlixy.framework.cache.impl;

import java.io.IOException;

import com.qianlixy.framework.cache.CacheClient;
import com.qianlixy.framework.cache.CollectionCacheClient;

public abstract class AbstractCacheFactory<T> {
	
	protected T client;

	public void setClient(T client) {
		this.client = client;
	}
	
	public abstract CacheClient buildCacheClient() throws IOException;
	
	public abstract CollectionCacheClient buildCollectionCacheClient() throws IOException;
	
}
