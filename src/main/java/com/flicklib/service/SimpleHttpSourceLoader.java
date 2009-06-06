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
import java.security.AccessControlException;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flicklib.tools.IOTools;

/**
 * @author zsombor
 * 
 */
public class SimpleHttpSourceLoader extends AbstractSourceLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHttpSourceLoader.class);
    
    private static final String ENCODING = "UTF-8";

    static {
        try {
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
            try {
                CookieHandler.setDefault(manager);
            } catch (AccessControlException ace) {
            	LOGGER.warn("cookie handler initialization failed!");
            }
        } catch (java.lang.NoClassDefFoundError ncde) {
        	LOGGER.warn("CookieManager is not accessible " + ncde.getMessage(), ncde);
        }
    }

    private final HttpCache cache;
    
    private boolean hideAgent = true;
    private Integer timeOut = null;
    private boolean internalRedirects = true;
    
    /**
	 * 
	 */
    public SimpleHttpSourceLoader(HttpCache cache) {
        this.cache = cache;
    }

    public SimpleHttpSourceLoader() {
        this(null);
    }
    
    public SimpleHttpSourceLoader(boolean internalRedirects) {
        this(null);
        setInternalRedirects(internalRedirects);
    }

    public void setInternalRedirects(boolean internalRedirects) {
        this.internalRedirects = internalRedirects;
    }
    
    /** {@inheritDoc} */
    @Override
    public Source loadSource(String url, boolean useCache) throws IOException {
    	Source result = null;
        if (useCache && cache != null) {
            Source source = cache.get(url);
            if (source != null) {
               result = source;
            }
        }
        if(result == null){
	        URL httpUrl = new URL(url);
	        result = load(httpUrl);
	        if (useCache) {
	            if (cache != null) {
	                cache.put(url, result);
	            }
	        }
        }
        return result;
    }

    private Source load(URL httpUrl) throws IOException, UnsupportedEncodingException {
        HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
        setupConnection(connection);
        return processRequest(connection);
    }

    private Source processRequest(HttpURLConnection connection) throws IOException, UnsupportedEncodingException {
        InputStream input = null;
        Source source = null;
        Reader reader = null;
        try {
            input = connection.getInputStream();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP  || responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
                String newLocation = connection.getHeaderField("Location");
                if(newLocation != null){
	                URL redirectUrl = new URL(connection.getURL(), newLocation);
	                LOGGER.info("redirect to "+redirectUrl.toString());
	                return load(redirectUrl);
                }
            } else {
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
	            reader = new InputStreamReader(input, encoding);
	            String content = IOTools.readerToString(reader);
	            source = new Source(connection.getURL().toString(), content, contentType);
	            LOGGER.info("request for " + connection.getURL().toString() + " processed, result content type : " + contentType + ", encoding :" + encoding
	                    + ", size:" + source.getContent().length());
            }
        } finally {
            IOTools.close(reader);
            IOTools.close(input);
        }
       
        return source;
    }

    private void setupConnection(final HttpURLConnection connection) {
        if (hideAgent) {
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.5) Gecko/2008120122 Firefox/3.0.5");
        }
        connection.setInstanceFollowRedirects(internalRedirects);
        if (timeOut != null) {
            connection.setReadTimeout(timeOut);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Source post(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException {
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
            buf.append(URLEncoder.encode(entry.getKey(), ENCODING)).append('=').append(URLEncoder.encode(entry.getValue(), ENCODING));
        }
        OutputStream outputStream = null;
        try{
        	outputStream = connection.getOutputStream();
            outputStream.write(buf.toString().getBytes(ENCODING));
        }finally{
        	IOTools.close(outputStream);
        }

        return processRequest(connection);
    }

}
