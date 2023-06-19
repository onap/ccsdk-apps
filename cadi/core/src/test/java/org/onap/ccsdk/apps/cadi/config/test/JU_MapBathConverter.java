/**
 * ============LICENSE_START====================================================
 * org.onap.ccsdk
 * ===========================================================================
 * Copyright (c) 2023 AT&T Intellectual Property. All rights reserved.
 * ===========================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END====================================================
 */

package org.onap.ccsdk.apps.cadi.config.test;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.onap.ccsdk.apps.cadi.Access;
import org.onap.ccsdk.apps.cadi.CadiException;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.Symm;
import org.onap.ccsdk.apps.cadi.filter.MapBathConverter;
import org.onap.ccsdk.apps.cadi.util.CSV;
import org.onap.ccsdk.apps.cadi.util.CSV.Visitor;
import org.onap.ccsdk.apps.cadi.util.CSV.Writer;

import junit.framework.Assert;

/**
 * Test a simple Migration conversion tool for CADI
 *
 * @author Instrumental(Jonathan)
 *
 */
public class JU_MapBathConverter {
    private static final String NEW_USER_SOMETHING_ORG = "NEW_USER@Something.org";
    private static final String OLD_ID = "OLD_ID";
    private static final String SHARED_PASS = "SHARED_PASS";
    private static CSV csv;
    private static ArrayList<String> expected;
    private static final Access access = new PropAccess();
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeClass
    public static void createFile() throws IOException {
        // Note, you cate a "MapBathConverter" by access to a File.
        // We will create that file now.  Local is fine.
        csv = new CSV(access,"JU_MapBathConverter.csv");
    }

    @BeforeClass
    public static void beforeClass() {
        expected = new ArrayList<>();
    }

    @Before
    public void before() {
        expected.clear();
    }

    @Test
    public void test() throws IOException, CadiException {
        CSV.Writer cw = csv.writer();
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(GregorianCalendar.MONTH, 6);
        try {
            try {
                // CSV can simply be OLD ID and NEW,  no passwords
                cw.row(exp(OLD_ID), exp(NEW_USER_SOMETHING_ORG),sdf.format(gc.getTime()));

                // Style 1 - Incoming ID/pass, create new cred with NweID and same Pass
                cw.row(exp(bath(OLD_ID,SHARED_PASS)), exp(NEW_USER_SOMETHING_ORG),sdf.format(gc.getTime()));
                // the response should be Basic with NEW_ID and OLD_PASS

                // Style 2
                cw.row(exp(bath(OLD_ID,"OLD_PASS")), exp(bath(NEW_USER_SOMETHING_ORG,"NEW_PASS")),sdf.format(gc.getTime()));

            } finally {
                cw.close();
            }

            final Iterator<String> exp = expected.iterator();
            csv.visit(new Visitor() {
                @Override
                public void visit(List<String> row) {
                    int i=0;
                    for(String s : row) {
                        switch(i++) {
                            case 0:
                            case 1:
                                Assert.assertEquals(exp.next(), s);
                                break;
                            case 2:
                                try {
                                    Date.valueOf(s);
                                } catch (Exception e) {
                                    Assert.assertTrue("Last entry should be a date",false);
                                }
                                break;
                            default:
                                Assert.fail("There should only be 3 columns in this test case.");
                        }
                    }
                }
            });

            MapBathConverter mbc = new MapBathConverter(access, csv);

            // Check no lookup just returns the same
            Assert.assertEquals("NonKey", "NonKey"); // if not in map, expect same value

            Iterator<String> exp1 = expected.iterator();
            // there's no passwords in CSV
            String old = exp1.next();
            String nw = exp1.next();
            Assert.assertEquals(nw, mbc.convert(access,old));

            Assert.assertEquals(bath(NEW_USER_SOMETHING_ORG,SHARED_PASS), mbc.convert(access,bath(OLD_ID,SHARED_PASS)));

            // Style 1 (new cred, old password)
            old = exp1.next();
            nw = bath(exp1.next(),SHARED_PASS);
            Assert.assertEquals(nw, mbc.convert(access,old));

            // Style 2
            old = exp1.next();
            nw = exp1.next();
            Assert.assertEquals(nw, mbc.convert(access,old));

        } finally {
            csv.delete();
        }
    }

    @Test
    public void testInsecureRole() throws IOException {
        CSV.Writer cw = csv.writer();
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(GregorianCalendar.MONTH, 6);
        try {
            try {
                // Invalid Scenario - Non Authenticated ID to authenticated User
                cw.row(exp(OLD_ID), exp(bath(NEW_USER_SOMETHING_ORG,"NEW_PASS")),sdf.format(gc.getTime()));

            } finally {
                cw.close();
            }

            try {
                new MapBathConverter(access, csv);
                Assert.fail("Invalid Data should throw Exception");
            } catch (CadiException e) {
                Assert.assertTrue("Invalid Data should throw Exception",true);
            }

        } finally {
            csv.delete();
        }
    }

    @Test
    public void testTooFewColumns() throws IOException, CadiException {
        CSV.Writer cw = csv.writer();
        try {
            try {
                cw.row(exp(bath(OLD_ID,"OLD_PASS")), exp(bath(NEW_USER_SOMETHING_ORG,"NEW_PASS")));
            } finally {
                cw.close();
            }

            try {
                new MapBathConverter(access, csv);
                Assert.fail("file with too few rows should throw exception");
            } catch(CadiException | IOException e) {
                Assert.assertTrue("Correctly thrown Exception",true);
            }
        } finally {
            csv.delete();
        }
    }

    @Test
    public void testNoFile() {
        try {
            new MapBathConverter(access, new CSV(access,"Bogus"));
            Assert.fail("Non Existent File should throw exception");
        } catch(CadiException | IOException e) {
            Assert.assertTrue("Correctly thrown Exception",true);
        }
    }

    @Test
    public void testBadRows() throws IOException {
        try {
            Writer cw = csv.writer();
            try {
                cw.row("Single Column");
            } finally {
                cw.close();
            }

            try {
                new MapBathConverter(access,csv);
                Assert.fail("Non Existent File should throw exception");
            } catch(CadiException | IOException e) {
                Assert.assertTrue("Correctly thrown Exception",true);
            }
        } finally {
            csv.delete();
        }

        // Check for deletion
        Assert.assertFalse(csv.toString() + "should have been deleted",new File(csv.toString()).exists());
    }

    private String bath(String user, String password) throws IOException {
        StringBuilder sb = new StringBuilder(user);
        sb.append(':');
        sb.append(password);
        byte[] encoded = Symm.base64noSplit.encode(sb.toString().getBytes());
        sb.setLength(0);
        sb.append("Basic ");
        sb.append(new String(encoded));
        return sb.toString();
    }

    private String exp(String s) {
        expected.add(s);
        return s;
    }
}
