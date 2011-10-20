package com.flicklib.service.cache;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.junit.Test;

import com.flicklib.service.HttpClientSourceLoader;
import com.flicklib.service.SourceLoader;
import com.flicklib.service.UrlConnectionResolver;

/**
 * Tests the output of different cache/resolver configurations
 * @author francisdb
 *
 */
public class CachesTest {
	
	private static final String TEST_CITY_ID = "3372";

	@Test
	public void test() throws IOException{
		String url = generateUrlForTitleSearch("Indiana Jones and the Kingdom of the Crystal Skull");
		SourceLoader empty = new HttpCacheSourceLoader(new UrlConnectionResolver(5000));
		SourceLoader empty2 = new HttpCacheSourceLoader(new HttpClientSourceLoader(5000));
		//HttpCache4J cache4j = new HttpCache4J();
		
		String emptyContent = empty.loadSource(url).getContent();
		String emptyContent2 = empty2.loadSource(url).getContent();
		//String cache4jContent = cache4j.get(url).getContent();
		
		assertEquals("two response should be the same", emptyContent, emptyContent2);
		//assertEquals(emptyContent2, cache4jContent);
	}
	
	protected String generateUrlForTitleSearch(String title) {
        try {
            return "http://port.hu/pls/ci/cinema.film_creator?i_text=" + URLEncoder.encode(title, "ISO-8859-2") + "&i_film_creator=1&i_city_id=" + TEST_CITY_ID;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding problem with UTF-8? " + e.getMessage(), e);
        }
    }
}
