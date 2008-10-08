/**
 * 
 */
package com.flicklib.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * Demo implementation of netflix auth
 * 
 * @author francisdb
 */
public class NetFlixAuthModule extends AbstractModule {
	
	private String apikey;
	private String sharedsecret;
	
	public NetFlixAuthModule(final String apikey, final String sharedsecret) {
		this.apikey = apikey;
		this.sharedsecret = sharedsecret;
	}

	/* (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named("netflix.key")).to(apikey);
		bindConstant().annotatedWith(Names.named("netflix.secret")).to(sharedsecret);
	}

}
