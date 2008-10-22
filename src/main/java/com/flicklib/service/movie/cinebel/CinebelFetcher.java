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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.HTMLElementName;

import com.flicklib.api.AbstractMovieInfoFetcher;
import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.domain.MovieService;
import com.flicklib.domain.MovieType;
import com.flicklib.service.Source;
import com.flicklib.service.SourceLoader;
import com.google.inject.Inject;

public class CinebelFetcher extends AbstractMovieInfoFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CinebelFetcher.class);
	
    private static final String ROOT = "http://www.cinebel.be";
    /**
     * TODO add support for en and fr
     */
    private static final String LANG = "nl";
    
    private final SourceLoader sourceLoader;
    
    @Inject
    public CinebelFetcher(final SourceLoader sourceLoader) {
		this.sourceLoader = sourceLoader;
	}
    
	@Override
	public MoviePage getMovieInfo(String idForSite) throws IOException {
		MoviePage page = new MoviePage(MovieService.CINEBEL);
		// No other movie types for this site?
		page.setType(MovieType.MOVIE);
		String url = generateMovieUrl(idForSite);
		page.setUrl(url);
		Source source = sourceLoader.loadSource(url);
		au.id.jericho.lib.html.Source jerichoSource = new au.id.jericho.lib.html.Source(source.getContent());
        jerichoSource.fullSequentialParse();
        
        // <span class="movieMainTitle">The Matrix</span>
        @SuppressWarnings("unchecked")
        List<Element> spanElements = jerichoSource.findAllElements(HTMLElementName.SPAN);
        for(Element spanElement: spanElements){
        	if("movieMainTitle".equals(spanElement.getAttributeValue("class"))){
        		page.setTitle(spanElement.getContent().getTextExtractor().toString());
        	}
        }
        
//        <img id="imgFilmPoster"
//			onmouseout="document.getElementById('big_poster').className='textRemovedWithCSS';"
//			onmouseover="document.getElementById('big_poster').className='';"
//		src="/portal/resources/movie/2920/ma2920.jpg"
// 		width="120" height="160" alt="De affiche van de film The Matrix"
// 	/>
        
        // <img src="/portal/resources/common/cineb9_off.gif" title="93%" alt="93%" width="48"  height="9" />
        
        // TODO support big images?
        @SuppressWarnings("unchecked")
        List<Element> imgElements = jerichoSource.findAllElements(HTMLElementName.IMG);
        for(Element imgElement: imgElements){
        	if("imgFilmPoster".equals(imgElement.getAttributeValue("id"))){
        		page.setImgUrl(ROOT + imgElement.getAttributeValue("src"));
        	}else{
        		String src = imgElement.getAttributeValue("src");
        		if(src.startsWith("/portal/resources/common/cineb9_off.gif") && src.endsWith("_off.gif")){
        			page.setScore(Integer.valueOf(imgElement.getAttributeValue("title").replace("%", "")));
        		}
        	}
        }
        
//        <div id="synopsis">
//		<h2 class="textRemovedWithCSS">Synopsis</h2>
//		<p>Thomas, een informaticus met een ongelukkig privéleven, neemt buiten ...</p>
//	</div>
        @SuppressWarnings("unchecked")
        List<Element> divElements = jerichoSource.findAllElements(HTMLElementName.DIV);
        for(Element divElement: divElements){
        	if("synopsis".equals(divElement.getAttributeValue("id"))){
        		Element par = (Element) divElement.findAllElements(HTMLElementName.P).get(0);
        		page.setDescription(par.getContent().getTextExtractor().toString());
        	}
        }
        
        // TODO get genre, runtime, director, length
        
        
        return page; 
	}

	@Override
	public List<? extends MovieSearchResult> search(String title) throws IOException {
		LOGGER.trace("search for "+title);
		List<MovieSearchResult> list = new ArrayList<MovieSearchResult>();
		String url = generateSearchUrl(title, 30);
		Source source = sourceLoader.loadSource(url);
		au.id.jericho.lib.html.Source jerichoSource = new au.id.jericho.lib.html.Source(source.getContent());
        jerichoSource.fullSequentialParse();
        
    	// find all links
    	//<a href="/nl/film/102-Forrest-gump.htm" class="blink">
		//<h1 class='blink'><b>Forrest gump</b></h1>
		//</a>
    	@SuppressWarnings("unchecked")
        List<Element> linkElements = jerichoSource.findAllElements(HTMLElementName.A);
    	for(Element linkElement: linkElements){
	        String href = linkElement.getAttributeValue("href");
	        String cssClass = linkElement.getAttributeValue("class");
	        String movieTitle = linkElement.getContent().getTextExtractor().toString();
	        if ("blink".equals(cssClass) && href.startsWith("/"+LANG+"/film") && href.endsWith(".htm")) {
	        	MovieSearchResult movieSite = new MovieSearchResult();
	        	movieSite.setTitle(movieTitle);
	        	movieSite.setUrl(ROOT + href);
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
		return ROOT+"/"+LANG+"/film/"+id+"-.htm";
	}

	private String generateSearchUrl(final String title, final int maxResults) {
		String encoded;
		try {
			encoded = URLEncoder.encode(title, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("utf-8 encoding not supported" ,e);
		}
		String url = ROOT+"/portal/faces/public/exo/search?portal:componentId=SearchContentPortlet&portal:type=render&portal:isSecure=false&lng="+LANG+"&query="+encoded+"&itemsPerPage="+maxResults+"&fuzzy=true&fieldToSearch=movieTitle&category=movie&movieWithSchedules=false";
		return url;
	}

}
