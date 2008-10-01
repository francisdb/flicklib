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
package com.flicklib.service.movie.apple;

import com.flicklib.domain.Movie;
import com.flicklib.domain.MoviePage;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author francisdb
 */
public class AppleTrailerFinderTest {

    /**
     * Test of findTrailerUrl method, of class AppleTrailerFinder.
     */
    @Test
    public void testFindTrailerUrl() {
        MoviePage site = new MoviePage();
        site.setMovie(new Movie());
        site.getMovie().setTitle("Big fish");
        AppleTrailerFinder instance = new AppleTrailerFinder();
        String url = instance.findTrailerUrl(site.getMovie().getTitle(), site.getIdForSite());
        assertEquals("http://www.apple.com/trailers/sony_pictures/big_fish/", url);
    }

}