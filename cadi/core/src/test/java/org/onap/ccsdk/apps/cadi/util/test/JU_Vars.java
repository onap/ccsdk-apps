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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.onap.ccsdk.apps.cadi.util.Vars;

public class JU_Vars {

    @Test
    public void coverage() {
        @SuppressWarnings("unused")
        Vars my_nonstatic_object_for_coverage = new Vars();
    }

    @Test
    public void convert() {
        String test = "test";
        List<String> list = new ArrayList<>();
        list.add("method");
        assertEquals(Vars.convert(test, list), test);
    }

    @Test
    public void convertTest1() {
        List<String> list = new ArrayList<>();
        list.add("method");
        assertEquals(Vars.convert("test", list), "test");
    }

    @Test
    public void convertTest2() {
        List<String> list = new ArrayList<>();
        list.add("method");
        assertEquals(Vars.convert("test", list), "test");
    }

    @Test
    public void test() {
        StringBuilder holder = new StringBuilder();
        String str,bstr;
        assertEquals(str = "set %1 to %2",Vars.convert(holder,str, "a","b"));
        assertEquals("set a to b",holder.toString());
        assertEquals(str,Vars.convert(null,str, "a","b"));
        holder.setLength(0);
        assertEquals(str,Vars.convert(holder,bstr="set %s to %s", "a","b"));
        assertEquals("set a to b",holder.toString());
        assertEquals(str,Vars.convert(null,bstr, "a","b"));

        holder.setLength(0);
        assertEquals(str = "%1=%2",Vars.convert(holder,str, "a","b"));
        assertEquals("a=b",holder.toString());
        assertEquals(str,Vars.convert(null,str, "a","b"));
        holder.setLength(0);
        assertEquals(str,Vars.convert(holder,bstr="%s=%s", "a","b"));
        assertEquals("a=b",holder.toString());
        assertEquals(str,Vars.convert(null,bstr, "a","b"));

        holder.setLength(0);
        assertEquals(str = "%1%2",Vars.convert(holder,str, "a","b"));
        assertEquals("ab",holder.toString());
        assertEquals(str ,Vars.convert(null,str, "a","b"));
        holder.setLength(0);
        assertEquals(str,Vars.convert(holder,bstr="%s%s", "a","b"));
        assertEquals("ab",holder.toString());
        assertEquals(str ,Vars.convert(null,bstr, "a","b"));


        holder.setLength(0);
        assertEquals(str = " %1=%2 ",Vars.convert(holder,str, "a","b"));
        assertEquals(" a=b ",holder.toString());
        assertEquals(str ,Vars.convert(null,str, "a","b"));
        holder.setLength(0);
        assertEquals(str,Vars.convert(holder,bstr = " %s=%s ", "a","b"));
        assertEquals(" a=b ",holder.toString());
        assertEquals(str ,Vars.convert(null,bstr, "a","b"));

        holder.setLength(0);
        assertEquals(str = " %1%2%10 ",Vars.convert(holder,str, "a","b","c","d","e","f","g","h","i","j"));
        assertEquals(" abj ",holder.toString());
        assertEquals(str,Vars.convert(null,str, "a","b","c","d","e","f","g","h","i","j"));
        holder.setLength(0);
        assertEquals(str=" %1%2%3 ",Vars.convert(holder,bstr = " %s%s%s ", "a","b","c","d","e","f","g","h","i","j"));
        assertEquals(" abc ",holder.toString());
        assertEquals(str,Vars.convert(null,bstr, "a","b","c","d","e","f","g","h","i","j"));


        holder.setLength(0);
        assertEquals(str = "set %1 to %2",Vars.convert(holder,str, "Something much","larger"));
        assertEquals("set Something much to larger",holder.toString());
        assertEquals(str,Vars.convert(null,str,"Something much","larger"));
        holder.setLength(0);
        assertEquals(str,Vars.convert(holder,bstr="set %s to %s", "Something much","larger"));
        assertEquals("set Something much to larger",holder.toString());
        assertEquals(str,Vars.convert(null,bstr, "Something much","larger"));

        holder.setLength(0);
        assertEquals(str = "Text without Vars",Vars.convert(holder,str));
        assertEquals(str,holder.toString());
        assertEquals(str = "Text without Vars",Vars.convert(null,str));


        holder.setLength(0);
        assertEquals(str = "Not %1 Enough %2 Vars %3",Vars.convert(holder,str, "a","b"));
        assertEquals("Not a Enough b Vars ",holder.toString());
        assertEquals(str ,Vars.convert(null,str, "a","b"));
        holder.setLength(0);
        assertEquals(str,Vars.convert(holder,bstr="Not %s Enough %s Vars %s", "a","b"));
        assertEquals("Not a Enough b Vars ",holder.toString());
        assertEquals(str ,Vars.convert(null,bstr, "a","b"));

        holder.setLength(0);
        assertEquals(str = "!@#$%^*()-+?/,:;.",Vars.convert(holder,str, "a","b"));
        assertEquals(str,holder.toString());
        assertEquals(str ,Vars.convert(null,str, "a","b"));

        holder.setLength(0);
        bstr = "%s !@#$%^*()-+?/,:;.";
        str = "%1 !@#$%^*()-+?/,:;.";
        assertEquals(str,Vars.convert(holder,bstr, "Not Acceptable"));
        assertEquals("Not Acceptable !@#$%^*()-+?/,:;.",holder.toString());
        assertEquals(str ,Vars.convert(null,bstr, "Not Acceptable"));
    }

}
