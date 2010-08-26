package com.flicklib.service.movie.tomatoes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.domain.MovieService;
import com.flicklib.service.movie.AlternateLiveTester;

public class TomatoesInfoFetcherTest extends AlternateLiveTester {
    TomatoesInfoFetcher fetcher;

    public TomatoesInfoFetcherTest(boolean flag, boolean internalRedirects) {
        super(flag, internalRedirects);
        fetcher = new TomatoesInfoFetcher(new TomatoesParser(), loader);
    }
	
	@Test
	public void testGetMovieInfo() throws IOException {
		MoviePage page = fetcher.getMovieInfo("pulp_fiction");
		assertEquals(MovieService.TOMATOES, page.getService());
		assertTrue("starts with 'Prizefighter Butch Coolidge has decided to stop payment on a deal he's made with the devil'", 
				page.getPlot().startsWith("Prizefighter Butch Coolidge has decided to stop payment on a deal he's made with the devi"));
		System.out.println("score = "+page.getScore());
	}

	@Test
	public void testSearch() throws IOException {
		List<MovieSearchResult> results = fetcher.search("Pulp Fiction");
		assertEquals(5, results.size());
		assertEquals("Pulp Fiction", results.get(0).getTitle());
		assertEquals(Integer.valueOf(1994), results.get(0).getYear());
	}


}
