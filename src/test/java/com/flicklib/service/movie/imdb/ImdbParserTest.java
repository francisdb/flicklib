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

import org.junit.Test;

import com.flicklib.domain.MoviePage;
import com.flicklib.service.FileSourceLoader;

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