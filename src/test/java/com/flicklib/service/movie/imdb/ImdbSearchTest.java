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
package com.flicklib.service.movie.imdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.flicklib.domain.MoviePage;
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
        ImdbSearch instance = new ImdbSearch(new HttpSourceLoader(null), new ImdbParser());
        List<MoviePage> result = instance.getResults("Pulp Fiction");
        assertTrue(result.size() > 0);
        assertEquals("Pulp Fiction", result.get(0).getMovie().getTitle());
        
        result = instance.getResults("Die Hard 4");
        assertTrue(result.size() > 0);
        assertEquals("Live Free or Die Hard", result.get(0).getMovie().getTitle());
        
        result = instance.getResults("Black Tie White Noise");
        assertTrue(result.size() > 0);
        assertEquals(Integer.valueOf(1993), result.get(0).getMovie().getYear());
        assertEquals("David Bowie: Black Tie White Noise", result.get(0).getMovie().getTitle());
    }

    /**
     * Test of generateImdbTitleSearchUrl method, of class ImdbSearch.
     */
    @Test
    public void testGenerateImdbTitleSearchUrl() {
        String title = "Pulp Fiction";
        ImdbSearch instance = new ImdbSearch(new HttpSourceLoader(null), new ImdbParser());
        String expResult = "http://www.imdb.com/find?q=Pulp+Fiction;s=tt;site=aka";
        String result = instance.generateImdbTitleSearchUrl(title);
        assertEquals(expResult, result);
        
    }


}