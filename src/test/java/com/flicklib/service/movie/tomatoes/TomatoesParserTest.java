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
package com.flicklib.service.movie.tomatoes;

import com.flicklib.domain.Movie;
import com.flicklib.domain.MoviePage;
import com.flicklib.service.FileSourceLoader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author francisdb
 */
public class TomatoesParserTest {

    /**
     * Test of parse method, of class TomatoesParser.
     * @throws Exception 
     */
    @Test
    public void testOfflineParse() throws Exception {
        String source = new FileSourceLoader().load("tomatoes/Pulp Fiction Movie Reviews, Pictures - Rotten Tomatoes.html");
        MoviePage site = new MoviePage();
        site.setMovie(new Movie());
        TomatoesParser instance = new TomatoesParser();
        instance.parse(source, site);
        assertEquals(Integer.valueOf(96), site.getScore());
    }
    
    @Test
    public void testOfflineParse2() throws Exception {
        String source = new FileSourceLoader().load("tomatoes/Waist Deep Movie Reviews, Pictures - Rotten Tomatoes.html");
        MoviePage site = new MoviePage();
        site.setMovie(new Movie());
        TomatoesParser instance = new TomatoesParser();
        instance.parse(source, site);
        assertEquals(Integer.valueOf(26), site.getScore());
    }

}