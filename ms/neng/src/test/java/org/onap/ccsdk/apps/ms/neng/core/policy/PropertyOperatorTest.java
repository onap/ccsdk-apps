/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Modifications Copyright (C) 2018 IBM.
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

package org.onap.ccsdk.apps.ms.neng.core.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PropertyOperatorTest {
    @Mock
    private PolicyParameters params = mock(PolicyParameters.class);

    @Test
    public void operationFunction() throws Exception {
        assertEquals(null, PropertyOperator.operationFunction(""));
        assertEquals(null, PropertyOperator.operationFunction("  "));

        assertEquals(null, PropertyOperator.operationFunction("  (("));

        assertEquals("to_lower_case", PropertyOperator.operationFunction("to_lower_case()"));
        assertEquals("to_lower_case", PropertyOperator.operationFunction("  to_lower_case() "));

        assertEquals("sub_str", PropertyOperator.operationFunction("sub_str(0,5)"));
        assertEquals("sub_str", PropertyOperator.operationFunction("\tsub_str(0,5)"));
    }

    @Test
    public void camelConverted() throws Exception {
        assertEquals("", PropertyOperator.camelConverted(""));

        assertEquals("toLowerCase", PropertyOperator.camelConverted("to_lower_case"));
    }

    @Test
    public void applyToLowerCase() throws Exception {
        Map<String, String> props = new HashMap<>();
        props.put("property-operation", "to_lower_case()");
        PropertyOperator op = new PropertyOperator();
        assertEquals("asdf", op.apply("ASDF", props, params, null));
    }

    @Test
    public void applyToUpperCase() throws Exception {
        Map<String, String> props = new HashMap<>();
        props.put("property-operation", "to_upper_case()");
        PropertyOperator op = new PropertyOperator();
        assertEquals("ASDF", op.apply("asdf", props, params, null));
    }
    
    @Test
    public void testApply() throws Exception {
        PropertyOperator op = new PropertyOperator();
        assertNull("ASDF", op.apply("asdf", "", params));
    }

    @Test
    public void applySubstr() throws Exception {
        when(params.mapFunction("sub_str")).thenReturn("substring");
        PropertyOperator op = new PropertyOperator();

        Map<String, String> props = new HashMap<>();

        props.put("property-operation", "sub_str(0,5)");
        assertEquals("01234", op.apply("0123456789", props, params, null));

        props.put("property-operation", "    sub_str(0,4)");
        assertEquals("0123", op.apply("0123456789", props, params, null));

        props.put("property-operation", "sub_str(1,5)");
        assertEquals("1234", op.apply("0123456789", props, params, null));

        props.put("property-operation", "sub_str(1)");
        assertEquals("0", op.apply("0", props, params, null));

        props.put("property-operation", "sub_str(-2)");
        assertEquals("89", op.apply("0123456789", props, params, null));

        props.put("property-operation", "sub_str(-3)");
        assertEquals("789", op.apply("0123456789", props, params, null));
    }
    
    @Test
    public void testApply_non_mapped() throws Exception {
        String operation = "to_upper_case";
        PolicyParameters policyParams = mock(PolicyParameters.class);
        when(policyParams.mapFunction("sub_str")).thenReturn("substring");
        PropertyOperator op = new PropertyOperator();
        String resp = op.apply("MyString", operation, policyParams);
        assertEquals("MYSTRING", resp);
    }
}
