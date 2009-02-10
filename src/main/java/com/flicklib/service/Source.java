/*
 * This file is part of Flicklib.
 * 
 * Copyright (C) Francis De Brabandere
 * 
 * Movie Browser is free software; you can redistribute it and/or modify
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
package com.flicklib.service;

import java.io.Serializable;

public class Source implements Serializable{
	
	public static final long serialVersionUID = 1L;

    private final String url;
    private final String content;
    private final String contentType;
    
    /**
     * Creates a new Source
     * @param url
     * @param content
     * @param contentType 
     */
    public Source(final String url, final String content, final String contentType) {
    	this.url = url;
        this.content = content;
        this.contentType = contentType;
	}
    
    /**
     * Html source constructor
     * @param url
     * @param content
     */
    public Source(final String url, final String content) {
        this.url = url;
        this.content = content;
        this.contentType = "text/html";
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
    
	
}
