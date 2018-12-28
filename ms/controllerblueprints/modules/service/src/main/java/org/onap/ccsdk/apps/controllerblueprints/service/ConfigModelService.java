/*
 * Copyright © 2017-2018 AT&T Intellectual Property.
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
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintCatalogService;
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintFileUtils;
import org.onap.ccsdk.apps.controllerblueprints.service.domain.ConfigModel;
import org.onap.ccsdk.apps.controllerblueprints.service.load.BluePrintLoadConfiguration;
import org.onap.ccsdk.apps.controllerblueprints.service.repository.ConfigModelContentRepository;
import org.onap.ccsdk.apps.controllerblueprints.service.repository.ConfigModelRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ConfigModelService.java Purpose: Provide Service Template Service processing ConfigModelService
 *
 * @author Brinda Santh
 * @version 1.0
 */

@Service
public class ConfigModelService {

    private static EELFLogger log = EELFManager.getInstance().getLogger(ConfigModelService.class);

    private BluePrintLoadConfiguration bluePrintLoadConfiguration;
    private BluePrintCatalogService bluePrintCatalogService;
    private ConfigModelRepository configModelRepository;
    private ConfigModelContentRepository configModelContentRepository;
    private CbaFileManagementService cbaFileManagementService;
    private static final String CONFIG_MODEL_ID_FAILURE_MSG = "failed to get config model id(%d) from repo";

    /**
     * This is a ConfigModelService constructor.
     *
     * @param configModelRepository        configModelRepository
     * @param configModelContentRepository configModelContentRepository
     */
    public ConfigModelService(BluePrintLoadConfiguration bluePrintLoadConfiguration,
                              BluePrintCatalogService bluePrintCatalogService,
                              ConfigModelRepository configModelRepository,
                              ConfigModelContentRepository configModelContentRepository,
                              CbaFileManagementService cbaFileManagementService) {
        this.bluePrintLoadConfiguration = bluePrintLoadConfiguration;
        this.bluePrintCatalogService = bluePrintCatalogService;
        this.configModelRepository = configModelRepository;
        this.configModelContentRepository = configModelContentRepository;
        this.cbaFileManagementService = cbaFileManagementService;
        log.info("Config Model Service Initiated...");
    }

    /**
     * This is a saveConfigModel method
     *
     * @param filePart filePart
     * @return ConfigModel
     * @throws BluePrintException BluePrintException
     */
    public Mono<BlueprintInfoResponse> saveConfigModel(FilePart filePart) throws BluePrintException {
        try {

            Path cbaLocation = BluePrintFileUtils.Companion.getCbaStorageDirectory(bluePrintLoadConfiguration.blueprintArchivePath);
            return this.cbaFileManagementService.saveCBAFile(filePart, cbaLocation).map(fileName ->
                    bluePrintCatalogService.uploadToDataBase(cbaLocation.resolve(fileName).toString(), false));

        } catch (IOException | BluePrintException e) {
            return Mono.error(new BluePrintException("Error uploading the CBA file in channel.", e));
        }
    }

    /**
     * This is a publishConfigModel method
     *
     * @param id id
     * @return ConfigModel
     * @throws BluePrintException BluePrintException
     */
    public ConfigModel publishConfigModel(Long id) throws BluePrintException {
        // TODO Implement publish Functionality
        return null;
    }

    /**
     * This is a searchConfigModels method
     *
     * @param tags tags
     * @return ConfigModel
     */
    public List<ConfigModel> searchConfigModels(String tags) {
        List<ConfigModel> models = configModelRepository.findByTagsContainingIgnoreCase(tags);
        if (models != null) {
            for (ConfigModel configModel : models) {
                configModel.setConfigModelContent(null);
            }
        }
        return models;
    }

    /**
     * This is a getConfigModelByNameAndVersion method
     *
     * @param name    name
     * @param version version
     * @return ConfigModel
     */
    public ResponseEntity<Resource> getConfigModelByNameAndVersion(@NotNull String name, String version) throws BluePrintException {
        BlueprintFileResponse blueprintFileResponse = this.bluePrintCatalogService.downloadFromDataBase(name, version);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/plain"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + blueprintFileResponse.getName() + "\"")
                .body(new ByteArrayResource(blueprintFileResponse.getFile()));
    }

    /**
     * This is a downloadCBAFile method to find the target file to download and return a file ressource using MONO
     *
     * @param (id)
     * @return ResponseEntity<Resource>
     */
    public ResponseEntity<Resource> downloadCBAFile(@NotNull String id) {

        String path = bluePrintLoadConfiguration.blueprintArchivePath + "/" + UUID.randomUUID().toString() + ".zip";
        BlueprintFileResponse blueprintFileResponse = this.bluePrintCatalogService.downloadFromDataBase(id, path);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/plain"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + blueprintFileResponse.getName() + "\"")
                .body(new ByteArrayResource(blueprintFileResponse.getFile()));
    }

    /**
     * This is a getConfigModel method
     *
     * @param id id
     * @return ConfigModel
     * @throws BluePrintException BluePrintException
     */
    public ConfigModel getConfigModel(@NotNull String id) throws BluePrintException {
        ConfigModel configModel;
        Optional<ConfigModel> dbConfigModel = configModelRepository.findById(id);
        if (dbConfigModel.isPresent()) {
            configModel = dbConfigModel.get();
        } else {
            throw new BluePrintException(String.format(CONFIG_MODEL_ID_FAILURE_MSG, id));
        }

        return configModel;
    }

    /**
     * This is a deleteConfigModel method
     *
     * @param id id
     * @throws BluePrintException BluePrintException
     */

    @Transactional
    public void deleteConfigModel(@NotNull String id) throws BluePrintException {
        Optional<ConfigModel> dbConfigModel = configModelRepository.findById(id);
        if (dbConfigModel.isPresent()) {
            configModelContentRepository.deleteByConfigModel(dbConfigModel.get());
            configModelRepository.delete(dbConfigModel.get());
        } else {
            throw new BluePrintException(String.format(CONFIG_MODEL_ID_FAILURE_MSG, id));
        }
    }

}
