/*******************************************************************************
 * ============LICENSE_START====================================================
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

package org.onap.ccsdk.apps.cadi.taf.dos.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.*;
import org.mockito.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.onap.ccsdk.apps.cadi.Access;
import org.onap.ccsdk.apps.cadi.CachedPrincipal.Resp;
import org.onap.ccsdk.apps.cadi.CadiException;
import org.onap.ccsdk.apps.cadi.config.Config;
import org.onap.ccsdk.apps.cadi.taf.TafResp;
import org.onap.ccsdk.apps.cadi.Taf.LifeForm;
import org.onap.ccsdk.apps.cadi.taf.dos.DenialOfServiceTaf;
import org.onap.ccsdk.apps.cadi.taf.dos.DenialOfServiceTaf.Counter;

public class JU_DenialOfServiceTaf {

    @Mock
    HttpServletResponse respMock;

    @Mock
    HttpServletRequest reqMock1;

    @Mock
    HttpServletRequest reqMock2;

    @Mock
    HttpServletRequest reqMock3;

    @Mock
    Access accessMock;

    private File dosIPFile;
    private File dosIDFile;
    private File dosDir;
    private final String dosDirName = "test";

    private final String id1 = "id1";
    private final String id2 = "id2";

    private final String ip1 = "111.111.111.111";
    private final String ip2 = "222.222.222.222";

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        dosDir = new File(dosDirName);
        dosDir.mkdirs();
        dosIPFile = new File(dosDirName, "/dosIP");
        dosIDFile = new File(dosDirName, "/dosID");
        dosIPFile.delete();
        dosIDFile.delete();

        when(accessMock.getProperty(Config.AAF_DATA_DIR, null)).thenReturn(dosDirName);
        when(reqMock1.getRemoteAddr()).thenReturn(ip1);
        when(reqMock2.getRemoteAddr()).thenReturn(ip2);

        setPrivateField(DenialOfServiceTaf.class, "deniedIP", null);
        setPrivateField(DenialOfServiceTaf.class, "deniedID", null);
        setPrivateField(DenialOfServiceTaf.class, "dosIP", null);
        setPrivateField(DenialOfServiceTaf.class, "dosID", null);
    }

    @After
    public void tearDown() {
        dosIPFile = new File(dosDirName, "/dosIP");
        dosIDFile = new File(dosDirName, "/dosID");
        dosIPFile.delete();
        dosIDFile.delete();
    }

    @Test
    public void constructorTest() throws CadiException {
        @SuppressWarnings("unused")
        DenialOfServiceTaf dost;

        // coverage...
        when(accessMock.getProperty(Config.AAF_DATA_DIR, null)).thenReturn(null);
        dost = new DenialOfServiceTaf(accessMock);

        when(accessMock.getProperty(Config.AAF_DATA_DIR, null)).thenReturn(dosDirName);
        dost = new DenialOfServiceTaf(accessMock);

        // more coverage...
        dost = new DenialOfServiceTaf(accessMock);

        // more coverage...
        setPrivateField(DenialOfServiceTaf.class, "dosID", null);
        dost = new DenialOfServiceTaf(accessMock);
    }

    @Test
    public void validateTest() throws CadiException {
        DenialOfServiceTaf dost;
        TafResp tafResp;

        dost = new DenialOfServiceTaf(accessMock);
        tafResp = dost.validate(LifeForm.SBLF, reqMock1, respMock);

        assertThat(tafResp.desc(), is("Not processing this transaction: This Transaction is not denied"));
        assertThat(tafResp.taf(), is("DenialOfServiceTaf"));

        assertThat(DenialOfServiceTaf.denyIP(ip1), is(true));

        tafResp = dost.validate(LifeForm.SBLF, reqMock1, respMock);
        assertThat(tafResp.desc(), is(ip1 + " is on the IP Denial list"));

        tafResp = dost.validate(LifeForm.SBLF, reqMock2, respMock);
        assertThat(tafResp.desc(), is("Not processing this transaction: This Transaction is not denied"));
        assertThat(tafResp.taf(), is("DenialOfServiceTaf"));
    }

    @Test
    public void revalidateTest() throws CadiException {
        DenialOfServiceTaf dost = new DenialOfServiceTaf(accessMock);
        Resp resp = dost.revalidate(null, null);
        assertThat(resp, is(Resp.NOT_MINE));
    }

    @Test
    public void denyIPTest() throws CadiException {
        assertThat(DenialOfServiceTaf.isDeniedIP(ip1), is(nullValue()));
        assertThat(DenialOfServiceTaf.denyIP(ip1), is(true));  // true because it's been added
        assertThat(DenialOfServiceTaf.denyIP(ip2), is(true));  // true because it's been added
        assertThat(DenialOfServiceTaf.denyIP(ip1), is(false)); // false because it's already been added
        assertThat(DenialOfServiceTaf.denyIP(ip2), is(false)); // false because it's already been added

        Counter counter;
        counter = DenialOfServiceTaf.isDeniedIP(ip1);
        assertThat(counter.getName(), is(ip1));
        assertThat(counter.getCount(), is(0));
        assertThat(counter.getLast(), is(0L));
        assertThat(counter.toString(), is(ip1 + " is on the denied list, but has not attempted Access" ));

        DenialOfServiceTaf dost = new DenialOfServiceTaf(accessMock);
        dost.validate(LifeForm.SBLF, reqMock1, respMock);
        long approxTime = System.currentTimeMillis();

        counter = DenialOfServiceTaf.isDeniedIP(ip1);
        assertThat(counter.getName(), is(ip1));
        assertThat(counter.getCount(), is(1));
        assertThat((Math.abs(approxTime - counter.getLast()) < 10), is(true));
        assertThat(counter.toString().contains(ip1), is(true));
        assertThat(counter.toString().contains(" has been denied 1 times since "), is(true));
        assertThat(counter.toString().contains(".  Last denial was "), is(true));

        // coverage...
        dost.validate(LifeForm.SBLF, reqMock1, respMock);

        assertThat(DenialOfServiceTaf.removeDenyIP(ip1), is(true));
        assertThat(DenialOfServiceTaf.removeDenyIP(ip1), is(false));
        assertThat(DenialOfServiceTaf.removeDenyIP(ip2), is(true));
        assertThat(DenialOfServiceTaf.removeDenyIP(ip2), is(false));
    }

    @Test
    public void denyIDTest() throws CadiException {
        assertThat(DenialOfServiceTaf.isDeniedID(id1), is(nullValue()));
        assertThat(DenialOfServiceTaf.denyID(id1), is(true));  // true because it's been added
        assertThat(DenialOfServiceTaf.denyID(id2), is(true));  // true because it's been added
        assertThat(DenialOfServiceTaf.denyID(id1), is(false)); // false because it's already been added
        assertThat(DenialOfServiceTaf.denyID(id2), is(false)); // false because it's already been added

        Counter counter;
        counter = DenialOfServiceTaf.isDeniedID(id1);
        assertThat(counter.getName(), is(id1));
        assertThat(counter.getCount(), is(0));
        assertThat(counter.getLast(), is(0L));

        assertThat(DenialOfServiceTaf.removeDenyID(id1), is(true));
        assertThat(DenialOfServiceTaf.removeDenyID(id1), is(false));
        assertThat(DenialOfServiceTaf.removeDenyID(id2), is(true));
        assertThat(DenialOfServiceTaf.removeDenyID(id2), is(false));
    }

    @Test
    public void reportTest() throws CadiException {
        DenialOfServiceTaf dost = new DenialOfServiceTaf(accessMock);
        List<String> denials = dost.report();
        assertThat(denials.size(), is(0));

        DenialOfServiceTaf.denyID(id1);
        DenialOfServiceTaf.denyID(id2);

        DenialOfServiceTaf.denyIP(ip1);
        DenialOfServiceTaf.denyIP(ip2);

        denials = dost.report();
        assertThat(denials.size(), is(4));
        for (String denied : denials) {
            switch (denied.split(" ", 2)[0]) {
                case ip1:
                case ip2:
                case id1:
                case id2:
                    break;
                default:
                    fail("The line: [" + denied + "] shouldn't be in the report");
            }
        }
    }

    @Test
    public void respDenyIDTest() {
        TafResp tafResp = DenialOfServiceTaf.respDenyID(accessMock, id1);
        assertThat(tafResp.desc(), is(id1 + " is on the Identity Denial list"));
    }

    @Test
    public void ipFileIOTest() throws CadiException, IOException {
        @SuppressWarnings("unused")
        DenialOfServiceTaf dost;

        dosIPFile.createNewFile();

        // coverage...
        DenialOfServiceTaf.denyIP(ip1);
        DenialOfServiceTaf.removeDenyIP(ip1);

        dost = new DenialOfServiceTaf(accessMock);
        DenialOfServiceTaf.denyIP(ip1);
        DenialOfServiceTaf.denyIP(ip2);
        // coverage...
        DenialOfServiceTaf.denyIP(ip2);

        String contents = readContentsFromFile(dosIPFile);
        assertThat(contents.contains(ip1), is(true));
        assertThat(contents.contains(ip2), is(true));

        // Removing all ips should delete the file
        assertThat(dosIPFile.exists(), is(true));
        DenialOfServiceTaf.removeDenyIP(ip1);
        DenialOfServiceTaf.removeDenyIP(ip2);
        assertThat(dosIPFile.exists(), is(false));

        dosIPFile.createNewFile();

        DenialOfServiceTaf.denyIP(ip1);
        DenialOfServiceTaf.denyIP(ip2);

        setPrivateField(DenialOfServiceTaf.class, "dosIP", null);
        dost = new DenialOfServiceTaf(accessMock);

        contents = readContentsFromFile(dosIPFile);
        assertThat(contents.contains(ip1), is(true));
        assertThat(contents.contains(ip2), is(true));

        dosIPFile.delete();

        // coverage...
        setPrivateField(DenialOfServiceTaf.class, "deniedIP", null);
        DenialOfServiceTaf.denyIP(ip1);
        dosIPFile.delete();
        DenialOfServiceTaf.removeDenyIP(ip1);

        // coverage...
        dosIPFile.delete();
        setPrivateField(DenialOfServiceTaf.class, "dosIP", null);
        dost = new DenialOfServiceTaf(accessMock);
    }

    @Test
    public void idFileIOTest() throws CadiException, IOException {
        @SuppressWarnings("unused")
        DenialOfServiceTaf dost;

        dosIDFile.createNewFile();

        // coverage...
        DenialOfServiceTaf.denyID(id1);
        DenialOfServiceTaf.removeDenyID(id1);

        dost = new DenialOfServiceTaf(accessMock);
        DenialOfServiceTaf.denyID(id1);
        DenialOfServiceTaf.denyID(id2);
        // coverage...
        DenialOfServiceTaf.denyID(id2);

        String contents = readContentsFromFile(dosIDFile);
        assertThat(contents.contains(id1), is(true));
        assertThat(contents.contains(id2), is(true));

        // Removing all ids should delete the file
        assertThat(dosIDFile.exists(), is(true));
        DenialOfServiceTaf.removeDenyID(id1);
        DenialOfServiceTaf.removeDenyID(id2);
        assertThat(dosIDFile.exists(), is(false));

        dosIDFile.createNewFile();

        DenialOfServiceTaf.denyID(id1);
        DenialOfServiceTaf.denyID(id2);

        setPrivateField(DenialOfServiceTaf.class, "dosID", null);
        dost = new DenialOfServiceTaf(accessMock);

        contents = readContentsFromFile(dosIDFile);
        assertThat(contents.contains(id1), is(true));
        assertThat(contents.contains(id2), is(true));

        dosIDFile.delete();

        // coverage...
        setPrivateField(DenialOfServiceTaf.class, "deniedID", null);
        DenialOfServiceTaf.denyID(id1);
        dosIDFile.delete();
        DenialOfServiceTaf.removeDenyID(id1);

        // coverage...
        dosIDFile.delete();
        setPrivateField(DenialOfServiceTaf.class, "dosID", null);
        dost = new DenialOfServiceTaf(accessMock);
    }

    private void setPrivateField(Class<?> clazz, String fieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
            field.setAccessible(false);
        } catch (Exception e) {
            System.err.println("Could not set field [" + fieldName + "] to " + value);
        }
    }

    private String readContentsFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

}
