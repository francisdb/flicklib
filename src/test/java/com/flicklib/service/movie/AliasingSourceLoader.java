package com.flicklib.service.movie;
/*
 * This file is part of Flicklib.
 * 
 * Copyright (C) Zsombor Gegesy
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flicklib.service.SourceLoader;

public class AliasingSourceLoader implements SourceLoader {

    final static Logger LOG = LoggerFactory.getLogger(AliasingSourceLoader.class);

    SourceLoader parent;
    Map<String, String> mapping = new HashMap<String, String>();

    public AliasingSourceLoader(SourceLoader parent) {
        super();
        this.parent = parent;
    }

    public void putAlias(String from, String to) {
        mapping.put(from, to);
    }

    @Override
    public String load(String url) throws IOException {
        String result = mapping.get(url);
        if (result != null) {
            LOG.info("loading " + url + " from " + result);
            url = result;
        } else {
            LOG.info("loading " + url);
        }
        return parent.load(url);
    }
}
