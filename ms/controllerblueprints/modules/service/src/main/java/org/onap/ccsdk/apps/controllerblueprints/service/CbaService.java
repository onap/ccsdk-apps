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
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException;
import org.onap.ccsdk.apps.controllerblueprints.service.domain.ConfigModel;
import org.onap.ccsdk.apps.controllerblueprints.service.model.UploadCbaResponse;
import org.onap.ccsdk.apps.controllerblueprints.service.utils.ConfigModelUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;

/**
 * CbaService.java Purpose: Provide Service Template Service processing CbaService
 *
 * @author Steve Siani
 * @version 1.0
 */

@Service
public class CbaService {

    private static EELFLogger log = EELFManager.getInstance().getLogger(CbaService.class);

    @Value("${load.blueprintsPath}")
    private String cbaPath;
    private Path cbaLocation;

    private CbaFileManagementService cbaFileManagementService;
    private CbaCompressionService cbaCompressionService;
    private CbaToDatabaseService cbaToDatabaseService;


    /**
     * This is a CbaService constructor.
     */
    public CbaService(CbaFileManagementService cbaFileManagementService, CbaCompressionService cbaCompressionService, CbaToDatabaseService cbaToDatabaseService) {

        this.cbaFileManagementService = cbaFileManagementService;
        this.cbaCompressionService = cbaCompressionService;
        this.cbaToDatabaseService = cbaToDatabaseService;

        log.info("CBA Service Initiated...");
    }

    /**
     * This method would be used by SpringBoot to initialize the cba location
     */
    @EventListener(ApplicationReadyEvent.class)
    private void initCbaService() {
        try {
            this.cbaLocation = ConfigModelUtils.getCbaStorageDirectory(cbaPath);
        } catch (Exception e) {
            log.error("Failed in Config Model Storage Service loading", e);
        }
    }

    /**
     * This is a deleteCBA method
     *
     * @param cbaID cbaID
     * @return
     * @throws BluePrintException BluePrintException
     */
    public void deleteCBA(String cbaID){

        //TODO: Call the method to delete Models related to CBA from database
    }

    /**
     * This is a saveCBAFile method
     * tske a {@link FilePart}, transfer it to disk using {@link AsynchronousFileChannel}s and return a {@link Mono} representing the result
     *
     * @param filePart - the request part containing the file to be saved
     * @return a {@link Mono<UploadCbaResponse>} representing the result of the operation
     */
    public Mono<UploadCbaResponse> saveCBAFile(FilePart filePart) {

        try {
                return this.cbaFileManagementService.saveCBAFile(filePart, cbaLocation).map(fileName -> {
                ConfigModel configModel;
                UploadCbaResponse uploadCbaResponse;
                try {
                    String cbaDirectory = this.cbaCompressionService.decompressCBAFile(fileName, cbaLocation);
                    configModel = this.cbaToDatabaseService.storeBluePrints(cbaDirectory);
                    uploadCbaResponse = new UploadCbaResponse(configModel.getId(), configModel.getArtifactName());//TODO: plan a good CBA response
                } catch (BluePrintException ex) {
                    uploadCbaResponse = new UploadCbaResponse((short) -1, ex.getCode(), ex.getMessage());
                } finally {
                    try {
                        this.cbaFileManagementService.cleanupSavedCBA(fileName, cbaLocation);
                    } catch (BluePrintException be) {
                        uploadCbaResponse = new UploadCbaResponse((short) -1, be.getCode(), be.getMessage());
                    }
                }
                return uploadCbaResponse;
            });

        } catch (IOException e) {
            UploadCbaResponse uploadCbaResponse = new UploadCbaResponse((short) -1, 100, "Error uploading the CBA file in channel \n" + e.getMessage());
            return Mono.just(uploadCbaResponse);
        }
        catch (BluePrintException be) {
            UploadCbaResponse uploadCbaResponse = new UploadCbaResponse((short) -1, be.getCode(), "Error In the CBA file \n" + be.getMessage());
            return Mono.just(uploadCbaResponse);
        }
    }
}