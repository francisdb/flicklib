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
package com.flicklib.service.movie.movieweb;

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
import com.flicklib.service.HttpSourceLoader;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 *
 * @author francisdb
 */
@Singleton
public class MovieWebInfoFetcher extends AbstractMovieInfoFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieWebInfoFetcher.class);

    private final Parser parser;
    private final HttpSourceLoader sourceLoader;

    /**
     * Creates a new MovieWebInfoFetcher
     * @param movieWebInfoParser
     * @param httpLoader 
     */
    @Inject
    public MovieWebInfoFetcher(final @MovieWeb Parser movieWebInfoParser, final HttpSourceLoader httpLoader) {
        this.parser = movieWebInfoParser;
        this.sourceLoader = httpLoader;
    }

    @Override
	public MoviePage getMovieInfo(String id) throws IOException {
		MoviePage site = null;
		if (id.startsWith(MovieService.MOVIEWEB.getUrl())) {
			com.flicklib.service.Source source = sourceLoader.loadSource(id);
			site = new MoviePage(MovieService.MOVIEWEB);
			site.setIdForSite(id);
			site.setUrl(id);
			parser.parse(source, site);
		}else{
			throw new IOException("Trying to get movie info for MovieWeb but the supplied id is not a movieweb id!");			
		}
		return site;
	}
    
    @Override
    public List<MovieSearchResult> search(String title) throws IOException {
        List<MovieSearchResult> result = new ArrayList<MovieSearchResult>();
        String urlToLoad = createMovieWebSearchUrl(title);

        com.flicklib.service.Source source = sourceLoader.loadSource(urlToLoad);
        Source jerichoSource = new Source(source.getContent());
        //source.setLogWriter(new OutputStreamWriter(System.err)); // send log messages to stderr
        jerichoSource.fullSequentialParse();

        //Element titleElement = (Element)source.findAllElements(HTMLElementName.TITLE).get(0);
        //System.out.println(titleElement.getContent().extractText());

        // <div id="bubble_allCritics" class="percentBubble" style="display:none;">     57%    </div>

        String movieUrl = null;
        List<?> aElements = jerichoSource.findAllElements(HTMLElementName.A);
        for (Iterator<?> i = aElements.iterator(); i.hasNext();) {
            Element aElement = (Element) i.next();
            String url = aElement.getAttributeValue("href");
            if (url != null && url.startsWith("/movies/film/")) {
                String movieName = aElement.getContent().getTextExtractor().toString();
                if (movieName != null && movieName.trim().length() != 0) {

                    movieUrl = "http://www.movieweb.com" + url;

                    MovieSearchResult m = new MovieSearchResult();
                    m.setIdForSite(movieUrl);
                    m.setTitle(movieName);
                    m.setService(MovieService.MOVIEWEB);
                    result.add(m);
                    
                    LOGGER.info("found title: " + movieName + " -> " + movieUrl);
                }
            }
        }

        return result;
    }
    
    
    @Deprecated
    public MoviePage fetch(Movie movie, String id) {
        MoviePage site = new MoviePage();
        //site.setMovie(movie);
        site.setService(MovieService.MOVIEWEB);
        String urlToLoad = createMovieWebSearchUrl(movie.getTitle());
        try {
            com.flicklib.service.Source source = sourceLoader.loadSource(urlToLoad);
            Source jerichoSource = new Source(source.getContent());
            //source.setLogWriter(new OutputStreamWriter(System.err)); // send log messages to stderr
            jerichoSource.fullSequentialParse();

            //Element titleElement = (Element)source.findAllElements(HTMLElementName.TITLE).get(0);
            //System.out.println(titleElement.getContent().extractText());

            // <div id="bubble_allCritics" class="percentBubble" style="display:none;">     57%    </div>

            String movieUrl = null;
            List<?> aElements = jerichoSource.findAllElements(HTMLElementName.A);
            for (Iterator<?> i = aElements.iterator(); i.hasNext();) {
                Element aElement = (Element) i.next();
                String url = aElement.getAttributeValue("href");
                if (url != null && url.endsWith("summary.php")) {
                    String movieName = aElement.getContent().getTextExtractor().toString();
                    if (movieUrl == null && movieName != null && movieName.trim().length() != 0) {

                        movieUrl = "http://www.movieweb.com" + url;
                        LOGGER.info("taking first result: " + movieName + " -> " + movieUrl);
                    }
                }
            }
            if (movieUrl == null) {
                LOGGER.warn("Movie not found on MovieWeb: "+movie.getTitle());
            }else{
                site.setUrl(movieUrl);
                source = sourceLoader.loadSource(movieUrl);
                parser.parse(source, site);
            }
        } catch (IOException ex) {
            LOGGER.error("Loading from MovieWeb failed: "+urlToLoad, ex);
        }
        return site;
    }

    private String createMovieWebSearchUrl(String title) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(title, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error("Could not cencode UTF-8", ex);
        }
        return "http://www.movieweb.com/search/?search=" + encoded;
    }

}
