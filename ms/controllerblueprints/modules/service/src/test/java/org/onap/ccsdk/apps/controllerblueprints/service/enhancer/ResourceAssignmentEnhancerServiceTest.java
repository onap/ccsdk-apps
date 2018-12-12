/*
 *  Copyright © 2017-2018 AT&T Intellectual Property.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.onap.ccsdk.apps.controllerblueprints.service.enhancer;

import com.att.eelf.configuration.EELFLogger;
import com.att.eelf.configuration.EELFManager;
import org.junit.Before;
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException;
import org.onap.ccsdk.apps.controllerblueprints.resource.dict.utils.ResourceDictionaryTestUtils;

/**
 * ResourceAssignmentEnhancerService.
 *
 * @author Brinda Santh
 */
public class ResourceAssignmentEnhancerServiceTest {
    private static EELFLogger log = EELFManager.getInstance().getLogger(ResourceAssignmentEnhancerServiceTest.class);

    @Before
    public void setUp() {
        // Setup dummy Source Instance Mapping
        ResourceDictionaryTestUtils.setUpResourceSourceMapping();
    }

    //@Test
    public void testEnhanceBluePrint() throws BluePrintException {
        /*
        FIXME("Test Once Implemented")

        List<ResourceAssignment> resourceAssignments = JacksonUtils
                .getListFromClassPathFile("enhance/enhance-resource-assignment.json", ResourceAssignment.class);
        Assert.assertNotNull("Failed to get Resource Assignment", resourceAssignments);

        ResourceDefinitionRepoService resourceDefinitionRepoService = new ResourceDefinitionFileRepoService("./../../../../components/model-catalog");
        ResourceAssignmentEnhancerService resourceAssignmentEnhancerService =
                new ResourceAssignmentEnhancerDefaultService(resourceDefinitionRepoService);
        ServiceTemplate serviceTemplate = resourceAssignmentEnhancerService.enhanceBluePrint(resourceAssignments);
        Assert.assertNotNull("Failed to get Enriched service Template", serviceTemplate);
        log.trace("Enhanced Service Template : {}", JacksonUtils.getJson(serviceTemplate, true));
        */
    }
}

