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

/**
 * CbaService.java Purpose: Provide Service Template Service processing CbaService
 *
 * @author Steve Siani
 * @version 1.0
 */

@Deprecated
public class CbaService {
    /*

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



    @EventListener(ApplicationReadyEvent.class)
    private void initCbaService() {
        this.cbaLocation = BluePrintFileUtils.Companion.getCbaStorageDirectory(cbaArchivePath);
        log.info("CBA service Initiated...");
    }


    public Mono<BlueprintInfoResponse> uploadCBAFile(FilePart filePart) {

        try {
            return this.cbaFileManagementService.saveCBAFile(filePart, cbaLocation).map(fileName ->
                    bluePrintCatalogService.uploadToDataBase(cbaLocation.resolve(fileName).toString()));

        } catch (IOException | BluePrintException e) {
            return Mono.error(new BluePrintException("Error uploading the CBA file in channel.", e));
        }
    }


    public void deleteCBA(@NotNull Long id) throws BluePrintException {
        this.bluePrintDatabaseService.deleteCBA(id);
    }


    public ResponseEntity<Resource> downloadCBAFile(@NotNull String id) {
        BlueprintFileResponse blueprintFileResponse = this.bluePrintCatalogService.downloadBlueprintArchive(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/plain"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + blueprintFileResponse.getName() + "\"")
                .body(new ByteArrayResource(blueprintFileResponse.getFile()));
    }


    public BlueprintInfoResponse findCBAByID(@NotNull String id) {
        return bluePrintCatalogService.findBlueprintById(id);
    }


    public List<BlueprintInfoResponse> findAllCBA() {
        return bluePrintCatalogService.findAllBlueprint();
    }


    public BlueprintInfoResponse findCBAByNameAndVersion(@NotNull String name, @NotNull String version) throws BluePrintException {
        return null;
    }
    */
}