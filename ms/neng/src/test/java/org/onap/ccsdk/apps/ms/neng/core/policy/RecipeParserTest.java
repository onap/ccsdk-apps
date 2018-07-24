/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
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
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RecipeParserTest {
    @Mock
    private PolicyParameters params = mock(PolicyParameters.class);

    @Test
    public void parseRecipe() throws Exception {
        assertEquals(0, RecipeParser.parseRecipe(params, "").size());
        assertEquals("[]", RecipeParser.parseRecipe(params, "").toString());

        assertEquals(1, RecipeParser.parseRecipe(params, "a").size());
        assertEquals("[a]", RecipeParser.parseRecipe(params, "a").toString());

        assertEquals(2, RecipeParser.parseRecipe(params, "a,b").size());
        assertEquals(2, RecipeParser.parseRecipe(params, "a|b").size());
        assertEquals(2, RecipeParser.parseRecipe(params, "a:b").size());

        assertEquals("[a, b]", RecipeParser.parseRecipe(params, "a,b").toString());
        assertEquals("[a, b]", RecipeParser.parseRecipe(params, "a|b").toString());
        assertEquals("[a, b]", RecipeParser.parseRecipe(params, "a:b").toString());

        assertEquals(3, RecipeParser.parseRecipe(params, "a,b,c").size());
        assertEquals(3, RecipeParser.parseRecipe(params, "a|b|c").size());
        assertEquals(3, RecipeParser.parseRecipe(params, "a:b:c").size());

        assertEquals("[a, b, c]", RecipeParser.parseRecipe(params, "a,b,c").toString());
        assertEquals("[a, b, c]", RecipeParser.parseRecipe(params, "a|b|c").toString());
        assertEquals("[a, b, c]", RecipeParser.parseRecipe(params, "a:b:c").toString());
    }
}
