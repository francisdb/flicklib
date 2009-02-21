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
package com.flicklib.service.movie.imdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.flicklib.domain.MovieSearchResult;
import com.flicklib.service.HttpSourceLoader;

/**
 *
 * @author francisdb
 */
public class ImdbSearchTest {




    /**
     * Test of getResults method, of class ImdbSearch.
     * TODO fix nullpointer because of imdbparser dependency!
     * @throws Exception 
     */
    @Test
    //@Ignore
    public void testGetResults_String() throws Exception {
        ImdbSearch instance = new ImdbSearch(new HttpSourceLoader(null, false), new ImdbParser());
        List<MovieSearchResult> result = instance.getResults("Pulp Fiction");
        assertTrue(result.size() > 0);
        assertEquals("Pulp Fiction", result.get(0).getTitle());
        
        result = instance.getResults("Die Hard 4");
        assertTrue(result.size() > 0);
        assertEquals("Live Free or Die Hard", result.get(0).getTitle());
        
        result = instance.getResults("Black Tie White Noise");
        assertTrue(result.size() > 0);
        assertEquals(Integer.valueOf(1993), result.get(0).getYear());
        assertEquals("David Bowie: Black Tie White Noise", result.get(0).getTitle());
    }

    /**
     * Test of generateImdbTitleSearchUrl method, of class ImdbSearch.
     */
    @Test
    public void testGenerateImdbTitleSearchUrl() {
        String title = "Pulp Fiction";
        ImdbSearch instance = new ImdbSearch(new HttpSourceLoader(null, false), new ImdbParser());
        String expResult = "http://www.imdb.com/find?q=Pulp+Fiction;s=tt;site=aka";
        String result = instance.generateImdbTitleSearchUrl(title);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testAlternateTitle() throws IOException {
        ImdbInfoFetcher fetcher = new ImdbInfoFetcher(new HttpSourceLoader(null, true));
        List<MovieSearchResult> list = fetcher.search("Life Is a Miracle");
        assertNotNull("search result", list);
        assertTrue("more then 2 result", list.size() > 2);
        assertEquals("1. result:title", "Zivot je cudo", list.get(0).getTitle());
        assertEquals("1. result:alternate", "Life Is a Miracle", list.get(0).getAlternateTitle());
        assertEquals("1. result:year", Integer.valueOf(2004), list.get(0).getYear());

        assertEquals("2. result:title", "Sung ming yun oi dung ting", list.get(1).getTitle());
        assertEquals("2. result:alternate", "Life Is a Miracle", list.get(1).getAlternateTitle());
        assertEquals("2. result:year", Integer.valueOf(2001), list.get(1).getYear());
    }


}