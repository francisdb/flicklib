package com.flicklib.service.movie.blippr;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flicklib.api.MovieInfoFetcher;
import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.domain.MovieService;
import com.flicklib.service.Source;
import com.flicklib.service.SourceLoader;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class BlipprInfoFetcher implements MovieInfoFetcher {

    final static Logger LOG = LoggerFactory.getLogger(BlipprInfoFetcher.class);

    private static final String ROOT = "http://api.blippr.com/v2/";

    private final SourceLoader sourceLoader;

    @Inject
    public BlipprInfoFetcher(final SourceLoader sourceLoader) {
        this.sourceLoader = sourceLoader;
    }

    @Override
    public MoviePage fetch(String title) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MoviePage getMovieInfo(String idForSite) throws IOException {
        URL url = new URL(getUrlForID(idForSite));
        Source source = sourceLoader.loadSource(url.toExternalForm());
        LOG.info(url.toString());
        LOG.info(source.getContentType());
        LOG.info(source.getContent());
        MoviePage page = new MoviePage();
        try {
            JSONObject jsonObject = new JSONObject(source.getContent());
            JSONObject node = jsonObject.optJSONObject("title");
            resultToMovie(page, node);
        } catch (JSONException e) {
            throw new IOException(e.getMessage(), e);
        }
        return page;
    }

    private String getUrlForID(String idForSite) {
        return ROOT + "titles/" + idForSite + ".json";
    }

    @Override
    public List<MovieSearchResult> search(String title) throws IOException {
        URL url = new URL(ROOT + "movies/search.json?query=" + URLEncoder.encode(title, "UTF-8"));
        LOG.info("Searching with :" + url.toString());
        Source source = sourceLoader.loadSource(url.toExternalForm());
        return parseSearchResult(source);
    }

    private List<MovieSearchResult> parseSearchResult(Source source) throws IOException {
        LOG.info("response content type is : " + source.getContentType());
        LOG.info("response is : " + source.getContent());

        List<MovieSearchResult> results = new ArrayList<MovieSearchResult>();
        try {
            JSONObject jsonObject = new JSONObject(source.getContent());
            JSONObject search = jsonObject.getJSONObject("search");
            JSONArray array = search.getJSONObject("titles").getJSONArray("title");
            for (int i = 0; i < array.length(); i++) {
                JSONObject result = array.getJSONObject(i);
                try {
                    MoviePage movieSearchResult = new MoviePage();
                    resultToMovie(movieSearchResult, result);
                    results.add(movieSearchResult);
                } catch (JSONException e) {
                    LOG.error("error during accessing json : "+result +", e : "+e.getMessage(), e);
                }
            }

        } catch (JSONException e) {
            throw new IOException(e.getMessage(), e);
        }
        return results;
    }

    private void resultToMovie(MoviePage movie, JSONObject result) throws JSONException {
        movie.setIdForSite(result.getString("id"));
        movie.setUrl(getUrlForID(movie.getIdForSite()));
        movie.setService(MovieService.BLIPPR);
        movie.setTitle(result.getString("name"));
        movie.setScore(result.getInt("score"));
        JSONObject images = result.getJSONObject("images");
        String[] PREFERENCE = new String[] { "medium", "small", "thumb", "square", "square_thumb" };
        for (int i = 0; i < PREFERENCE.length && movie.getImgUrl() == null; i++) {
            String url = images.optString(PREFERENCE[i]);
            if (url != null) {
                movie.setImgUrl(url);
            }
        }
        movie.setVotes(result.optInt("reviews_count", 0));
        if (result.has("summary")) {
            movie.setDescription(result.getString("summary"));
        }
        JSONObject metaData = result.optJSONObject("metadata");
        if (metaData != null) {
            // JSONArray tags = metaData.getJSONObject("tags").getJSONArray("tag");
            for (String value : wrap(metaData.optJSONObject("genres"), "genre")) {
                movie.getGenres().add(value);
            }
            for (String value : wrap(metaData.optJSONObject("actors"), "actor")) {
                movie.getActors().add(value);
            }
            for (String value : wrap(metaData.optJSONObject("directors"), "director")) {
                movie.getDirectors().add(value);
            }
        }
        JSONObject yearObj = result.optJSONObject("year");
        if (yearObj != null) {
            movie.setYear(yearObj.getInt("year"));
        }
        
    }

    public Iterable<String> wrap(JSONObject object, String prop) {
        if (object == null) {
            return Collections.emptyList();
        }
        Object opt = object.opt(prop);
        if (opt instanceof JSONArray) {
            return new JSONArrayIterator((JSONArray) opt);
        }
        return Collections.singletonList((String) opt);
    }
    
    
    final static class JSONArrayIterator implements Iterator<String>, Iterable<String> {
        JSONArray array;
        int position;

        public JSONArrayIterator(JSONArray array) {
            this.array = array;
        }
        @Override
        public boolean hasNext() {
            return position < array.length();
        }

        @Override
        public String next() {
            try {
                return array.getString(position++);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public Iterator<String> iterator() {
            return this;
        }

        @Override
        public void remove() {
            throw new IllegalArgumentException();
        }

    }

    @Override
    public List<? extends MovieSearchResult> search(String title, String year) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

}
