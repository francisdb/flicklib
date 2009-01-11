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
package com.flicklib.service.movie.flixter;

import java.io.IOException;

import com.flicklib.domain.MoviePage;
import com.flicklib.service.HttpSourceLoader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author francisdb
 */
public class FlixterInfoFetcherTest {

    /**
     * Test of fetch method, of class FlixterInfoFetcher.
     * @throws IOException 
     */
    @Test
    public void testFetch() throws IOException {
        FlixterParser parser = new FlixterParser();
        FlixterInfoFetcher fetcher = new FlixterInfoFetcher(parser, new HttpSourceLoader(null));
        MoviePage site = fetcher.fetch("The X-Files I Want to Believe");
        assertTrue(site.getUrl().contains("http://www.flixster.com/movie/the-x-files-i-want-to-believe-the-x-files-2"));
        assertNotNull("MovieWebStars is null", site.getScore());

        site = fetcher.fetch("pulp fiction");
        assertTrue(site.getUrl().contains("http://www.flixster.com/movie/pulp-fiction"));
        assertEquals("Pulp Fiction", site.getTitle());
        
    }

}