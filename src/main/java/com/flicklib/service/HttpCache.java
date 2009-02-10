package com.flicklib.service;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.google.inject.Singleton;

@Singleton
public class HttpCache {

	private static final Logger logger = LoggerFactory.getLogger(HttpCache.class);
	
	private final CacheManager manager;
	private final Cache cache;
	
	public HttpCache() {
		URL url = getClass().getResource("/ehcache-flicklib.xml");
		manager = new CacheManager(url);
		manager.addCache("httpCache");
		cache = manager.getCache("httpCache");
		logger.debug("Started cache, " + cache.getSize()+" pages cached.");
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    @Override
			public void run() { 
		    	manager.shutdown();
		    	logger.debug("shut down cache.");
		    }
		});
	}
	
	public Source get(String url){
		Source source = null;
		Element element = cache.get(url);
		if(element != null){
			source = (Source) element.getObjectValue();
		}
		return source;
	}
	
	public void put(String url, Source page){
		logger.debug("Caching result for "+url+" ("+page.getContentType()+")");
		cache.put(new Element(url, page));
	}

}
