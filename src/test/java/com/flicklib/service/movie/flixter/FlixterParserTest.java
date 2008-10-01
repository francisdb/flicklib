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

import com.flicklib.domain.Movie;
import com.flicklib.domain.MoviePage;
import com.flicklib.service.FileSourceLoader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author francisdb
 */
public class FlixterParserTest {


    /**
     * Test of parse method, of class FlixterParser.
     * @throws Exception 
     */
    @Test
    public void testParse() throws Exception{
        String source = new FileSourceLoader().load("flixter/the-x-files-i-want-to-believe-the-x-files-2.html");
        MoviePage site = new MoviePage();
        site.setMovie(new Movie());
        FlixterParser instance = new FlixterParser();
        instance.parse(source, site);
        assertEquals(Integer.valueOf(60), site.getScore());
        assertEquals(Integer.valueOf(14259), site.getVotes());
        assertEquals("The X-Files: I Want to Believe (The X Files 2)", site.getMovie().getTitle());
    }

}