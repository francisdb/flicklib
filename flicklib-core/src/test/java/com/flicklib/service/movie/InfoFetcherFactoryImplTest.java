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
package com.flicklib.service.movie;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.flicklib.api.InfoFetcherFactory;
import com.flicklib.api.MovieInfoFetcher;
import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.domain.MovieService;

/**
 * @author francisdb
 *
 */
public class InfoFetcherFactoryImplTest {
	
	private InfoFetcherFactory factory;
	
	@Before
	public void setupFactory(){
		MovieInfoFetcher fetcher = new MockInfoFetcher();
		factory = new InfoFetcherFactoryImpl(
				fetcher, fetcher, fetcher, fetcher, fetcher, fetcher, fetcher, fetcher, fetcher, fetcher, fetcher);
	}
	
	@Test
	public void testAllServices(){
		MovieInfoFetcher fetcher;
		for (MovieService service:MovieService.values()){
			fetcher = factory.get(service);
			Assert.assertNotNull("fetcher for " + service + " is null", fetcher);
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void testNull(){
		factory.get(null);
	}
	
	private class MockInfoFetcher implements MovieInfoFetcher {

		@Override
		public MoviePage fetch(String title) throws IOException {
			return null;
		}

		@Override
		public MoviePage getMovieInfo(String idForSite) throws IOException {
			return null;
		}

		@Override
		public List<? extends MovieSearchResult> search(String title) throws IOException {
			return null;
		}

		@Override
		public List<? extends MovieSearchResult> search(String title, String year) throws IOException {
			return null;
		}
	}

}
