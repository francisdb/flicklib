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
package com.flicklib.service.movie.imdb;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.EndTag;
import au.id.jericho.lib.html.HTMLElementName;
import au.id.jericho.lib.html.Source;

import com.flicklib.domain.MoviePage;
import com.flicklib.service.movie.AbstractJerichoParser;
import com.flicklib.tools.ElementOnlyTextExtractor;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 *
 * @author francisdb
 */
@Singleton
public class ImdbParser extends AbstractJerichoParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImdbParser.class);

    @Inject
    public ImdbParser() {
    }


	@Override
    public void parse(final String html, Source source, MoviePage movie) {

        ImdbParserRegex regexParser = new ImdbParserRegex(html);

        movie.setType(regexParser.getType());
        Element titleHeader = (Element) source.findAllElements(HTMLElementName.H1).get(0);
        String title = new ElementOnlyTextExtractor(titleHeader.getContent()).toString();
        title = ImdbParserRegex.cleanTitle(title);
        movie.setTitle(title);

        List<?> yearLinks = titleHeader.findAllElements(HTMLElementName.A);
        if (yearLinks.size() > 0) {
            Element yearLink = (Element) yearLinks.get(0);
            String year = yearLink.getContent().getTextExtractor().toString();
            try {
                movie.setYear(Integer.valueOf(year));
            } catch (NumberFormatException ex) {
                LOGGER.error("Could not parse year '" + year + "' to integer", ex);
            }
        }

        List<?> linkElements = source.findAllElements(HTMLElementName.A);
        for (Iterator<?> i = linkElements.iterator(); i.hasNext();) {
            Element linkElement = (Element) i.next();
            if ("poster".equals(linkElement.getAttributeValue("name"))) {
                // A element can contain other tags so need to extract the text from it:
                List<?> imgs = linkElement.getContent().findAllElements(HTMLElementName.IMG);
                Element img = (Element) imgs.get(0);
                String imgUrl = img.getAttributeValue("src");
                movie.setImgUrl(imgUrl);
            }
            String href = linkElement.getAttributeValue("href");
            if (href != null && href.contains("/Sections/Genres/")) {
                String genre = linkElement.getContent().getTextExtractor().toString();
                // TODO find a better way to parse these out, make sure it are only the movie genres
                if (!genre.toLowerCase().contains("imdb")) {
                    movie.addGenre(linkElement.getContent().getTextExtractor().toString());
                }
            }
            if (href != null && href.contains("/Sections/Languages/")) {
                movie.addLanguage(linkElement.getContent().getTextExtractor().toString());
            }

        }

        linkElements = source.findAllElements(HTMLElementName.B);
        for (Iterator<?> i = linkElements.iterator(); i.hasNext();) {
            Element bElement = (Element) i.next();
            if (bElement.getContent().getTextExtractor().toString().contains("User Rating:")) {
                Element next = source.findNextElement(bElement.getEndTag().getEnd());
                String rating = next.getContent().getTextExtractor().toString();
                // skip (awaiting 5 votes)
                if (!rating.contains("awaiting")) {
                    parseRatingString(movie, rating);
                    next = source.findNextElement(next.getEndTag().getEnd());
                    parseVotes(movie, next);
                }
            }
        }

        linkElements = source.findAllElements(HTMLElementName.H5);
        String hText;
        for (Iterator<?> i = linkElements.iterator(); i.hasNext();) {
            Element hElement = (Element) i.next();
            hText = hElement.getContent().getTextExtractor().toString();
            int end = hElement.getEnd();
            if (hText.contains("Plot Outline")) {
                movie.setPlot(source.subSequence(end, source.findNextStartTag(end).getBegin()).toString().trim());
            } else if (hText.contains("Plot:")) {
                movie.setPlot(source.subSequence(end, source.findNextStartTag(end).getBegin()).toString().trim());
            } else if (hText.contains("Runtime")) {
                EndTag next = source.findNextEndTag(end);
                //System.out.println(next);
                String runtime = source.subSequence(end, next.getBegin()).toString().trim();
                movie.setRuntime(parseRuntime(runtime));
            } else if (hText.contains("Director")) {
                Element aElement = source.findNextElement(end);
                movie.setDirector(aElement.getContent().getTextExtractor().toString());
            } else if (hText.contains("User Rating")) {
                Element aElement = source.findNextElement(end);
                @SuppressWarnings("unchecked")
                List<Element> boldOnes = aElement.findAllElements(HTMLElementName.B);
                if (boldOnes.size()>0) {
                    Element element = boldOnes.get(0);
                    String rating = element.getTextExtractor().toString();
                    if (!rating.contains("awaiting")) {
                        parseRatingString(movie, rating);
                        Element next = source.findNextElement(element.getEndTag().getEnd());
                        parseVotes(movie, next);
                    }
                }
            } /*else if (hText.contains("Genre")) {
                
            }*/
            		
        }

        if (movie.getTitle() == null) {
            //System.out.println(source.toString());
            movie.setPlot("Not found");
        }

    }

    private void parseVotes(MoviePage movieSite, Element element) {
        String votes = element.getContent().getTextExtractor().toString();

        votes = votes.replaceAll("\\(", "");
        votes = votes.replaceAll("votes(\\))*", "");
        votes = votes.replaceAll(",", "");
        votes = votes.trim();
        try {
            movieSite.setVotes(Integer.valueOf(votes));
        } catch (NumberFormatException ex) {
            LOGGER.error("Could not parse the votes '" + votes + "' to Integer", ex);
        }
    }

    private void parseRatingString(MoviePage movieSite, String rating) {
        // to percentage
        rating = rating.replace("/10", "");
        try {
            int theScore = Math.round(Float.valueOf(rating).floatValue() * 10);
            movieSite.setScore(theScore);
        } catch (NumberFormatException ex) {
            LOGGER.error("Could not parse rating '" + rating + "' to Float", ex);
        }
    }

    private Integer parseRuntime(String runtimeString) {
        String runtime = runtimeString.substring(0, runtimeString.indexOf("min")).trim();
        int colonIndex = runtime.indexOf(":");
        if (colonIndex != -1) {
            runtime = runtime.substring(colonIndex + 1);
        }

        return Integer.valueOf(runtime);
    }
}
