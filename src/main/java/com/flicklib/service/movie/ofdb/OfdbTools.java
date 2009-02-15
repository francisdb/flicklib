package com.flicklib.service.movie.ofdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flicklib.domain.MovieSearchResult;
import com.flicklib.domain.MovieType;

public class OfdbTools {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OfdbTools.class);

	private OfdbTools() {
		throw new UnsupportedOperationException("Utility class");
	}
	
	public static MovieType toMovieType(String ofdbType) {
		MovieType type = MovieType.MOVIE;
		if ("Kurzfilm".equals(ofdbType)) {
			type = MovieType.SHORT_FILM;
		} else if ("TV-Serie".equals(ofdbType)) {
			type = MovieType.TV_SERIES;
		} else if ("TV-Mini-Serie".equals(ofdbType)) {
			type = MovieType.MINI_SERIES;
		}
		// TODO handle more types?
		return type;
	}
	
	public static String handleType(String titleType, MovieSearchResult movieSearchResult){
		if(titleType.endsWith("]")){
        	String type = titleType.substring(titleType.lastIndexOf('[') + 1, titleType.lastIndexOf(']'));
        	movieSearchResult.setType(toMovieType(type));
        	titleType = titleType.substring(0, titleType.lastIndexOf('['));
        }else{
        	movieSearchResult.setType(MovieType.MOVIE);
    	}
		return titleType.trim();
	}
	
	public static String handleYear(String titleYear, MovieSearchResult movieSearchResult){
		if(titleYear.endsWith(")")){
			String year = titleYear.substring(titleYear.lastIndexOf('(') + 1, titleYear.lastIndexOf(')'));
			try{
				movieSearchResult.setYear(Integer.parseInt(year));
			}catch(NumberFormatException ex){
				LOGGER.warn("Could not parse year: "+ex.getMessage());
			}
			titleYear = titleYear.substring(0, titleYear.lastIndexOf('('));
		}
		return titleYear.trim();
	}
}
