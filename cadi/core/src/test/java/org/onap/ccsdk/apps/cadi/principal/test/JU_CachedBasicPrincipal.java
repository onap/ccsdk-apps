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

package org.onap.ccsdk.apps.cadi.principal.test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;

import org.onap.ccsdk.apps.cadi.BasicCred;
import org.onap.ccsdk.apps.cadi.CachedPrincipal;
import org.onap.ccsdk.apps.cadi.principal.CachedBasicPrincipal;
import org.onap.ccsdk.apps.cadi.taf.HttpTaf;

public class JU_CachedBasicPrincipal {
    private Field creatorField;
    private Field timeToLiveField;

    @Mock
    private HttpTaf creator;

    private CachedPrincipal.Resp resp;

    @Before
    public void setup() throws NoSuchFieldException, SecurityException {
        MockitoAnnotations.initMocks(this);

        creatorField = CachedBasicPrincipal.class.getDeclaredField("creator");
        timeToLiveField = CachedBasicPrincipal.class.getDeclaredField("timeToLive");

        creatorField.setAccessible(true);
        timeToLiveField.setAccessible(true);
    }

    @Test
    public void Constructor1Test() throws IllegalArgumentException, IllegalAccessException {
        String name = "User";
        String password = "password";
        BasicCred bc = mock(BasicCred.class);
        when(bc.getUser()).thenReturn(name);
        when(bc.getCred()).thenReturn(password.getBytes());

        long timeToLive = 10000L;
        long expires = System.currentTimeMillis() + timeToLive;
        CachedBasicPrincipal cbp = new CachedBasicPrincipal(creator, bc, "domain", timeToLive);

        assertThat((HttpTaf)creatorField.get(cbp), is(creator));
        assertThat((Long)timeToLiveField.get(cbp), is(timeToLive));
        assertTrue(Math.abs(cbp.expires() - expires) < 10);
    }

    @Test
    public void Constructor2Test() throws Exception {
        String name = "User";
        String password = "password";
        String content = name + ":" + password;
        long timeToLive = 10000L;
        long expires = System.currentTimeMillis() + timeToLive;
        CachedBasicPrincipal cbp = new CachedBasicPrincipal(creator, content, "domain", timeToLive);

        assertThat((HttpTaf)creatorField.get(cbp), is(creator));
        assertThat((Long)timeToLiveField.get(cbp), is(timeToLive));
        assertTrue(Math.abs(cbp.expires() - expires) < 10);
    }

    @Test
    public void revalidateTest() throws IOException, IllegalArgumentException, IllegalAccessException, InterruptedException {
        resp = CachedPrincipal.Resp.REVALIDATED;
        when(creator.revalidate((CachedPrincipal)any(), any())).thenReturn(resp);

        String name = "User";
        String password = "password";
        String content = name + ":" + password;
        long timeToLive = 10000L;
        long expires = System.currentTimeMillis() + timeToLive;
        CachedBasicPrincipal cbp = new CachedBasicPrincipal(creator, content, "domain", timeToLive);

        assertTrue(Math.abs(cbp.expires() - expires) < 10);

        Thread.sleep(1);
        expires = System.currentTimeMillis() + timeToLive;
        assertThat(cbp.revalidate(new Object()), is(resp));
        assertTrue(Math.abs(cbp.expires() - expires) < 10);

        resp = CachedPrincipal.Resp.UNVALIDATED;
        when(creator.revalidate((CachedPrincipal)any(), any())).thenReturn(resp);
        expires = System.currentTimeMillis() + timeToLive;
        cbp = new CachedBasicPrincipal(creator, content, "domain", timeToLive);

        assertThat(cbp.revalidate(new Object()), is(resp));
        assertTrue(Math.abs(cbp.expires() - expires) < 10);
    }

}
