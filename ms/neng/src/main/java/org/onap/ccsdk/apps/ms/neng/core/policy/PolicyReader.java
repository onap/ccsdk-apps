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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Converts policy data to the structure expected by this micro-service.
 */
public abstract class PolicyReader implements PolicyFinder {
    /**
     * Extracts the naming-models from a policy.
     * 
     * @param policy    the policy
     * @return          the list of naming-models found in the policy
     */
    private static final String NAMING_MODELS = "naming-models";
    private static final String NAMING_TYPE = "naming-type";

    public static List<Map<String, ?>> namingModels(Map<String, ?> policy) {

        Set<String> keys = policy.keySet();
        // TODO : retrieve naming-models at any level
        if (keys.contains("config")) {
            @SuppressWarnings("unchecked")
            Map<String, ?> config = (Map<String, ?>) policy.get("config");
            List<Map<String, ?>> namingModels = list(map(config, "content"), NAMING_MODELS);
            if (namingModels == null) {
                namingModels = list(config, NAMING_MODELS);
            }
            return namingModels;
        } else {
            return list(map(map(policy, "input"), "naming-model"), NAMING_MODELS);
        }

    }

    /**
     * Extracts a naming-model from the given list of models, based on a naming-type.
     * 
     * @param namingModels    a list of naming-models
     * @param namingType      the naming type
     * @return                the naming model found, or null if none is found
     */
    public static Map<String, ?> namingModel(List<Map<String, ?>> namingModels, String namingType) {
        Map<String, ?> theModel = null;
        if (namingModels != null) {
            for (Map<String, ?> model : namingModels) {
                Object val = model.get(NAMING_TYPE);
                if (namingType.equals(val)) {
                    theModel = model;
                    break;
                }
            }
        }
        return theModel;
    }
    
    /**
     * Extracts a naming-model from the given list of models, based on a naming-type, by using a more
     * relaxed/liberal comparison of the naming-type.
     * 
     * <p/>When doing the relaxed comparison, upper-case/lower-case differences are ignored, some
     * special characters like underscore, dash etc. are ignored, and the presence of the 'NAME'
     * at the end of the naming-type is ignored.
     * 
     * @param namingModels    a list of naming-models
     * @param namingType      the naming type
     * @return                the naming model found, or null if none is found
     */
    public static Map<String, ?> namingModelRelaxed(List<Map<String, ?>> namingModels, String namingType) {
        Map<String, ?> theModel = null;
        if (namingModels != null) {
            for (Map<String, ?> model : namingModels) {
                Object val = model.get(NAMING_TYPE);
                if (val != null) {
                    if (namingType.equalsIgnoreCase(val.toString())) {
                        theModel = model;
                        break;
                    }
                }
            }
            if (theModel == null) {
                namingType = relaxedNamingType(namingType);
                for (Map<String, ?> model : namingModels) {
                    Object val = model.get(NAMING_TYPE);
                    if (val != null) {
                        String relaxedVal = relaxedNamingType(val.toString());
                        if (namingType.equalsIgnoreCase(relaxedVal)) {
                            theModel = model;
                            break;
                        }
                    }
                }
            }
        }
        return theModel;
    }

    /**
     * Finds the naming-operation from the given naming-model. 
     */
    public static String namingOperation(Map<String, ?> namimgModel) {
        return value(namimgModel, "name-operation");
    }

    /**
     * Finds the naming-property with a given name from a naming-model.
     *  
     * @param namingModel    a naming-model
     * @param propertyName   the property name
     * @return               the property found, or null if none is found
     */
    public static Map<String, ?> namingProperty(Map<String, ?> namingModel, String propertyName) {
        List<Map<String, ?>> properties = namingProperties(namingModel);
        Map<String, ?> theProp = null;
        if (properties != null) {
            for (Map<String, ?> prop : properties) {
                String onePropName = propertyName(prop);
                if (propertyName.equals(onePropName)) {
                    theProp = prop;
                    break;
                }
            }
        }
        return theProp;
    }

    /**
     * Finds the naming-recipe from the given naming-model. 
     */
    public static String namingRecipe(Map<String, ?> namimgModel) {
        return value(namimgModel, "naming-recipe");
    }

    /**
     * Finds the naming-type from the given naming-model. 
     */
    public static String namingType(Map<String, ?> namimgModelOrElement) {
        return value(namimgModelOrElement, NAMING_TYPE);
    }

    /**
     * Finds the property-value from the given property map. 
     */
    public static String propertyValue(Map<String, ?> properties) {
        String value  = value(properties, "property-value");
        if (value != null && !Pattern.matches("\\$\\{.*\\}.*", value)) {
            return value;
        }
        return null;
    }

    /**
     * Converts the a naming-type to a relaxed/liberal convention.
     * 
     * <p/>In the relaxed convention, all characters are upper-case, some special characters like underscore, 
     * dash etc. are ignored, and there is no 'NAME' at the end of the naming-type.
     * 
     *  @param type    the naming-type
     */
    public static String relaxedNamingType(String type) {
        if (type == null) {
            return type;
        }
        type = type.toUpperCase();
        if (type.endsWith("NAME")) {
            type = type.substring(0, type.length() - 4);
        }
        type = type.replaceAll("-", "");
        type = type.replaceAll("_", "");
        return type;
    }

    /**
     * Builds a PolicySequence based on given properties. 
     */
    public static PolicySequence seq(Map<String, ?> properties) {
        properties = map(properties, "increment-sequence");
        String scope = value(properties, "scope");
        String startValue = value(properties, "start-value");
        String max = value(properties, "max");
        String increment = value(properties, "increment");
        String type = value(properties, "sequence-type");
        String length = value(properties, "length");

        PolicySequence seq = new PolicySequence();
        seq.setScope(scope);
        seq.setStartValue(number(startValue, 1));
        seq.setMaxValueString(max);
        seq.setIncrement(number(increment, 1));
        seq.setType(type);
        seq.setLength(number(length, 3));

        return seq;
    }
    
    /**
     * Finds the value of a given key, as a String, in a map.
     * 
     * @param map    a map, which can be null
     * @param key    a key
     * @return       the value of the key in the map, or null if there is none.
     */
    public static String value(Map<String, ?> map, String key) {
        String value = null;
        if (map != null) {
            value = (String) map.get(key);
            if (!(value instanceof String)) {
                value = null;
            }
            if (value != null && !Pattern.matches("\\$\\{.*\\}.*", value)) {
                return value;
            } else {
                value = null;
            }
        }
        return value;
    }

    /**
     * Finds the value of a given key, as a String, in a map.
     * 
     * @param map      a map, which can be null
     * @param key      a key
     * @param relaxed  if true, relaxed/liberal comparison of the keys is done, by ignoring special
     *     characters dash and underscore
     * @return         the value of the key in the map, or null if there is none.
     */
    public static String value(Map<String, ?> map, String key, boolean relaxed) {
        if (relaxed) {
            String value = null;
            if (map != null) {
                for (final String aKey : map.keySet()) {
                    if (aKey.equalsIgnoreCase(key)) {
                        value = (String) map.get(aKey);
                        break;
                    }
                }
                if (value == null) {
                    key = key.replaceAll("_", "");
                    key = key.replaceAll("-", "");
                    for (final String aKey : map.keySet()) {
                        String keyDup = aKey.replaceAll("_", "");
                        keyDup = keyDup.replaceAll("-", "");
                        if (keyDup.equalsIgnoreCase(key)) {
                            value = (String) map.get(aKey);
                            break;
                        }
                    }
                }
                if (!(value instanceof String)) {
                    value = null;
                }
                if (value != null && !Pattern.matches("\\$\\{.*\\}.*", value)) {
                    return value;
                } else {
                    value = null;
                }
            }
            return value;
        } else {
            return value(map, key);
        }
    }

    static Map<String, ?> dependentNamingModel(List<Map<String, ?>> namingModels, String recipeItem) {
        Map<String, ?> theModel = namingModel(namingModels, recipeItem);
        if (theModel == null) {
            theModel = namingModelRelaxed(namingModels, recipeItem);
        }
        return theModel;
    }

    Map<String, Object> getPolicy(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
    }
    
    static List<Map<String, ?>> list(Map<String, ?> policy, String name) {
        List<Map<String, ?>> list = null;
        if (policy != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, ?>> listObj = (List<Map<String, ?>>) policy.get(name);
            if (listObj instanceof List<?>) {
                list = listObj;
            }
        }
        return list;
    }
    
    static Map<String, ?> map(Map<String, ?> policy, String name) {
        Map<String, ?> map = null;
        if (policy != null) {
            @SuppressWarnings("unchecked")
            Map<String, ?> mapObj = (Map<String, ?>) policy.get(name);
            if (mapObj instanceof Map<?, ?>) {
                map = mapObj;
            }
        }
        return map;
    }

    static List<Map<String, ?>> namingProperties(Map<String, ?> namimgModel) {
        return list(namimgModel, "naming-properties");
    }
    
    static long number(String str, long defaultValue) {
        long value;
        try {
            value = Long.valueOf(str);
        } catch (Exception e) {
            value = defaultValue;
        }
        return value;
    }

    static String propertyName(Map<String, ?> properties) {
        return value(properties, "property-name");
    }

    static String propertyOperation(Map<String, ?> properties) {
        return value(properties, "property-operation");
    }
}
