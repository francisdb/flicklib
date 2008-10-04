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
package com.flicklib.api;

import java.io.IOException;
import java.util.List;

import com.flicklib.domain.MoviePage;


/**
 *
 * @author fdb
 */
public interface MovieInfoFetcher {
	
    /**
     * Fetches movie info from a service and complements the movieInfo object
     * @param title 
     * @return the parsed moviePage
     */
    MoviePage fetch(String title);
    
    /**
     * Performs a search on the service and returns results
     * @param title
     * @return the result list, never null
     * @throws IOException
     */
    List<? extends MovieSearchResult> search(String title) throws IOException;
    
    /**
     * Looks up the detailed movie info for a movieIdForSite
     * @param movieIdForSite 
     * @return the moviepage
     * @throws IOException
     */
    MoviePage getMovieInfo(String movieIdForSite) throws IOException;
    
}
