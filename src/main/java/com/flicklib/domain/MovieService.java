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

/**
 *
 * @author francisdb
 */
public enum MovieService {
    /**
     * http://www.imdb.com
     */
    IMDB("IMDB", "http://www.imdb.com"),
    
    /**
     * http://www.rottentomatoes.com
     */
    TOMATOES("Rotten Tomatoes", "http://www.rottentomatoes.com", "Tomatoes"),
    
    /**
     * http://www.movieweb.com
     */
    MOVIEWEB("MovieWeb", "http://www.movieweb.com/", "MWeb"),
    
    /**
     * http://www.omdb.com
     */
    OMDB("OMDB", "http://www.omdb.com"),
    
    /**
     * http://www.google.com/movies
     */
    GOOGLE("Google movies", "http://www.google.com/movies", "Google"),
    
    /**
     * http://www.flixster.com
     */
    FLIXSTER("Flixter", "http://www.flixter.com"),
    
    /**
     * http://www.port.hu
     */
    PORTHU("Port.hu", "http://www.port.hu"),
    
    /**
     * http://www.netflix.com
     */
    NETFLIX("Netflix", "http://www.netflix.com");

    private final String name;
    private final String url;
    private final String shortName;
    
    MovieService(final String name, final String url) {
        this.name = name;
        this.url = url;
        this.shortName = name;
    }
    MovieService(final String name, final String url, final String shortName) {
        this.name = name;
        this.url = url;
        this.shortName = shortName;
    }

    public String getName(){
        return name;
    }

    public String getUrl(){
        return url;
    }
    
    public String getShortName() {
        return shortName;
    }

}
