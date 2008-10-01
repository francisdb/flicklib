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
package com.flicklib.service.movie;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.flicklib.api.InfoFetcherFactory;
import com.flicklib.api.MovieInfoFetcher;
import com.flicklib.domain.MovieService;
import com.flicklib.service.movie.flixter.Flixster;
import com.flicklib.service.movie.google.Google;
import com.flicklib.service.movie.imdb.Imdb;
import com.flicklib.service.movie.movieweb.MovieWeb;
import com.flicklib.service.movie.netflix.Netflix;
import com.flicklib.service.movie.omdb.Omdb;
import com.flicklib.service.movie.tomatoes.RottenTomatoes;

/**
 *
 * @author francisdb
 */
@Singleton
public class InfoFetcherFactoryImpl implements InfoFetcherFactory{
    
    private final MovieInfoFetcher imdbInfoFetcher;
    private final MovieInfoFetcher movieWebInfoFetcher;
    private final MovieInfoFetcher tomatoesInfoFetcher;
    private final MovieInfoFetcher googleInfoFetcher;
    private final MovieInfoFetcher flixterInfoFetcher;
    private final MovieInfoFetcher omdbInfoFetcher;
    private final MovieInfoFetcher netflixInfoFetcher;;

    /**
     * Constructs a new InfoFetcherFactoryImpl
     * @param imdbInfoFetcher
     * @param movieWebInfoFetcher
     * @param tomatoesInfoFetcher
     * @param googleInfoFetcher
     * @param flixterInfoFetcher
     * @param omdbInfoFetcher
     */
    @Inject
    public InfoFetcherFactoryImpl(
            final @Imdb MovieInfoFetcher imdbInfoFetcher,
            final @MovieWeb MovieInfoFetcher movieWebInfoFetcher,
            final @RottenTomatoes MovieInfoFetcher tomatoesInfoFetcher,
            final @Google MovieInfoFetcher googleInfoFetcher,
            final @Flixster MovieInfoFetcher flixterInfoFetcher,
            final @Omdb MovieInfoFetcher omdbInfoFetcher,
            final @Netflix MovieInfoFetcher netflixInfoFetcher) {
        this.imdbInfoFetcher = imdbInfoFetcher;
        this.movieWebInfoFetcher = movieWebInfoFetcher;
        this.tomatoesInfoFetcher = tomatoesInfoFetcher;
        this.googleInfoFetcher = googleInfoFetcher;
        this.flixterInfoFetcher = flixterInfoFetcher;
        this.omdbInfoFetcher = omdbInfoFetcher;
        this.netflixInfoFetcher = netflixInfoFetcher;
    }
    
    

    @Override
    public MovieInfoFetcher get(MovieService service) {
        MovieInfoFetcher fetcher = null;
        switch(service){
            case FLIXSTER:
                fetcher = flixterInfoFetcher;
                break;
            case GOOGLE:
                fetcher = googleInfoFetcher;
                break;
            case IMDB:
                fetcher = imdbInfoFetcher;
                break;
            case MOVIEWEB:
                fetcher = movieWebInfoFetcher;
                break;
            case OMDB:
                fetcher = omdbInfoFetcher;
                break;
            case TOMATOES:
                fetcher = tomatoesInfoFetcher;
                break;
            case NETFLIX:
                fetcher = netflixInfoFetcher;
                break;
            default:
                throw new AssertionError("Unknown service: "+service);
        }
        return fetcher;
    }

}
