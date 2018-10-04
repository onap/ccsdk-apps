/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 IBM.
 * ================================================================================
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
 * ============LICENSE_END=========================================================
 */

package org.onap.ccsdk.apps.ms.neng.core.resource.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class NameGenRequestTest {
    private NameGenRequest nameGenRequest;

    @Before
    public void setup() {
        nameGenRequest = new NameGenRequest();
    }

    @Test
    public void TestGetSetElements() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("el1", "el2");
        list.add(map);
        nameGenRequest.setElements(list);
        Assert.assertEquals(list, nameGenRequest.getElements());
    }

    @Test
    public void TestToStringFunction() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("el1", "el2");
        list.add(map);
        nameGenRequest.setElements(list);
        Assert.assertEquals("elements: [{el1=el2}]", nameGenRequest.toString());
    }

    @Test
    public void TestGetSetUseDb() {
        nameGenRequest.setUseDb(true);
        Assert.assertTrue(nameGenRequest.getUseDb());
    }
}
