package com.flicklib.service;

import java.io.IOException;
import java.util.Map;

/**
 * Responisble for handling http requests
 * @author francisdb
 *
 */
public interface ResponseResolver {
	
	/**
	 * Performs a http get
	 * @param url
	 * @return the source
	 * @throws IOException
	 */
	Source get(String url) throws IOException;
	
	/**
	 * Performs a http post
	 * @param url
	 * @param parameters
	 * @param headers
	 * @return
	 * @throws IOException
	 */
	Source post(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException;
}