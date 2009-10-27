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
package com.flicklib.service.movie.google;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.flicklib.domain.MoviePage;
import com.flicklib.service.HttpSourceLoader;
import com.flicklib.service.cache.HttpCache4J;

/**
 *
 * @author francisdb
 */
public class GoogleInfoFetcherTest {


    /**
     * Test of fetch method, of class GoogleInfoFetcher.
     * @throws IOException 
     */
    @Test
    public void testFetch() throws IOException {
        GoogleParser googleParser = new GoogleParser();
        GoogleInfoFetcher instance = new GoogleInfoFetcher(googleParser, new HttpSourceLoader(new HttpCache4J()));
        MoviePage site = instance.fetch("Pulp Fiction");
        assertNotNull("Google score is null", site.getScore());
    }

}