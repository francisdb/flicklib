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

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.TextExtractor;

import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.service.movie.AbstractJerichoParser;
import com.flicklib.tools.ElementOnlyTextExtractor;
import com.google.inject.Singleton;
/**
 *
 * @author francisdb
 */
@Singleton
public class FlixsterParser extends AbstractJerichoParser{

    //private static final Logger LOGGER = LoggerFactory.getLogger(FlixsterParser.class);
    

@Override
    public void parse(Source source, MoviePage movieSite) {
    	
    	List<?> h1Elements = source.getAllElements(HTMLElementName.H1);
    	for (Iterator<?> i = h1Elements.iterator(); i.hasNext();) {
    		Element h1Element = (Element) i.next();
    		List<?> aElements = h1Element.getAllElements(HTMLElementName.A);
            for (Iterator<?> i2 = aElements.iterator(); i2.hasNext();) {
                Element aElement = (Element) i2.next();
                if(aElement.getAttributeValue("href").contains("/movie/")){
                	parseTitle(aElement.getContent().getTextExtractor().toString().trim(), movieSite);
                }
            }
    	}
    	
        
        
        List<?> h4Elements = source.getAllElements(HTMLElementName.H4);
        for (Iterator<?> i = h4Elements.iterator(); i.hasNext();) {
            Element h4Element = (Element) i.next();
            TextExtractor extractor = new ElementOnlyTextExtractor(h4Element.getContent());
            String content = extractor.toString().trim();
            
            // TODO use "Critics" score
            if (content.equals("Flixster Users")) {
                Element next = source.getNextElement(h4Element.getEnd());
                next = (Element) next.getAllElements(HTMLElementName.SPAN).get(0);
                String votes = new ElementOnlyTextExtractor(next.getContent()).toString().trim();
                votes = votes.replace("%", "");
                movieSite.setScore(Integer.valueOf(votes));
            }
        }
    }
    
    static void parseTitle(String title,MovieSearchResult mv) {
        Matcher matcher = Pattern.compile("(.*) \\((\\d{4})\\)").matcher(title);
        if (matcher.find()) {
        	String year = matcher.group(2);
        	mv.setYear(Integer.parseInt(year));
        	mv.setTitle(matcher.group(1));
        } else {
        	mv.setTitle(title);
        }
    }

}
