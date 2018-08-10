/*
 * ﻿============LICENSE_START=======================================================
 * org.onap.ccsdk
 * ================================================================================
 * Copyright © 2017-2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 *
 *
 */

package org.onap.ccsdk.apps.controllerblueprints.service.validator;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException;
import org.onap.ccsdk.apps.controllerblueprints.core.utils.JacksonUtils;
import org.onap.ccsdk.apps.controllerblueprints.resource.dict.data.DictionaryDefinition;
import org.onap.ccsdk.apps.controllerblueprints.service.domain.ResourceDictionary;

/**
 * ResourceDictionaryValidator.java Purpose: Provide Validation Service for Model Type Resource
 * Dictionary Validator
 *
 * @author Brinda Santh
 * @version 1.0
 */
public class ResourceDictionaryValidator {

    private ResourceDictionaryValidator() {}

    /**
     * This is a validateResourceDictionaryDefinition
     * 
     * @param definitionContent
     * @return boolean
     * @throws BluePrintException
     */
    public static boolean validateResourceDictionaryDefinition(String definitionContent)
            throws BluePrintException {
        boolean valid = true;
        if (StringUtils.isNotBlank(definitionContent)) {
            DictionaryDefinition dictionaryDefinition =
                    JacksonUtils.readValue(definitionContent, DictionaryDefinition.class);
            if (dictionaryDefinition == null) {
                throw new BluePrintException(
                        "Resource dictionary definition is not valid content " + definitionContent);
            }

        }
        return valid;
    }

    /**
     * This is a validateResourceDictionary method
     * 
     * @param resourceDictionary
     * @return boolean
     *
     */
    public static boolean validateResourceDictionary(ResourceDictionary resourceDictionary) {

        Preconditions.checkNotNull(resourceDictionary,"ResourceDictionary Information is missing." );

        Preconditions.checkArgument( StringUtils.isNotBlank(resourceDictionary.getName()),
                "DataDictionary Alias Name Information is missing.");
        Preconditions.checkArgument( StringUtils.isNotBlank(resourceDictionary.getResourcePath()),
                "DataDictionary Resource Name Information is missing.");
        Preconditions.checkArgument( StringUtils.isNotBlank(resourceDictionary.getResourceType()),
                "DataDictionary Resource Type Information is missing.");
        Preconditions.checkArgument( StringUtils.isNotBlank(resourceDictionary.getDefinition()),
                "DataDictionary Definition Information is missing.");
        Preconditions.checkArgument( StringUtils.isNotBlank(resourceDictionary.getDescription()),
                "DataDictionary Description Information is missing.");
        Preconditions.checkArgument( StringUtils.isNotBlank(resourceDictionary.getTags()),
                "DataDictionary Tags Information is missing.");
        Preconditions.checkArgument( StringUtils.isNotBlank(resourceDictionary.getUpdatedBy()),
                "DataDictionary Updated By Information is missing.");
        return true;

    }

}
