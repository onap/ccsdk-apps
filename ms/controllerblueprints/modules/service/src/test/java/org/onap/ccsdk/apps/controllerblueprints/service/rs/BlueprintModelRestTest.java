/*
 * Copyright © 2019 Bell Canada Intellectual Property.
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
 */
package org.onap.ccsdk.apps.controllerblueprints.service.rs;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.onap.ccsdk.apps.controllerblueprints.TestApplication;
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintArchiveUtils;
import org.onap.ccsdk.apps.controllerblueprints.service.domain.BlueprintModelSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * BlueprintModelRestTest.java Purpose: Integration test at API level
 *
 * @author Vinal Patel
 * @version 1.0
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TestApplication.class})
@ComponentScan(basePackages = {"org.onap.ccsdk.apps.controllerblueprints"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@EnableAutoConfiguration
public class BlueprintModelRestTest {

    private static String id, name, version, tag, result;

    @Value("${controllerblueprints.loadBluePrintPaths}")
    private String loadBluePrintPaths;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${controllerblueprints.loadBlueprintsExamplesPath}")
    private String blueprintArchivePath;

    private BlueprintModelSearch blueprintModelSearch;
    private String filename = "test.zip";
    private File blueprintFile, zipBlueprintFile;

    @Before
    public void setUp() throws Exception {
        blueprintFile = new File(loadBluePrintPaths);
        if (blueprintFile.isDirectory()) {
            zipBlueprintFile = new File(Paths.get(blueprintArchivePath).resolve(filename).toString());
            BluePrintArchiveUtils.Companion.compress(blueprintFile, zipBlueprintFile, true);
        }
    }

    @After
    public void tearDown() throws Exception {
        zipBlueprintFile.delete();
    }

    @Test
    public void test1_saveBluePrint() throws IOException, JSONException {
        WebTestClient(HttpMethod.POST,
                BodyInserters.fromMultipartData("file", new ByteArrayResource(Files.readAllBytes(zipBlueprintFile.toPath())) {
                    @Override
                    public String getFilename() {
                        return filename;
                    }
                }),
                "/api/v1/blueprint-model",
                HttpStatus.OK, true);
    }

    @Test
    public void test2_getBluePrintByNameAndVersion() throws JSONException {
        WebTestClient(HttpMethod.GET, null, "/api/v1/blueprint-model/by-name/" + name + "/version/" + version, HttpStatus.OK, false);
    }


    @Test
    public void test3_getBlueprintModel() throws JSONException {
        WebTestClient(HttpMethod.GET, null, "/api/v1/blueprint-model/" + id, HttpStatus.OK, false);
    }

    @Test
    public void test4_getAllBlueprintModel() throws JSONException {
        WebTestClient(HttpMethod.GET, null, "/api/v1/blueprint-model", HttpStatus.OK, false);
    }

    @Test
    public void test5_downloadBluePrint() throws JSONException {
        WebTestClient(HttpMethod.GET, null, "/api/v1/blueprint-model/download/" + id, HttpStatus.OK, false);
    }

    @Test
    public void test6_publishBlueprintModel() {
    }

    @Test
    public void test7_searchBlueprintModels() throws JSONException {
        WebTestClient(HttpMethod.GET, null, "/api/v1/blueprint-model/search/" + name, HttpStatus.OK, false);
    }

    @Test
    public void test8_downloadBlueprintByNameAndVersion() throws JSONException {
        WebTestClient(HttpMethod.GET, null, "/api/v1/blueprint-model/download/by-name/" + name + "/version/" + version, HttpStatus.OK, false);
    }

    @Test
    public void test9_deleteBluePrint() {
        //TODO: Use WebTestClient function
        //WebTestClient(HttpMethod.DELETE, null, "/api/v1/blueprint-model/" + id, HttpStatus.OK, false);
        webTestClient.delete().uri("/api/v1/blueprint-model/" + id)
                .header("Authorization", "Basic " + Base64Utils
                        .encodeToString(("ccsdkapps" + ":" + "ccsdkapps").getBytes(UTF_8)))
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    private void WebTestClient(HttpMethod requestMethod, BodyInserters.MultipartInserter body, String uri, HttpStatus expectedResponceStatus, Boolean setParam) throws JSONException {

        result = new String(webTestClient.method(requestMethod).uri(uri)
                .header("Authorization", "Basic " + Base64Utils
                        .encodeToString(("ccsdkapps" + ":" + "ccsdkapps").getBytes(UTF_8)))
                .body(body)
                .exchange()
                .expectStatus().isEqualTo(expectedResponceStatus)
                .expectBody()
                .returnResult().getResponseBody());

        if (setParam) {
            JSONObject jsonResponse = new JSONObject(result);
            JSONObject blueprintModelSearchJSON = jsonResponse.getJSONObject("blueprintModel");
            Gson gson = new Gson();
            BlueprintModelSearch blueprintModelSearch = gson.fromJson(blueprintModelSearchJSON.toString(), BlueprintModelSearch.class);
            id = blueprintModelSearch.getId();
            name = blueprintModelSearch.getArtifactName();
            version = blueprintModelSearch.getArtifactVersion();
            tag = blueprintModelSearch.getTags();
        }
    }
}