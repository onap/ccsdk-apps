/*******************************************************************************
 * Copyright © 2017-2018 AT&T Intellectual Property.
 * 
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
 ******************************************************************************/

package org.onap.ccsdk.apps.ms.vlantagapi.util;

import java.util.List;

import org.onap.ccsdk.sli.adaptors.ra.ResourceAllocator;
import org.onap.ccsdk.sli.adaptors.ra.comp.ResourceEntity;
import org.onap.ccsdk.sli.adaptors.ra.comp.ResourceRequest;
import org.onap.ccsdk.sli.adaptors.ra.comp.ResourceResponse;
import org.onap.ccsdk.sli.adaptors.ra.comp.ResourceTarget;
import org.onap.ccsdk.sli.adaptors.rm.data.AllocationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockResourceAllocator extends ResourceAllocator{
	protected static final Logger logger = LoggerFactory.getLogger(MockResourceAllocator.class);
	
	public AllocationStatus reserve(ResourceEntity sd, ResourceTarget rt, ResourceRequest rr,
            List<ResourceResponse> rsList) throws Exception {
        
		
		ResourceResponse rres = new ResourceResponse();
		rres.endPointPosition = "VPE-Core1";
		rres.resourceAllocated = "2001";
		rres.resourceName="vlan-id-outer";
		
		rsList.add(rres);
		
		return AllocationStatus.Success;
    }
	
	public AllocationStatus release(ResourceEntity sd, ResourceRequest rr) throws Exception {
        return AllocationStatus.Success;
    }
}
