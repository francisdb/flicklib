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
package com.flicklib.service.movie.cinebel;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.service.HttpSourceLoader;
import com.flicklib.service.SourceLoader;
import com.flicklib.service.cache.HttpCache4J;

/**
 * @author francisdb
 *
 */
public class CinebelFetetcherTest {
	
	private CinebelFetcher fetcher;
	
	@Before
	public void setup(){
		SourceLoader loader = new HttpSourceLoader(new HttpCache4J());
		fetcher = new CinebelFetcher(loader, new CinebelParser());
	}

	@Test
	public void testSearchExact() throws IOException{
		List<? extends MovieSearchResult> results = fetcher.search("Forrest Gump");
		Assert.assertNotNull(results);
		Assert.assertTrue("No results returned", results.size() > 0);
		Assert.assertEquals("forrest gump", results.get(0).getTitle().toLowerCase());
		Assert.assertEquals("102", results.get(0).getIdForSite());
	}
	
	@Test
	public void testSearch() throws IOException{
		List<? extends MovieSearchResult> results = fetcher.search("Bond");
		Assert.assertNotNull(results);
		Assert.assertTrue("No results returned", results.size() > 0);
	}
	
	@Test
	public void testGetMovieInfo() throws IOException{
		MoviePage page = fetcher.getMovieInfo("2920");
		Assert.assertNotNull(page);
		Assert.assertEquals("The Matrix", page.getTitle());
		Assert.assertNotNull(page.getDescription());
		Assert.assertNotNull(page.getPlot());
		Assert.assertNotNull(page.getImgUrl());
		Assert.assertNotNull(page.getScore());
	}
	
}
