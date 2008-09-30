/*
 * This file is part of Flicklib.
 * 
 * Copyright (C) Francis De Brabandere
 * 
 * Flicklib is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Movie Browser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.flicklib.service.movie.movieweb;

import com.flicklib.domain.Movie;
import com.flicklib.domain.MoviePage;
import com.flicklib.service.HttpSourceLoader;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author francisdb
 */
public class MovieWebInfoFetcherTest {


    /**
     * Test of load method, of class MovieWebInfoFetcher.
     */
    @Test
    public void testFetch() {
        Movie movie = new Movie();
        movie.setTitle("Pulp Fiction");
        MovieWebParser parser = new MovieWebParser();
        MovieWebInfoFetcher fetcher = new MovieWebInfoFetcher(parser, new HttpSourceLoader(null));
        MoviePage site = fetcher.fetch(movie, null);
        assertNotNull("MovieWebStars is null", site.getScore());
    }

}