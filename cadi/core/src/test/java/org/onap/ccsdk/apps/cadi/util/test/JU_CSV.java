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

package org.onap.ccsdk.apps.cadi.util.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.onap.ccsdk.apps.cadi.Access;
import org.onap.ccsdk.apps.cadi.CadiException;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.util.CSV;
import org.onap.ccsdk.apps.cadi.util.CSV.Visitor;
import org.onap.ccsdk.apps.cadi.util.CSV.Writer;
import org.onap.ccsdk.apps.cadi.util.Holder;

public class JU_CSV {

    private String filename;
    private File file;
    private static ArrayList<Object> expected;

    @Before
    public void start() {
        filename = "Sample.csv";
        file = new File(filename);
    }

    @After
    public void end() {
        if(file!=null) {
            file.delete();
        }
    }

    @BeforeClass
    public static void before() {
        expected = new ArrayList<>();
    }

    @Test
    public void test() throws IOException, CadiException {
        Access access = new PropAccess();
        CSV csv = new CSV(access,file);
        // Can't visit for file that doesn't exist
        try {
            csv.visit(new Visitor() {
                @Override
                public void visit(List<String> row) {
                }});
        } catch(IOException e) {
            Assert.assertTrue("CSV correctly created exception",true);
        }

        Writer writer = csv.writer();
        try {
            writer.row(add("\"hello\""));
            writer.comment("Ignore Comments");
            writer.row(add("dXNlcjpwYXNzd29yZA=="),add("dXNlckBzb21ldGhpbmcub3JnOm90aGVyUGFzc3dvcmQ="));
            writer.row(); // no output
            writer.row(add("There is, but one thing to say"), add(" and that is"), add("\"All the best\""));
        } finally {
            writer.close();
        }

        PrintStream garbage = new PrintStream(new FileOutputStream(file, true));
        try {
            garbage.println("# Ignore empty spaces, etc");
            garbage.println("   ");
            garbage.println("# Ignore blank lines");
            garbage.println();
        } finally {
            garbage.close();
        }


    ////////////
    // Tests
    ////////////
        final Holder<Integer> hi = new Holder<>(0);
        csv.visit(new CSV.Visitor() {
            @Override
            public void visit(List<String> row) {
                for(String s: row) {
//                    System.out.println(hi.value + ") " + s);
                    Assert.assertEquals(expected.get(hi.get()),s);
                    hi.set(hi.get()+1); // increment
                }
            }
        });

    }

    private String add(String s) {
        expected.add(s);
        return s;
    }

}
