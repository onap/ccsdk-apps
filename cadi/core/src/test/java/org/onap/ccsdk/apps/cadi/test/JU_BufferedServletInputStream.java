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

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.*;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.onap.ccsdk.apps.cadi.BufferedServletInputStream;

import static junit.framework.Assert.assertEquals;

public class JU_BufferedServletInputStream {
    private BufferedServletInputStream bsis;
    private String expected;

    @Before
    public void setup() throws FileNotFoundException {
        expected = new String("This is the expected output");
        bsis = new BufferedServletInputStream(new ByteArrayInputStream(expected.getBytes()));
    }

    @After
    public void tearDown() throws IOException {
        bsis.close();
    }

    @Test
    public void ByteReadNoMarkTest() throws Exception {
        int c;
        int i = 0;
        byte output[] = new byte[100];
        while ((c = bsis.read()) != -1) {
            output[i++] = (byte)c;
        }
        Assert.assertEquals(new String(output, 0, i), expected);
    }

    @Test
    public void ByteReadMarkTest() throws Exception {
        bsis.mark(0);
        int c;
        int i = 0;
        byte output[] = new byte[100];
        while ((c = bsis.read()) != -1) {
            output[i++] = (byte)c;
        }
        Assert.assertEquals(new String(output, 0, i), expected);
    }

    @Test
    public void ByteReadStateIsStoreTest() throws Exception {
        Field state_field = BufferedServletInputStream.class.getDeclaredField("state");
        state_field.setAccessible(true);
        bsis.mark(0);
        int c;
        int i = 0;
        byte output[] = new byte[100];
        while ((c = bsis.read()) != -1) {
            output[i++] = (byte)c;
        }
        bsis.reset();
        Assert.assertEquals(state_field.get(bsis), 2);  // state == READ
    }

    @Test
    public void ByteReadStateIsReadTest() throws Exception {
        bsis.mark(0);  // Initialize the capacitor
        boolean isReset = false;
        int c;
        int i = 0;
        byte output[] = new byte[100];
        while ((c = bsis.read()) != -1) {
            output[i++] = (byte)c;
            if ((i > 5) && !isReset) {
                // Close the capacitor and start over. This is done for coverage purposes
                i = 0;
                isReset = true;
                bsis.reset();  // Sets state to READ
            }
        }
        Assert.assertEquals(new String(output, 0, i), expected);
    }

    @Test
    public void ByteReadStateIsNoneTest() throws Exception {
        Field state_field = BufferedServletInputStream.class.getDeclaredField("state");
        state_field.setAccessible(true);
        bsis.mark(0);  // Initialize the capacitor
        int c;
        c = bsis.read();
        // Close the capacitor. This is done for coverage purposes
        bsis.reset();  // Sets state to READ
        state_field.setInt(bsis, 0);  // state == NONE
        c = bsis.read();
        Assert.assertEquals(c, -1);
    }

    @Test
    public void ByteArrayReadNoMarkTest() throws Exception {
        byte output[] = new byte[100];
        int count = bsis.read(output, 0, expected.length());
        Assert.assertEquals(new String(output, 0, count), expected);
        Assert.assertEquals(count, expected.length());
    }

    @Test
    public void ByteArrayReadTest() throws Exception {
        byte[] output = new byte[100];
        bsis.mark(0);
        bsis.read(output);
        Assert.assertEquals(new String(output, 0, expected.length()), expected);
    }

    @Test
    public void ByteArrayReadStateIsStoreTest() throws Exception {
        byte output[] = new byte[100];
        bsis.mark(0);
        int count = bsis.read(output, 0, expected.length());
        Assert.assertEquals(new String(output, 0, count), expected);
        Assert.assertEquals(count, expected.length());

        count = bsis.read(output, 0, 0);
        Assert.assertEquals(count, -1);
    }

    @Test
    public void ByteArrayReadStateIsReadTest() throws Exception {
        byte output[] = new byte[200];
        for (int i = 0; i < 2; ++i) {
            bsis.mark(0);
            bsis.read(output, 0, 100);
            Assert.assertEquals(new String(output, 0, expected.length()), expected);

            bsis.reset();
            bsis.read(output, 0, output.length);
            Assert.assertEquals(new String(output, 0, expected.length()), expected);
            bsis = new BufferedServletInputStream(new ByteArrayInputStream(output));
            if (i == 0) {
                output = new byte[200];
            }
        }

        Assert.assertEquals(new String(output, 0, expected.length()), expected);
    }

    @Test
    public void ByteArrayReadStateIsNoneTest() throws Exception {
        byte output[] = new byte[100];
        bsis.mark(0);

        Field state_field = BufferedServletInputStream.class.getDeclaredField("state");
        state_field.setAccessible(true);
        state_field.setInt(bsis, 0);  // state == NONE

        int count = bsis.read(output, 0, 100);
        Assert.assertEquals(count, -1);
    }

    @Test
    public void skipTest() throws Exception {
        byte output[] = new byte[100];
        bsis.mark(0);
        bsis.read(output, 0, 10);
        long count = bsis.skip(200);
        // skip returns the number left _before_ skipping. that number starts at 256
        Assert.assertEquals(count, 246);

        count = bsis.skip(200);
        Assert.assertEquals(count, 17);
    }

    @Test
    public void availableTest() throws Exception {
        int count = bsis.available();
        Assert.assertEquals(count, 27);
        bsis.mark(0);
        count = bsis.available();
        Assert.assertEquals(count, 27);
    }

    @Test
    public void bufferedTest() throws Exception {
        bsis.mark(0);
        Assert.assertEquals(bsis.buffered(), 0);
    }

    @Test
    public void closeTest() throws Exception {
        Field capacitor_field = BufferedServletInputStream.class.getDeclaredField("capacitor");
        capacitor_field.setAccessible(true);
        bsis.mark(0);
        Assert.assertNotNull(capacitor_field.get(bsis));
        bsis.close();
        Assert.assertNull(capacitor_field.get(bsis));
    }

    @Test
    public void markTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field state_field = BufferedServletInputStream.class.getDeclaredField("state");
        Field capacitor_field = BufferedServletInputStream.class.getDeclaredField("capacitor");
        capacitor_field.setAccessible(true);
        state_field.setAccessible(true);

        // capacitor is null initially
        Assert.assertNull(capacitor_field.get(bsis));

        state_field.setInt(bsis, 0);  // state == NONE
        bsis.mark(0);  // the value passed into mark is ignored
        Assert.assertNotNull(capacitor_field.get(bsis));
        Assert.assertEquals(state_field.get(bsis), 1);  // state == STORE

        state_field.setInt(bsis, 1);  // state == STORE
        bsis.mark(0);  // the value passed into mark is ignored
        Assert.assertEquals(state_field.get(bsis), 1);  // state == STORE

        state_field.setInt(bsis, 2);  // state == READ
        bsis.mark(0);  // the value passed into mark is ignored
        Assert.assertEquals(state_field.get(bsis), 1);  // state == STORE
    }

    @Test
    public void resetTest() throws Exception {
        Field state_field = BufferedServletInputStream.class.getDeclaredField("state");
        state_field.setAccessible(true);

        bsis.mark(0);
        Assert.assertEquals(state_field.get(bsis), 1);  // state == STORE
        bsis.reset();
        Assert.assertEquals(state_field.get(bsis), 2);  // state == READ
        bsis.reset();
        Assert.assertEquals(state_field.get(bsis), 2);  // state == READ

        state_field.setInt(bsis, -1);  // state is invalid
        bsis.reset();  // This call does nothing. It is for coverage alone
        Assert.assertEquals(state_field.get(bsis), -1);  // state doesn't change

        try {
            state_field.setInt(bsis, 0);  // state == NONE
            bsis.reset();
        } catch (IOException e) {
            Assert.assertEquals(e.getMessage(), "InputStream has not been marked");
        }
    }

    @Test
    public void markSupportedTest() {
        Assert.assertTrue(bsis.markSupported());
    }

    // "Bug" 4/22/2013
    // Some XML code expects Buffered InputStream can never return 0...  This isn't actually true, but we'll accommodate as far
    // as we can.
    // Here, we make sure we set and read the Buffered data, making sure the buffer is empty on the last test...
    @Test
    public void issue04_22_2013() throws IOException {
        String testString = "We want to read in and get out with a Buffered Stream seamlessly.";
        ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes());
        BufferedServletInputStream bsis = new BufferedServletInputStream(bais);
        try {
            bsis.mark(0);
            byte aa[] = new byte[testString.length()];  // 65 count... important for our test (divisible by 5);

            int read;
            for (int i=0;i<aa.length;i+=5) {
                read = bsis.read(aa, i, 5);
                assertEquals(5,read);
            }
            // System.out.println(new String(aa));

            bsis.reset();

            byte bb[] = new byte[aa.length];
            read = 0;
            for (int i=0;read>=0;i+=read) {
                read = bsis.read(bb,i,5);
                switch(i) {
                    case 65:
                        assertEquals(read,-1);
                        break;
                    default:
                        assertEquals(read,5);
                }
            }
            // System.out.println(new String(bb));
            assertEquals(testString,new String(aa));
            assertEquals(testString,new String(bb));

        } finally {
            bsis.close();
            bais.close();
        }

    }


}
