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
package com.flicklib.service.movie.tomatoes;

import com.flicklib.api.AbstractMovieInfoFetcher;
import com.flicklib.api.MovieInfoFetcher;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.flicklib.api.Parser;
import com.flicklib.domain.Movie;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.domain.MovieService;
import com.flicklib.domain.MoviePage;
import com.flicklib.service.Source;
import com.flicklib.service.SourceLoader;
import com.flicklib.service.movie.imdb.Imdb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author fdb
 */
@Singleton
public class TomatoesInfoFetcher extends AbstractMovieInfoFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(TomatoesInfoFetcher.class);

    private final SourceLoader sourceLoader;
    private final Parser tomatoesParser;
    private final MovieInfoFetcher imdbFetcher;

    @Inject
    public TomatoesInfoFetcher(final @RottenTomatoes Parser tomatoesParser, final SourceLoader sourceLoader, final @Imdb MovieInfoFetcher imdbFetcher) {
        this.sourceLoader = sourceLoader;
        this.tomatoesParser = tomatoesParser;
        this.imdbFetcher = imdbFetcher;
    }

    
    @Override
    public MoviePage getMovieInfo(String id) throws IOException {
        MoviePage site = new MoviePage(MovieService.TOMATOES);
        site.setIdForSite(id);
        String url = generateTomatoesUrl(id);
        site.setUrl(url);
        Source source = sourceLoader.loadSource(site.getUrl());
        tomatoesParser.parse(source, site);

        return site;
    }

    @Override
    public List<MovieSearchResult> search(String title) throws IOException {
        // use the imdb fetcher, to load IMDB id-s.
        
        List<? extends MovieSearchResult> list = imdbFetcher.search(title);
        List<MovieSearchResult> result = new ArrayList<MovieSearchResult>(list.size());
        for (MovieSearchResult search: list) {
            MovieSearchResult msr = new MovieSearchResult ();
            msr.setService(MovieService.TOMATOES);
            msr.setIdForSite(search.getIdForSite());
            String url = generateTomatoesUrl(search.getIdForSite());
            msr.setUrl(url);
            msr.setTitle(search.getTitle());
            msr.setYear(search.getYear());
            
            result.add(msr);
        }
        return result;
    }
    
    
    
    @Deprecated
    public MoviePage fetch(Movie movie, String id) {
        MoviePage site = new MoviePage();
        //site.setMovie(movie);
        site.setService(MovieService.TOMATOES);
        if (id == null || "".equals(id)) {
            LOGGER.error("IMDB id missing", new IOException("No imdb id available, not implemented"));
        }else{
            try {
                String url = generateTomatoesUrl(id);
                site.setUrl(url);
                Source source = sourceLoader.loadSource(site.getUrl());
                tomatoesParser.parse(source, site);
            } catch (IOException ex) {
                LOGGER.error("Loading from rotten tomatoes failed", ex);
            }
        }
        return site;
    }

    /**
     *
     * @param movie 
     * @return the tomatoes url
     */
    private String generateTomatoesUrl(String imdbId) {
        return "http://www.rottentomatoes.com/alias?type=imdbid&s=" + imdbId;
    }
}
