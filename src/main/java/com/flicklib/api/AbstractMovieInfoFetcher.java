package com.flicklib.api;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flicklib.domain.MoviePage;
import com.flicklib.service.movie.flixter.FlixterInfoFetcher;

public abstract class AbstractMovieInfoFetcher implements MovieInfoFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlixterInfoFetcher.class);

    public AbstractMovieInfoFetcher() {
    }

    @Override
    public MoviePage fetch(String title) {
        try {
            List<? extends MovieSearchResult> search = this.search(title);
            if (search.size() > 0) {
                MovieSearchResult firstResult = search.get(0);
                if (firstResult instanceof MoviePage) {
                    return (MoviePage) firstResult;
                }
                return this.getMovieInfo(firstResult.getIdForSite());
            }
        } catch (IOException e) {
            LOGGER.error("fetching by " + this.getClass().getName() + " failed:" + e.getMessage(), e);
        }
        return null;
    }

}
