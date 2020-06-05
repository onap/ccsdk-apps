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

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.core.sli.provider.base.SvcLogicServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
@EnableAutoConfiguration
public class ExecuteGraphController {
  @Autowired
  protected SvcLogicServiceBase svc;

	@RequestMapping(value = "/executeGraph", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, String> executeGraph(@RequestBody String input) {
		HashMap<String, String> hash = new HashMap<String, String>();
		Properties parms = new Properties();

		hash.put("status", "success");
		JsonObject jsonInput = new Gson().fromJson(input, JsonObject.class);
		JsonObject passthroughObj = jsonInput.get("input").getAsJsonObject();

		writeResponseToCtx(passthroughObj.toString(), parms, "input");

		JsonObject inputObject = jsonInput.get("graphDetails").getAsJsonObject();
		try {
			// Any of these can throw a nullpointer exception
			String calledModule = inputObject.get("module").getAsString();
			String calledRpc = inputObject.get("rpc").getAsString();
			String modeStr = inputObject.get("mode").getAsString();
			// execute should only throw a SvcLogicException
			Properties respProps = svc.execute(calledModule, calledRpc, null, modeStr, parms);
			for (Entry<Object, Object> prop : respProps.entrySet()) {
				hash.put((String) prop.getKey(), (String) prop.getValue());
			}
		} catch (NullPointerException npe) {
			HashMap<String, String> errorHash = new HashMap<String, String>();
			errorHash.put("error-message", "check that you populated module, rpc and or mode correctly.");
			return errorHash;
		} catch (SvcLogicException e) {
			HashMap<String, String> errorHash = new HashMap<String, String>();
			errorHash.put("status", "failure");
			errorHash.put("message", e.getMessage());
			return errorHash;
		}
		return hash;
	}

	public static void writeResponseToCtx(String resp, Properties ctx, String prefix) {
		JsonParser jp = new JsonParser();
		JsonElement element = jp.parse(resp);
		writeJsonObject(element.getAsJsonObject(), ctx, prefix + ".");
	}

	public static void writeJsonObject(JsonObject obj, Properties ctx, String root) {
		for (Entry<String, JsonElement> entry : obj.entrySet()) {
			if (entry.getValue().isJsonObject()) {
				writeJsonObject(entry.getValue().getAsJsonObject(), ctx, root + entry.getKey() + ".");
			} else if (entry.getValue().isJsonArray()) {
				JsonArray array = entry.getValue().getAsJsonArray();
				ctx.put(root + entry.getKey() + "_length", String.valueOf(array.size()));
				Integer arrayIdx = 0;
				for (JsonElement element : array) {
					if (element.isJsonObject()) {
						writeJsonObject(element.getAsJsonObject(), ctx, root + entry.getKey() + "[" + arrayIdx + "].");
					}
					arrayIdx++;
				}
			} else {
				ctx.put(root + entry.getKey(), entry.getValue().getAsString());
			}
		}
	}


}
