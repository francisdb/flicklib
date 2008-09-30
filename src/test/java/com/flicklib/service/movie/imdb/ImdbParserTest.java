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

import com.flicklib.domain.Movie;
import com.flicklib.domain.MoviePage;
import com.flicklib.service.FileSourceLoader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author francisdb
 */
public class ImdbParserTest {

    /**
     * Test of parse method, of class ImdbParser.
     * 
     * 
     * @throws Exception 
     */
    @Test
    public void testParse() throws Exception{
        String source = new FileSourceLoader().load("imdb/Pulp Fiction (1994).html");
        MoviePage site = new MoviePage();
        site.setMovie(new Movie());
        ImdbParser instance = new ImdbParser();
        instance.parse(source, site);
        //assertEquals(Integer.valueOf(89), site.getIdForSite());
        assertEquals("Pulp Fiction", site.getMovie().getTitle());
        assertEquals("Quentin Tarantino", site.getMovie().getDirector());
        assertEquals(Integer.valueOf(1994), site.getMovie().getYear());
        assertEquals("Pulp%20Fiction%20%281994%29_files/MV5BMjE0ODk2NjczOV5BMl5BanBnXkFtZTYwNDQ0NDg4.jpg", site.getImgUrl());
        assertEquals("The lives of two mob hit men, a boxer, a gangster's wife, and a pair of\ndiner bandits intertwine in four tales of violence and redemption.", site.getMovie().getPlot());
        assertEquals(Integer.valueOf(154), site.getMovie().getRuntime());
        assertEquals(Integer.valueOf(298638), site.getVotes());
        // TODO test other fields
        
        source = new FileSourceLoader().load("imdb/A Couple of White Chicks at the Hairdresser (2007).html");
        site = new MoviePage();
        site.setMovie(new Movie());
        instance.parse(source, site);
        //assertEquals(Integer.valueOf(89), site.getIdForSite());
        assertEquals("A Couple of White Chicks at the Hairdresser", site.getMovie().getTitle());
        assertEquals("Roxanne Messina Captor", site.getMovie().getDirector());
        assertEquals(Integer.valueOf(2007), site.getMovie().getYear());
        assertEquals(null, site.getImgUrl());
        assertEquals("\"A COUPLE OF WHITE CHICKS AT THE HAIRDRESSER\" is a Dramady about two very different women who develop...", site.getMovie().getPlot());
        assertEquals(null, site.getMovie().getRuntime());
        assertEquals(null, site.getVotes());
    }

}