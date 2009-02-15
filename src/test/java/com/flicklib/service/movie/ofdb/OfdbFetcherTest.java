package com.flicklib.service.movie.ofdb;

import static org.junit.Assert.*;

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

public class OfdbFetcherTest {
    private SourceLoader loader;
    private OfdbFetcher fetcher;

    
    @Before
    public void setUp() throws Exception {
        loader = new HttpSourceLoader(60000, false);
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
			System.out.println(result.getTitle()+" / "+result.getOriginalTitle()+" / "+result.getYear()+" / "+result.getType());
		}
		
	}
	

	@Test
	public void testGetMovieInfo() throws IOException {
		MoviePage page = fetcher.getMovieInfo("1050,Pulp-Fiction");
		System.out.println(page.getTitle());
		System.out.println(page.getScore());
		System.out.println(page.getDescription());
		assertEquals(MovieService.OFDB, page.getService());
		assertEquals("Pulp Fiction", page.getTitle());
		assertNotNull(page.getScore());
		assertNotNull(page.getPlot());
		assertNotNull(page.getDescription());
		
		// this was causing problems before
		page = fetcher.getMovieInfo("3635,Dune---Der-Wüstenplanet");
		assertEquals(MovieService.OFDB, page.getService());
		// FIXME parse type and test it
		assertEquals("Dune - Der Wüstenplanet [TV-Mini-Serie]", page.getTitle());
		assertNotNull(page.getScore());
		assertNotNull(page.getPlot());
		assertNotNull(page.getDescription());
		
	}

}
