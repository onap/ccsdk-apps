/*******************************************************************************
 * * org.onap.ccsdk
 * * ===========================================================================
 * * Copyright © 2023 AT&T Intellectual Property. All rights reserved.
 * * ===========================================================================
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 * *
 *  * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 * * ============LICENSE_END====================================================
 * *
 * *
 ******************************************************************************/

package org.onap.ccsdk.apps.cadi.util.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import org.junit.*;

import org.onap.ccsdk.apps.cadi.util.Chmod;

public class JU_Chmod {

    private File file;
    private String filePath;

    @Before
    public void setup() throws IOException {
        file = File.createTempFile("chmod_test", "");
        filePath = file.getAbsolutePath();
    }

    @After
    public void tearDown() {
        file.delete();
    }

    @Test
    public void to755Test() throws IOException {
        Chmod.to755.chmod(file);
        Set<PosixFilePermission> set = Files.getPosixFilePermissions(Paths.get(filePath));
        assertThat(PosixFilePermissions.toString(set), is("rwxr-xr-x"));
    }

    @Test
    public void to644Test() throws IOException {
        Chmod.to644.chmod(file);
        Set<PosixFilePermission> set = Files.getPosixFilePermissions(Paths.get(filePath));
        assertThat(PosixFilePermissions.toString(set), is("rw-r--r--"));
    }

    @Test
    public void to400Test() throws IOException {
        Chmod.to400.chmod(file);
        Set<PosixFilePermission> set = Files.getPosixFilePermissions(Paths.get(filePath));
        assertThat(PosixFilePermissions.toString(set), is("r--------"));
    }

}
