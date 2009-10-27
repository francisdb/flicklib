/**
 * 
 */
package com.flicklib.service.cache;

import java.io.IOException;
import java.util.Map;

import com.flicklib.service.ResponseResolver;
import com.flicklib.service.Source;

final class MockResolver implements ResponseResolver {
	
	private int count = 0;
	
	@Override
	public Source get(String url) throws IOException {
		count++;
		Source source = new Source(url, "mock");
		return source;
	}

	@Override
	public Source post(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException {
		count++;
		Source source = new Source(url, "mock");
		return source;
	}
	
	public int getCallCount() {
		return count;
	}
}