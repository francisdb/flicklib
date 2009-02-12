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

import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.HTMLElementName;
import au.id.jericho.lib.html.Source;

import com.flicklib.api.AbstractMovieInfoFetcher;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.flicklib.api.Parser;
import com.flicklib.domain.Movie;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.domain.MovieService;
import com.flicklib.domain.MoviePage;
import com.flicklib.service.SourceLoader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
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

    @Inject
    public TomatoesInfoFetcher(final @RottenTomatoes Parser tomatoesParser, final SourceLoader sourceLoader) {
        this.sourceLoader = sourceLoader;
        this.tomatoesParser = tomatoesParser;
    }

    
    @Override
    public MoviePage getMovieInfo(String id) throws IOException {
        MoviePage site = new MoviePage(MovieService.TOMATOES);
        String url = generateTomatoesUrl(id);
        site.setUrl(url);
        com.flicklib.service.Source source = sourceLoader.loadSource(site.getUrl());
        tomatoesParser.parse(source, site);
        return site;
    }

    @Override
    public List<MovieSearchResult> search(String title) throws IOException {
        // use the imdb fetcher, to load IMDB id-s.
        
    	List<MovieSearchResult> result = new ArrayList<MovieSearchResult>();
        
        com.flicklib.service.Source source = sourceLoader.loadSource(createTomatoesSearchUrl(title));
        Source jerichoSource = new Source(source.getContent());
        //source.setLogWriter(new OutputStreamWriter(System.err)); // send log messages to stderr
        jerichoSource.fullSequentialParse();
        
        List<?> divElements = jerichoSource.findAllElements(HTMLElementName.DIV);
        for (Iterator<?> i = divElements.iterator(); i.hasNext();) {
            Element divElement = (Element) i.next();
            //<div id="search_results_main" class="content clearfix">
            if("search_results_main".equals(divElement.getAttributeValue("id"))){
            	List<?> trElements = divElement.findAllElements(HTMLElementName.TR);
            	for (Iterator<?> j = trElements.iterator(); j.hasNext();) {
                    Element trElement = (Element) j.next();
                    
                    MovieSearchResult m = new MovieSearchResult();
                    
                    
	            	List<?> aElements = trElement.findAllElements(HTMLElementName.A);
	            	for (Iterator<?> k = aElements.iterator(); k.hasNext();) {
	                    Element aElement = (Element) k.next();
	                    String url = aElement.getAttributeValue("href");
	                    if (url != null && url.startsWith("/m/")) {
			                String movieName = aElement.getContent().getTextExtractor().toString();
			                if (movieName != null && movieName.trim().length() != 0) {
			                    String movieUrl = MovieService.TOMATOES.getUrl() + url;
			                    m.setUrl(movieUrl);
			                    m.setIdForSite(url.replace("/m/", ""));
			                    m.setTitle(movieName);
			                    m.setService(MovieService.TOMATOES);
			                    result.add(m);
			                    LOGGER.trace("taking result: " + movieName + " -> " + movieUrl);
			                }
	                    }
	            	}
	            	
	            	List<?> strongElements = trElement.findAllElements(HTMLElementName.STRONG);
	            	for (Iterator<?> k = strongElements.iterator(); k.hasNext();) {
	                    Element strongElement = (Element) k.next();
	                    String year = strongElement.getContent().getTextExtractor().toString();
	                    if(year.trim().length() > 0){
	                    	m.setYear(Integer.valueOf(year));
	                    }
	            	}
            	}            	
            }
        }
        return result;
    }
    
    
    /**
     * @deprecated not part of the interface
     * @param movie
     * @param imdbId
     * @return the MoviePage
     */
    @Deprecated
    public MoviePage fetch(Movie movie, String imdbId) {
        MoviePage site = new MoviePage();
        //site.setMovie(movie);
        site.setService(MovieService.TOMATOES);
        if (imdbId == null || "".equals(imdbId)) {
            LOGGER.error("IMDB id missing", new IOException("No imdb id available, not implemented"));
        }else{
            try {
                String url = generateTomatoesUrlForImdb(imdbId);
                site.setUrl(url);
                com.flicklib.service.Source source = sourceLoader.loadSource(site.getUrl());
                tomatoesParser.parse(source, site);
            } catch (IOException ex) {
                LOGGER.error("Loading from rotten tomatoes failed", ex);
            }
        }
        return site;
    }

    private String createTomatoesSearchUrl(String title) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(title, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error("Could not cencode UTF-8", ex);
        }
        return MovieService.TOMATOES.getUrl()+"/search/full_search.php?search=" + encoded;
    }
    
    /**
     *
     * @param movie 
     * @return the tomatoes url
     */
    private String generateTomatoesUrlForImdb(String imdbId) {
        return MovieService.TOMATOES.getUrl()+"/alias?type=imdbid&s=" + imdbId;
    }
    
    /**
    *
    * @param movie 
    * @return the tomatoes url
    */
   private String generateTomatoesUrl(String id) {
       return MovieService.TOMATOES.getUrl()+"/m/" + id;
   }
}
