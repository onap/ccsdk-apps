/*
 * Copyright © 2018 IBM Intellectual Property.
 * Modifications Copyright © 2018 IBM.
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

package org.onap.ccsdk.apps.controllerblueprints.service;

import com.att.eelf.configuration.EELFLogger;
import com.att.eelf.configuration.EELFManager;
import org.jetbrains.annotations.NotNull;
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException;
import org.onap.ccsdk.apps.controllerblueprints.core.data.BlueprintFileResponse;
import org.onap.ccsdk.apps.controllerblueprints.core.data.BlueprintInfoResponse;
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintFileUtils;
import org.onap.ccsdk.apps.controllerblueprints.service.load.BluePrintCatalogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * CbaService.java Purpose: Provide Service Template Service processing CbaService
 *
 * @author Steve Siani
 * @version 1.0
 */

@Service
public class CbaService {

    private static EELFLogger log = EELFManager.getInstance().getLogger(CbaService.class);

    @Value("${controllerblueprints.blueprintArchivePath}")
    private String cbaArchivePath;
    private Path cbaLocation;

    @Autowired
    private CbaFileManagementService cbaFileManagementService;

    @Autowired
    private BluePrintDatabaseService bluePrintDatabaseService;

    @Autowired
    private BluePrintCatalogServiceImpl bluePrintCatalogService;


    /**
     * This method would be used by SpringBoot to initialize the cba location
     */
    @EventListener(ApplicationReadyEvent.class)
    private void initCbaService() {
        this.cbaLocation = BluePrintFileUtils.Companion.getCbaStorageDirectory(cbaArchivePath);
        log.info("CBA service Initiated...");
    }

    /**
     * This is a uploadCBAFile method
     * take a {@link FilePart}, transfer it to disk using WebFlux and return a {@link Mono} representing the result
     *
     * @param filePart - the request part containing the file to be saved
     * @return a {@link Mono<  String  >} representing the result of the operation
     */
    public Mono<BlueprintInfoResponse> uploadCBAFile(FilePart filePart) {

        try {
            return this.cbaFileManagementService.saveCBAFile(filePart, cbaLocation).map(fileName ->
                    bluePrintCatalogService.uploadToDataBase(cbaLocation.resolve(fileName).toString()));

        } catch (IOException | BluePrintException e) {
            return Mono.error(new BluePrintException("Error uploading the CBA file in channel.", e));
        }
    }

    /**
     * This is a deleteCba method
     *
     * @param id id
     * @throws BluePrintException BluePrintException
     */
    public void deleteCBA(@NotNull Long id) throws BluePrintException {
        this.bluePrintDatabaseService.deleteCBA(id);
    }

    /**
     * This is a downloadCBAFile method to find the target file to download and return a file ressource using MONO
     *
     * @param (id)
     * @return ResponseEntity<Resource>
     */
    public ResponseEntity<Resource> downloadCBAFile(@NotNull String id) {
        BlueprintFileResponse blueprintFileResponse = this.bluePrintCatalogService.downloadBlueprintArchive(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/plain"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + blueprintFileResponse.getName() + "\"")
                .body(new ByteArrayResource(blueprintFileResponse.getFile()));
    }

    /**
     * This is a findCBAByID method to find a CBA By the UUID
     *
     * @param (id)
     * @return BlueprintInfoResponse
     */
    public BlueprintInfoResponse findCBAByID(@NotNull String id) {
        return bluePrintCatalogService.findBlueprintById(id);
    }

    /**
     * This is a findAllCBA method to retrieve all the CBAs in Database
     *
     * @return List<BlueprintInfoResponse> list with the controller blueprint archives
     */
    public List<BlueprintInfoResponse> findAllCBA() {
        return bluePrintCatalogService.findAllBlueprint();
    }

    /**
     * This is a findCBAByNameAndVersion method to find a CBA by Name and version
     *
     * @param (name, version)
     * @return BlueprintInfoResponse
     * @throws BluePrintException BluePrintException
     */
    public BlueprintInfoResponse findCBAByNameAndVersion(@NotNull String name, @NotNull String version) throws BluePrintException {
        return null;
    }
}