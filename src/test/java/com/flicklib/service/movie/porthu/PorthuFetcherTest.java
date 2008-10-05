/**
 * 
 */
package com.flicklib.service.movie.porthu;

/*
 * This file is part of Flicklib.
 * 
 * Copyright (C) Zsombor Gegesy
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

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.flicklib.api.MovieSearchResult;
import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieService;
import com.flicklib.service.FileSourceLoader;
import com.flicklib.service.movie.AliasingSourceLoader;

/**
 * @author zsombor
 * 
 */
public class PorthuFetcherTest {

    AliasingSourceLoader loader;
    PorthuFetcher fetcher;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        loader = new AliasingSourceLoader(new FileSourceLoader());
        loader.putAlias("http://port.hu/pls/ci/cinema.film_creator?i_text=keresztapa&i_film_creator=1&i_city_id=3372", "porthu/filmsearch-response.html");
        loader.putAlias("http://port.hu/pls/fi/films.film_page?i_where=2&i_film_id=5609&i_city_id=3372&i_county_id=-1", "porthu/film_page.html");
        fetcher = new PorthuFetcher(loader);
    }

    @Test
    public void testSearch() {
        try {
            List<MovieSearchResult> result = fetcher.search("keresztapa");

            Assert.assertNotNull("result not null", result);
            Assert.assertEquals("has enough results", 11, result.size());

            check(result.get(0), "A blues - Keresztapák és fiaik", "67762", "The Blues - Godfather's And Sons", "amerikai filmsorozat, 96 perc, 2003", 2003);
            check(result.get(4), "A Keresztapa", "5609", "The Godfather", "színes magyarul beszélő amerikai gengszterfilm, 171 perc, 1972", 1972);

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    void check(MovieSearchResult m, String title, String id, String alternateTitle, String description, int year) {
        Assert.assertEquals("title", title, m.getTitle());
        Assert.assertEquals("id", id, m.getIdForSite());
        Assert.assertEquals("alternate title", alternateTitle, m.getAlternateTitle());
        Assert.assertEquals("description", description, m.getDescription());
        Assert.assertEquals("year", Integer.valueOf(year), m.getYear());
    }

    @Test
    public void testGetMovieInfo() {
        try {
            MoviePage moviePage = fetcher.getMovieInfo("5609");
            Assert.assertNotNull("movie page", moviePage);
            Assert.assertEquals("service type", MovieService.PORTHU, moviePage.getService());
            Assert.assertEquals("title", "A Keresztapa", moviePage.getTitle());
            Assert.assertEquals("alternate title", "The Godfather", moviePage.getAlternateTitle());
            Assert.assertEquals("year", Integer.valueOf(1972), moviePage.getYear());
            Assert.assertNotNull("has plot", moviePage.getPlot());
            Assert.assertTrue("plot", moviePage.getPlot().startsWith("A gengszterfilmek legnagyobbika, világhírű"));
            Assert.assertEquals("director", "Francis Ford Coppola", moviePage.getDirector());
            Assert.assertEquals("score", Integer.valueOf(94), moviePage.getScore());
            Assert.assertEquals("votes", Integer.valueOf(80), moviePage.getVotes());

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

    }

}
