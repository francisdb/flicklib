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
package com.flicklib.domain;

import java.util.HashSet;
import java.util.Set;


/**
 * 
 * @author francisdb
 */
public class MoviePage extends MovieSearchResult {

    /**
     * Score from 0 - 100
     */
    private Integer score;

    private Integer votes;

    private String imgUrl;

    private String plot;

    /**
     * Runtime in minutes
     */
    private Integer runtime;
    private Set<String> genres = new HashSet<String>();;
    private Set<String> languages = new HashSet<String>();;

    private String director;

    public MoviePage() {
    }

    public MoviePage(MovieService service) {
        super(service);
    }

    /**
     * Gets the score (0-100) 
     * @return the score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Sets the score (0-100) 
     * @param score
     */
    public void setScore(Integer score) {
    	if(score != null && (score < 0 || score > 100)){
    		throw new IllegalArgumentException("Score must be null or a value in the range 0-100");
    	}
        this.score = score;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    /**
     * @return the runtime
     */
    public Integer getRuntime() {
        return runtime;
    }

    /**
     * @param runtime
     *            the runtime to set
     */
    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    /**
     * @return the genres
     */
    public Set<String> getGenres() {
        return genres;
    }

    /**
     * @param genres
     *            the genres to set
     */
    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    /**
     * @return the languages
     */
    public Set<String> getLanguages() {
        return languages;
    }

    /**
     * @param languages
     *            the languages to set
     */
    public void setLanguages(Set<String> languages) {
        this.languages = languages;
    }

    /**
     * @return the director
     */
    public String getDirector() {
        return director;
    }

    /**
     * @param director
     *            the director to set
     */
    public void setDirector(String director) {
        this.director = director;
    }

    public void addGenre(String genre) {
        this.genres.add(genre);
    }

    public void addLanguage(String language) {
        this.languages.add(language);
    }
    
    @Override
    public String toString() {
    	return getClass().getSimpleName()+" / "+getService()+" / "+getUrl();
    }

}
