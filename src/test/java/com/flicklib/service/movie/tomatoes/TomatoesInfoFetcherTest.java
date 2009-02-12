package com.flicklib.service.movie.tomatoes;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.domain.MovieService;
import com.flicklib.service.HttpSourceLoader;

public class TomatoesInfoFetcherTest {

	@Test
	public void testGetMovieInfo() throws IOException {
		TomatoesInfoFetcher fetcher = new TomatoesInfoFetcher(
				new TomatoesParser(), new HttpSourceLoader(10000, false));
		MoviePage page = fetcher.getMovieInfo("pulp_fiction");
		assertEquals(MovieService.TOMATOES, page.getService());
		System.out.println("score = "+page.getScore());
	}

	@Test
	public void testSearch() throws IOException {
		TomatoesInfoFetcher fetcher = new TomatoesInfoFetcher(
				new TomatoesParser(), new HttpSourceLoader(10000, false));
		List<MovieSearchResult> results = fetcher.search("Pulp Fiction");
		assertEquals(10, results.size());
		assertEquals("Pulp Fiction", results.get(0).getTitle());
		assertEquals(Integer.valueOf(1994), results.get(0).getYear());
	}


}
