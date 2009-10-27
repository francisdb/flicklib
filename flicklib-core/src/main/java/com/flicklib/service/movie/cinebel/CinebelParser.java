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

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;

import com.flicklib.api.Parser;
import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieService;
import com.flicklib.service.Source;

/**
 * @author francisdb
 *
 */
public class CinebelParser implements Parser {

	/* (non-Javadoc)
	 * @see com.flicklib.api.Parser#parse(com.flicklib.service.Source, com.flicklib.domain.MoviePage)
	 */
	@Override
	public void parse(Source source, MoviePage page) {

		net.htmlparser.jericho.Source jerichoSource = source.getJerichoSource();
        
        // <span class="movieMainTitle">The Matrix</span>
        List<Element> spanElements = jerichoSource.getAllElements(HTMLElementName.SPAN);
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
        List<Element> imgElements = jerichoSource.getAllElements(HTMLElementName.IMG);
        for(Element imgElement: imgElements){
        	if("imgFilmPoster".equals(imgElement.getAttributeValue("id"))){
        		page.setImgUrl(MovieService.CINEBEL.getUrl() + imgElement.getAttributeValue("src"));
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
        List<Element> divElements = jerichoSource.getAllElements(HTMLElementName.DIV);
        for(Element divElement: divElements){
        	if("synopsis".equals(divElement.getAttributeValue("id"))){
        		Element par = (Element) divElement.getAllElements(HTMLElementName.P).get(0);
        		String synopsis = par.getContent().getTextExtractor().toString();
        		page.setDescription(synopsis);
        		page.setPlot(synopsis);
        	}
        }
        
        // TODO get genre, runtime, director, length
	}

}