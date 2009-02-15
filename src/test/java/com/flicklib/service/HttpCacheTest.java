package com.flicklib.service;
/*
 * This file is part of Flicklib.
 *
 * Copyright (C) Francis De Brabandere
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import static org.junit.Assert.*;

import org.junit.Test;


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
