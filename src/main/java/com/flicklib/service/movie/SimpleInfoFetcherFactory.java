package com.flicklib.service.movie;

import com.flicklib.api.InfoFetcherFactory;
import com.flicklib.api.MovieInfoFetcher;
import com.flicklib.domain.MovieService;
import com.flicklib.service.SourceLoader;
import com.flicklib.service.movie.cinebel.CinebelFetcher;
import com.flicklib.service.movie.flixter.FlixsterInfoFetcher;
import com.flicklib.service.movie.google.GoogleInfoFetcher;
import com.flicklib.service.movie.imdb.ImdbInfoFetcher;
import com.flicklib.service.movie.movieweb.MovieWebInfoFetcher;
import com.flicklib.service.movie.ofdb.OfdbFetcher;
import com.flicklib.service.movie.porthu.PorthuFetcher;
import com.flicklib.service.movie.tomatoes.TomatoesInfoFetcher;
import com.flicklib.service.movie.xpress.XpressHuFetcher;

public class SimpleInfoFetcherFactory implements InfoFetcherFactory {

    private final MovieInfoFetcher imdbInfoFetcher;
    private final MovieInfoFetcher movieWebInfoFetcher;
    private final MovieInfoFetcher tomatoesInfoFetcher;
    private final MovieInfoFetcher googleInfoFetcher;
    private final MovieInfoFetcher flixterInfoFetcher;
    private final MovieInfoFetcher netflixInfoFetcher;
    private final MovieInfoFetcher porthuInfoFetcher;
    private final MovieInfoFetcher cinebelInfoFetcher;
    private final MovieInfoFetcher ofdbInfoFetcher;
    private final MovieInfoFetcher xpressHuFetcher;

    public SimpleInfoFetcherFactory(final SourceLoader sourceLoader) {
        this.imdbInfoFetcher = new ImdbInfoFetcher(sourceLoader);
        this.movieWebInfoFetcher = new MovieWebInfoFetcher(sourceLoader);
        this.tomatoesInfoFetcher = new TomatoesInfoFetcher(sourceLoader);
        this.googleInfoFetcher = new GoogleInfoFetcher(sourceLoader);
        this.flixterInfoFetcher = new FlixsterInfoFetcher(sourceLoader);
        // TODO: netflix needs an API key ...
        this.netflixInfoFetcher = null; // new NetflixInfoFetcher()
        this.porthuInfoFetcher = new PorthuFetcher(sourceLoader);
        this.cinebelInfoFetcher = new CinebelFetcher(sourceLoader);
        this.ofdbInfoFetcher = new OfdbFetcher(sourceLoader);
        this.xpressHuFetcher = new XpressHuFetcher(sourceLoader);
    }
    
    
    @Override
    public MovieInfoFetcher get(MovieService service) {
        MovieInfoFetcher fetcher = null;
        switch(service){
            case FLIXSTER:
                fetcher = flixterInfoFetcher;
                break;
            case GOOGLE:
                fetcher = googleInfoFetcher;
                break;
            case IMDB:
                fetcher = imdbInfoFetcher;
                break;
            case MOVIEWEB:
                fetcher = movieWebInfoFetcher;
                break;
            case TOMATOES:
                fetcher = tomatoesInfoFetcher;
                break;
            case NETFLIX:
                fetcher = netflixInfoFetcher;
                break;
            case PORTHU :
                fetcher = porthuInfoFetcher;
                break;
            case CINEBEL :
                fetcher = cinebelInfoFetcher;
                break;
            case OFDB :
                fetcher = ofdbInfoFetcher;
                break;
            case XPRESSHU : 
                fetcher = xpressHuFetcher;
                break;
            default:
                // keep this exception in here!
                throw new RuntimeException("Unknown service: "+service);
        }
        return fetcher;
    }


}
