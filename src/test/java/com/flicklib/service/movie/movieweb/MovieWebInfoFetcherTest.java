/*
 * This file is part of Flicklib.
 *
 * Copyright (C) Francis De Brabandere
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
package com.flicklib.service.movie.movieweb;

import java.io.IOException;

import com.flicklib.domain.Movie;
import com.flicklib.domain.MoviePage;
import com.flicklib.service.HttpSourceLoader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author francisdb
 */
public class MovieWebInfoFetcherTest {


    /**
     * Test of load method, of class MovieWebInfoFetcher.
     * @throws IOException 
     */
    @Test
    public void testFetch() throws IOException {
        Movie movie = new Movie();
        movie.setTitle("Pulp Fiction");
        MovieWebParser parser = new MovieWebParser();
        MovieWebInfoFetcher fetcher = new MovieWebInfoFetcher(parser, new HttpSourceLoader(null));
        MoviePage site = fetcher.fetch("Pulp Fiction");
        assertNotNull("MovieWebStars is null", site.getScore());
    }

}