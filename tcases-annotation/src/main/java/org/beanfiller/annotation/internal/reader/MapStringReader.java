/* Copyright 2018 The Beanfiller Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package org.beanfiller.annotation.internal.reader;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * For Strings defining a Map&lt;String, String&gt;, defines a parser to read values
 */
public class MapStringReader {

    public static Map<String, String> parse(@Nonnull String[] havingStrings) {
        Map<String, String> flatMap = new HashMap<>();
        for (String havingString : havingStrings) {
            flatMap.putAll(parseHasValues(havingString));
        }
        return flatMap;
    }

    @Nonnull
    static Map<String, String> parseHasValues(@Nonnull String havingString) {
        Map<String, String> map = new HashMap<>();
        StringTokenizer tokenizer = new StringTokenizer(havingString, ",");
        // TODO: Deal with quoting and escaping for values with comma, allowed in Tcases?
        while (tokenizer.hasMoreTokens()) {
            String next = tokenizer.nextToken();
            int equalsPos = next.indexOf(':');
            if (equalsPos <= 0) {
                throw new IllegalStateException("Having expression must have a colon: '" + next + '\'');
            }
            String key = next.substring(0, equalsPos);
            String value = next.substring(equalsPos + 1);
            if (map.put(key, value) != null) {
                throw new IllegalStateException("Duplicate key: '" + key + '\'');
            }
        }
        return map;
    }
}
