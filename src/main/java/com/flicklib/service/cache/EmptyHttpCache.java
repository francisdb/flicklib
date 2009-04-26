/**
 * 
 */
package com.flicklib.service.cache;

import com.flicklib.service.HttpCache;
import com.flicklib.service.Source;

/**
 * 
 * @author zsombor
 *
 */
public class EmptyHttpCache implements HttpCache {

	/* (non-Javadoc)
	 * @see com.flicklib.service.HttpCache#get(java.lang.String)
	 */
	@Override
	public Source get(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.flicklib.service.HttpCache#put(java.lang.String, com.flicklib.service.Source)
	 */
	@Override
	public void put(String url, Source page) {
		// TODO Auto-generated method stub

	}

}
