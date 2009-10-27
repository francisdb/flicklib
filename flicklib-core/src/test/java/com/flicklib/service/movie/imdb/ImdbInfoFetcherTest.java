package com.flicklib.service.movie.imdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.domain.MovieService;
import com.flicklib.domain.MovieType;
import com.flicklib.service.HttpSourceLoader;
import com.flicklib.service.SourceLoader;
import com.flicklib.service.UrlConnectionResolver;
import com.flicklib.service.cache.EmptyHttpCache;


public class ImdbInfoFetcherTest {
	 private SourceLoader loader;
	    private ImdbInfoFetcher fetcher;

	    
	    @Before
	    public void setUp() throws Exception {
	        loader = new HttpSourceLoader(new EmptyHttpCache(new UrlConnectionResolver(5000)));
	        fetcher = new ImdbInfoFetcher(loader);
	    }
	    


		@Test
		public void testSearchString() throws IOException {
			List<? extends MovieSearchResult> res = fetcher.search("Twin Peaks");
			for(MovieSearchResult result:res){
				assertEquals(MovieService.IMDB, result.getService());
				System.out.println(result.getTitle()+" / "+result.getOriginalTitle()+" / "+result.getYear()+" / "+result.getType());
			}
			assertEquals("Twin Peaks", res.get(0).getTitle());
			//assertEquals("Twin Peaks", res.get(0).getOriginalTitle());
			assertEquals(Integer.valueOf(1990), res.get(0).getYear());

			assertEquals(MovieType.TV_SERIES, res.get(0).getType());
			assertEquals(MovieType.MOVIE, res.get(1).getType());
			
			
			List<? extends MovieSearchResult> res2 = fetcher.search("mar adentro");
			for(MovieSearchResult result:res2){
				assertEquals(MovieService.IMDB, result.getService());
				assertNotNull(result.getIdForSite());
				System.out.println(result.getTitle()+" / "+result.getOriginalTitle()+" / "+result.getYear()+" / "+result.getType());
			}
			
		}
		

		@Test
		public void testGetMovieInfo() throws IOException {
			MoviePage page = fetcher.getMovieInfo("0133093");
			assertEquals(MovieService.IMDB, page.getService());
			assertEquals("The Matrix", page.getTitle());
			assertEquals(2, page.getDirectors().size());
			assertTrue(page.getDirectors().contains("Andy Wachowski"));
			assertTrue(page.getDirectors().contains("Larry Wachowski"));
			
			assertNotNull(page.getScore());
			assertNotNull(page.getPlot());
			assertTrue(page.getGenres().contains("Action"));
			assertTrue(page.getGenres().contains("Adventure"));
			assertTrue(page.getGenres().contains("Sci-Fi"));
			assertNotNull(page.getIdForSite());
			
		}
}
