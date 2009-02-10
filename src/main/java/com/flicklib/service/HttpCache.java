/*
 * This file is part of Flicklib.
 * 
 * Copyright (C) Francis De Brabandere
 * 
 * Movie Browser is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Movie Browser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpCache.class);
	
	private final CacheManager manager;
	private final Cache cache;
	
	public HttpCache() {
		URL url = getClass().getResource("/ehcache-flicklib.xml");
		manager = new CacheManager(url);
		manager.addCache("httpCache");
		cache = manager.getCache("httpCache");
		LOGGER.debug("Started cache, " + cache.getSize()+" pages cached.");
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    @Override
			public void run() { 
				LOGGER.debug(cache.getStatistics().toString());
		    	manager.shutdown();
		    	LOGGER.debug("shut down cache.");
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
		LOGGER.debug("Caching result for "+url+" ("+page.getContentType()+")");
		cache.put(new Element(url, page));
	}

}
