/**
 * ============LICENSE_START====================================================
 * org.onap.ccsdk
 * ===========================================================================
 * Copyright (c) 2023 AT&T Intellectual Property. All rights reserved.
 * ===========================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END====================================================
 */

package org.onap.ccsdk.apps.cadi.config.test;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Test;
import org.onap.ccsdk.apps.cadi.CadiException;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.config.Config;
import org.onap.ccsdk.apps.cadi.config.RegistrationPropHolder;

public class JU_RegistrationPropHolder {

    @Test
    public void testBlank() {
        PropAccess pa = new PropAccess();
        RegistrationPropHolder rph;
        int ju_port = 20;
        try {
            ////////////////
            // Check Required Properties
            ////////////////
            try {
                rph = new RegistrationPropHolder(pa,20);
            } catch (CadiException e) {
                Assert.assertEquals(
                        "\ncadi_latitude must be defined." +
                        "\ncadi_longitude must be defined.",e.getMessage());
            }

            try {
                pa.setProperty(Config.CADI_LATITUDE, "32.7");
                rph = new RegistrationPropHolder(pa,20);
            } catch (CadiException e) {
                Assert.assertEquals(
                        "\ncadi_longitude must be defined.",e.getMessage());
            }

            pa.setProperty(Config.CADI_LONGITUDE, "-72.0");
            rph = new RegistrationPropHolder(pa,ju_port);

            ////////////////
            // Validate Default Properties
            ////////////////
            for(String dot_le : new String[] {"",".helm"}) {
                assertEquals(rph.hostname,rph.default_fqdn);
                assertEquals("",rph.lcontainer);
                assertEquals(rph.hostname,rph.public_fqdn);
                assertEquals(ju_port,rph.getEntryPort(dot_le));
                assertEquals(rph.hostname,rph.getEntryFQDN("",dot_le));
            }

            String ns = "myns";
            pa.setProperty(Config.AAF_LOCATOR_APP_NS, ns);
            for(String dot_le : new String[] {"",".helm"}) {
                assertEquals(rph.hostname,rph.default_fqdn);
                assertEquals("",rph.lcontainer);
                assertEquals(rph.hostname,rph.public_fqdn);
                assertEquals(ju_port,rph.getEntryPort(dot_le));
                assertEquals(rph.hostname,rph.getEntryFQDN("",dot_le));
            }

            String ns2 = "onap";
            pa.setProperty(Config.AAF_LOCATOR_APP_NS+".helm", ns2);
            for(String dot_le : new String[] {"",".helm"}) {
                assertEquals(rph.hostname,rph.default_fqdn);
                assertEquals("",rph.lcontainer);
                assertEquals(rph.hostname,rph.public_fqdn);
                assertEquals(ju_port,rph.getEntryPort(dot_le));
                assertEquals(rph.hostname,rph.getEntryFQDN("",dot_le));
            }

            ////////////////
            // Validate Public Host and Port settings
            ////////////////
            String public_hostname = "com.public.hostname";
            int public_port = 999;
            pa.setProperty(Config.AAF_LOCATOR_PUBLIC_FQDN, public_hostname);
            pa.setProperty(Config.AAF_LOCATOR_PUBLIC_PORT,Integer.toString(public_port));
            RegistrationPropHolder pubRPH = new RegistrationPropHolder(pa,ju_port);
            assertEquals(public_hostname,pubRPH.public_fqdn);
            assertEquals(public_port,pubRPH.getEntryPort(""));


            final String url = "https://aaf.osaaf.org:8095/org.osaaf.aaf.service:2.1";
            String name="theName";
            assertEquals(url,rph.replacements(getClass().getSimpleName(),url, name, ""));

            String alu = "aaf.osaaf.org:8095";
            String curl = url.replace(alu, Config.AAF_LOCATE_URL_TAG);
            pa.setProperty(Config.AAF_LOCATE_URL,"https://"+alu);
            assertEquals(url.replace("8095","8095/locate"),rph.replacements(getClass().getSimpleName(),curl, name, ""));

            String root_ns = "org.osaaf.aaf";
            curl = url.replace(root_ns, "AAF_NS");
            pa.setProperty(Config.AAF_ROOT_NS,root_ns);
            assertEquals(url,rph.replacements(getClass().getSimpleName(),curl, name, ""));

            curl = url.replace(root_ns, "%AAF_NS");
            pa.setProperty(Config.AAF_ROOT_NS,root_ns);
            assertEquals(url,rph.replacements(getClass().getSimpleName(),curl, name, ""));

            final String fqdn = "%C.%CNS.%NS.%N";
            String target = "myns.theName";
            assertEquals(target,rph.replacements(getClass().getSimpleName(),fqdn, name, ""));

            pa.setProperty(Config.AAF_LOCATOR_CONTAINER_NS+".hello", "mycontns");
            target = "mycontns.myns.theName";
            assertEquals(target,rph.replacements(getClass().getSimpleName(),fqdn, name, ".hello"));

            pa.setProperty(Config.AAF_LOCATOR_CONTAINER+".hello","helloC");
            target = "helloC.mycontns.myns.theName";
            assertEquals(target,rph.replacements(getClass().getSimpleName(),fqdn, name, ".hello"));

            pa.setProperty(Config.AAF_LOCATOR_CONTAINER_NS,"c_ns");
            target = "c_ns.myns.theName";
            assertEquals(target,rph.replacements(getClass().getSimpleName(),fqdn, name, ""));


        } catch (UnknownHostException | CadiException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }


}
