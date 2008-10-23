package com.flicklib.service.movie.porthu;

/*
 * This file is part of Flicklib.
 * 
 * Copyright (C) Zsombor Gegesy
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
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.HTMLElements;
import au.id.jericho.lib.html.Source;

import com.flicklib.api.AbstractMovieInfoFetcher;
import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.domain.MovieService;
import com.flicklib.service.SourceLoader;
import com.flicklib.tools.ElementOnlyTextExtractor;
import com.flicklib.tools.LevenshteinDistance;
import com.google.inject.Inject;

public class PorthuFetcher extends AbstractMovieInfoFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(PorthuFetcher.class);

    private static final String FILM_INFO_URL = "/pls/fi/films.film_page";
    private static final String TEST_CITY_ID = "3372";

    private static final Pattern FILM_ID_PATTERN = Pattern.compile("i_film_id=(\\d*)");

    private static final Pattern YEAR_END_PATTERN = Pattern.compile("(\\d+)\\z");

    private static final Pattern RUNTIME_PATTERN = Pattern.compile("(\\d+) perc");

    /**
     * match (x.y)/10 where x and y are numbers.
     */
    private static final Pattern SCORE_PATTERN = Pattern.compile("(\\d+(\\.\\d)?)/10");

    private final SourceLoader sourceLoader;

    @Inject
    public PorthuFetcher(final SourceLoader sourceLoader) {
        this.sourceLoader = sourceLoader;
    }

    @Override
    public MoviePage getMovieInfo(String id) throws IOException {

        String url = generateUrlForFilmID(id);
        Source source = loadContent(url);

        MoviePage mp = parseMovieInfoPage(source, id);
        mp.setUrl(url);
        return mp;
    }

    private MoviePage parseMovieInfoPage(Source source, String id) throws IOException {
        MoviePage mp = new MoviePage(MovieService.PORTHU);
        mp.setIdForSite(id);
        {
            @SuppressWarnings("unchecked")
            List<Element> titleElements = source.findAllElements("class", "blackbigtitle", false);
            if (titleElements.size() == 0) {
                throw new RuntimeException("no <span class='blackbigtitle'> found!");
            }
            Element titleElement = titleElements.get(0);

            setEnglishAndOriginalTitle(mp, getOriginalAndEnglishTitle(titleElement), titleElement.getContent().getTextExtractor().toString());
        }
        {
            @SuppressWarnings("unchecked")
            List<Element> spanElements = source.findAllElements("span");
            int btxtCount = 0;
            for (int i = 0; i < spanElements.size(); i++) {
                Element span = spanElements.get(i);
                if ("btxt".equals(span.getAttributeValue("class"))) {
                    btxtCount++;
                    String content = span.getTextExtractor().toString();
                    // the first <span class='btxt'> contains the description
                    if (btxtCount == 1) {
                        initDescriptionAndYear(content, mp);
                    } else {
                        if (content.trim().equals("rendező:")) {
                            // the next span contains the name of the director
                            Element nextSpan = spanElements.get(i + 1);
                            LOGGER.debug("director is : " + nextSpan);
                            mp.setDirector(nextSpan.getTextExtractor().toString());
                        }
                        if (content.startsWith("A film értékelése:")) {

                            mp.setScore(getScore(content));
                            // the next span contains the vote count
                            Element nextSpan = spanElements.get(i + 1);
                            LOGGER.debug("vote count : " + nextSpan);
                            mp.setVotes(getVotes(nextSpan.getTextExtractor().toString()));
                        }
                    }
                }
            }
            
            if (mp.getScore()==null) {
                // we have to fetch with ajax
                parseAjaxVoteObjectResponse(mp, id);
            }
        }

        mp.setPlot(getPlot(source));
        return mp;
    }


    @SuppressWarnings("unchecked")
    private String getPlot(Source source) {
        List<Element> tables = source.findAllElements("table");
        // find a table which doesn't have 'id', it's width='100%'
        // cellpadding="0" cellspacing="0' and it's parent a td
        for (Element table : tables) {
            // 
            if (table.getAttributeValue("id") == null && "100%".equals(table.getAttributeValue("width")) && "0".equals(table.getAttributeValue("cellpadding"))
                    && "0".equals(table.getAttributeValue("cellspacing"))) {
                if ("td".equals(table.getParentElement().getName())) {
                    List<Element> txtElements = table.findAllElements("class", "txt", true);
                    if (txtElements.size() > 0) {
                        return txtElements.get(0).getContent().getTextExtractor().toString();
                    }
                }
            }
        }
        return null;
    }

    private String getOriginalAndEnglishTitle(Element titleElement) {
        // // strange, this doesn't works, the span content is always
        // List<Element> alternateTitleSpan =
        // titleElement.getParentElement().findAllElements("class", "txt",
        // false);
        // if (alternateTitleSpan.size()==0) {
        // throw new
        // RuntimeException("no <span class='txt'> found next to the title!");
        // }
        // mp.setAlternateTitle(alternateTitleSpan.get(0).getContent().getTextExtractor().toString());*/
        String fullTag = titleElement.getParentElement().toString();
        // pattern for matching: <span class="txt">(<SOMETHING>)
        Pattern p = Pattern.compile("<span class=\"txt\">\\((.*)\\)");
        Matcher matcher = p.matcher(fullTag);
        if (matcher.find()) {
            if (matcher.groupCount() > 0) {
                LOGGER.debug("match:" + matcher.group() + " -> " + matcher.group(1));
                return matcher.group(1);
            }
        }
        return null;
    }

    public List<MovieSearchResult> search(String title) throws IOException {
        List<MovieSearchResult> result = new ArrayList<MovieSearchResult>();
        String url = generateUrlForTitleSearch(title);

        com.flicklib.service.Source flicklibSource = sourceLoader.loadSource(url);
        Source jerichoSource = parseContent(flicklibSource);

        if (isMoviePageUrl(flicklibSource.getUrl())) {
            String id = collectIdFromUrl(flicklibSource.getUrl());
            MoviePage mp = parseMovieInfoPage(jerichoSource, id);
            mp.setUrl(flicklibSource.getUrl());
            result.add(mp);
            return result;
        }
        MovieSearchResultComparator comparator = new MovieSearchResultComparator();
        TreeSet<MovieSearchResult> orderedSet = new TreeSet<MovieSearchResult>(comparator);

        @SuppressWarnings("unchecked")
        List<Element> spans = (List<Element>) jerichoSource.findAllElements(HTMLElements.SPAN);
        for (int i = 0; i < spans.size(); i++) {
            Element span = spans.get(i);

            if ("btxt".equals(span.getAttributeValue("class"))) {
                @SuppressWarnings("unchecked")
                List<Element> childs = span.getContent().findAllElements();
                if (childs.size() > 0) {
                    Element link = childs.get(0);
                    LOGGER.trace("link : " + link);
                    if ("a".equals(link.getName())) {
                        String href = link.getAttributeValue("href");
                        if (href.startsWith(FILM_INFO_URL)) {
                            LOGGER.info("film url :" + href);
                            String movieTitle = link.getContent().getTextExtractor().toString();
                            MovieSearchResult msr = new MovieSearchResult(MovieService.PORTHU);
                            msr.setUrl(href);
                            msr.setIdForSite(collectIdFromUrl(href));

                            String basetitle = unbracket(new ElementOnlyTextExtractor(span.getContent()).toString());
                            setEnglishAndOriginalTitle(msr, basetitle, movieTitle);

                            int distance = LevenshteinDistance.distance(movieTitle, title);
                            int distance2 = LevenshteinDistance.distance(msr.getAlternateTitle(), title);
                            LOGGER
                                    .info("found [distance:" + distance + ", alternate distance:" + distance2 + "]:" + movieTitle + '/'
                                            + msr.getAlternateTitle());
                            comparator.set(msr, Math.min(distance, distance2));
                            // next span has style 'txt' and contains the
                            // description

                            if (i + 1 < spans.size()) {
                                Element descSpan = spans.get(i + 1);
                                if ("txt".equals(descSpan.getAttributeValue("class"))) {
                                    String description = descSpan.getContent().getTextExtractor().toString();
                                    initDescriptionAndYear(description, msr);
                                }
                            }

                            orderedSet.add(msr);
                        }
                    }
                }
            }
        }
        result.addAll(orderedSet);
        return result;
    }

    private void setEnglishAndOriginalTitle(MovieSearchResult msr, String basetitle, String alternateTitle) {
        if (basetitle != null) {
            int perPos = basetitle.indexOf('/');
            if (perPos != -1) {
                // original title/english title
                msr.setOriginalTitle(basetitle.substring(0, perPos));
                msr.setTitle(basetitle.substring(perPos + 1));
            } else {
                msr.setTitle(basetitle);
            }
            msr.setAlternateTitle(alternateTitle);
        } else {
            msr.setTitle(unbracket(alternateTitle));
        }
    }

    private static class MovieSearchResultComparator implements Comparator<MovieSearchResult>, Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        
        private final Map<MovieSearchResult, Integer> scoreMap;

        public MovieSearchResultComparator() {
            this.scoreMap = new HashMap<MovieSearchResult, Integer>();
        }

        public void set(MovieSearchResult movie, Integer score) {
            this.scoreMap.put(movie, score);
        }

        @Override
        public int compare(MovieSearchResult o1, MovieSearchResult o2) {
            Integer d1 = scoreMap.get(o1);
            Integer d2 = scoreMap.get(o2);
            int value = safeCompare(d1, d2);
            if (value != 0) {
                return value;
            }
            // both null, or both not-null, but both has the same score.
            value = o1.getTitle().compareTo(o2.getTitle());
            if (value != 0) {
                return value;
            }
            // they have the same title ...
            // sort according to the creation year, newer first.
            value = -safeCompare(o1.getYear(), o2.getYear());
            return value;
        }

        private int safeCompare(Integer d1, Integer d2) {
            if (d1 != null) {
                if (d2 != null) {
                    return d1.compareTo(d2);
                } else {
                    return -1;
                }
            } else {
                if (d2 != null) {
                    return 1;
                }
            }
            return 0;
        }

    }

    /**
     * initialize the description and year field based on a generic, plain
     * description: 'színes magyarul beszélő amerikai gengszterfilm, 171 perc,
     * 1972'
     * 
     * @param description
     * @param msr
     */
    private void initDescriptionAndYear(String description, MovieSearchResult msr) {
        description = unbracket(description);

        if (msr instanceof MoviePage) {
            ((MoviePage) msr).setRuntime(getRuntime(description));
        }
        msr.setYear(getYear(description));
        msr.setDescription(description);
    }

    /**
     * remove brackets from the start and end of the string.
     * 
     * @param text
     * @return
     */
    private static String unbracket(String text) {
        text = text.trim();
        if (text.startsWith("(")) {
            text = text.substring(1);
        }
        if (text.endsWith(")")) {
            text = text.substring(0, text.length() - 1);
        }
        return text.trim();
    }

    /**
     * search for port.hu movie id in the url.
     * 
     * @param url
     * @return
     */
    private String collectIdFromUrl(String url) {
        Matcher matcher = FILM_ID_PATTERN.matcher(url);
        if (matcher.find()) {
            if (matcher.groupCount() == 1) {
                return matcher.group(1);
            }
        }
        return null;
    }

    private Integer getYear(String description) {
        Matcher matcher = YEAR_END_PATTERN.matcher(description);
        if (matcher.find()) {
            if (matcher.groupCount() == 1) {
                return Integer.parseInt(matcher.group(1));
            }
        }
        return null;
    }

    private Integer getRuntime(String description) {
        Matcher matcher = RUNTIME_PATTERN.matcher(description);
        if (matcher.find()) {
            if (matcher.groupCount() == 1) {
                return Integer.parseInt(matcher.group(1));
            }
        }
        return null;
    }

    private Integer getScore(String scoreText) {
        Matcher matcher = SCORE_PATTERN.matcher(scoreText);
        if (matcher.find()) {
            if (matcher.groupCount() == 2) {
                float score = Float.parseFloat(matcher.group(1));
                return Integer.valueOf((int) (score * 10));
            }
        }
        return null;
    }

    /**
     * get vote count from a string like: "(32 szavazat)" -> 32
     * 
     * @param string
     * @return
     */
    private Integer getVotes(String string) {
        String txt = unbracket(string);
        String[] array = txt.split(" ");
        if (array.length == 2 && "szavazat".equals(array[1])) {
            return Integer.parseInt(array[0]);
        }
        return null;
    }

    /**
     * load content from the given URL, and returns the parser object
     * 
     * @param url
     * @return
     * @throws IOException
     */
    private Source loadContent(String url) throws IOException {
        com.flicklib.service.Source content = sourceLoader.loadSource(url);
        return parseContent(content);
    }

	private void parseAjaxVoteObjectResponse(MoviePage mp, String id) throws IOException {
        Source voteObject = fetchVoteObject(id);
        @SuppressWarnings("unchecked")
        List<Element> spanElements = voteObject.findAllElements("span");
        for (Element span : spanElements) {
            String content = span.getTextExtractor().toString();
            String classAttr = span.getAttributeValue("class");
            if ("btxt".equals(classAttr)) {
                mp.setScore(getScore(content));
            }
            if ("rtxt".equals(classAttr)) {
                mp.setVotes(getVotes(content));
            }
        }

    }

    private Source fetchVoteObject(String id) throws IOException {
        Map<String,String> params = new LinkedHashMap<String,String>();
        //i_object_id=73833&i_area_id=6&i_is_separator=0
        params.put("i_object_id", id);
        params.put("i_area_id","6");
        params.put("i_is_separator", "0");
        
        Map<String,String> headers = new LinkedHashMap<String,String>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("X-Prototype-Version", "1.6.0.2");
        headers.put("Referer", generateUrlForFilmID(id));
        
        com.flicklib.service.Source content = sourceLoader.post("http://port.hu/pls/fi/VOTE.print_vote_box?", params, headers);
        return parseContent(content);
    }

    private Source parseContent(com.flicklib.service.Source content) {
        Source source = new Source(content.getContent());
        source.fullSequentialParse();
        return source;
    }

    protected String generateUrlForTitleSearch(String title) {
        try {
            return "http://port.hu/pls/ci/cinema.film_creator?i_text=" + URLEncoder.encode(title, "ISO-8859-2") + "&i_film_creator=1&i_city_id=" + TEST_CITY_ID;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding problem with UTF-8? " + e.getMessage(), e);
        }
    }

    protected String generateUrlForFilmID(String id) {
        return "http://port.hu" + FILM_INFO_URL + "?i_where=2&i_film_id=" + id + "&i_city_id=" + TEST_CITY_ID + "&i_county_id=-1";
    }

    protected boolean isMoviePageUrl(String url) {
        return FILM_INFO_URL.startsWith(url) || url.startsWith("http://port.hu" + FILM_INFO_URL);
    }
}
