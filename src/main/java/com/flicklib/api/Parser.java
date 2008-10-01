/*
 * This file is part of Flicklib.
 *
 * Copyright (C) Francis De Brabandere
 *
 * Licensed under the GNU Lesser General Public License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flicklib.api;

import com.flicklib.domain.MoviePage;


/**
 *
 * @author francisdb
 */
public interface Parser {
    /**
     * Parses the html page source to info for the movie
     * @param html
     * @param movieSite 
     */
    public void parse(String html, MoviePage movieSite);
}
