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

import java.io.IOException;
import java.util.Map;

/**
 * Abstract base class for various source loaders.
 * 
 * @author zsombor
 *
 */
public abstract class AbstractSourceLoader implements SourceLoader {


	/** {@inheritDoc} */
    @Override
    public Source loadSource(String url) throws IOException {
        return loadSource(url, true);
    }
    
    /** {@inheritDoc} */
    @Override
    public Source post(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException {
        return loadSource(url, true);
    }

}
