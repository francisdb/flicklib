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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import com.flicklib.tools.IOTools;

/**
 * @author zsombor
 * 
 */
public class SimpleHttpSourceLoader extends AbstractSourceLoader {

	private	final HttpCache cache;
	private boolean hideAgent = true;
	private Integer timeOut = null;

	/**
	 * 
	 */
	public SimpleHttpSourceLoader(HttpCache cache) {
		this.cache = cache;
	}
	
	public SimpleHttpSourceLoader() {
		this(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.flicklib.service.SourceLoader#loadSource(java.lang.String,
	 * boolean)
	 */
	@Override
	public Source loadSource(String url, boolean useCache) throws IOException {
		if (useCache && cache!=null) {
			Source source = cache.get(url);
			if (source != null) {
				return source;
			}
		}
		URL httpUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
		setupConnection(connection);
		Source source = processRequest(connection);
		if (useCache) {
			if (cache != null) {
				cache.put(url, source);
			}
		}
		return source;
	}

	private Source processRequest(HttpURLConnection connection) throws IOException,
			UnsupportedEncodingException {
		InputStream input = connection.getInputStream();
		String encoding = connection.getContentEncoding();
		String contentType = connection.getHeaderField("Content-Type");
		if (encoding == null) {
			if (contentType != null && contentType.indexOf("charset") != -1) {
				encoding = contentType.replaceAll(".*charset=(.*)", "$1");
			}
		}
		if (encoding == null) {
			// the old default ...
			encoding = "ISO-8859-1";
		}
		Reader reader = new InputStreamReader(input, encoding);
		
		Source source = new Source(connection.getURL().toString(), IOTools
				.readerToString(reader), contentType);
		reader.close();
		return source;
	}

	private void setupConnection(HttpURLConnection connection) {
		if (hideAgent) {
			connection
					.addRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.5) Gecko/2008120122 Firefox/3.0.5");
		}
		connection.setInstanceFollowRedirects(true);
		if (timeOut != null) {
			connection.setReadTimeout(timeOut);
		}
	}

	@Override
	public Source post(String url, Map<String, String> parameters,
			Map<String, String> headers) throws IOException {
		URL httpUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
		setupConnection(connection);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		for (Entry<String, String> entry : headers.entrySet()) {
			connection.setRequestProperty(entry.getKey(), entry.getValue());
		}

		StringBuilder buf = new StringBuilder();
		boolean first = true;
		for (Entry<String, String> entry : parameters.entrySet()) {
			if (first) {
				first = false;
			} else {
				buf.append('&');
			}
			buf.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append('=')
					.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}
		OutputStream outputStream = connection.getOutputStream();
		outputStream.write(buf.toString().getBytes("UTF-8"));
		outputStream.close();
		
		return processRequest(connection);
	}

	static {
		CookieManager manager = new CookieManager(null, new CookiePolicy() {
			@Override
			public boolean shouldAccept(URI uri, HttpCookie cookie) {
				if (cookie.getMaxAge() == 0) {
					cookie.setMaxAge(-1);
				}
				if (cookie.getDomain() == null) {
					cookie.setDomain(uri.getHost());
				}
				return true;
			}
		});
		CookieHandler.setDefault(manager);
	}

}
