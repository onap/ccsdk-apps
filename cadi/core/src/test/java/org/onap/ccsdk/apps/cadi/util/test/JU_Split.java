/*******************************************************************************
 * * org.onap.ccsdk
 * * ===========================================================================
 * * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
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

package org.onap.ccsdk.apps.cadi.util.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;

import org.onap.ccsdk.apps.cadi.util.Split;

public class JU_Split {

    @Test
    public void splitTest() {
        String[] output = Split.split('c', "ctestctc", 0, "ctestctc".length());
        assertThat(output.length, is(4));
        assertThat(output[0], is(""));
        assertThat(output[1], is("test"));
        assertThat(output[2], is("t"));
        assertThat(output[3], is(""));

        output = Split.split('c', "ctestctc", 0, 4);
        assertThat(output.length, is(2));
        assertThat(output[0], is(""));
        assertThat(output[1], is("tes"));

        output = Split.split('c', "test", 0, "test".length());
        assertThat(output.length, is(1));
        assertThat(output[0], is("test"));

        assertThat(Split.split('c', null, 0, 0).length, is(0));

        // Test with fewer arguments
        output = Split.split('c', "ctestctc");
        assertThat(output.length, is(4));
        assertThat(output[0], is(""));
        assertThat(output[1], is("test"));
        assertThat(output[2], is("t"));
        assertThat(output[3], is(""));
    }

    @Test
    public void splitTrimTest() {
        String[] output = Split.splitTrim('c', " cte stc ctc ", 0, " cte stc ctc ".length());
        assertThat(output.length, is(5));
        assertThat(output[0], is(""));
        assertThat(output[1], is("te st"));
        assertThat(output[2], is(""));
        assertThat(output[3], is("t"));
        assertThat(output[4], is(""));

        output = Split.splitTrim('c', " cte stc ctc ", 0, 5);
        assertThat(output.length, is(2));
        assertThat(output[0], is(""));
        assertThat(output[1], is("te"));

        assertThat(Split.splitTrim('c', " te st ", 0, " te st ".length())[0], is("te st"));

        assertThat(Split.splitTrim('c', null, 0, 0).length, is(0));

        // Test with 2 arguments
        output = Split.splitTrim('c', " cte stc ctc ");
        assertThat(output.length, is(5));
        assertThat(output[0], is(""));
        assertThat(output[1], is("te st"));
        assertThat(output[2], is(""));
        assertThat(output[3], is("t"));
        assertThat(output[4], is(""));

        // Tests with 1 argument
        output = Split.splitTrim('c', " cte stc ctc ", 1);
        assertThat(output.length, is(1));
        assertThat(output[0], is("cte stc ctc"));

        output = Split.splitTrim('c', "testctest2", 2);
        assertThat(output.length, is(2));
        assertThat(output[0], is("test"));
        assertThat(output[1], is("test2"));

        output = Split.splitTrim('c', " cte stc ctc ", 4);
        assertThat(output.length, is(4));
        assertThat(output[0], is(""));
        assertThat(output[1], is("te st"));
        assertThat(output[2], is(""));

        assertThat(Split.splitTrim('c', null, 0).length, is(0));
    }

    @Test
    public void coverageTest() {
        @SuppressWarnings("unused")
        Split split = new Split();
    }

}
