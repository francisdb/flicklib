/**
 * 
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
