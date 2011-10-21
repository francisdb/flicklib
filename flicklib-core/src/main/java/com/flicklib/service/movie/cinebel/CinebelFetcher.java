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
package com.flicklib.service.movie.cinebel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flicklib.api.AbstractMovieInfoFetcher;
import com.flicklib.api.Parser;
import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.domain.MovieService;
import com.flicklib.domain.MovieType;
import com.flicklib.service.Source;
import com.flicklib.service.SourceLoader;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author francisdb
 *
 */
@Singleton
public class CinebelFetcher extends AbstractMovieInfoFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CinebelFetcher.class);

    /**
     * TODO add support for en and fr
     * TODO add support for fetching info curently available in the movies (find count link and go to page)
     */
    private static final String LANG = "nl";
    
    private final SourceLoader sourceLoader;
    private final Parser parser;
    
    @Inject
    public CinebelFetcher(final SourceLoader sourceLoader, @Cinebel final Parser parser) {
		this.sourceLoader = sourceLoader;
		this.parser = parser;
	}
    
    public CinebelFetcher(final SourceLoader loader) {
        this(loader, new CinebelParser());
    }
    
	@Override
	public MoviePage getMovieInfo(String idForSite) throws IOException {		
		MoviePage page = new MoviePage(MovieService.CINEBEL);
		// No other movie types for this site?
		page.setType(MovieType.MOVIE);
		String url = generateMovieUrl(idForSite);
		page.setUrl(url);
		Source source = sourceLoader.loadSource(url);
		
		parser.parse(source, page);
		
		
        return page; 
	}

	@Override
	public List<? extends MovieSearchResult> search(String title) throws IOException {
		LOGGER.trace("search for "+title);
		List<MovieSearchResult> list = new ArrayList<MovieSearchResult>();
		String url = generateSearchUrl(title, 30);
		Source source = sourceLoader.loadSource(url);
		net.htmlparser.jericho.Source jerichoSource = source.getJerichoSource();
        
    	// find all links
    	//<a href="/nl/film/102-Forrest-gump.htm" class="blink">
		//<h1 class='blink'><b>Forrest gump</b></h1>
		//</a>
        List<Element> linkElements = jerichoSource.getAllElements(HTMLElementName.A);
    	for(Element linkElement: linkElements){
	        String href = linkElement.getAttributeValue("href");
	        String cssClass = linkElement.getAttributeValue("class");
	        if ("blink".equals(cssClass) && href.startsWith("/"+LANG+"/film") && href.endsWith(".htm")) {
	        	MovieSearchResult movieSite = new MovieSearchResult();
		        String movieTitle = linkElement.getContent().getTextExtractor().toString();
	        	movieSite.setTitle(movieTitle);
	        	movieSite.setUrl(MovieService.CINEBEL.getUrl() + href);
	        	String id = href.substring(("/"+LANG+"/film/").length(), href.indexOf('-'));
	        	movieSite.setIdForSite(id);
	        	// TODO pick up the year
	        	// movieSite.setYear(year);
	        	list.add(movieSite);
	        }
    	}

		
        return list;
	}
	
	private String generateMovieUrl(final String id){
		//http://www.cinebel.be/nl/film/102-.htm
		return MovieService.CINEBEL.getUrl()+"/"+LANG+"/film/"+id+"/";
	}

	private String generateSearchUrl(final String title, final int maxResults) {
		String encoded;
		try {
			encoded = URLEncoder.encode(title, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("utf-8 encoding not supported" ,e);
		}
		//String url = MovieService.CINEBEL.getUrl()+"/portal/faces/public/exo/search?portal:componentId=SearchContentPortlet&portal:type=render&portal:isSecure=false&lng="+LANG+"&query="+encoded+"&itemsPerPage="+maxResults+"&fuzzy=true&fieldToSearch=movieTitle&category=movie&movieWithSchedules=false";
		String url =  MovieService.CINEBEL.getUrl() + "/nl/zoek?query="+encoded+"&x=13&y=13";
		return url;
	}

}
