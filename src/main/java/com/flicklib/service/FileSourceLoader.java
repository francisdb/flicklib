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
package com.flicklib.service;

import com.flicklib.tools.IOTools;
import java.io.IOException;
import java.io.InputStream;

/**
 * Loads a page source file from the class path
 * @author francisdb
 */
public class FileSourceLoader implements SourceLoader {

    @Override
    public String load(String url) throws IOException {
        return getOrPost(url);
    }
    
    private String getOrPost(String url) throws IOException {
        String source = null;
        InputStream fis = null;
        try {
            fis = FileSourceLoader.class.getClassLoader().getResourceAsStream(url);
            source = IOTools.inputSreamToString(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return source;
    }
}
