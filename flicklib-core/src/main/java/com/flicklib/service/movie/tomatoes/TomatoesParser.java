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

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import com.flicklib.domain.MoviePage;
import com.google.inject.Singleton;
import com.flicklib.service.movie.AbstractJerichoParser;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author francisdb
 */
@Singleton
public class TomatoesParser extends AbstractJerichoParser {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TomatoesParser.class);

    @Override
    public void parse(final String html, Source source, MoviePage movieSite) {
    	if(movieSite.getUrl() != null){
    		movieSite.setIdForSite(movieSite.getUrl().replace("http://www.rottentomatoes.com/m/", ""));
    	}
    	
        List<?> divElements = source.getAllElements(HTMLElementName.DIV);
        for (Iterator<?> i = divElements.iterator(); i.hasNext();) {
            Element divElement = (Element) i.next();
            
            // TODO add id for site
            
            String id = divElement.getAttributeValue("id");
            if (id != null && "tomatometer_score".equals(id)) {
                String userRating = divElement.getContent().getTextExtractor().toString().trim();
                if (!"".equals(userRating) && !"N/A".equals(userRating)) {
                    userRating = userRating.replace("%", "");
                    userRating = userRating.trim();
                    try {
                        int score = Integer.valueOf(userRating);
                        movieSite.setScore(score);
                    } catch (NumberFormatException ex) {
                        LOGGER.error("Could not parse " + userRating + " to Integer", ex);
                    }
                }
            }
        }
        Element elementById = source.getElementById("movie_synopsis_all");
        if (elementById!=null) {
        	movieSite.setPlot(elementById.getContent().getTextExtractor().toString());
        }

    }
}