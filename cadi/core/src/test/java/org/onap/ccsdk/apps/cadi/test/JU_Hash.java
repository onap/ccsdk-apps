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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.onap.ccsdk.apps.cadi.Hash;

public class JU_Hash {
    // Some common test vectors
    private String quickBrownFoxVector = "The quick brown fox jumps over the lazy dog";
    private String quickBrownFoxMD5 = "0x9e107d9d372bb6826bd81d3542a419d6";
    private String quickBrownFoxSHA256 = "0xd7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592";

    private String emptyVector = "";
    private String emptyMD5 = "0xd41d8cd98f00b204e9800998ecf8427e";
    private String emptySHA256 = "0xe3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";


    private byte[] same1 = "this is a twin".getBytes();
    private byte[] same2 = "this is a twin".getBytes();
    private byte[] different1 = "guvf vf n gjva".getBytes();
    private byte[] different2 = "this is an only child".getBytes();


    private String uppersDec = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String uppersHex1 = "0x4142434445464748494A4B4C4D4E4F505152535455565758595A";
    private String uppersHex2 = "0x4142434445464748494a4b4c4d4e4f505152535455565758595a";
    private String uppersHexNo0x1 = "4142434445464748494a4b4c4d4e4f505152535455565758595a";
    private String uppersHexNo0x2 = "4142434445464748494A4B4C4D4E4F505152535455565758595A";

    private String lowersDec = "abcdefghijklmnopqrstuvwxyz";
    private String lowersHex = "0x6162636465666768696a6b6c6d6e6f707172737475767778797a";
    private String lowersHexNo0x1 = "6162636465666768696a6b6c6d6e6f707172737475767778797a";
    private String lowersHexNo0x2 = "6162636465666768696A6B6C6D6E6F707172737475767778797A";

    private String numbersDec = "1234567890";
    private String numbersHex = "0x31323334353637383930";
    private String numbersHexNo0x = "31323334353637383930";
        
    @SuppressWarnings("unused")
    @BeforeClass
    public static void getCoverage() {
        // All of this class's methods are static, so we never need to instantiate an object.
        // That said, we can't get 100% coverage unless we instantiate one
        Hash hash = new Hash();
    }

    @Test
    public void hashMD5Test() throws Exception {
        byte[] output = Hash.hashMD5(quickBrownFoxVector.getBytes());
        assertEquals(quickBrownFoxMD5, new String(Hash.toHex(output)));

        output = Hash.hashMD5(emptyVector.getBytes());
        assertEquals(emptyMD5, new String(Hash.toHex(output)));
    }

    @Test
    public void hashMD5WithOffsetTest() throws Exception {
        byte[] output = Hash.hashMD5(quickBrownFoxVector.getBytes(), 0, quickBrownFoxVector.length());
        assertEquals(quickBrownFoxMD5, new String(Hash.toHex(output)));

        output = Hash.hashMD5(emptyVector.getBytes(), 0, emptyVector.length());
        assertEquals(emptyMD5, new String(Hash.toHex(output)));
    }

    @Test
    public void hashMD5AsStringHexTest() throws Exception {
        String output = Hash.hashMD5asStringHex(quickBrownFoxVector);
        assertEquals(quickBrownFoxMD5, output);

        output = Hash.hashMD5asStringHex(emptyVector);
        assertEquals(emptyMD5, output);
    }

    @Test
    public void hashSHA256Test() throws Exception {
        byte[] output = Hash.hashSHA256(quickBrownFoxVector.getBytes());
        assertEquals(quickBrownFoxSHA256, new String(Hash.toHex(output)));

        output = Hash.hashSHA256(emptyVector.getBytes());
        assertEquals(emptySHA256, new String(Hash.toHex(output)));
    }

    @Test
    public void hashSHA256WithOffsetTest() throws Exception {
        byte[] output = Hash.hashSHA256(quickBrownFoxVector.getBytes(), 0, quickBrownFoxVector.length());
        assertEquals(quickBrownFoxSHA256, new String(Hash.toHex(output)));

        output = Hash.hashSHA256(emptyVector.getBytes(), 0, emptyVector.length());
        assertEquals(emptySHA256, new String(Hash.toHex(output)));
    }

    @Test
    public void hashSHA256AsStringHexTest() throws Exception {
        String output = Hash.hashSHA256asStringHex(quickBrownFoxVector);
        assertEquals(quickBrownFoxSHA256, output);

        output = Hash.hashSHA256asStringHex(emptyVector);
        assertEquals(emptySHA256, output);
    }

    @Test
    public void hashSaltySHA256AsStringHexTest() throws Exception {
        String input = "password";
        String hash1 = Hash.hashSHA256asStringHex(input, 10);
        String hash2 = Hash.hashSHA256asStringHex(input, 10);
        String hash3 = Hash.hashSHA256asStringHex(input, 11);

        assertEquals(hash1, hash2);
        assertThat(hash1, not(equalTo(hash3)));
    }

    @Test
    public void isEqualTest() throws Exception {
        assertTrue(Hash.isEqual(same1, same2));
        assertFalse(Hash.isEqual(same1, different1));
        assertFalse(Hash.isEqual(same1, different2));
    }

    @Test
    public void compareToTest() throws Exception {
        assertEquals(0, Hash.compareTo(same1, same2));
        // different1 is rot13(same1), so the difference should be 13
        assertEquals(13, Hash.compareTo(same1, different1));
        assertEquals(-78, Hash.compareTo(same1, different2));
    }

    @Test
    public void toHexNo0xTest() throws Exception {
        assertEquals(uppersHexNo0x1, Hash.toHexNo0x(uppersDec.getBytes()));
        assertEquals(lowersHexNo0x1, Hash.toHexNo0x(lowersDec.getBytes()));
        assertEquals(numbersHexNo0x, Hash.toHexNo0x(numbersDec.getBytes()));
    }

    @Test
    public void toHexTest() throws Exception {
        assertEquals(uppersHex2, Hash.toHex(uppersDec.getBytes()));
        assertEquals(lowersHex, Hash.toHex(lowersDec.getBytes()));
        assertEquals(numbersHex, Hash.toHex(numbersDec.getBytes()));
    }

    @Test
    public void toHexWithOffset() throws Exception {
        assertEquals(uppersHex2, Hash.toHex(uppersDec.getBytes(), 0, uppersDec.length()));
        assertEquals(lowersHex, Hash.toHex(lowersDec.getBytes(), 0, lowersDec.length()));
        assertEquals(numbersHex, Hash.toHex(numbersDec.getBytes(), 0, numbersDec.length()));
    }

    @Test
    public void fromHexTest() throws Exception {
        assertEquals(uppersDec, new String(Hash.fromHex(uppersHex1)));
        assertEquals(lowersDec, new String(Hash.fromHex(lowersHex)));
        assertEquals(numbersDec, new String(Hash.fromHex(numbersHex)));

        // This string doesn't begin with "0x"
        assertNull(Hash.fromHex("0X65"));

            // This string has invalid hex characters
        assertNull(Hash.fromHex("0xQ"));
    }

    @Test
    public void fromHexNo0xTest() throws Exception {
        assertEquals(uppersDec, new String(Hash.fromHexNo0x(uppersHexNo0x1)));
        assertEquals(lowersDec, new String(Hash.fromHexNo0x(lowersHexNo0x1)));
        assertEquals(uppersDec, new String(Hash.fromHexNo0x(uppersHexNo0x2)));
        assertEquals(lowersDec, new String(Hash.fromHexNo0x(lowersHexNo0x2)));
        byte[] output = Hash.fromHexNo0x("ABC");
        assertEquals(new String(new byte[] {(byte)0x0A, (byte)0xBC}), new String(output));
        assertNull(Hash.fromHexNo0x("~~"));
    }
    
    @Test
    public void aaf_941() throws Exception {
        // User notes: From reported error "aaf" not coded right for odd digits
    	// Note:  In the original concept, this isn't a valid Hex digit.  It has to do with whether to assume an initial 
    	// char of "0" if left out.
    	
    	String sample = "aaf";
    	byte[] bytes = Hash.fromHexNo0x(sample);
    	String back = Hash.toHexNo0x(bytes);
    	// Note: We don't presume to know that someone left off leading 0 on start.
    	assertEquals("0aaf", back);
    	
    	sample = "0x0aaf";
    	bytes = Hash.fromHex(sample);
    	back = Hash.toHex(bytes);
    	assertEquals(sample, back);
    	
    	// Assumed leading zero.  Note, we ALWAYS translate back with leading zero.  
    	bytes = Hash.fromHex("0xaaf");
    	back = Hash.toHex(bytes);
    	assertEquals(sample, back);

    }
}
