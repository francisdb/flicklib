package com.flicklib.service;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.flicklib.service.cache.EmptyHttpCache;

public class SimpleHttpSourceLoaderTest {

	@Test
	public void testPost() throws IOException {
		SimpleHttpSourceLoader loader = new SimpleHttpSourceLoader();
		Map<String,String> headers = new HashMap<String, String>();
		Map<String,String> params = new HashMap<String, String>();
		params.put("username", "test");
		Source source = loader.post("http://www.piiix.com/login", params, headers);
		assertTrue(source.getContent(). startsWith("<!DOCTYPE"));
		System.out.println(source.getContent());
	}

	@Test
	public void testLoadSource() throws IOException {
		SimpleHttpSourceLoader loader = new SimpleHttpSourceLoader();
		Source source = loader.loadSource("http://www.google.com");
		assertTrue(source.getContent(). startsWith("<html>"));
	}
	
	@Test
	public void testLoadSourceStringBoolean() throws IOException {
		SimpleHttpSourceLoader loader = new SimpleHttpSourceLoader(new EmptyHttpCache());
		Source source = loader.loadSource("http://www.google.com/search?q=flicklib", true);
		assertTrue(source.getContent().length() > 0);
		//System.out.println(source.getContent());
	}

}
