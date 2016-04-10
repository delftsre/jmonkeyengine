/*
 * Copyright (c) 2009-2015 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.util;

import java.util.Map.Entry;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Check if the {@link ListMap} class is working correctly.
 * 
 * @author Kirill Vainer
 */
public class ListMapTest {

    @Test
    public void testListMap() {
        ListMap<String, String> listMap = new ListMap<String, String>();
        listMap.put("bob", "hello");

        assertTrue("get value by key", "hello".equals(listMap.get("bob")));
        assertTrue("get and remove a value by key", "hello".equals(listMap.remove("bob")));
        assertTrue("size 0 after removing a single pair", listMap.size() == 0);
        assertTrue("is empty after removing a single pair", listMap.isEmpty());

        listMap.put("abc", "1");
        listMap.put("def", "2");
        listMap.put("ghi", "3");
        listMap.put("jkl", "4");
        listMap.put("mno", "5");
        assertTrue("get a value from a bigger list", "3".equals(listMap.get("ghi")));
        assertTrue("get listMap size", listMap.size() == 5);
        assertTrue("is not empty", !listMap.isEmpty());

        // check iteration order, should be consistent
        for (int i = 0; i < listMap.size(); i++) {
            String expectedValue = Integer.toString(i + 1);
            String key = listMap.getKey(i);
            String value = listMap.getValue(i);
            Entry<String, String> entry = listMap.getEntry(i);
            assertTrue("key index", key.equals(entry.getKey()));
            assertTrue("value index", value.equals(entry.getValue()));
            assertTrue("expected value", expectedValue.equals(value));
        }
    }
}
