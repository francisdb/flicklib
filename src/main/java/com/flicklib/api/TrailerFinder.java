/*
 * This file is part of Flicklib.
 *
 * Copyright (C) Francis De Brabandere
 *
 * Licensed under the GNU Lesser General Public License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flicklib.api;

/**
 * Finds the url for the trailer site/page
 * @author francisdb
 */
public interface TrailerFinder {

    /**
     * Finds a trailer for this title or id
     * @param title
     * @param localId
     * @return the url String for this title
     */
    String findTrailerUrl(String title, String localId);

}
