package com.flicklib.service.movie;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.flicklib.service.AbstractSourceLoader;
import com.flicklib.service.HttpSourceLoader;
import com.flicklib.service.SimpleHttpSourceLoader;

@RunWith(value = Parameterized.class)
public class AlternateLiveTester {
	
	private static final int TIMEOUT = 30000;

    @SuppressWarnings("unchecked")
    @Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][] { { Boolean.TRUE, Boolean.TRUE }, { Boolean.TRUE, Boolean.FALSE }, { Boolean.FALSE, Boolean.FALSE } });
    }

    protected AbstractSourceLoader loader;

    public AlternateLiveTester(boolean internalHttpClient, boolean internalRedirects) {
        loader = internalHttpClient ? new SimpleHttpSourceLoader(internalRedirects, TIMEOUT) : new HttpSourceLoader(TIMEOUT, false);
    }

}
