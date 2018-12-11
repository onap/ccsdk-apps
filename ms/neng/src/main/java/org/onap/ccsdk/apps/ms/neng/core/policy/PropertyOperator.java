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

import static org.onap.ccsdk.apps.ms.neng.core.policy.PolicyReader.propertyOperation;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Applies property operations while processing a policy.
 */
public class PropertyOperator {
    /**
     * Apply a property found in the policy.
     * 
     * <p/> If the property cannot be applied correctly for any reason, the value is returned as is.
     * 
     * @param value           the String to which the property has to be applied
     * @param propertyMap     a map representing the property, the key of which is "property-operation"
     *     and the value is the actual property
     * @param policyParams    parameters configuring policy 
     * @param recipeItem      a special recipe item (such as TIMESTAMP, UUID)
     * @return                the result of applying the property
     * @throws Exception      all exceptions are propagated
     */
    public String apply(String value, Map<String, ?> propertyMap, PolicyParameters policyParams,
                                      String recipeItem) throws Exception {
        String op = propertyOperation(propertyMap);
        String mapped = null;
        if (op != null) {
            String fn = operationFunction(op);
            if (fn != null) {
                mapped = policyParams.mapFunction(fn);
                if (mapped == null) {
                    mapped = camelConverted(fn);
                }
                if (mapped != null) {
                    op = op.replaceFirst(fn, mapped);
                }
            }
            value = applyJavaOperation(value, op, mapped);
        } else if (recipeItem != null) {
            value = applyOperationByRecipeName(recipeItem, policyParams);
        }
        return value;
    }

    /**
     * Apply a property found in the policy.
     * 
     * <p/> If the property cannot be applied correctly for any reason, the value is returned as is.
     * 
     * @param value           the String to which the property has to be applied
     * @param operation       the operation to be applied
     * @param policyParams    parameters configuring policy 
     * @return                the result of applying the property
     * @throws Exception      all exceptions are propagated
     */
    public String apply(String value, String operation, PolicyParameters policyParams) throws Exception {
        String mapped = null;
        if (operation != null) {
            String fn = operationFunction(operation);
            if (fn != null) {
                mapped = policyParams.mapFunction(fn);
                if (mapped == null) {
                    mapped = camelConverted(fn);
                }
                if (mapped != null) {
                    operation = operation.replaceFirst(fn, mapped);
                }
            }
            value = applyJavaOperation(value, operation, mapped);
        }
        return value;
    }

    private String applyJavaOperation(String inputString, String op, String methodName) throws Exception {
        String postOp = null;
        try {
            String argPart = "";
            if (op.indexOf("(") > 0) {
                int funcStartIndex = op.indexOf("(");
                int funcEndIndex = op.lastIndexOf(")");
                argPart = op.substring(funcStartIndex + 1, funcEndIndex);
            }

            String[] args = new String[0];
            if (inputString != null) {
                argPart = inputString + "," + argPart;
                args = argPart.split(",");
            }

            PolicyPropertyMethodUtils utils;
            for (Method m : PolicyPropertyMethodUtils.class.getDeclaredMethods()) {
                if (m.getName().equals(methodName) && m.getParameterCount() == args.length) {
                    postOp = (String) m.invoke(utils, (Object[])args);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return postOp;
    }

    private String applyOperationByRecipeName(String recipeItem, PolicyParameters policyParams) throws Exception {
        String mapped = policyParams.mapFunction(recipeItem);
        if (mapped == null) {
            mapped = camelConverted(recipeItem);
        }
        return applyJavaOperation(null, recipeItem, mapped);
    }
    
    static String operationFunction(String operation) throws Exception {
        operation = operation.trim();
        int i = 0;
        for (; i < operation.length(); ++i) {
            char ch = operation.charAt(i);
            if (!Character.isJavaIdentifierPart(ch)) {
                break;
            }
        }
        String value = operation.substring(0, i);
        if (value.length() == 0) {
            value = null;
        }
        return value;
    }

    static String camelConverted(String fn) throws Exception {
        boolean upperNext = false;
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < fn.length(); ++i) {
            char ch = fn.charAt(i);
            if (ch == '_') {
                upperNext = true;
            } else {
                if (upperNext) {
                    ch = Character.toUpperCase(ch);
                }
                buf.append(ch);
                upperNext = false;
            }
        }
        return buf.toString();
    }


}
