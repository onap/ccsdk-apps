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

package org.onap.ccsdk.apps.ms.neng.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.onap.ccsdk.apps.ms.neng.core.exceptions.NengException;
import org.onap.ccsdk.apps.ms.neng.core.gen.NameGenerator;
import org.onap.ccsdk.apps.ms.neng.core.persistence.NamePersister;
import org.onap.ccsdk.apps.ms.neng.core.policy.PolicyFinder;
import org.onap.ccsdk.apps.ms.neng.core.policy.PolicyParameters;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.HelloWorld;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.NameGenRequest;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.NameGenResponse;
import org.onap.ccsdk.apps.ms.neng.core.seq.SequenceGenerator;
import org.onap.ccsdk.apps.ms.neng.core.validator.AaiNameValidator;
import org.onap.ccsdk.apps.ms.neng.core.validator.DbNameValidator;
import org.onap.ccsdk.apps.ms.neng.core.validator.ExternalKeyValidator;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.GeneratedName;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.PolicyDetails;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.ServiceParameter;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.GeneratedNameRespository;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.PolicyDetailsRepository;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.ServiceParameterRepository;
import org.onap.ccsdk.apps.ms.neng.service.extinf.impl.AaiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Implementation of the APIs exposed by this micro-service.
 */
@Service
public class SpringServiceImpl implements SpringService {
    private static Logger log = Logger.getLogger(SpringServiceImpl.class.getName());

    @Autowired AaiServiceImpl aaiService;
    @Autowired ExternalKeyValidator externalKeyValidator;
    @Autowired @Qualifier("policyFinderServiceImpl") PolicyFinder policyFinder;
    @Autowired @Qualifier("policyFinderServiceDbImpl") PolicyFinder policyFinderDbImpl;
    @Autowired PolicyParameters policyParameters;
    @Autowired SequenceGenerator sequenceGenerator;
    @Autowired DbNameValidator dbNameValidator;
    @Autowired PolicyDetailsRepository policyDetailsRepository;
    @Autowired AaiNameValidator aaiNameValidator;
    @Autowired NamePersister namePersister;
    @Autowired ServiceParameterRepository serviceParamRepo;
    @Autowired GeneratedNameRespository generatedNameRepository;

    /**
     * Heart-beat/ping API.
     */
    @Override
    public HelloWorld getQuickHello(String name) {
        if (name == null || name.isEmpty()) {
            name = "world";
        }
        String message = "Hello " + name + "!";
        log.info(message);
        HelloWorld hello = new HelloWorld(message);
        log.info(hello.toString());
        return hello;
    }

    /**
     * Name generation API.
     */
    @Transactional(rollbackOn = Exception.class)
    public NameGenResponse genNetworkElementName(NameGenRequest request) throws Exception {
        try {
            Map<String, Map<String, String>> earlierNames = new HashMap<String, Map<String, String>>();
            List<Map<String, String>> allElements = new ArrayList<Map<String, String>>();
            Map<String, Map<String, ?>> policyCache = new HashMap<String, Map<String, ?>>();
            List<Map<String, String>> generatedNames = new ArrayList<Map<String, String>>();
            validateRequest(request);
            if (request.getElements() != null && request.getElements().size() > 0) {
                allElements.addAll(request.getElements());
            }
            PolicyFinder policyFinderImpl = findPolicyFinderImpl(request);
            for (Map<String, String> requestElement : allElements) {
                log.info("Processing " + requestElement.toString());
                NameGenerator nameGen = new NameGenerator(policyFinderImpl, policyParameters, sequenceGenerator,
                                dbNameValidator, aaiNameValidator, namePersister, requestElement, allElements,
                                earlierNames, policyCache);
                generatedNames.add(nameGen.generate());
            }
            NameGenResponse resp = new NameGenResponse();
            resp.setElements(generatedNames);
            return resp;
        } catch (Exception e) {
            if (e instanceof NengException) {
                throw e;
            } else {
                e.printStackTrace();
                throw new Exception("Internal error occurred while processing the request");
            }
        }
    }

    /**
     * Name removal API.
     */
    @Transactional(rollbackOn = Exception.class)
    public NameGenResponse releaseNetworkElementName(NameGenRequest request) throws Exception {
        NameGenResponse response = new NameGenResponse();
        Timestamp curTime = new Timestamp(System.currentTimeMillis());
        try {
            for (Map<String, String> requestElement : request.getElements()) {
                String key = requestElement.get("external-key");
                List<GeneratedName> generatedNames = generatedNameRepository.findByExternalId(key);
                for (GeneratedName generatedName : generatedNames) {
                    generatedName.setIsReleased("Y");
                    generatedName.setLastUpdatedTime(curTime);
                }
                buildUnAssignResponse(generatedNames, response);
            }
            return response;
        } catch (Exception e) {
            if (e instanceof NengException) {
                throw e;
            } else {
                e.printStackTrace();
                throw new Exception("Internal error occurred while processing the request");
            }
        }
    }

    /**
     * API to return naming policy cached in this micro-service.
     */
    @Override
    public PolicyDetails getPolicyDetails(String policyName) {
        try {
            return policyDetailsRepository.findPolicyResponseByName(policyName);
        } catch (Exception e) {
            return new PolicyDetails();
        }
    }

    /**
     * API to add a naming policy to the database cache in this micro-service.
     */
    @Override
    public void addPolicy(Object request) throws Exception {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> policyData = (Map<String, Object>)request;
            PolicyDetails pd = new PolicyDetails();
            pd.setPolicyName((String) policyData.get("policyName"));
            ObjectMapper objectmapper = new ObjectMapper();
            log.info(objectmapper.writeValueAsString(policyData.get("policyValue")));
            pd.setPolicyResponse(objectmapper.writeValueAsString(policyData.get("policyValue")));
            policyDetailsRepository.save(pd);
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }
    
    void buildUnAssignResponse(List<GeneratedName> generatedNames, NameGenResponse response) {
        if (response.getElements() == null) {
            response.setElements(new ArrayList<Map<String, String>>());
        }
        for (GeneratedName generatedName : generatedNames) {
            Map<String, String> element = new HashMap<>();
            element.put("external-key", generatedName.getExternalId());
            element.put("resource-name", generatedName.getElementType());
            element.put("resource-value", generatedName.getName());
            response.getElements().add(element);
        }
    }

    void validateRequest(NameGenRequest request) throws Exception {
        List<Map<String, String>> elems = request.getElements();
        if (elems != null && elems.size() > 0) {
            boolean error = false;
            Set<String> externalKeySet = elems.stream().map(s -> s.get("external-key")).collect(Collectors.toSet());
            if (externalKeySet.size() != request.getElements().size()) {
                error = true;
            }
            for (String externalKey : externalKeySet) {
                if (externalKey == null || externalKeyValidator.isPresent(externalKey)) {
                    error = true;
                }
            }
            if (error) {
                throw new NengException("External Key is required and must be unique");
            }
        }
    }

    private PolicyFinder findPolicyFinderImpl(NameGenRequest request) {
        if (request.getUseDb() != null && !request.getUseDb()) {
            return this.policyFinder;
        }
        ServiceParameter param = serviceParamRepo.findByName("use_db_policy");
        if ((request.getUseDb() != null && request.getUseDb().booleanValue()) 
                        || (param != null && "Y".equals(param.getValue()))) {
            return policyFinderDbImpl;
        }
        return this.policyFinder;
    }
}
