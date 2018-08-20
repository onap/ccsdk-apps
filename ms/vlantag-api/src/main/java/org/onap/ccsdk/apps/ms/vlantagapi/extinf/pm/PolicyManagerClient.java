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
import java.util.ArrayList;
import java.util.List;
import org.onap.ccsdk.apps.ms.vlantagapi.core.exception.VlantagApiException;
import org.onap.ccsdk.apps.ms.vlantagapi.core.extinf.pm.model.PolicyConfig;
import org.onap.ccsdk.apps.ms.vlantagapi.core.extinf.pm.model.PolicyData;
import org.onap.ccsdk.apps.ms.vlantagapi.core.extinf.pm.model.PolicyEngineResponse;
import org.onap.ccsdk.apps.ms.vlantagapi.core.extinf.pm.model.RequestObject;
import org.onap.ccsdk.apps.ms.vlantagapi.core.extinf.pm.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * PolicyManagerClient.java Purpose: Client for Policy Manager applications and
 * gets policies from policy manager.
 *
 * @author Saurav Paira
 * @version 1.0
 */
@Component
public class PolicyManagerClient {

	private static Logger log = LoggerFactory.getLogger(PolicyManagerClient.class);
	
	private static final String CLIENT_AUTH = "ClientAuth";
	private static final String ENVIRONMENT = "Environment";

    private static final String POLICYMGR_URL_PN = "intf.pdp.policyengine.url";
    private static final String POLICYMGR_AUTHORIZATION_PN = "intf.pdp.policyengine.http.headers.authorization";
    private static final String POLICYMGR_CLIENTAUTH_PN = "intf.pdp.policyengine.http.headers.clientauth";
    private static final String POLICYMGR_ENVIRONMENT_PN = "intf.pdp.policyengine.http.headers.environment";

	@Autowired
	Environment env;
    /*
     * Main method to call to get the vlan tag selection policy data
     */

    public synchronized List<ResourceModel> getPolicy(final String policyName) throws VlantagApiException {
        return getPolicyFromPDP(policyName);
    }

    /*
     * REST call to Policy Manager
     */
	public PolicyEngineResponse[] getConfigUsingPost(RequestObject requestObject) throws VlantagApiException {

		PolicyEngineResponse[] result = null;
		
		String url = env.getProperty(POLICYMGR_URL_PN);
		log.info("url si : {}", url);
		HttpHeaders headers = getRequestHeaders();
		HttpEntity<RequestObject> request = new HttpEntity<>(requestObject, headers);
		RestTemplate restTemplate = getRestTemplate();
		
		try {
			ResponseEntity<PolicyEngineResponse[]> response = restTemplate.exchange(url, HttpMethod.POST, request, PolicyEngineResponse[].class);
			result = response.getBody();

		} catch (RestClientException rce) {
			throw new VlantagApiException(rce.getLocalizedMessage());
		}
		return result;
	}

	private HttpHeaders getRequestHeaders() {
		HttpHeaders headers = new HttpHeaders();
		//Read from application environment with appropriate profile
		headers.add(HttpHeaders.AUTHORIZATION, env.getProperty(POLICYMGR_AUTHORIZATION_PN));
		headers.add(CLIENT_AUTH, env.getProperty(POLICYMGR_CLIENTAUTH_PN));
		headers.add(ENVIRONMENT, env.getProperty(POLICYMGR_ENVIRONMENT_PN));
		headers.add(HttpHeaders.ACCEPT, "application/json");
		headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
		return headers;
	}

	public List<ResourceModel> getPolicyFromPDP(String policyName) throws VlantagApiException {

        List<ResourceModel> retVal = null;

        try {
            RequestObject requestObject = new RequestObject();
            requestObject.setPolicyName(policyName);
            retVal = extractResourceModelsFromResponse(getConfigUsingPost(requestObject));
        } catch (IOException e) {
            throw new VlantagApiException(e);
        } 
        return retVal;
    }

    public List<ResourceModel> extractResourceModelsFromResponse(PolicyEngineResponse[] result)
            throws IOException {

        List<ResourceModel> retVal = null;
        if (null != result && result.length > 0) {

            //There will be only one element in the result - confirmed with PDP developer
            //Also, due to escaped duble quoted strings in the JSON, the config element will be deserialized
            //as a String rather than as PolicyConfig. Which is good anyway for our processing, but FYI
            // So , get the String and separately pass it through a new ObjectMapper
            String configValue = result[0].getConfig();
            ObjectMapper om = getConfigDeserializerObjectMapper();
            PolicyConfig config = om.readValue(configValue, PolicyConfig.class);
            retVal = config.getContent().getResourceModels();
        }
        return retVal;
    }

    private ObjectMapper getConfigDeserializerObjectMapper() {

        /// We need a special deserializer for Policy data. Depending on policy name sent as input, the policy data returned
        // differs not only in content, but in structure too. In other words, Polymorphism!!! There will be a(n) abstract
        // PolicyData and one child each for each policy. The SOLID ASSUMPTION here is that there is one unique attribute in each
        // policy to distinguish it from one another. 
        // If that turns out to be wrong, we will need another desrialization policy and corresponding new deserializer

        PolicyDataDeserializer deserializer = new PolicyDataDeserializer();
        deserializer.registerExtendersByUniqueness("key-type", ResourceModel.class);

        SimpleModule module = new SimpleModule("PolymorphicPolicyDataDeserializerModule",
                new Version(1, 0, 0, null, null, null));
        module.addDeserializer(PolicyData.class, deserializer);

        ObjectMapper om = new ObjectMapper();
        om.registerModule(module);
        om.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        return om;
    }

    /*
     * Get appropriately decorated REST template
     */
    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(
                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        om.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        om.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        MappingJackson2HttpMessageConverter convertor = new MappingJackson2HttpMessageConverter(om);
        List<HttpMessageConverter<?>> messageConvertors = new ArrayList<>();
        messageConvertors.add(convertor);
        restTemplate.setMessageConverters(messageConvertors);
        return restTemplate;
    }
    
    public void setEnv(Environment env) {
        this.env = env;
    }

}
