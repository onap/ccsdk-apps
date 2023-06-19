/*******************************************************************************
 * ============LICENSE_START====================================================
 * * org.onap.ccsdk
 * * ===========================================================================
 * * Copyright © 2023 AT&T Intellectual Property. All rights reserved.
 * * ===========================================================================
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 * *
 *  * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 * * ============LICENSE_END====================================================
 * *
 * *
 ******************************************************************************/

package org.onap.ccsdk.apps.cadi.test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.onap.ccsdk.apps.cadi.Capacitor;

import java.lang.reflect.*;

public class JU_Capacitor {
    private Capacitor cap;
    public final static String TEST_DATA =
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
            "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb" +
            "cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc" +
            "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
            "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
            "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";

    @Before
    public void setup() {
        cap = new Capacitor();
    }

    @Test
    public void singleByteTest() throws Exception {
        assertEquals(cap.read(), -1);
        cap.setForRead();
        Field curr_field = Capacitor.class.getDeclaredField("curr");
        curr_field.setAccessible(true);
        Field idx_field = Capacitor.class.getDeclaredField("idx");
        idx_field.setAccessible(true);
        assertNull(curr_field.get(cap));
        assertEquals(idx_field.get(cap), 0);

        for (int iter = 0; iter < 20; ++iter) {
            for (int i = 0; i < 20; ++i) {
                cap.put((byte)('a' + i));
            }
            cap.setForRead();
            byte[] array = new byte[20];
            for (int i = 0; i < 20; ++i) {
                array[i]=(byte)cap.read();
            }
            assertEquals("abcdefghijklmnopqrst", new String(array));
            assertEquals(-1, cap.read());

            cap.done();
        }

        for (int i = 0; i < 500; i++) {
            cap.put((byte)'a');
        }
        cap.setForRead();
        byte[] array = new byte[500];
        for (int i = 0; i < 500; ++i) {
            array[i]=(byte)cap.read();
        }
        assertEquals((new String(array)).length(), 500);
        assertEquals(-1, cap.read());
    }

    @Test
    public void availableTest() {
        assertEquals(cap.available(), 0);
        for (int i = 0; i < 100; ++i) {
            cap.put((byte)'a');
        }
        // The Capacitor can hold 256 bytes. After reading 100 bytes,
        // it should have 156 available
        assertEquals(cap.available(), 156);
    }

    @Test
    public void byteArrayTest() {
        byte[] arrayA = TEST_DATA.getBytes();
        assertEquals(cap.read(arrayA, 0, arrayA.length), -1);

        cap.put(arrayA, 0, arrayA.length);

        byte[] arrayB = new byte[arrayA.length];
        cap.setForRead();
        assertEquals(arrayA.length, cap.read(arrayB, 0, arrayB.length));
        assertEquals(TEST_DATA, new String(arrayB));
        assertEquals(-1, cap.read());
        cap.done();

        String b = "This is some content that we want to read";
        byte[] a = b.getBytes();
        byte[] c = new byte[b.length()]; // we want to use this to test reading offsets, etc

        for (int i = 0; i < a.length; i += 11) {
            cap.put(a, i, Math.min(11, a.length-i));
        }
        cap.reset();
        int read;
        for (int i = 0; i < c.length; i += read) {
            read = cap.read(c, i, Math.min(3, c.length-i));
        }
        assertEquals(b, new String(c));
    }

    @Test
    public void resetTest() throws Exception {
        cap.reset();
        Field curr_field = Capacitor.class.getDeclaredField("curr");
        curr_field.setAccessible(true);
        Field idx_field = Capacitor.class.getDeclaredField("idx");
        idx_field.setAccessible(true);
        assertNull(curr_field.get(cap));
        assertEquals(idx_field.get(cap), 0);

        cap.put((byte)'a');
        cap.reset();
        assertNotNull(curr_field.get(cap));
        assertEquals(idx_field.get(cap), 1);
    }

    @Test
    public void skipTest() throws Exception {
        // capacitor can't skip if nothing has been put into it
        assertEquals(cap.skip(10), 0);
        cap.put((byte)'a');
        // The Capacitor can hold 256 bytes. If we try  to skip 100 bytes,
        // it should only skip 1 byte, leaving 255 remaining
        assertEquals(cap.skip(100), 255);

        // Skipping 200 bytes leaves 0 remaining
        assertEquals(cap.skip(200), 0);
    }
}
