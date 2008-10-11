package com.flicklib.service.movie.porthu;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.service.HttpSourceLoader;
import com.flicklib.service.SourceLoader;

/**
 * @author zsombor
 * 
 */
public class PorthuLiveFetcherTest {
    SourceLoader loader;
    PorthuFetcher fetcher;

    
    @Before
    public void setUp() throws Exception {
        loader = new HttpSourceLoader(60000);
        fetcher = new PorthuFetcher(loader);
    }
    
    @Test
    public void testPerfectTitleMatch() {
        try {
            List<MovieSearchResult> list = fetcher.search("Indiana Jones and the Kingdom of the Crystal Skull");
            Assert.assertEquals("search result size", 1, list.size());
            MovieSearchResult searchResult = list.get(0);
            Assert.assertNotNull("search result not null", searchResult);
            Assert.assertTrue("search result is an extended search result", searchResult instanceof MoviePage);
            
            MoviePage moviePage = (MoviePage) searchResult;
            
            Assert.assertEquals("title", "Indiana Jones és a kristálykoponya királysága", moviePage.getTitle());
            Assert.assertEquals("alternate title", "Indiana Jones and the Kingdom of the Crystal Skull", moviePage.getAlternateTitle());
            Assert.assertEquals("year", Integer.valueOf(2008), moviePage.getYear());
            Assert.assertNotNull("has plot", moviePage.getPlot());

            
            
            
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        
    }
    
    
}
