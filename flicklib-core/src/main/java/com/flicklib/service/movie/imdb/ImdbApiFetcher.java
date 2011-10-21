package com.flicklib.service.movie.imdb;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flicklib.api.AbstractMovieInfoFetcher;
import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.domain.MovieService;
import com.flicklib.service.Source;
import com.flicklib.service.SourceLoader;

public class ImdbApiFetcher extends AbstractMovieInfoFetcher {
	final static Logger LOG = LoggerFactory.getLogger(ImdbApiFetcher.class);

	private final SourceLoader loader;

	public ImdbApiFetcher(SourceLoader loader) {
		this.loader = loader;
	}

	@Override
	public List<? extends MovieSearchResult> search(String title) throws IOException {
		Source source = loader.loadSource(generateSearchUrl(title));
		// it seems that it's returns the first result, only ...
		return Collections.singletonList(parseJson(source));
	}

	@Override
	public MoviePage getMovieInfo(String idForSite) throws IOException {
		Source source = loader.loadSource(generateUrl(idForSite));
		return parseJson(source);

	}

	private MoviePage parseJson(Source source) throws IOException {
		LOG.info("response from " + source.getUrl() + " type is :" + source.getContentType());

		JSONObject obj;
		try {
			obj = new JSONObject(source.getContent());
			return parseInfo(obj);
		} catch (JSONException e) {
			LOG.error("Parsing " + source.getContent() + " were failed:" + e.getMessage(), e);
			throw new IOException(e);
		}
	}

	private MoviePage parseInfo(JSONObject obj) throws JSONException {
		MoviePage mp = new MoviePage(MovieService.IMDB);
		mp.setIdForSite(obj.getString("ID"));
		mp.setTitle(obj.getString("Title"));
		mp.setYear(obj.optInt("Year"));
		mp.setPlot(obj.getString("Plot"));
		mp.setVotes(obj.optInt("Votes"));
		mp.setImgUrl(obj.getString("Poster"));
		mp.setScore((int) Math.round(obj.optDouble("Rating") * 10));
		for (String genre : obj.optString("Genre", "").split(",")) {
			mp.getGenres().add(genre);
		}
		for (String director : obj.optString("Director", "").split(",")) {
			mp.getDirectors().add(director);
		}
		for (String actor : obj.optString("Actors", "").split(",")) {
			mp.getActors().add(actor);
		}
		return mp;
	}

	private String generateUrl(String id) {
		return "http://www.imdbapi.com/?i=" + id + "&plot=full&r=JSON&tomatoes=true";
	}

	private String generateSearchUrl(String id) {
		return "http://www.imdbapi.com/?t=" + id + "&plot=full&r=JSON&tomatoes=true";
	}
	
}
