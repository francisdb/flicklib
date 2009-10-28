package com.flicklib.service.movie.blippr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.service.HttpClientResponseResolver;
import com.flicklib.service.HttpSourceLoader;
import com.flicklib.service.cache.EmptyHttpCache;

public class BlipprInfoFetcherTest {
	
	@Test
	public void testSearchString() throws Exception {
		BlipprInfoFetcher fetcher = new BlipprInfoFetcher(new HttpSourceLoader(new EmptyHttpCache(new HttpClientResponseResolver(5000))));
		List<? extends MovieSearchResult> list = fetcher.search("The Matrix");
		assertFalse("expecting results", list.isEmpty());
		assertEquals("The Matrix", list.get(0).getTitle());
	}

	@Test
	public void testGetMovieInfo() throws Exception {
		BlipprInfoFetcher fetcher = new BlipprInfoFetcher(new HttpSourceLoader(new EmptyHttpCache(new HttpClientResponseResolver(5000))));
		MoviePage page = fetcher.getMovieInfo("1167");
		assertNotNull("expecting results", page);
		assertEquals("Pulp Fiction", page.getTitle());
		assertNotNull(page.getScore());
	}
}
