package com.flicklib.service;

import com.flicklib.service.cache.HttpCacheSourceLoader;

public abstract class TestUtil {
	public static SourceLoader createLoader() {
		return new HttpCacheSourceLoader(new UrlConnectionResolver(10000));
	}
}
