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
package com.flicklib.module;

import com.flicklib.api.InfoFetcherFactory;
import com.flicklib.api.MovieInfoFetcher;
import com.flicklib.api.Parser;
import com.flicklib.api.SubtitlesLoader;
import com.flicklib.service.HttpSourceLoader;
import com.flicklib.service.SourceLoader;
import com.flicklib.service.movie.InfoFetcherFactoryImpl;
import com.flicklib.service.movie.cinebel.Cinebel;
import com.flicklib.service.movie.cinebel.CinebelFetcher;
import com.flicklib.service.movie.flixter.Flixster;
import com.flicklib.service.movie.flixter.FlixterInfoFetcher;
import com.flicklib.service.movie.flixter.FlixterParser;
import com.flicklib.service.movie.google.Google;
import com.flicklib.service.movie.google.GoogleInfoFetcher;
import com.flicklib.service.movie.google.GoogleParser;
import com.flicklib.service.movie.imdb.Imdb;
import com.flicklib.service.movie.imdb.ImdbInfoFetcher;
import com.flicklib.service.movie.imdb.ImdbParser;
import com.flicklib.service.movie.movieweb.MovieWeb;
import com.flicklib.service.movie.movieweb.MovieWebInfoFetcher;
import com.flicklib.service.movie.movieweb.MovieWebParser;
import com.flicklib.service.movie.netflix.Netflix;
import com.flicklib.service.movie.netflix.NetflixInfoFetcher;
import com.flicklib.service.movie.omdb.Omdb;
import com.flicklib.service.movie.omdb.OmdbFetcher;
import com.flicklib.service.movie.porthu.PortHu;
import com.flicklib.service.movie.porthu.PorthuFetcher;
import com.flicklib.service.movie.tomatoes.RottenTomatoes;
import com.flicklib.service.movie.tomatoes.TomatoesInfoFetcher;
import com.flicklib.service.movie.tomatoes.TomatoesParser;
import com.flicklib.service.sub.OpenSubtitlesLoader;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 *
 * @author francisdb
 */
public class FlicklibModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SourceLoader.class).to(HttpSourceLoader.class);

        bind(SubtitlesLoader.class).to(OpenSubtitlesLoader.class);

        bind(InfoFetcherFactory.class).to(InfoFetcherFactoryImpl.class);

        bind(Parser.class).annotatedWith(MovieWeb.class).to(MovieWebParser.class);
        bind(Parser.class).annotatedWith(Imdb.class).to(ImdbParser.class);
        bind(Parser.class).annotatedWith(RottenTomatoes.class).to(TomatoesParser.class);
        bind(Parser.class).annotatedWith(Google.class).to(GoogleParser.class);
        bind(Parser.class).annotatedWith(Flixster.class).to(FlixterParser.class);

        bind(MovieInfoFetcher.class).annotatedWith(Imdb.class).to(ImdbInfoFetcher.class);
        bind(MovieInfoFetcher.class).annotatedWith(MovieWeb.class).to(MovieWebInfoFetcher.class);
        bind(MovieInfoFetcher.class).annotatedWith(RottenTomatoes.class).to(TomatoesInfoFetcher.class);
        bind(MovieInfoFetcher.class).annotatedWith(Google.class).to(GoogleInfoFetcher.class);
        bind(MovieInfoFetcher.class).annotatedWith(Flixster.class).to(FlixterInfoFetcher.class);
        bind(MovieInfoFetcher.class).annotatedWith(Omdb.class).to(OmdbFetcher.class);
        bind(MovieInfoFetcher.class).annotatedWith(Netflix.class).to(NetflixInfoFetcher.class);
        bind(MovieInfoFetcher.class).annotatedWith(PortHu.class).to(PorthuFetcher.class);
        bind(MovieInfoFetcher.class).annotatedWith(Cinebel.class).to(CinebelFetcher.class);

        bindConstant().annotatedWith(Names.named("http.timeout")).to(20 * 1000);
    }
}
