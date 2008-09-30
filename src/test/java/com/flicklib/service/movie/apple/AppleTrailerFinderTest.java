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