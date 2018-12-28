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
import org.onap.ccsdk.apps.controllerblueprints.service.domain.CbaContent;
import org.onap.ccsdk.apps.controllerblueprints.service.domain.ConfigModel;
import org.onap.ccsdk.apps.controllerblueprints.service.repository.ConfigModelContentRepository;
import org.onap.ccsdk.apps.controllerblueprints.service.repository.ConfigModelRepository;
import org.onap.ccsdk.apps.controllerblueprints.service.utils.CbaStateEnum;
import org.onap.ccsdk.apps.controllerblueprints.service.utils.ConfigModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class acts as a Rest Service that would store in the Database the Blueprints.
 * @author Ruben Chang
 */

@Service
public class BluePrintDatabaseService {

    //Log used to trace the transactions using the EELFLogger class
    private static EELFLogger log = EELFManager.getInstance().getLogger(BluePrintDatabaseService.class);

    @Autowired
    private ConfigModelRepository configModelRepository;
    @Autowired
    private ConfigModelContentRepository configModelContentRepository;
    @Autowired
    private ConfigModelCreateService configModelCreateService;	
	@Autowired
    private CBAContentService cbaContentService;

    /**
     * This method will store the blueprints into the DB on the tables CONFIG_MODEL and CONFIG_MODEL_CONTENT
     * @param (cbaDirectory, id, cbaArchiveToSave) String path in which the components are stored, CBA file name and the path to archive it self
     * @return BlueprintInfoResponse The Blueprint information object stored in the DB
     */
    public BlueprintInfoResponse storeBluePrints(String cbaDirectory, String id, File cbaArchiveToSave) throws BluePrintException {
        log.info("*************************** storeBluePrints **********************");
        ConfigModel configModel;
        CbaContent cbaContent;
        String version = "1.0";//TODO Read these information from metadata
        String description = "Initial description for CBA archive " + cbaArchiveToSave.getName();//TODO

        try {
            configModel = ConfigModelUtils.getConfigModel(cbaDirectory);
            log.info("Loaded service template successfully: {}", cbaDirectory);
        } catch (Exception e) {
            throw new BluePrintException("Load config model " + cbaDirectory + " error : "+e.getMessage());
        }

        byte[] file;
        try {
            file = Files.readAllBytes(cbaArchiveToSave.toPath());
        } catch (IOException e) {
            throw new BluePrintException("Fail to read the CBA to save in database.", e);
        }

        cbaContent = this.cbaContentService.saveCBAContent(id, cbaArchiveToSave.getName(), version,
                CbaStateEnum.DRAFT.getState(), description, file);
        configModel.setConfigModelCBA(cbaContent);
        this.configModelCreateService.saveConfigModel(configModel);

        return new BlueprintInfoResponse(cbaContent.getCbaUUID(), cbaContent.getCbaName(),
                cbaContent.getCbaDescription(), cbaContent.getCbaState(), cbaContent.getCbaVersion());

    }

    /**
     * This is a deleteConfigModel method
     *
     * @param id id
     * @throws BluePrintException BluePrintException
     */
    public void deleteCBA(@NotNull Long id) throws BluePrintException {
        Optional<ConfigModel> dbConfigModel = configModelRepository.findById(id);

       //TODO: Delete CBA and ConfigModel

    }
	
	/**
     * Get a list of the controller blueprint archives
     * @return List<BlueprintInfoResponse> List with the controller blueprint archives
     */
    public List<BlueprintInfoResponse> listAllBlueprintArchive() {

        List<CbaContent> cbaContents = this.cbaContentService.getList();
        List<BlueprintInfoResponse> blueprintInfoResponses = new ArrayList<>();
        for (CbaContent cbaContent: cbaContents) {
            blueprintInfoResponses.add(new BlueprintInfoResponse(cbaContent.getCbaUUID(), cbaContent.getCbaName(),
                    cbaContent.getCbaDescription(), cbaContent.getCbaState(), cbaContent.getCbaVersion()));
        }
        return blueprintInfoResponses;
    }

    /**
     * get a Controller Blueprint Archive by UUID
     * @param uuID the User Identifier Controller Blueprint archive
     * @return BlueprintInfoResponse the Controller Blueprint archive information
     */
    public BlueprintInfoResponse getBlueprintByUUID(String uuID) {
        CbaContent cbaContent =  findByUUID(uuID);
        return new BlueprintInfoResponse(cbaContent.getCbaUUID(), cbaContent.getCbaName(),
                cbaContent.getCbaDescription(), cbaContent.getCbaState(), cbaContent.getCbaVersion());
    }

    /**
     * Method getBlueprintArchiveByUUID Find a Controller Blueprint Archive by UUID
     * @param uuID the User Identifier Controller Blueprint archive
     * @return BlueprintFileResponse the Blueprint information with file in byte[]
     */
    public BlueprintFileResponse getBlueprintArchiveByUUID(String uuID) {
        CbaContent cbaContent =  findByUUID(uuID);
        return new BlueprintFileResponse(cbaContent.getCbaUUID(), cbaContent.getCbaName(),
                cbaContent.getCbaDescription(), cbaContent.getCbaState(),
                cbaContent.getCbaVersion(), cbaContent.getCbaFile());
    }

    /**
     * Find a Controller Blueprint by UUID
     * @param uuID the User Identifier Controller Blueprint archive
     * @return CbaContent the Controller Blueprint archive information
     */
    private CbaContent findByUUID(String uuID) {
        Optional<CbaContent> optionalCbaContent = this.cbaContentService.findByUUID(uuID);
        return optionalCbaContent.get();
    }
}