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

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.onap.ccsdk.apps.controllerblueprints.TestApplication;
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException;
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintArchiveUtils;
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintFileUtils;
import org.onap.ccsdk.apps.controllerblueprints.service.domain.ConfigModel;
import org.onap.ccsdk.apps.controllerblueprints.service.utils.CbaStateEnum;
import org.onap.ccsdk.apps.controllerblueprints.service.utils.ConfigModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Path;


/**
 * Class that would test the blueprint storage method to the DataBase.
 * @author Ruben Chang
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TestApplication.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CbaToDatabaseServiceTest {

    //Service that would be used to store the object in the DB
    @Autowired
    CbaToDatabaseService databaseService;
    //Path in which the blueprints are already stored. This value is taken from the .properties file
    @Value("${controllerblueprints.loadBlueprintsExamplesPath}")
    private String path;
    //Directory that would be used by the service to read the blueprint structure
    private String cbaDirectory;
    //Path in which the cbaDirectory would be found
    private Path cbaArchivePath;
    //Path of the file that would be stored as a blob in the CBA_CONTENT table
    private Path zipFilePath;

    @Before
    public void setUpEnvironment(){
        cbaArchivePath  =   BluePrintFileUtils.Companion.getCbaStorageDirectory(path);
    }


    @Test
    public void storeBluePrints_sucessCase() {
        cbaDirectory = "CBA_sucessCase";
        String fileName = "baseconfiguration.zip";
        String version = "1.0";
        String description = "CBA Sucess Case of Blueprints";
        try {
            zipFilePath = BluePrintFileUtils.Companion.getCbaStorageDirectory(cbaArchivePath.toString()).resolve("CBA_Zip_Test.zip");
            cbaArchivePath   =   cbaArchivePath.resolve(cbaDirectory);
            ConfigModel model = databaseService.storeBluePrints(cbaArchivePath.toString(), fileName, zipFilePath);
            Assert.assertNotNull(model);
            Assert.assertNotNull(model.getConfigModelContents());
            Assert.assertNotNull(model.getConfigModelCBA());
            Assert.assertEquals(fileName, model.getConfigModelCBA().getCbaName());
            Assert.assertTrue("The blueprint should have 2 templates",model.getConfigModelContents().size() == 2);
            Assert.assertTrue("Artifact name should be activation-blueprint","activation-blueprint".equals(model.getArtifactName()));
        } catch (BluePrintException be) {
            Assert.fail(be.getMessage());
        }
    }

    @Test(expected = BluePrintException.class)
    public void storeBluePrints_badDirectoryPath() throws BluePrintException {
        cbaDirectory = "CBA_badDirectoryPath";
        String fileName = "starter-blueprint.zip";
        String version = "1.0";
        String description = "CBA Bad Directory Case of Blueprints";

        ConfigModel model = null;
        try {
            zipFilePath = BluePrintFileUtils.Companion.getCbaStorageDirectory(cbaArchivePath.toString()).resolve("CBA_Zip_Test.zip");
            cbaArchivePath   =   cbaArchivePath.resolve(cbaDirectory);
            model = databaseService.storeBluePrints(cbaArchivePath.toString(), fileName, zipFilePath);

        } catch (BluePrintException e) {
            Assert.assertNull(model);
            String exceptionMessage = "Load config model starter-blueprint error : File '" + cbaArchivePath.toString() + "/starter-blueprint/TOSCA-Metadata/TOSCA.meta' does not exist";
            Assert.assertTrue(exceptionMessage.equals(e.getMessage()));
            throw e;
        }
    }

    @Test(expected = BluePrintException.class)
    public void storeBluePrints_invalidContent() throws BluePrintException {
        cbaDirectory = "CBA_invalidContent";
        String fileName = "baseconfiguration.zip";
        String version = "1.0";
        String description = "CBA Invalid Content Case of Blueprints";
        ConfigModel model = null;
        try {
            zipFilePath = BluePrintFileUtils.Companion.getCbaStorageDirectory(cbaArchivePath.toString()).resolve("CBA_Zip_Test.zip");
            cbaArchivePath   =   cbaArchivePath.resolve(cbaDirectory);
            model = databaseService.storeBluePrints(cbaArchivePath.toString(), fileName, cbaArchivePath);

        } catch (BluePrintException e) {
            Assert.assertNull(model);
            String exceptionMessage = "Load config model baseconfiguration error : failed to get Blueprint package version";
            Assert.assertTrue(exceptionMessage.equals(e.getMessage()));
            throw e;
        }
    }

    @Test(expected = BluePrintException.class)
    public void storeBluePrints_invalidStructure() throws BluePrintException {
        cbaDirectory = "CBA_invalidStructure";
        String fileName = "java.zip";
        String version = "1.0";
        String description = "CBA Invalid Structure Case of Blueprints";
        ConfigModel model = null;
        try {
            zipFilePath = BluePrintFileUtils.Companion.getCbaStorageDirectory(cbaArchivePath.toString()).resolve("CBA_Zip_Test.zip");
            cbaArchivePath   =   cbaArchivePath.resolve(cbaDirectory);
            model = databaseService.storeBluePrints(cbaArchivePath.toString(), fileName, cbaArchivePath);

        } catch (BluePrintException e) {
            Assert.assertNull(model);
            String exceptionMessage = "Load config model com error : File '"+cbaArchivePath.toString()+"/com/TOSCA-Metadata/TOSCA.meta' does not exist";
            Assert.assertTrue(exceptionMessage.equals(e.getMessage()));
            throw e;
        }
    }


}
