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

import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.HTMLElementName;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.TextExtractor;
import com.flicklib.domain.MoviePage;
import com.google.inject.Singleton;
import com.flicklib.service.movie.AbstractJerichoParser;
import com.flicklib.tools.ElementOnlyTextExtractor;
import java.util.Iterator;
import java.util.List;
/**
 *
 * @author francisdb
 */
@Singleton
public class FlixsterParser extends AbstractJerichoParser{

    //private static final Logger LOGGER = LoggerFactory.getLogger(FlixsterParser.class);
    
    @Override
    public void parse(String html, Source source, MoviePage movieSite) {
    	
    	List<?> h1Elements = source.findAllElements(HTMLElementName.H1);
    	for (Iterator<?> i = h1Elements.iterator(); i.hasNext();) {
    		Element h1Element = (Element) i.next();
    		List<?> aElements = h1Element.findAllElements(HTMLElementName.A);
            for (Iterator<?> i2 = aElements.iterator(); i2.hasNext();) {
                Element aElement = (Element) i2.next();
                if(aElement.getAttributeValue("href").contains("/movie/")){
                    movieSite.setTitle(aElement.getContent().getTextExtractor().toString().trim());
                }
            }
    	}
    	
        
        
        List<?> h4Elements = source.findAllElements(HTMLElementName.H4);
        for (Iterator<?> i = h4Elements.iterator(); i.hasNext();) {
            Element h4Element = (Element) i.next();
            TextExtractor extractor = new ElementOnlyTextExtractor(h4Element.getContent());
            String content = extractor.toString().trim();
            
            // TODO use "Critics" score
            if (content.equals("Flixster Users")) {
                Element next = source.findNextElement(h4Element.getEnd());
                next = (Element) next.findAllElements(HTMLElementName.SPAN).get(0);
                String votes = new ElementOnlyTextExtractor(next.getContent()).toString().trim();
                votes = votes.replace("%", "");
                movieSite.setScore(Integer.valueOf(votes));
            }
        }
    }

}
