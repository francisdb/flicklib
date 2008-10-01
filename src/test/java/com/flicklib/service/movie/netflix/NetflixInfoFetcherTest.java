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
package com.flicklib.service.movie.netflix;

import static org.junit.Assert.*;

import org.junit.Test;

import com.flicklib.domain.Movie;
import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieService;

/**
 * @author francisdb
 *
 */
public class NetflixInfoFetcherTest {

	/**
	 * Test method for {@link com.flicklib.service.movie.netflix.NetflixInfoFetcher#fetch(com.flicklib.domain.Movie, java.lang.String)}.
	 */
	@Test
	public void testFetch() {
		Movie movie = new Movie();
        movie.setTitle("Pulp Fiction");
		NetflixInfoFetcher fetcher = new NetflixInfoFetcher();
		MoviePage site = fetcher.fetch(movie, null);
        assertNotNull("Score is null", site.getScore());
        assertNotNull("ID is null", site.getIdForSite());
        assertNotNull("ImgUrl is null", site.getImgUrl());
        assertNotNull("Url is null", site.getUrl());
        assertEquals(MovieService.NETFLIX, site.getService());
        System.out.println(site.getScore());
        System.out.println(site.getUrl());
	}

}
