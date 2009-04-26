package com.flicklib.service.movie;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.flicklib.service.AbstractSourceLoader;
import com.flicklib.service.HttpSourceLoader;
import com.flicklib.service.SimpleHttpSourceLoader;

@RunWith(value=Parameterized.class)
public class AlternateLiveTester {

	@SuppressWarnings("unchecked")
	@Parameters
    public static Collection data() {
    	return Arrays.asList(new Object[] [] { { Boolean.TRUE }, {Boolean.FALSE}});
    }

	protected AbstractSourceLoader loader;
	
    public AlternateLiveTester (boolean internalHttpClient) {
        loader = internalHttpClient ? new SimpleHttpSourceLoader() : 
        	new HttpSourceLoader(60000, false);
    }


}
