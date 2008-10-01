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
package com.flicklib.tools;

import au.id.jericho.lib.html.Segment;
import au.id.jericho.lib.html.StartTag;
import au.id.jericho.lib.html.TextExtractor;

/**
 * @author francisdb
 *
 */
public class ElementOnlyTextExtractor extends TextExtractor {

    /**
     * Constructs a new ElementOnlyTextExtractor based on the specified segment
     * @param segment
     */
    public ElementOnlyTextExtractor(final Segment segment) {
        super(segment);
    }

    @Override
    public boolean excludeElement(StartTag startTag) {
        //LOGGER.debug(startTag.toString());
        return true;
    }
}
