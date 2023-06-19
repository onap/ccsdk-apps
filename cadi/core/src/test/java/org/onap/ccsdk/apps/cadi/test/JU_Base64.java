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

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

import org.junit.Test;
import org.onap.ccsdk.apps.cadi.Symm;
import org.onap.ccsdk.apps.cadi.config.Config;

public class JU_Base64 {
    private static final String encoding = "Man is distinguished, not only by his reason, but by this singular " +
            "passion from other animals, which is a lust of the mind, that by a " +
            "perseverance of delight in the continued and indefatigable generation of " +
            "knowledge, exceeds the short vehemence of any carnal pleasure.";

    private static final String expected =
            "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz\n" +
            "IHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2Yg\n" +
            "dGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGlu\n" +
            "dWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRo\n" +
            "ZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=";

    @Test
    public void test() throws Exception {
        // Test with different Padding
        assertEncoded("leas",     "bGVhcw==");
        assertEncoded("leasu",    "bGVhc3U=");
        assertEncoded("leasur",   "bGVhc3Vy");
        assertEncoded("leasure",  "bGVhc3VyZQ==");
        assertEncoded("leasure.", "bGVhc3VyZS4=");

        // Test with line ends
        assertEncoded(encoding, expected);
    }

    @Test
    public void symmetric() throws IOException {
        String symmetric = new String(Symm.keygen());
        Symm bsym = Symm.obtain(symmetric);
        String result = bsym.encode(encoding);
        assertThat(bsym.decode(result), is(encoding));

        char[] manipulate = symmetric.toCharArray();
        int spot = new SecureRandom().nextInt(manipulate.length);
        manipulate[spot]|=0xFF;
        String newsymmetric = new String(manipulate);
        assertThat(symmetric, is(not(newsymmetric)));
        try {
            bsym = Symm.obtain(newsymmetric);
            result = bsym.decode(result);
            assertThat(result, is(encoding));
        } catch (IOException e) {
            // this is what we want to see if key wrong
        }
    }

    private void assertEncoded(String toEncode, String expected) throws IOException {
        String result = Symm.base64.encode(toEncode);
        assertThat(result, is(expected));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Symm.base64.decode(new ByteArrayInputStream(result.getBytes()), baos);
        result = baos.toString(Config.UTF_8);
        assertThat(result, is(toEncode));
    }

}
