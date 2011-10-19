package com.flicklib.service.cache;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.junit.Test;

import com.flicklib.service.HttpClientResponseResolver;
import com.flicklib.service.UrlConnectionResolver;

/**
 * Tests the output of different cache/resolver configurations
 * @author francisdb
 *
 */
public class CachesTest {
	
	private static final String TEST_CITY_ID = "3372";

	@Test
	public void test(){
		String url = generateUrlForTitleSearch("Indiana Jones and the Kingdom of the Crystal Skull");
		EmptyHttpCache empty = new EmptyHttpCache(new UrlConnectionResolver(5000));
		EmptyHttpCache empty2 = new EmptyHttpCache(new HttpClientResponseResolver(5000));
		//HttpCache4J cache4j = new HttpCache4J();
		
		String emptyContent = empty.get(url).getContent();
		String emptyContent2 = empty2.get(url).getContent();
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
