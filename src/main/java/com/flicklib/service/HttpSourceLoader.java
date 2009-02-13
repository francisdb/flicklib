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
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.RedirectException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flicklib.module.FlicklibModule;
import com.flicklib.tools.IOTools;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Loads a http request
 * 
 * @author francisdb
 */
@Singleton
public class HttpSourceLoader implements SourceLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpSourceLoader.class);

    private final HttpClient client;
    
    private final HttpCache cache;

    @Inject
    public HttpSourceLoader(
    		@Named(value = FlicklibModule.HTTP_TIMEOUT) final Integer timeout, 
    		@Named(value = FlicklibModule.HTTP_CACHE) final Boolean useCache) {
    	// http://hc.apache.org/httpclient-3.x/performance.html#Concurrent_execution_of_HTTP_methods
        client = new HttpClient(new MultiThreadedHttpConnectionManager());
        if (timeout != null) {
            // wait max x sec
            client.getParams().setSoTimeout(timeout);
            //manager.getParams().setSoTimeout(timeout);
            // LOGGER.info("Timeout = "+client.getParams().getSoTimeout());
        }
        if(useCache){
        	cache = new HttpCache();
        }else{
        	cache = null;
        }
    }
   
    
    @Override
    public Source loadSource(String url) throws IOException {
        Source source = null;
        if(cache != null){
        	source = cache.get(url);
        }
        if(source == null){
        	source = load(url);
        	if(cache != null){
        		cache.put(url, source);
        	}
        }
        return source;
    }
    
    private Source load(String url) throws IOException {
        GetMethod httpMethod = null;
        Reader is = null;
        try {
            LOGGER.info("Loading " + url);
            httpMethod = new GetMethod(url);   
            try{
            	client.executeMethod(httpMethod);
            }catch(RedirectException ex){
            	throw new IOException("Redirect problem: "+ex.getMessage(), ex);
            }
            LOGGER.debug("Finished loading at " + httpMethod.getURI().toString());
            String responseCharset = httpMethod.getResponseCharSet();
            if (responseCharset != null) {
                is = new InputStreamReader(httpMethod.getResponseBodyAsStream(), responseCharset);
            } else {
                is = new InputStreamReader(httpMethod.getResponseBodyAsStream());
            }
            String contentType = httpMethod.getResponseHeader("Content-Type").getValue();
            // String contentType = URLConnection.guessContentTypeFromName(url)
            return new Source(httpMethod.getURI().toString(), IOTools.readerToString(is), contentType);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    LOGGER.error("Could not close InputStream", is);
                }
            }
            if (httpMethod != null) {
                httpMethod.releaseConnection();
            }
        }
    }
    
    public Source post(String url, Map<String,String> parameters, Map<String,String> headers) throws IOException {
        PostMethod httpMethod = null;
        Reader is = null;
        try {
            LOGGER.info("Loading " + url);
            httpMethod = new PostMethod(url);
            if (parameters!=null) {
                for (String key : parameters.keySet()) {
                    httpMethod.addParameter(key, parameters.get(key));
                }
            }
            if (headers!=null) {
                for (String key : headers.keySet()) {
                    httpMethod.addRequestHeader(key, headers.get(key));
                }
            }            
            try{
                client.executeMethod(httpMethod);
            }catch(RedirectException ex){
                throw new IOException("Redirect problem: "+ex.getMessage(), ex);
            }
            LOGGER.info("Finished loading at " + httpMethod.getURI().toString());
            String responseCharset = httpMethod.getResponseCharSet();
            if (responseCharset != null) {
                is = new InputStreamReader(httpMethod.getResponseBodyAsStream(), responseCharset);
            } else {
                is = new InputStreamReader(httpMethod.getResponseBodyAsStream());
            }
            String contentType = httpMethod.getResponseHeader("Content-Type").getValue();
            // String contentType = URLConnection.guessContentTypeFromName(url)
            return new Source(httpMethod.getURI().toString(), IOTools.readerToString(is), contentType);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    LOGGER.error("Could not close InputStream", is);
                }
            }
            if (httpMethod != null) {
                httpMethod.releaseConnection();
            }
        }
        
    }
    

}
