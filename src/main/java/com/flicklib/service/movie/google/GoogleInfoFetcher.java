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
package com.flicklib.service.movie.google;

import java.io.IOException;
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
import com.flicklib.tools.Param;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 *
 * @author francisdb
 */
@Singleton
public class GoogleInfoFetcher extends AbstractMovieInfoFetcher {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleInfoFetcher.class);
    
    private final Parser googleParser;
    private final HttpSourceLoader httpLoader;

    /**
     * Constructs a new GoogleInfoFetcher
     * @param googleParser
     * @param httpLoader
     */
    @Inject
    public GoogleInfoFetcher(final @Google Parser googleParser, HttpSourceLoader httpLoader) {
        this.googleParser = googleParser;
        this.httpLoader = httpLoader;
    }
    
    @Override
    public MoviePage getMovieInfo(String id) throws IOException {
        if (id.startsWith("http://www.google.com/movies/reviews")) {
            MoviePage site = new MoviePage (MovieService.GOOGLE);
            site.setUrl(id);
            com.flicklib.service.Source source = httpLoader.loadSource(id);
            googleParser.parse(source, site);
            return site;
        }
        return null;
    }
    
    @Override
    public List<MovieSearchResult> search(String title) throws IOException {
        String params = Param.paramString("q", title);
        com.flicklib.service.Source sourceString = httpLoader.loadSource("http://www.google.com/movies"+params);
        Source source = sourceString.getJerichoSource();
        //source.setLogWriter(new OutputStreamWriter(System.err)); // send log messages to stderr

        //Element titleElement = (Element)source.findAllElements(HTMLElementName.TITLE).get(0);
        //System.out.println(titleElement.getContent().extractText());

        // <div id="bubble_allCritics" class="percentBubble" style="display:none;">     57%    </div>

        List<MovieSearchResult> result = new ArrayList<MovieSearchResult>();
        List<?> aElements = source.findAllElements(HTMLElementName.A);
        for (Iterator<?> i = aElements.iterator(); i.hasNext();) {
            Element aElement = (Element) i.next();
            String url = aElement.getAttributeValue("href");
            // /movies/reviews?cid=b939f27b219eb36f&fq=Pulp+Fiction&hl=en
            if (url != null && url.startsWith("/movies/reviews?cid=")) {
                String movieUrl = null;
                movieUrl = "http://www.google.com" + url;
                String movieName = aElement.getContent().getTextExtractor().toString();
                if (movieName!=null && movieName.trim().length()>0) {
                    if (!isReviewTitle(movieName)) {
                        LOGGER.debug("found result: " + movieName + " -> " + movieUrl);
                        
                        MovieSearchResult s = new MovieSearchResult();
                        s.setService(MovieService.GOOGLE);
                        s.setTitle(movieName);
                        s.setIdForSite(movieUrl);
                        s.setUrl(movieUrl);
                        result.add(s);
                    }
                }
            }
        }
        return result;
    }
    
    private boolean isReviewTitle(String title) {
        // filter out '73 reviews' style titles or '1 review'
        String[] words = title.split(" ");
        if (words.length==2) {
            if (words[1].startsWith("review")) {
                try {
                    Integer.parseInt(words[0].trim());
                    return true;
                } catch(NumberFormatException e) {
                    return false;
                }
            }
        }
        return false;
        
    }
    

    @Deprecated
    public MoviePage fetch(Movie movie, String id) {
        MoviePage site = new MoviePage();
        //site.setMovie(movie);
        site.setService(MovieService.GOOGLE);
        try {
            String params = Param.paramString("q", movie.getTitle());
            com.flicklib.service.Source httpSource = httpLoader.loadSource("http://www.google.com/movies"+params);
            Source source = httpSource.getJerichoSource();
            //source.setLogWriter(new OutputStreamWriter(System.err)); // send log messages to stderr

            //Element titleElement = (Element)source.findAllElements(HTMLElementName.TITLE).get(0);
            //System.out.println(titleElement.getContent().extractText());

            // <div id="bubble_allCritics" class="percentBubble" style="display:none;">     57%    </div>

            String movieUrl = null;
            List<?> aElements = source.findAllElements(HTMLElementName.A);
            for (Iterator<?> i = aElements.iterator(); i.hasNext() && movieUrl == null;) {
                Element aElement = (Element) i.next();
                String url = aElement.getAttributeValue("href");
                // /movies/reviews?cid=b939f27b219eb36f&fq=Pulp+Fiction&hl=en
                if (url != null && url.startsWith("/movies/reviews?cid=")) {
                    movieUrl = "http://www.google.com" + url;
                    String movieName = aElement.getContent().getTextExtractor().toString();
                    LOGGER.info("taking first result: " + movieName + " -> " + movieUrl);
                }
            }
            if (movieUrl == null) {
                throw new IOException("Movie not found on Google: "+movie.getTitle());
            }
            site.setUrl(movieUrl);
            httpSource = httpLoader.loadSource(movieUrl);
            googleParser.parse(httpSource, site);
        } catch (IOException ex) {
            LOGGER.error("Loading from Google failed", ex);
        }
        return site;
    }

}
