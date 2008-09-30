/*
 * This file is part of Flicklib.
 * 
 * Copyright (C) Francis De Brabandere
 * 
 * Flicklib is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Movie Browser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.flicklib.service.sub;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flicklib.api.SubtitlesLoader;
import com.flicklib.domain.Subtitle;
import com.flicklib.service.HttpSourceLoader;

/**
 *
 * @author francisdb
 */
public class OpenSubtitlesLoaderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenSubtitlesLoaderTest.class);

    /**
     * Test of search method, of class OpenSubtitlesLoader.
     * @throws Exception 
     */
    @Test
    @Ignore
    public void testSearch() throws Exception {
        SubtitlesLoader loader = new OpenSubtitlesLoader(new HttpSourceLoader(null));
        Set<Subtitle> result = loader.search("The Science of Sleep", null);
        assertTrue(result.size() > 0);
        for(Subtitle sub:result){
            LOGGER.info(sub.getFileName());
        }
        result = loader.search("The.Science.of.Sleep.LIMITED.DVDRip.XViD.-iMBT.avi", null);
        assertTrue(result.size() > 0);
        for(Subtitle sub:result){
            LOGGER.info(sub.getFileName());
        }
    }

}