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

import com.flicklib.tools.Param;

/**
 *
 * @author francisdb
 */
public class ImdbUrlGenerator {

    private ImdbUrlGenerator() {
        // Utility class
    }

    /**
     * Generates the imdb url from the imdb id
     * @param localid 
     * @param movie
     * @return the imdb url
     */
    public static String generateImdbUrl(String localid) {
        return "http://www.imdb.com/title/tt" + Param.encode(localid) + "/";

    }
}
