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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.flicklib.domain.MoviePage;
import com.flicklib.service.FileSourceLoader;
import com.flicklib.service.Source;

/**
 *
 * @author francisdb
 */
public class GoogleParserTest {

    /**
     * Test of parse method, of class GoogleParser.
     * @throws Exception 
     */
    @Test
    public void testParse() throws Exception {
        Source source = new FileSourceLoader().loadSource("google/reviews.html");
        MoviePage site = new MoviePage();
        GoogleParser instance = new GoogleParser();
        instance.parse(source, site);
        assertEquals(Integer.valueOf(78), site.getScore());
    }

}