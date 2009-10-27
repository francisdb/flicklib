package com.flicklib.service.cache;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.codehaus.httpcache4j.HTTPMethod;
import org.codehaus.httpcache4j.HTTPRequest;
import org.codehaus.httpcache4j.HTTPResponse;
import org.codehaus.httpcache4j.MIMEType;
import org.codehaus.httpcache4j.Parameter;
import org.codehaus.httpcache4j.cache.HTTPCache;
import org.codehaus.httpcache4j.cache.MemoryCacheStorage;
import org.codehaus.httpcache4j.client.HTTPClientResponseResolver;
import org.codehaus.httpcache4j.payload.ByteArrayPayload;
import org.codehaus.httpcache4j.payload.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flicklib.service.HttpCache;
import com.flicklib.service.Source;
import com.flicklib.tools.IOTools;

public class HttpCache4J implements HttpCache{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpCache4J.class);
	
	private HTTPCache cache;
	
	public HttpCache4J() {
		this.cache = new HTTPCache(new MemoryCacheStorage(), new HTTPClientResponseResolver(new HttpClient()));
	}

	/** {@inheritDoc} */
	@Override
	public Source get(String url, boolean forceRefresh) {
		HTTPRequest request = new HTTPRequest(URI.create(url));
		HTTPResponse response = cache.doCachedRequest(request, forceRefresh);
		String content = payloadToString(response);
		String respUrl = response.getHeaders().getFirstHeaderValue("Content-Location");
		System.err.println(response.getHeaders().toString());
		String theUrl = url;
		if(respUrl != null){
			System.err.println(respUrl);
			theUrl = respUrl;
		}
		Source source = new Source(theUrl, content);
		return source;
	}
	
	/** {@inheritDoc} */
	@Override
	public Source post(String url, Map<String, String> parameters, Map<String, String> headers) {
		HTTPRequest request = new HTTPRequest(URI.create(url), HTTPMethod.POST);
		for(Entry<String, String> header:headers.entrySet()) {
			request.addHeader(header.getKey(), header.getValue());
		}
		
		StringBuilder builder = new StringBuilder();
		// TODO fix when this is fixed: http://jira.codehaus.org/browse/HTCJ-51
		// or see in httpclient if we can reuse code
		for(Entry<String,String> param:parameters.entrySet()){
			if(builder.length() > 0){
				builder.append("&");
			}
			builder.append(URLEncoder.encode(param.getKey())).append("=").append(URLEncoder.encode(param.getValue()));
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(builder.toString().getBytes());
		
		MIMEType mimeType = new MIMEType("application/x-www-form-urlencoded");
		try {
			Payload payload = new ByteArrayPayload(bis, mimeType);
			request = request.payload(payload);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		HTTPResponse response = cache.doCachedRequest(request, false);
		String content = payloadToString(response);
		
		String respUrl = response.getHeaders().getFirstHeaderValue("Content-Location");
		String theUrl = url;
		if(respUrl != null){
			theUrl = respUrl;
		}
		Source source = new Source(theUrl, content);
		return source;
	}
	
	/** {@inheritDoc} */
	@Override
	public Source get(String url) {
		return get(url, false);
	}
	
	private String payloadToString(HTTPResponse response){
		String content = null;
		InputStream is = null;
		try{
			is = response.getPayload().getInputStream();
			 //httpMethod.addRequestHeader("Content-Type","text/html; charset=UTF-8");
			String contentType = response.getHeaders().getFirstHeaderValue("Content-Type");
			System.out.println(contentType);
			MIMEType mimeType = new MIMEType(contentType);
			// default as described in: http://hc.apache.org/httpclient-3.x/charencodings.html
			String encoding = "ISO-8859-1";
			if(contentType.contains("charset")){
				for(Parameter parameter:mimeType.getParameters()){
					if("charset".equals(parameter.getName())){
						encoding = parameter.getValue();
					}
				}
			}
			System.out.println(encoding);
			content = IOTools.inputSreamToString(is, encoding);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			IOTools.close(is);
		}
		return content;
	}

}