/*
 * This file is part of Flicklib.
 *
 * Copyright (C) Zsombor Gegesy
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
