package com.flicklib.service.movie.ofdb;

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
import com.flicklib.service.cache.HttpCache4J;

public class OfdbFetcherTest {
    private SourceLoader loader;
    private OfdbFetcher fetcher;

    
    @Before
    public void setUp() throws Exception {
        loader = new HttpSourceLoader(new HttpCache4J());
        fetcher = new OfdbFetcher(loader, new OfdbParser());
    }
    


	@Test
	public void testSearchString() throws IOException {
		List<? extends MovieSearchResult> res = fetcher.search("Twin Peaks");
		for(MovieSearchResult result:res){
			assertEquals(MovieService.OFDB, result.getService());
			System.out.println(result.getTitle()+" / "+result.getOriginalTitle()+" / "+result.getYear()+" / "+result.getType());
		}
		assertEquals("Geheimnis von Twin Peaks, Das", res.get(0).getTitle());
		assertEquals("Twin Peaks", res.get(0).getOriginalTitle());
		assertEquals(Integer.valueOf(1990), res.get(0).getYear());
		assertEquals(MovieType.MOVIE, res.get(0).getType());
		
		assertEquals(MovieType.TV_SERIES, res.get(1).getType());
		
		List<? extends MovieSearchResult> res2 = fetcher.search("mar adentro");
		for(MovieSearchResult result:res2){
			assertEquals(MovieService.OFDB, result.getService());
			assertNotNull(result.getIdForSite());
			System.out.println(result.getTitle()+" / "+result.getOriginalTitle()+" / "+result.getYear()+" / "+result.getType());
		}
		
	}
	

	@Test
	public void testGetMovieInfo() throws IOException {
		MoviePage page = fetcher.getMovieInfo("1050,Pulp-Fiction");
		assertEquals(MovieService.OFDB, page.getService());
		assertEquals("Pulp Fiction", page.getTitle());
		assertNotNull(page.getScore());
		assertNotNull(page.getPlot());
		assertNotNull(page.getDescription());

		page = fetcher.getMovieInfo("3635,Dune---Der-Wüstenplanet");
		assertEquals(MovieService.OFDB, page.getService());
		assertEquals(MovieType.MINI_SERIES, page.getType());
		assertEquals("Dune - Der Wüstenplanet", page.getAlternateTitle());
		assertEquals("Dune", page.getTitle());
		assertEquals("Dune", page.getOriginalTitle());
		assertEquals("John Harrison", page.getDirector());
		assertEquals("http://img.ofdb.de/film/3/3635.jpg", page.getImgUrl());
		assertEquals(Integer.valueOf(2000), page.getYear());
		assertTrue(page.getGenres().contains("Abenteuer"));
		assertTrue(page.getGenres().contains("Mystery"));
		assertNotNull(page.getScore());
		assertNotNull(page.getPlot());
		assertNotNull(page.getDescription());
		assertNotNull(page.getIdForSite());
		
	}

}
