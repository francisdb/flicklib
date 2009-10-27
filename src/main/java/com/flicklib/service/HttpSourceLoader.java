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
package com.flicklib.service;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Loads a http request
 * 
 * @author francisdb
 */
@Singleton
public class HttpSourceLoader implements SourceLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpSourceLoader.class);

    private final HttpCache cache;

    @Inject
    public HttpSourceLoader(
    		final HttpCache httpCache) {
    	this.cache = httpCache;
    }
   
    @Override
    public Source loadSource(String url, boolean useCache) throws IOException {
        Source source = cache.get(url);
        return source;
    }
    
	/** {@inheritDoc} */
    @Override
    public Source loadSource(String url) throws IOException {
        return loadSource(url, true);
    }
    
    /** {@inheritDoc} */
    @Override
    public Source post(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException {
    	// TODO add params and headers
    	 Source source = cache.post(url, parameters, headers);
         return source;
    }
   

}
