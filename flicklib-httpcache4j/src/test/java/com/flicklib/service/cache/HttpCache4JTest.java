package com.flicklib.service.cache;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.flicklib.service.HttpCache;
import com.flicklib.service.Source;

public class HttpCache4JTest {

	@Test
	public void testHttpCache4J() {
		HttpCache cache = new HttpCache4J();
		
		long start = System.currentTimeMillis();
		
		Source result = cache.get("http://www.yahoo.com");
		assertNotNull(result);
		assertTrue(result.getContent().length() > 100);
		
		long between = System.currentTimeMillis();
		
		Source result2 = cache.get("http://www.yahoo.com");
		assertNotNull(result2);
		assertTrue(result2.getContent().length() > 100);
		
		long end = System.currentTimeMillis();
		
		System.out.println(between - start + " msec");
		System.out.println(end - between + " msec");
		
		// first call should be faster
		assertTrue("cached should be faster", end - between < between - start);
	}

}
