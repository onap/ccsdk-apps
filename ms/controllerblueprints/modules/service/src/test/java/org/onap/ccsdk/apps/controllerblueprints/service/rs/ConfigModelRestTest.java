/*
 * Copyright © 2017-2018 AT&T Intellectual Property.
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

@Deprecated
public class ConfigModelRestTest {

    /*
    private static EELFLogger log = EELFManager.getInstance().getLogger(ConfigModelRestTest.class);

    @Autowired
    ConfigModelRest configModelRest;

    ConfigModel configModel;

    String name = "vrr-test";
    String version = "1.0.0";

    @Before
    public void setUp() {

    }


    @After
    public void tearDown() {
    }

    @Deprecated
    @Test
    public void test02SaveServiceTemplate() throws Exception {
        log.info("************************ test02SaveServiceTemplate  ******************");


        configModel = ConfigModelUtils.getConfigModel("load/blueprints/vrr-test");

        configModel = configModelRest.saveConfigModel(configModel);
        Assert.assertNotNull("Failed to ConfigModel, Return object is Null", configModel);
        Assert.assertNotNull("Failed to ConfigModel Id , Return ID object is Null", configModel.getId());
        Assert.assertNotNull("Failed to ConfigModel Content, Return object is Null",
                configModel.getConfigModelContent());
    }


    @Test
    public void test03PublishServiceTemplate() throws Exception {
        log.info("** test03PublishServiceTemplate  *****************");

        ConfigModel configModel = configModelRest.getConfigModelByNameAndVersion(name, version);
        log.info("Publishing Config Model " + configModel.getId());
        configModel = configModelRest.publishConfigModel(configModel.getId());
        Assert.assertNotNull("Failed to ConfigModel, Return object is Null", configModel);
        Assert.assertNotNull("Failed to ConfigModel Id ", configModel.getId());
        Assert.assertNotNull("Failed to ConfigModel Content", configModel.getConfigModelContent());
        Assert.assertEquals("Failed to update the publish indicator", "Y", configModel.getPublished());
    }


    @Deprecated
    @Test
    public void test04GetConfigModel() throws Exception {
        log.info("** test04GetConfigModel  *****************");

        ConfigModel configModel = configModelRest.getConfigModelByNameAndVersion(name, version);
        Assert.assertNotNull("Failed to get ConfigModel for the Name (" + configModel.getArtifactName() + ") and ("
                + configModel.getArtifactVersion() + ")", configModel);
        Assert.assertNotNull("Failed to get ConfigModel Id", configModel.getId());

        configModel = configModelRest.getConfigModel(configModel.getId());
        Assert.assertNotNull("Failed to get ConfigModel for the Id (" + configModel.getId() + ") ", configModel);

    }

    @Test
    public void test07SearchConfigModels() throws Exception {
        log.info("** test07SearchConfigModels  *****************");

        List<ConfigModel> configModels = configModelRest.searchConfigModels("vrr-test");
        Assert.assertNotNull("Failed to search ConfigModel", configModels);
        Assert.assertTrue("Failed to search ConfigModel with count", configModels.size() > 0);
        // update the ServiceModelContent
    }


    @Test
    public void test08DeleteConfigModels() throws Exception {
        log.info("** test08DeleteConfigModels  *****************");

        ConfigModel configModel = configModelRest.getConfigModelByNameAndVersion(name, version);
        configModelRest.deleteConfigModel(configModel.getId());

    }

*/
}
