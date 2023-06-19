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
 *
 */

package org.onap.ccsdk.apps.cadi.filter.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.onap.ccsdk.apps.cadi.filter.MapPermConverter;

public class JU_MapPermConverter {

    private static final String tag = "tag";
    private static final String value = "value";
    private static final String nontag = "nontag";

    @Test
    public void test() {
        MapPermConverter converter = new MapPermConverter();
        assertThat(converter.map().isEmpty(), is(true));
        converter.map().put(tag, value);
        assertThat(converter.convert(tag), is(value));
        assertThat(converter.convert(nontag), is(nontag));
    }

}
