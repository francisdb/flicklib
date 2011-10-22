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
package com.flicklib.service.movie.apple;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flicklib.api.TrailerFinder;
import com.flicklib.service.Source;
import com.flicklib.service.SourceLoader;
import com.flicklib.tools.Param;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * TODO get rid of the restlet dependency
 * @author francisdb
 */
@Singleton
public class AppleTrailerFinder implements TrailerFinder {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppleTrailerFinder.class);

	final SourceLoader loader;

	@Inject
	public AppleTrailerFinder(SourceLoader loader) {
		this.loader = loader;
	}

	@Override
	public String findTrailerUrl(String title, String localId) {
		JSONObject obj;
		String url = null;
		try {
			obj = getJSON(title);
			JSONArray array = obj.getJSONObject("responseData").getJSONArray("results");
			for (int i = 0; i< array.length();i++) {
				JSONObject o = array.getJSONObject(i);
				LOGGER.info("results:"+o);
				String unescapedUrl = o.optString("unescapedUrl", "");
				if (unescapedUrl.startsWith("http://trailers.apple.com")) {
					return unescapedUrl;
				}
			}
			
			
		} catch (IOException e) {
			LOGGER.info("error :"+e.getMessage(), e);
		} catch (JSONException e) {
			LOGGER.info("error :"+e.getMessage(), e);
		}
		
		return url;
	}

	protected JSONObject getJSON(String title) throws IOException, JSONException {
		//String url = "http://trailers.apple.com/trailers/home/scripts/quickfind.php?"+Param.paramString("q", title);
		String url = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q="+Param.encode(title+ " site:apple.com");
		Source src = loader.loadSource(url, true);
		JSONObject obj = new JSONObject(src.getContent());
		return obj;
	}

}
