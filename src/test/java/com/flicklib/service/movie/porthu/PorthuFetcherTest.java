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
package com.flicklib.service.movie.porthu;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.domain.MovieService;
import com.flicklib.service.FileSourceLoader;
import com.flicklib.service.movie.AliasingSourceLoader;
import static org.junit.Assert.*;

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
        loader.putAlias("http://port.hu/pls/fi/films.film_page?i_where=2&i_film_id=5609&i_city_id=3372&i_county_id=-1", "porthu/film_page.html");
        loader.putAlias("http://port.hu/pls/fi/films.film_page?i_where=2&i_film_id=80364&i_city_id=3372&i_county_id=-1", "porthu/film_page2.html");
        loader.putAlias("http://port.hu/pls/fi/films.film_page?i_where=2&i_film_id=1269&i_city_id=3372&i_county_id=-1", "porthu/film_page3.html");
        loader.putAlias("http://port.hu/pls/ci/cinema.film_creator?i_text=keresztapa&i_film_creator=1&i_city_id=3372", "porthu/filmsearch-response.html");
        loader.putAlias("http://port.hu/pls/ci/cinema.film_creator?i_text=a+kiraly+osszes+embere&i_film_creator=1&i_city_id=3372",
                "porthu/filmsearch-response2.html");
        loader.putAlias("http://port.hu/pls/fi/films.film_page?i_where=2&i_film_id=75033&i_city_id=3372&i_county_id=-1", "porthu/film_page4.html");
        fetcher = new PorthuFetcher(loader);
    }

    @Test
    public void testSearch() {
        try {
            List<MovieSearchResult> result = fetcher.search("keresztapa");

            assertNotNull("result not null", result);
            assertEquals("has enough results", 11, result.size());

            check(result.get(0), "A Keresztapa", "5609", "The Godfather", "színes magyarul beszélő amerikai gengszterfilm, 171 perc, 1972", 1972);
            check(result.get(10), "A blues - Keresztapák és fiaik", "67762", "The Blues - Godfather's And Sons", "amerikai filmsorozat, 96 perc, 2003", 2003);

            result = fetcher.search("a kiraly osszes embere");
            assertNotNull("result not null", result);
            assertEquals("has enough results", 3, result.size());
            check(result.get(0), "A király összes embere", "83459", "All the King's Men",
                    "színes, fekete-fehér magyarul beszélő német-amerikai filmdráma, 128 perc, 2006", 2006);
            check(result.get(1), "A király összes embere", "39441", "All the King's Men", "fekete-fehér feliratos amerikai filmdráma, 105 perc, 1949", 1949);
            check(result.get(2), "All the King's Men", "40444", "", "színes amerikai-angol háborús filmdráma, 110 perc, 1999", 1999);

        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    void check(MovieSearchResult m, String alternateTitle, String id, String title, String description, int year) {
        assertEquals("title", title, m.getTitle());
        assertEquals("id", id, m.getIdForSite());
        assertEquals("alternate title", alternateTitle, m.getAlternateTitle());
        assertEquals("description", description, m.getDescription());
        assertEquals("year", Integer.valueOf(year), m.getYear());
    }

    @Test
    public void testGetMovieInfo() {
        try {
            MoviePage moviePage = fetcher.getMovieInfo("5609");
            assertNotNull("movie page", moviePage);
            assertEquals("service type", MovieService.PORTHU, moviePage.getService());
            assertEquals("title", "A Keresztapa", moviePage.getAlternateTitle());
            assertEquals("alternate title", "The Godfather", moviePage.getTitle());
            assertEquals("year", Integer.valueOf(1972), moviePage.getYear());
            assertNotNull("has plot", moviePage.getPlot());
            assertTrue("plot", moviePage.getPlot().startsWith("A gengszterfilmek legnagyobbika, világhírű"));
            assertEquals("director", "Francis Ford Coppola", moviePage.getDirector());
            assertEquals("score", Integer.valueOf(94), moviePage.getScore());
            assertEquals("votes", Integer.valueOf(80), moviePage.getVotes());

            moviePage = fetcher.getMovieInfo("80364");
            assertNotNull("movie page", moviePage);
            assertEquals("service type", MovieService.PORTHU, moviePage.getService());
            assertEquals("title", "Parfüm: Egy gyilkos története", moviePage.getAlternateTitle());
            assertEquals("alternate title", "Perfume: The Story of a Murderer", moviePage.getTitle());
            assertEquals("year", Integer.valueOf(2006), moviePage.getYear());
            assertNotNull("has plot", moviePage.getPlot());
            assertTrue("plot", moviePage.getPlot().startsWith("1766-ban, a franciaországi Grasse városban a parfűmkészítő"));
            assertEquals("director", "Tom Tykwer", moviePage.getDirector());
            assertEquals("score", Integer.valueOf(80), moviePage.getScore());
            assertEquals("votes", Integer.valueOf(114), moviePage.getVotes());
            
            moviePage = fetcher.getMovieInfo("1269");
            assertNotNull("movie page", moviePage);
            assertEquals("service type", MovieService.PORTHU, moviePage.getService());
            assertEquals("title", "Star Trek 8. - Kapcsolatfelvétel", moviePage.getAlternateTitle());
            assertEquals("alternate title", "Star Trek: First Contact", moviePage.getTitle());
            assertEquals("year", Integer.valueOf(1996), moviePage.getYear());
            assertNotNull("has plot", moviePage.getPlot());
            assertTrue("plot", moviePage.getPlot().startsWith("A Star Trek filmek legújabb és magasan legjobb darabja"));
            assertEquals("director", "Jonathan Frakes", moviePage.getDirector());
            assertEquals("score", Integer.valueOf(100), moviePage.getScore());
            assertEquals("votes", Integer.valueOf(4), moviePage.getVotes());


            moviePage = fetcher.getMovieInfo("75033");
            assertNotNull("movie page", moviePage);
            assertEquals("service type", MovieService.PORTHU, moviePage.getService());
            assertEquals("title", "Terkel in Trouble", moviePage.getTitle());
            assertEquals("alternate title", "Terhelt Terkel", moviePage.getAlternateTitle());
            assertEquals("original title", "Terkel i knibe", moviePage.getOriginalTitle());
            assertEquals("year", Integer.valueOf(2004), moviePage.getYear());
            assertNotNull("has plot", moviePage.getPlot());
            assertTrue("plot", moviePage.getPlot().startsWith("Végy egy göndör hajú, pattanásos tizenhét éves tinédzsert"));
            assertEquals("director", "Kresten Vestbjerg Andersen", moviePage.getDirector());
            assertEquals("score", Integer.valueOf(78), moviePage.getScore());
            assertEquals("votes", Integer.valueOf(9), moviePage.getVotes());
            assertEquals("runtime", Integer.valueOf(78), moviePage.getRuntime());

            
            
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

}
