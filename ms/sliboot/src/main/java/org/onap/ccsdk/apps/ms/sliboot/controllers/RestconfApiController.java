/*-
 * ============LICENSE_START=======================================================
 * ONAP - CCSDK
 * ================================================================================
 * Copyright (C) 2020 AT&T Intellectual Property. All rights reserved.
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

package org.onap.ccsdk.apps.ms.sliboot.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.onap.ccsdk.apps.ms.sliboot.swagger.ConfigApi;
import org.onap.ccsdk.apps.ms.sliboot.swagger.OperationalApi;
import org.onap.ccsdk.apps.ms.sliboot.swagger.OperationsApi;
import org.onap.ccsdk.apps.ms.sliboot.data.TestResultsOperationalRepository;
import org.onap.ccsdk.apps.ms.sliboot.swagger.model.*;
import org.onap.ccsdk.apps.services.RestApplicationException;
import org.onap.ccsdk.apps.services.RestException;
import org.onap.ccsdk.apps.services.RestProtocolException;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.core.sli.provider.base.SvcLogicServiceBase;
import org.onap.ccsdk.apps.ms.sliboot.data.TestResultConfig;
import org.onap.ccsdk.apps.ms.sliboot.data.TestResultsConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-02-20T12:50:11.207-05:00")

@RestController
@ComponentScan(basePackages = {"org.onap.ccsdk.apps.ms.sliboot.*", "org.onap.ccsdk.apps.services", "org.onap.ccsdk.apps.filters"})
@EntityScan("org.onap.ccsdk.apps.ms.sliboot.*")
public class RestconfApiController implements ConfigApi, OperationalApi, OperationsApi {

	private final ObjectMapper objectMapper;
	private final HttpServletRequest request;

    @Autowired
    protected SvcLogicServiceBase svc;

    @Autowired
	private TestResultsConfigRepository testResultsConfigRepository;

    @Autowired
	private TestResultsOperationalRepository testResultsOperationalRepository;

	private static final Logger log = LoggerFactory.getLogger(RestconfApiController.class);

	@org.springframework.beans.factory.annotation.Autowired
	public RestconfApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}


	@Override
	public Optional<ObjectMapper> getObjectMapper() {
		return Optional.ofNullable(objectMapper);
	}

	@Override
	public Optional<HttpServletRequest> getRequest() {
		return Optional.ofNullable(request);
	}
	@Override
	public Optional<String> getAcceptHeader() {
		return ConfigApi.super.getAcceptHeader();
	}

	@Override
	public ResponseEntity<SliApiHealthcheck> operationsSLIAPIhealthcheckPost() {

		SliApiResponseFields respFields = new SliApiResponseFields();
		HttpStatus httpStatus = HttpStatus.OK;

		try {
			log.info("Calling SLI-API:healthcheck DG");
			SvcLogicContext ctxIn = new SvcLogicContext();
			SvcLogicContext ctxOut = svc.execute("sli", "healthcheck", null, "sync", ctxIn);
			Properties respProps = ctxOut.toProperties();

			respFields.setAckFinalIndicator(respProps.getProperty("ack-final-indicator", "Y"));
			respFields.setResponseCode(respProps.getProperty("error-code", "200"));
			respFields.setResponseMessage(respProps.getProperty("error-message", "Success"));
			respFields.setContextMemoryJson(propsToJson(respProps, "context-memory"));

		} catch (Exception e) {
			respFields.setAckFinalIndicator("true");
			respFields.setResponseCode("500");
			respFields.setResponseMessage(e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("Error calling healthcheck directed graph", e);

		}

		SliApiHealthcheck resp = new SliApiHealthcheck();
		resp.setOutput(respFields);
		return (new ResponseEntity<>(resp, httpStatus));
	}

	@Override
	public ResponseEntity<SliApiVlbcheck> operationsSLIAPIvlbcheckPost() {

		SliApiResponseFields respFields = new SliApiResponseFields();
		HttpStatus httpStatus = HttpStatus.OK;

		try {
			log.info("Calling SLI-API:vlbcheck DG");
			SvcLogicContext ctxIn = new SvcLogicContext();
			SvcLogicContext ctxOut = svc.execute("sli", "vlbcheck", null, "sync", ctxIn);
			Properties respProps = ctxOut.toProperties();
			respFields.setAckFinalIndicator(respProps.getProperty("ack-final-indicator", "Y"));
			respFields.setResponseCode(respProps.getProperty("error-code", "200"));
			respFields.setResponseMessage(respProps.getProperty("error-message", "Success"));
			respFields.setContextMemoryJson(propsToJson(respProps, "context-memory"));

		} catch (Exception e) {
			respFields.setAckFinalIndicator("true");
			respFields.setResponseCode("500");
			respFields.setResponseMessage(e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("Error calling vlbcheck directed graph", e);

		}

		SliApiVlbcheck resp = new SliApiVlbcheck();
		resp.setOutput(respFields);
		return (new ResponseEntity<>(resp, httpStatus));
	}

	@Override
	public ResponseEntity<SliApiExecuteGraph> operationsSLIAPIexecuteGraphPost(@Valid SliApiExecutegraphInputBodyparam executeGraphInput) {

		SvcLogicContext ctxIn = new SvcLogicContext();
		SliApiExecuteGraph resp = new SliApiExecuteGraph();
		SliApiResponseFields respFields = new SliApiResponseFields();
		String executeGraphInputJson = null;

		try {
			 executeGraphInputJson = objectMapper.writeValueAsString(executeGraphInput);
			 log.info("Input as JSON is "+executeGraphInputJson);
		} catch (JsonProcessingException e) {

			respFields.setAckFinalIndicator("true");
			respFields.setResponseCode("500");
			respFields.setResponseMessage(e.getMessage());
			log.error("Cannot create JSON from input object", e);
			resp.setOutput(respFields);
			return (new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR));

		}
		JsonObject jsonInput = new Gson().fromJson(executeGraphInputJson, JsonObject.class);
		JsonObject passthroughObj = jsonInput.get("input").getAsJsonObject();

		ctxIn.mergeJson("input", passthroughObj.toString());

		try {
			// Any of these can throw a nullpointer exception
			String calledModule = executeGraphInput.getInput().getModuleName();
			String calledRpc = executeGraphInput.getInput().getRpcName();
			String modeStr = executeGraphInput.getInput().getMode().toString();
			// execute should only throw a SvcLogicException
			SvcLogicContext ctxOut = svc.execute(calledModule, calledRpc, null, modeStr, ctxIn);
			Properties respProps = ctxOut.toProperties();

			respFields.setAckFinalIndicator(respProps.getProperty("ack-final-indicator", "Y"));
			respFields.setResponseCode(respProps.getProperty("error-code", "200"));
			respFields.setResponseMessage(respProps.getProperty("error-message", "SUCCESS"));
			respFields.setContextMemoryJson(propsToJson(respProps, "context-memory"));
			resp.setOutput(respFields);
			return (new ResponseEntity<>(resp, HttpStatus.valueOf(Integer.parseInt(respFields.getResponseCode()))));

		} catch (NullPointerException npe) {
			respFields.setAckFinalIndicator("true");
			respFields.setResponseCode("500");
			respFields.setResponseMessage("Check that you populated module, rpc and or mode correctly.");

			resp.setOutput(respFields);
			return (new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR));
		} catch (SvcLogicException e) {
			respFields.setAckFinalIndicator("true");
			respFields.setResponseCode("500");
			respFields.setResponseMessage(e.getMessage());
			resp.setOutput(respFields);
			return (new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}

	@Override
	public ResponseEntity<Void> configSLIAPItestResultsSLIAPItestResultTestIdentifierDelete(String testIdentifier) {

		List<TestResultConfig> testResultConfigs = testResultsConfigRepository.findByTestIdentifier(testIdentifier);

		if (testResultConfigs != null) {
			Iterator<TestResultConfig> testResultConfigIterator = testResultConfigs.iterator();
			while (testResultConfigIterator.hasNext()) {
				testResultsConfigRepository.delete(testResultConfigIterator.next());
			}
		}

		return (new ResponseEntity<>(HttpStatus.OK));
	}

	@Override
	public ResponseEntity<Void> configSLIAPItestResultsDelete() {

		testResultsConfigRepository.deleteAll();

		return (new ResponseEntity<>(HttpStatus.OK));
	}

	@Override
	public ResponseEntity<SliApiTestResults> operationalSLIAPItestResultsGet() {

		SliApiTestResults results = new SliApiTestResults();

		testResultsOperationalRepository.findAll().forEach(testResult -> {
			SliApiTestresultsTestResult item = null;
			try {
				item = objectMapper.readValue(testResult.getResults(), SliApiTestresultsTestResult.class);
				results.addTestResultItem(item);
			} catch (JsonProcessingException e) {
				log.error("Could not convert testResult", e);
			}
		});


		return new ResponseEntity<>(results, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<SliApiTestresultsTestResult> configSLIAPItestResultsSLIAPItestResultTestIdentifierGet(String testIdentifier) {

		List<TestResultConfig> testResultConfigs = testResultsConfigRepository.findByTestIdentifier(testIdentifier);

		if ((testResultConfigs == null) || (testResultConfigs.size() == 0)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			TestResultConfig testResultConfig = testResultConfigs.get(0);
			SliApiTestresultsTestResult testResult = null;
			try {
				testResult = objectMapper.readValue(testResultConfig.getResults(), SliApiTestresultsTestResult.class);
			} catch (JsonProcessingException e) {
				log.error("Cannot convert test result", e);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}


			return new ResponseEntity<>(testResult, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<SliApiTestResults> configSLIAPItestResultsGet() throws RestException {

		if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
		} else {
			log.warn("ObjectMapper or HttpServletRequest not configured in default RestconfApi interface so no example is generated");
		}

		SliApiTestResults results = new SliApiTestResults();

		if (testResultsConfigRepository.count() == 0) {
			throw new RestApplicationException("data-missing", "Request could not be completed because the relevant data model content does not exist", 404);
		}

		testResultsConfigRepository.findAll().forEach(testResult -> {
			SliApiTestresultsTestResult item = null;
			try {
				item = objectMapper.readValue(testResult.getResults(), SliApiTestresultsTestResult.class);
				results.addTestResultItem(item);
			} catch (JsonProcessingException e) {
				log.error("Could not convert testResult", e);
			}
		});


		return new ResponseEntity<>(results, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> configSLIAPItestResultsSLIAPItestResultTestIdentifierPut(String testIdentifier, @Valid SliApiTestresultsTestResult testResult) {

		if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
		} else {
			log.warn("ObjectMapper or HttpServletRequest not configured in default RestconfApi interface so no example is generated");
		}

		List<TestResultConfig> testResultConfigs = testResultsConfigRepository.findByTestIdentifier(testIdentifier);
		Iterator<TestResultConfig> testResultIter = testResultConfigs.iterator();
		while (testResultIter.hasNext()) {
			testResultsConfigRepository.delete(testResultIter.next());
		}

		TestResultConfig testResultConfig = null;
		try {
			testResultConfig = new TestResultConfig(testResult.getTestIdentifier(), objectMapper.writeValueAsString(testResult));
			testResultsConfigRepository.save(testResultConfig);
		} catch (JsonProcessingException e) {
			log.error("Could not save test result", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> configSLIAPItestResultsPost(@Valid SliApiTestResults testResults) {

		List<SliApiTestresultsTestResult> resultList = testResults.getTestResult();

		if (resultList == null) {
			log.error("Invalid input - no test results list");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Iterator<SliApiTestresultsTestResult> resultIterator = resultList.iterator();

		while (resultIterator.hasNext()) {
			SliApiTestresultsTestResult curResult = resultIterator.next();
			try {
				testResultsConfigRepository.save(new TestResultConfig(curResult.getTestIdentifier(), objectMapper.writeValueAsString(curResult)));
			} catch (JsonProcessingException e) {
				log.error("Could not save test result", e);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> configSLIAPItestResultsPut(@Valid SliApiTestResults testResults) {

		testResultsConfigRepository.deleteAll();

		List<SliApiTestresultsTestResult> resultList = testResults.getTestResult();

		Iterator<SliApiTestresultsTestResult> resultIterator = resultList.iterator();


		while (resultIterator.hasNext()) {
			SliApiTestresultsTestResult curResult = resultIterator.next();
			try {
				testResultsConfigRepository.save(new TestResultConfig(curResult.getTestIdentifier(), objectMapper.writeValueAsString(curResult)));
			} catch (JsonProcessingException e) {
				log.error("Could not save test result", e);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	public static String propsToJson(Properties props, String root)
	{
		StringBuffer sbuff = new StringBuffer();

		sbuff.append("{ \""+root+"\" : { ");
		boolean needComma = false;
		for (Map.Entry<Object, Object> prop : props.entrySet()) {
			sbuff.append("\""+(String) prop.getKey()+"\" : \""+(String)prop.getValue()+"\"");
			if (needComma) {
				sbuff.append(" , ");
			} else {
				needComma = true;
			}
		}
		sbuff.append(" } }");

		return(sbuff.toString());
	}

}
