/*
 * This file is part of Flicklib.
 *
 * Copyright (C) Zsombor Gegesy
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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flicklib.service.HttpCache;
import com.flicklib.service.ResponseResolver;
import com.flicklib.service.Source;
import com.google.inject.Inject;

/**
 * Mock implementation, just forwards to the resolver
 * 
 * @author zsombor
 *
 */
public class EmptyHttpCache implements HttpCache {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmptyHttpCache.class);

	private final ResponseResolver resolver;
	
	@Inject
	public EmptyHttpCache(
			final ResponseResolver resolver) {
		this.resolver = resolver;
	}
	
	/** {@inheritDoc} */
	@Override
	public Source get(final String url) {
		Source source = null;
		try {
			source = resolver.get(url);
		} catch (IOException e) {
			// FIXME throw an exception as the get failed!
			LOGGER.error(e.getMessage(), e);
		}
		return source;
	}
	
	@Override
	public Source get(String url, boolean forceRefresh) {
		return get(url);
	}
	
	
	@Override
	public Source post(String url, Map<String, String> parameters, Map<String, String> headers) {
		Source source = null;
		try {
			source = resolver.post(url, parameters, headers);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return source;
	}

}
