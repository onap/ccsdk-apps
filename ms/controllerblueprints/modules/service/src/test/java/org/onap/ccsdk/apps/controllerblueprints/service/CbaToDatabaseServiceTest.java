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

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.onap.ccsdk.apps.controllerblueprints.TestApplication;
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException;
import org.onap.ccsdk.apps.controllerblueprints.service.domain.ConfigModel;
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
    @Value("${load.blueprintsPath}")
    private String path;
    //Directory that would be used by the service to read the blueprint structure
    private String cbaDirectory;
    //Path in which the cbaDirectory would be found
    private Path blueprintPath;

    @Before
    public void setUpEnvironment(){
        try {
            blueprintPath  =   ConfigModelUtils.getCbaStorageDirectory(path);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }


    @Test
    public void storeBluePrints_sucessCase() {
        cbaDirectory = "CBA_sucessCase";
        try {

            blueprintPath   =   blueprintPath.resolve(cbaDirectory);
            ConfigModel model = databaseService.storeBluePrints(blueprintPath.toString());
            Assert.assertNotNull(model);
            Assert.assertNotNull(model.getConfigModelContents());
            Assert.assertTrue("The blueprint should have 2 templates",model.getConfigModelContents().size() == 2);
            Assert.assertTrue("Artifact name should be activation-blueprint","activation-blueprint".equals(model.getArtifactName()));

        } catch (BluePrintException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expected = BluePrintException.class)
    public void storeBluePrints_badDirectoryPath() throws BluePrintException {
        cbaDirectory = "CBA_badDirectoryPath";
        blueprintPath   =   blueprintPath.resolve(cbaDirectory);
        ConfigModel model = null;
        try {
            model = databaseService.storeBluePrints(blueprintPath.toString());

        } catch (BluePrintException e) {
            Assert.assertNull(model);
            String exceptionMessage = "Load config model starter-blueprint error : File '"+blueprintPath.toString()+"/starter-blueprint/TOSCA-Metadata/TOSCA.meta' does not exist";
            Assert.assertTrue(exceptionMessage.equals(e.getMessage()));
            throw e;
        }
    }

    @Test(expected = BluePrintException.class)
    public void storeBluePrints_invalidContent() throws BluePrintException {
        cbaDirectory = "CBA_invalidContent";
        blueprintPath   =   blueprintPath.resolve(cbaDirectory);
        ConfigModel model = null;
        try {
            model = databaseService.storeBluePrints(blueprintPath.toString());

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
        blueprintPath   =   blueprintPath.resolve(cbaDirectory);
        ConfigModel model = null;
        try {
            model = databaseService.storeBluePrints(blueprintPath.toString());

        } catch (BluePrintException e) {
            Assert.assertNull(model);
            String exceptionMessage = "Load config model com error : File '"+blueprintPath.toString()+"/com/TOSCA-Metadata/TOSCA.meta' does not exist";
            Assert.assertTrue(exceptionMessage.equals(e.getMessage()));
            throw e;
        }
    }


}
