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
package com.flicklib.service.movie.ofdb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.HTMLElementName;

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
public class OfdbFetcher extends AbstractMovieInfoFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfdbFetcher.class);
    
    private final SourceLoader sourceLoader;
    private final Parser parser;
    
    @Inject
    public OfdbFetcher(final SourceLoader sourceLoader, @Ofdb final Parser parser) {
		this.sourceLoader = sourceLoader;
		this.parser = parser;
	}
    
	@Override
	public MoviePage getMovieInfo(String idForSite) throws IOException {		
		MoviePage page = new MoviePage(MovieService.OFDB);
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
		List<MovieSearchResult> list = new ArrayList<MovieSearchResult>();
		String url = generateSearchUrl(title);
		Source source = sourceLoader.loadSource(url);
		au.id.jericho.lib.html.Source jerichoSource = new au.id.jericho.lib.html.Source(source.getContent());
        jerichoSource.fullSequentialParse();
        
    	// find all links
    	//<a href="film/1050,Pulp-Fiction" onmouseover="....">Pulp Fiction<font size="1"> / Pulp Fiction</font> (1994)</a>
        //<a href="film/33740,Pulp-Fiction-The-Facts" onmouseover="...">Pulp Fiction: The Facts [Kurzfilm]<font size="1"> / Pulp Fiction: The Facts</font> (2002)</a>
    	@SuppressWarnings("unchecked")
        List<Element> linkElements = jerichoSource.findAllElements(HTMLElementName.A);
    	for(Element linkElement: linkElements){
	        String href = linkElement.getAttributeValue("href");
	        if (href.startsWith("film/")) {
	        	MovieSearchResult movieSite = new MovieSearchResult();
	        	movieSite.setService(MovieService.OFDB);
		        String movieTitles = linkElement.getContent().getTextExtractor().toString();
		        String[] titles = movieTitles.split(Pattern.quote("/"));
		        String germanTitle = titles[0].trim();
		        if(germanTitle.endsWith("]")){
		        	String type = germanTitle.substring(germanTitle.lastIndexOf('[') + 1, germanTitle.lastIndexOf(']'));
		        	if("Kurzfilm".equals(type)){
		        		movieSite.setType(MovieType.SHORT_FILM);
		        	}else if("TV-Serie".equals(type)){
		        		movieSite.setType(MovieType.TV_SERIES);
		        	}else if("TV-Mini-Serie".equals(type)){
		        		movieSite.setType(MovieType.MINI_SERIES);
		        	}
		        	germanTitle = germanTitle.substring(0, germanTitle.lastIndexOf('['));
		        }else{
	        		movieSite.setType(MovieType.MOVIE);
	        	}
	        	movieSite.setTitle(germanTitle);
	        	if(titles.length > 1){
	        		String originalTitleYear = titles[1].trim();
	        		if(originalTitleYear.endsWith(")")){
	        			String year = originalTitleYear.substring(originalTitleYear.lastIndexOf('(') + 1, originalTitleYear.lastIndexOf(')'));
	        			try{
	        				movieSite.setYear(Integer.parseInt(year));
	        			}catch(NumberFormatException ex){
	        				LOGGER.warn("Could not parse year: "+ex.getMessage());
	        			}
	        			originalTitleYear = originalTitleYear.substring(0, originalTitleYear.lastIndexOf('('));
	        		}
	        		movieSite.setOriginalTitle(originalTitleYear.trim());
	        	}
	        	movieSite.setUrl(MovieService.OFDB.getUrl() + href);
	        	String id = href.substring(("film/").length());
	        	movieSite.setIdForSite(id);
	        	
	        	// TODO pick up the year
	        	// movieSite.setYear(year);
	        	list.add(movieSite);
	        }
    	}

		
        return list;
	}
	
	private String generateMovieUrl(final String id){
		// http://www.ofdb.de/film/1050,Pulp-Fiction
		String encoded;
		try {
			encoded = URLEncoder.encode(id, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("utf-8 encoding not supported" ,e);
		}
		return MovieService.OFDB.getUrl()+"/film/"+encoded;
	}

	private String generateSearchUrl(final String title) {
		String encoded;
		try {
			encoded = URLEncoder.encode(title, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("utf-8 encoding not supported" ,e);
		}
		String url = MovieService.OFDB.getUrl()+"/view.php?page=suchergebnis&Kat=Titel&SText="+encoded;
		return url;
	}

}
