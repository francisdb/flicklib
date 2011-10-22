package com.flicklib.service;

import com.flicklib.service.cache.MemoryCacheSourceLoader;
import com.flicklib.service.cache.PersistentCacheSourceLoader;

public abstract class TestUtil {

	public static SourceLoader wrapCache(SourceLoader realLoader) {
		String rootPath = "target/cache";
		return new MemoryCacheSourceLoader(new PersistentCacheSourceLoader(realLoader, rootPath, "yyyy-MM-dd"));
		
	}
	
	public static SourceLoader createLoader() {
		return wrapCache(new UrlConnectionResolver(10000));
	}
}
