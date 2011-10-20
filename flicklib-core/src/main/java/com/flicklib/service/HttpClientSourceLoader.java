package com.flicklib.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Map.Entry;

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
import com.google.inject.name.Named;

public class HttpClientSourceLoader implements SourceLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientSourceLoader.class);

	private final HttpClient client;

	@Inject
	public HttpClientSourceLoader(@Named(value = FlicklibModule.HTTP_TIMEOUT) final Integer timeout) {
		// http://hc.apache.org/httpclient-3.x/performance.html#Concurrent_execution_of_HTTP_methods
		client = new HttpClient(new MultiThreadedHttpConnectionManager());

		if (timeout != null) {
			// wait max x sec
			client.getParams().setSoTimeout(timeout);
			//manager.getParams().setSoTimeout(timeout);
			// LOGGER.info("Timeout = "+client.getParams().getSoTimeout());
		}
	}

	@Override
	public Source loadSource(String url) throws IOException {
		GetMethod httpMethod = null;
		Reader is = null;
		try {
			LOGGER.info("Loading " + url);
			httpMethod = new GetMethod(url);
			//httpMethod.addRequestHeader("Content-Type","text/html; charset=UTF-8");
			try {
				client.executeMethod(httpMethod);
			} catch (RedirectException ex) {
				throw new IOException("Redirect problem: " + ex.getMessage(), ex);
			}
			LOGGER.debug("Finished loading at " + httpMethod.getURI().toString());
			String responseCharset = httpMethod.getResponseCharSet();
			LOGGER.info("Response charset = " + responseCharset);
			if (responseCharset != null) {
				is = new InputStreamReader(httpMethod.getResponseBodyAsStream(), responseCharset);
			} else {
				// default as described in: http://hc.apache.org/httpclient-3.x/charencodings.html
				is = new InputStreamReader(httpMethod.getResponseBodyAsStream(), "ISO-8859-1");
			}
			String contentType = httpMethod.getResponseHeader("Content-Type").getValue();
			// String contentType = URLConnection.guessContentTypeFromName(url)
			return new Source(httpMethod.getURI().toString(), IOTools.readerToString(is), contentType);
		} finally {
			IOTools.close(is);
			if (httpMethod != null) {
				httpMethod.releaseConnection();
			}
		}
	}

	@Override
	public Source loadSource(String url, boolean useCache) throws IOException {
		return loadSource(url);
	}

	@Override
	public Source post(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException {
		PostMethod httpMethod = null;
		Reader is = null;
		try {
			LOGGER.info("Loading " + url);
			httpMethod = new PostMethod(url);
			if (parameters != null) {
				for (Entry<String, String> entry : parameters.entrySet()) {
					httpMethod.addParameter(entry.getKey(), entry.getValue());
				}
			}
			if (headers != null) {
				for (Entry<String, String> entry : headers.entrySet()) {
					httpMethod.addRequestHeader(entry.getKey(), entry.getValue());
				}
			}
			try {
				client.executeMethod(httpMethod);
			} catch (RedirectException ex) {
				throw new IOException("Redirect problem: " + ex.getMessage(), ex);
			}
			LOGGER.info("Finished loading at " + httpMethod.getURI().toString());
			String responseCharset = httpMethod.getResponseCharSet();
			LOGGER.info("Response charset = " + responseCharset);
			if (responseCharset != null) {
				is = new InputStreamReader(httpMethod.getResponseBodyAsStream(), responseCharset);
			} else {
				// default as described in: http://hc.apache.org/httpclient-3.x/charencodings.html
				is = new InputStreamReader(httpMethod.getResponseBodyAsStream(), "ISO-8859-1");
			}
			String contentType = httpMethod.getResponseHeader("Content-Type").getValue();
			// String contentType = URLConnection.guessContentTypeFromName(url)
			return new Source(httpMethod.getURI().toString(), IOTools.readerToString(is), contentType);
		} finally {
			IOTools.close(is);
			if (httpMethod != null) {
				httpMethod.releaseConnection();
			}
		}

	}

}
