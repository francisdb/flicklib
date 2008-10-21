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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.id.jericho.lib.html.Source;

import com.flicklib.api.AbstractMovieInfoFetcher;
import com.flicklib.api.Parser;
import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.domain.MovieService;
import com.flicklib.service.SourceLoader;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 
 * @author francisdb
 */
@Singleton
public class ImdbInfoFetcher extends AbstractMovieInfoFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImdbInfoFetcher.class);

    private final ImdbSearch imdbSearch;
    private final Parser imdbParser;
    private final SourceLoader loader;
    /*
     * FIXME this cache is a memory leak!
     */
    private final Map<String, List<MovieSearchResult>> imdbSearchCache = new HashMap<String, List<MovieSearchResult>>();

    @Inject
    public ImdbInfoFetcher(ImdbSearch imdbSearch, final @Imdb Parser imdbParser, SourceLoader loader) {
        this.imdbSearch = imdbSearch;
        this.imdbParser = imdbParser;
        this.loader = loader;
    }

    @Override
    public synchronized List<MovieSearchResult> search(String title) throws IOException {
        List<MovieSearchResult> list = imdbSearchCache.get(title.toLowerCase().trim());
        if (list == null) {
            list = imdbSearch.getResults(title);
            imdbSearchCache.put(title.toLowerCase().trim(), list);
        } else {
            LOGGER.debug("Returning cached result for " + title);
        }
        return list;
    }

    @Override
    public MoviePage getMovieInfo(String id) throws IOException {
        MoviePage site = new MoviePage();
        site.setService(MovieService.IMDB);
        site.setUrl(ImdbUrlGenerator.generateImdbUrl(id));
        site.setIdForSite(id);

        String source = loader.load(site.getUrl());
        Source jerichoSource = new Source(source);
        jerichoSource.fullSequentialParse();
        imdbParser.parse(source, site);

        return site;
    }
}
