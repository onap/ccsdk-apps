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
import org.onap.ccsdk.apps.controllerblueprints.service.utils.ConfigModelUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;


/**
 * CbaCompressionServiceTest.java Purpose: Test the decompressing method of CbaCompressionService
 *
 * @author Vinal Patel
 * @version 1.0
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TestApplication.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CbaCompressionServiceTest {

    @Value("${load.blueprintsPath}")
    private String cbaPath;
    private String zipfile;
    private String directorypath;
    private Path zipfilepath;


    /**
     *
     */
    @Before
    public void setUp() {
        try {
            zipfilepath = ConfigModelUtils.getCbaStorageDirectory(cbaPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        zipfile = "CBA_Zip_Test.zip";
        directorypath = zipfilepath.resolve(zipfile.substring(0,zipfile.lastIndexOf("."))).toAbsolutePath().toString();
    }
    @After
    public void clenup() throws BluePrintException {

        try {
            //Delete the Zip file from the repository
            ConfigModelUtils.deleteCBA(directorypath, zipfilepath);

        }
        catch (IOException ex){
            throw new BluePrintException("Fail while cleaning up CBA saved!", ex);
        }finally {
            File file = new File(zipfilepath.toString());
            file.setWritable(true);
        }
    }

    /**
     * @throws BluePrintException
     * Test will get success if it is able to decompress CBA file and returns the folder path
     */
    @Test
        public void testDecompressCBAFile_success() throws BluePrintException {
        Assert.assertEquals(directorypath,CbaCompressionService.decompressCBAFile(zipfile,zipfilepath));
        }

    /**
     *
     * @throws BluePrintException
     * Test get exception as folder on which it is decompressing CBA is having readonly permissions
     */
    @Test(expected = BluePrintException.class)
    public void testDecompressCBAFile_failure() throws BluePrintException {
        File file = new File(zipfilepath.toString());
        file.setReadOnly();
        Assert.assertEquals(directorypath,CbaCompressionService.decompressCBAFile(zipfile,zipfilepath));
    }

}
