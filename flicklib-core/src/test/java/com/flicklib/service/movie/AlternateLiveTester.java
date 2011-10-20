package com.flicklib.service.movie;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.flicklib.service.HttpClientSourceLoader;
import com.flicklib.service.SourceLoader;
import com.flicklib.service.UrlConnectionResolver;
import com.flicklib.service.cache.HttpCacheSourceLoader;

@RunWith(value = Parameterized.class)
public abstract class AlternateLiveTester {
	
	private static final int TIMEOUT = 30000;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { 
        		{ Boolean.TRUE, Boolean.TRUE }, 
        		//{ Boolean.TRUE, Boolean.FALSE }, 
        		{ Boolean.FALSE, Boolean.FALSE } });
    }

    protected SourceLoader loader;

    public AlternateLiveTester(boolean internalHttpClient, boolean internalRedirects) {
    	SourceLoader cache = null;
    	if(internalHttpClient){
    		//if(internalRedirects){
    		cache = new UrlConnectionResolver(TIMEOUT);
    	}else{
    		cache = new HttpClientSourceLoader(TIMEOUT);
    	}
    	loader = new HttpCacheSourceLoader(cache);
    }

}
