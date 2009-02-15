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
package com.flicklib.service.movie.flixter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.HTMLElementName;
import au.id.jericho.lib.html.Source;

import com.flicklib.api.AbstractMovieInfoFetcher;
import com.flicklib.api.Parser;
import com.flicklib.domain.Movie;
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
public class FlixsterInfoFetcher extends AbstractMovieInfoFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlixsterInfoFetcher.class);
    private final SourceLoader sourceLoader;
    private final Parser parser;

    /**
     * Constructs a new FlixsterInfoFetcher
     * @param parser
     * @param sourceLoader
     */
    @Inject
    public FlixsterInfoFetcher(final @Flixster Parser parser, final SourceLoader sourceLoader) {
        this.sourceLoader = sourceLoader;
        this.parser = parser;
    }

    @Override
    public List<MovieSearchResult> search(String title) throws IOException {
        List<MovieSearchResult> result = new ArrayList<MovieSearchResult>();
        
        com.flicklib.service.Source source = sourceLoader.loadSource(createFlixterSearchUrl(title));
        Source jerichoSource = new Source(source.getContent());
        //source.setLogWriter(new OutputStreamWriter(System.err)); // send log messages to stderr
        jerichoSource.fullSequentialParse();

        // <a onmouseover="mB(event, 770678072);" title=" The X-Files: I Want to Believe (The X Files 2)" href="/movie/the-x-files-i-want-to-believe-the-x-files-2"  >
        // The X-Files: I Want to Believe (The X Files 2)
        // </a>

        
        List<?> aElements = jerichoSource.findAllElements(HTMLElementName.A);
        for (Iterator<?> i = aElements.iterator(); i.hasNext();) {
            Element aElement = (Element) i.next();
            String url = aElement.getAttributeValue("href");
            if (url != null && url.startsWith("/movie/")) {
                String movieName = aElement.getContent().getTextExtractor().toString();
                if (movieName != null && movieName.trim().length() != 0) {
                    int jsessIdIndex = url.indexOf(";jsessionid=");
                    if (jsessIdIndex!=-1) {
                        url = url.substring(0, jsessIdIndex);
                    }
                    String movieUrl = MovieService.FLIXSTER.getUrl() + url;

                    MovieSearchResult m = new MovieSearchResult();
                    m.setIdForSite(movieUrl);
                    m.setTitle(movieName);
                    m.setService(MovieService.FLIXSTER);
                    result.add(m);
                    LOGGER.debug("taking result: " + movieName + " -> " + movieUrl);
                }
            }
        }
        return result;
    }
    
    @Override
    public MoviePage getMovieInfo(String id) {
        try {
            if (id.startsWith(MovieService.FLIXSTER.getUrl())) {
                com.flicklib.service.Source source = sourceLoader.loadSource(id);
                MoviePage site = new MoviePage(MovieService.FLIXSTER);
                site.setIdForSite(id);
                site.setUrl(id);
                parser.parse(source, site);
                
                return site;
            }
        } catch (IOException ex) {
            LOGGER.error("Loading from Flixter failed", ex);
        }
        return null;
    }
    
    @Deprecated
    public MoviePage fetch(Movie movie, String id) {
        MoviePage site = new MoviePage();
        //site.setMovie(movie);
        site.setService(MovieService.FLIXSTER);
        try {
            com.flicklib.service.Source source = sourceLoader.loadSource(createFlixterSearchUrl(movie.getTitle()));
            Source jerichoSource = new Source(source.getContent());
            //source.setLogWriter(new OutputStreamWriter(System.err)); // send log messages to stderr
            jerichoSource.fullSequentialParse();

            // <a onmouseover="mB(event, 770678072);" title=" The X-Files: I Want to Believe (The X Files 2)" href="/movie/the-x-files-i-want-to-believe-the-x-files-2"  >
            // The X-Files: I Want to Believe (The X Files 2)
            // </a>

            String movieUrl = null;
            List<?> aElements = jerichoSource.findAllElements(HTMLElementName.A);
            for (Iterator<?> i = aElements.iterator(); i.hasNext();) {
                Element aElement = (Element) i.next();
                String url = aElement.getAttributeValue("href");
                if (url != null && url.startsWith("/movie/")) {
                    String movieName = aElement.getContent().getTextExtractor().toString();
                    if (movieUrl == null && movieName != null && movieName.trim().length() != 0) {

                        movieUrl = MovieService.FLIXSTER.getUrl() + url;
                        LOGGER.trace("taking first result: " + movieName + " -> " + movieUrl);
                    }
                }
            }
            if (movieUrl == null) {
               LOGGER.warn("Movie not found on Flixter: " + movie.getTitle());
            }else{
                site.setUrl(movieUrl);
                source = sourceLoader.loadSource(movieUrl);
                parser.parse(source, site);
            }
        } catch (IOException ex) {
            LOGGER.error("Loading from Flixter failed", ex);
        }
        return site;
    }

    private String createFlixterSearchUrl(String title) {
        //String encoded = title.replace(" ", "%20");
        String encoded = title;
        try {
            encoded = URLEncoder.encode(encoded, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error("Could not cencode UTF-8", ex);
        }
        return MovieService.FLIXSTER.getUrl()+"/search?q=" + encoded;
    }
}