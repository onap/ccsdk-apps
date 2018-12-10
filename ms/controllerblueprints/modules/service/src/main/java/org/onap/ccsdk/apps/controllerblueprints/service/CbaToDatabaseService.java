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
import org.apache.commons.collections.CollectionUtils;
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException;
import org.onap.ccsdk.apps.controllerblueprints.service.domain.ConfigModel;
import org.onap.ccsdk.apps.controllerblueprints.service.repository.ConfigModelContentRepository;
import org.onap.ccsdk.apps.controllerblueprints.service.repository.ConfigModelRepository;
import org.onap.ccsdk.apps.controllerblueprints.service.utils.ConfigModelUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class acts as a Rest Service that would store in the Database the Blueprints.
 * @author Ruben Chang
 */

@Service
public class CbaToDatabaseService {

    //Log used to trace the transactions using the EELFLogger class
    private static EELFLogger log = EELFManager.getInstance().getLogger(CbaToDatabaseService.class);

    private ConfigModelRepository configModelRepository;
    private ConfigModelContentRepository configModelContentRepository;
    private ConfigModelCreateService configModelCreateService;

    /**
     * Constructor of the class
     * @param configModelRepository        configModelRepository
     * @param configModelContentRepository configModelContentRepository
     * @param configModelCreateService     configModelCreateService
     */
    public CbaToDatabaseService(ConfigModelRepository configModelRepository,
                                ConfigModelContentRepository configModelContentRepository,
                                ConfigModelCreateService configModelCreateService) {
        this.configModelRepository = configModelRepository;
        this.configModelContentRepository = configModelContentRepository;
        this.configModelCreateService = configModelCreateService;
        log.info("CbaToDatabaseService instantiation successfully");
    }

    /**
     * This method will store the blueprints into the DB on the tables CONFIG_MODEL and CONFIG_MODEL_CONTENT
     * @param path Path in which the components are stored
     * @return ConfigModel The Blueprint object stored in the DB
     */
    public ConfigModel storeBluePrints(String path) throws BluePrintException {
        log.info("*************************** storeBluePrints **********************");
        ConfigModel configModel = null;
        try {
            List<String> serviceTemplateDirs = ConfigModelUtils.getBlueprintNames(path);
            if (CollectionUtils.isNotEmpty(serviceTemplateDirs)) {
                for (String fileName : serviceTemplateDirs) {
                    try {
                        String bluePrintPath = path.concat("/").concat(fileName);
                        log.debug("***** Loading service template :  {}", bluePrintPath);
                        configModel = ConfigModelUtils.getConfigModel(bluePrintPath);
                        configModel = this.configModelCreateService.saveConfigModel(configModel);

                        log.info("Loaded service template successfully: {}", fileName);

                    } catch (Exception e) {
                        throw new BluePrintException("Load config model " + fileName + " error : "+e.getMessage());
                    }
                }
            } else {
                throw new BluePrintException("Invalid structure. The unzipped file does not contains Blueprints");
            }
        } catch (Exception e) {
            throw new BluePrintException(e.getMessage());
        }
        return configModel;
    }

}
