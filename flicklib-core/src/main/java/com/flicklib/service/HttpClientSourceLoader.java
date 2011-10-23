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
package com.flicklib.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flicklib.module.FlicklibModule;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class HttpClientSourceLoader implements SourceLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientSourceLoader.class);

	private final DefaultHttpClient client;
	private final BasicHttpContext context;

	@Inject
	public HttpClientSourceLoader(@Named(value = FlicklibModule.HTTP_TIMEOUT) final Integer timeout) {

	    ThreadSafeClientConnManager tm = new ThreadSafeClientConnManager();
	        client = new DefaultHttpClient(tm); 

	        context = new BasicHttpContext();
	        
		if (timeout != null) {
			// wait max x sec
		    HttpConnectionParams.setSoTimeout(client.getParams(), timeout);
		}
	}

	@Override
	public Source loadSource(String url) throws IOException {
		HttpResponse response = null;
		try {
			LOGGER.info("Loading " + url);
			HttpGet httpMethod = new HttpGet(url);
			//httpMethod.addRequestHeader("Content-Type","text/html; charset=UTF-8");
			response = client.execute(httpMethod, context);
			
			return buildSource(url, response, httpMethod);
		} finally {
			EntityUtils.consume(response.getEntity());
		}
	}

	@Override
	public Source loadSource(String url, boolean useCache) throws IOException {
		return loadSource(url);
	}

	@Override
	public Source post(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException {
                HttpResponse response = null;
		try {
			LOGGER.info("Loading " + url);
			HttpPost httpMethod = new HttpPost(url);
			if (parameters != null && !parameters.isEmpty()) {
			    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				for (Entry<String, String> entry : parameters.entrySet()) {
					formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				UrlEncodedFormEntity form = new UrlEncodedFormEntity(formparams, "UTF-8");
				httpMethod.setEntity(form);
			}
			if (headers != null && !headers.isEmpty()) {
				for (Entry<String, String> entry : headers.entrySet()) {
					httpMethod.addHeader(entry.getKey(), entry.getValue());
				}
			}
			response = client.execute(httpMethod, context);
			return buildSource(url, response, httpMethod);
		} finally {
		    EntityUtils.consume(response.getEntity());
		}

	}

    private Source buildSource(String url, HttpResponse response, HttpRequestBase httpMethod) throws IOException {
        LOGGER.info("Finished loading at " + httpMethod.getURI().toString());
        final HttpEntity entity = response.getEntity();
        String responseCharset = EntityUtils.getContentCharSet(entity);
        String contentType = EntityUtils.getContentMimeType(entity);
        LOGGER.info("Response charset = " + responseCharset);
        String content = EntityUtils.toString(entity);
        // String contentType = URLConnection.guessContentTypeFromName(url)
        return new Source(httpMethod.getURI().toString(), content, contentType, url);
    }

}
