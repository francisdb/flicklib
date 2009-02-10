import static org.junit.Assert.*;

import org.junit.Test;

import com.flicklib.service.HttpCache;
import com.flicklib.service.Source;


public class HttpCacheTest {

	@Test
	public void testHttpCache() {
		HttpCache cache = new HttpCache();
		Source source = new Source("JUnit", "blabla", "text/plain");
		cache.put("JUnit", source);
		assertEquals("blabla", cache.get("JUnit").getContent());
		assertEquals("text/plain", cache.get("JUnit").getContentType());
	}

}
