/*******************************************************************************
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

package org.onap.ccsdk.apps.cadi.util.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.onap.ccsdk.apps.cadi.util.NetMask;

public class JU_NetMask {

    @Test
    public void deriveTest() {
        String test = "test";
        assertEquals(NetMask.derive(test.getBytes()), 0);
    }

    @Test
    public void deriveTest2() {
        String test = "1.2.3.4";
        assertEquals(NetMask.derive(test.getBytes()), 0);
    }

    @Test
    public void deriveTest3() {
        String test = "1.2.4";
        assertEquals(NetMask.derive(test.getBytes()), 0);
    }

    @Test
    public void deriveTest4() {
        String test = "1.3.4";
        assertEquals(NetMask.derive(test.getBytes()), 0);
    }

    @Test
    public void deriveTest5() {
        String test = "2.3.4";
        assertEquals(NetMask.derive(test.getBytes()), 0);
    }

    @Test
    public void deriveTest6() {
        String test = "3.4";
        assertEquals(NetMask.derive(test.getBytes()), 0);
    }
}
