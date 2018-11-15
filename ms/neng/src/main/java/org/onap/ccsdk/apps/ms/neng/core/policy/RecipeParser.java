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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Parses recipe into a list of items.
 */
public class RecipeParser {

    /**
     * Parses recipe into a list of items.
     * 
     * @param policyParams    the policy parameters
     * @param recipe          the recipe from policy manager
     * @return                a list containing the items in the recipe
     * @throws Exception      all exceptions are propagated
     */
    private RecipeParser(){
        throw new IllegalStateException("Utility class");
    }
    public static List<String> parseRecipe(PolicyParameters policyParams, String recipe) throws Exception {
        String separatorAll = policyParams.getRecipeSeparator();
        if (separatorAll == null) {
            separatorAll = "|\":\",";
        }
        String[] separators = separatorAll.split("\"");
        List<String> recipeItems = new ArrayList<>();
        for (String separator : separators) {
            if (recipe.contains(separator)) {
                separator = "\\" + separator;
                recipeItems = Arrays.asList(recipe.split(separator));
                break;
            }
        }
        if (recipeItems.isEmpty() && recipe.length() > 0) {
            recipeItems.add(recipe);
        }
        return recipeItems;
    }
}
