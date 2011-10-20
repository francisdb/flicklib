package com.flicklib.service.cache;

import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;

import com.flicklib.service.Source;
import com.flicklib.service.SourceLoader;

/**
 * Simple, WeakHashMap based cache implementation.
 * 
 * @author zsombor
 *
 */
public class HttpCacheSourceLoader implements SourceLoader {
	private final SourceLoader resolver;
	private final WeakHashMap<String, Source> cache = new WeakHashMap<String, Source>();

	public HttpCacheSourceLoader(final SourceLoader resolver) {
		this.resolver = resolver;
	}

	@Override
	public Source loadSource(String url) throws IOException {
		return loadSource(url, true);
	}

	@Override
	public Source loadSource(String url, boolean useCache) throws IOException {
		Source source = null;
		if (useCache) {
			source = cache.get(url);
		}
		if (source == null) {
			source = resolver.loadSource(url, useCache);
			put(url, source);
		}
		return source;
	}

	private void put(String url, Source source) {
		cache.put(url, source);
	}

	@Override
	public Source post(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException {
		return resolver.post(url, parameters, headers);
	}

}
