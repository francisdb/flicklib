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
package com.flicklib.service.movie.omdb;

import com.flicklib.domain.Movie;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author francisdb
 */
public class OmdbFetcherTest {
	
    /**
     * Test of fetch method, of class OmdbFetcher.
     */
    @Test
    public void testFetch() {
        Movie movie = new Movie();
        OmdbFetcher instance = new OmdbFetcher();
        instance.fetch(movie, null);
        assertNotNull(movie);
    }

}