package com.flicklib.service.movie.blippr;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.flicklib.api.MovieInfoFetcher;
import com.flicklib.domain.MoviePage;
import com.flicklib.domain.MovieSearchResult;
import com.flicklib.service.Source;
import com.flicklib.service.SourceLoader;

public class BlipprInfoFetcher implements MovieInfoFetcher {
	
	private static final String ROOT = "http://api.blippr.com/v2/";
	
	private final SourceLoader sourceLoader;
	
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
		URL url = new URL(ROOT+"titles/"+idForSite+".json");
		Source source = sourceLoader.loadSource(url.toExternalForm());
		System.out.println(url);
		System.out.println(source.getContentType());
		System.out.println(source.getContent());
		MoviePage page = new MoviePage();
		try {
			JSONObject jsonObject = new JSONObject(source.getContent());
			JSONObject node = jsonObject.getJSONObject("nodes").getJSONObject("node");
			resultToMovie(page, node);
			page.setScore(node.getInt("score"));
			// TODO load metadata
		} catch (JSONException e) {
			throw new IOException(e.getMessage(), e);
		}
		return page;
	}

	@Override
	public List<MovieSearchResult> search(String title) throws IOException {
		URL url = new URL(ROOT+"search.json?media_type=movies&term="+URLEncoder.encode(title));
		Source source = sourceLoader.loadSource(url.toExternalForm());
		System.out.println(url);
		System.out.println(source.getContentType());
		System.out.println(source.getContent());
		
		List<MovieSearchResult> results = new ArrayList<MovieSearchResult>();
		try {
			JSONObject jsonObject = new JSONObject(source.getContent());
			JSONObject search = jsonObject.getJSONObject("search");
			long total = search.getJSONObject("query").getLong("total_results");
			System.out.println("total: "+total);
			JSONArray array = search.getJSONObject("results").getJSONArray("result");
			for(int i=0;i<array.length();i++){
				JSONObject result = array.getJSONObject(i);
				MovieSearchResult movieSearchResult = new MovieSearchResult();
				resultToMovie(movieSearchResult, result);
				results.add(movieSearchResult);				
			}
			
		} catch (JSONException e) {
			throw new IOException(e.getMessage(), e);
		}
		return results;
	}
	
	private void resultToMovie(MovieSearchResult movieSearchResult, JSONObject result) throws JSONException{
		movieSearchResult.setIdForSite(result.getString("id"));
		movieSearchResult.setTitle(result.getString("title"));
		if(result.has("summary")){
			movieSearchResult.setDescription(result.getString("summary"));
		}
	}

	@Override
	public List<? extends MovieSearchResult> search(String title, String year) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
