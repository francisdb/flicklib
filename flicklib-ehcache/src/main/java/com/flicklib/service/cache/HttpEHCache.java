/*
 * This file is part of Flicklib.
 *
 * Copyright (C) Francis De Brabandere
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flicklib.service.cache;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flicklib.service.HttpCache;
import com.flicklib.service.ResponseResolver;
import com.flicklib.service.Source;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class HttpEHCache implements HttpCache {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HttpEHCache.class);

	private final CacheManager manager;
	private final Cache cache;
	private final ResponseResolver resolver;

	@Inject
	public HttpEHCache(
			final ResponseResolver resolver) {
		this.resolver = resolver;
		URL url = getClass().getResource("/ehcache-flicklib.xml");
		manager = new CacheManager(url);
		manager.addCache("httpCache");
		cache = manager.getCache("httpCache");
		LOGGER.debug("Started cache, " + cache.getSize() + " pages cached.");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				LOGGER.debug(cache.getStatistics().toString());
				manager.shutdown();
				LOGGER.debug("shut down cache.");
			}
		});
	}

	@Override
	public Source get(String url, boolean forceRefresh) {
		Source source = null;
		Element element = null;
		if(!forceRefresh){
			element = cache.get(url);
		}
		if (element != null) {
			source = (Source) element.getObjectValue();
		}else{
			try {
				source = resolver.get(url);
				put(url, source);
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			
		}
		return source;
	}
	
	@Override
	public Source get(String url) {
		return get(url, false);
	}
	
	@Override
	public Source post(String url, Map<String, String> parameters, Map<String, String> headers) {
		// FIXME what about the parameters, they should be added to the cache
		Source source = null;
		Element element = cache.get(url);
		if (element != null) {
			source = (Source) element.getObjectValue();
		}else{
			try {
				source = resolver.post(url, parameters, headers);
				put(url, source);
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			
		}
		return source;
	}

	private void put(String url, Source page) {
		LOGGER.debug("Caching result for " + url + " (" + page.getContentType()
				+ ")");
		cache.put(new Element(url, page));
	}

}