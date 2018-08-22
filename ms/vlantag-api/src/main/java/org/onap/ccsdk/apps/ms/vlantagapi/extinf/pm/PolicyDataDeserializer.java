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
package org.onap.ccsdk.apps.ms.vlantagapi.extinf.pm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.onap.ccsdk.apps.ms.vlantagapi.core.extinf.pm.model.PolicyData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * PolicyDataDeserializer.java Purpose:
 * This is a custom deserializer for "policy-data" element of the response from the Policy management REST call
 * to get Policy details based on a policy name.
 * 
 * The response will have different "child tree"s ( policy data)  for different policy names.
 * Each type of child is structurally, syntactically, semantically unique.
 * However, there is no explicit way to identify the right child type in the "policy-data" element itself
 * Hence , the hack is  : assuming there is a unique property in each type of child of Policy-data,
 * look for that property to determine the correct target child type to deserialize to.
 * These unique properties are stored in polymorphicsRegistry and looked up against to find a match
 * If a match is found, use that child type is used.
 */
public class PolicyDataDeserializer extends StdDeserializer<PolicyData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(PolicyDataDeserializer.class);

	/*
	 * Keep a register of all extensions of PolicyData type by a unique attribute that distringuishes
	 * them from one another
	 */
	private Map<String, Class<? extends PolicyData>> polymorphicsRegistry =
	      new HashMap<>();

	public PolicyDataDeserializer() {
	    super(PolicyData.class);
	}

	/*
	 * Method to add to the registry of PolicyData extensions by their distinguishing attribure
	 */
	public void registerExtendersByUniqueness(String uniqueAttribute, Class<? extends PolicyData> policyDataClass) {
	    polymorphicsRegistry.put(uniqueAttribute, policyDataClass);
	}

	@Override
	public PolicyData deserialize(JsonParser jp, DeserializationContext ctxt) 
	      throws IOException {
		
		PolicyData retVal = null;
	    Class<? extends PolicyData> policyDataClass = null;
	    
	    ObjectMapper mapper = (ObjectMapper) jp.getCodec();
	    ObjectNode root = mapper.readTree(jp);

	    //Go through all the elements in this node. If a match is found in the registry of uniqueness,
	    // it means we identified what extension of PolicyData we are dealing with. If no match found, return null
	    Iterator<Entry<String, JsonNode>> elementsIterator = root.fields();
	    while (elementsIterator.hasNext())
	    {
	      Entry<String, JsonNode> element=elementsIterator.next();
	      String name = element.getKey();
	      
	      if (polymorphicsRegistry.containsKey(name))
	      {
	        policyDataClass = polymorphicsRegistry.get(name);
	        break;
	      }
	    }
	    
	    //If we know what child type of PolicyData we are working on , use that class to deserialize this node
	    // from its root. If not, throw exception
	    if (policyDataClass != null) {
	    	retVal = mapper.treeToValue(root,  policyDataClass);
	    }
	    else {
	    	log.info("No matching Child of PolicyData is present in this node tree");
	    }
	    return retVal;
	}
}

